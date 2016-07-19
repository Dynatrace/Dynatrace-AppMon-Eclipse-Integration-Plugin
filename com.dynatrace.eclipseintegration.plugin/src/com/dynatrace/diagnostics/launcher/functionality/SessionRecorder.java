package com.dynatrace.diagnostics.launcher.functionality;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.NotImplementedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.diagnostics.launcher.rest.RESTService;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ProfileNotFound;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAlreadyRecordingSession;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationRequiresSSLException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerErrorUnknown;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerHostUnknownException;
import com.dynatrace.diagnostics.launcher.ui.errorpopup.TransientErrorPopupManager;


/**
 * manages currently recorded sessions, starts and stops the
 * @author michael.kumar
 * @author Michal.Weyer
 */
public class SessionRecorder {

	private final Map<ILaunch, RecordedLaunch> recordedLaunches = new ConcurrentHashMap<ILaunch, RecordedLaunch>();
	private final RESTService restService;

	private class RecordedLaunch {
		private String profileName;
		private RecordedLaunch(String profileName) {
			this.profileName = profileName;
		}
	}


	public SessionRecorder(RESTService restService) {
		this.restService = restService;
	}

	/**
	 * Add a launch to be watched. The launch will be removed automatically from
	 * the internal watch list when a terminate is signaled by eclipse.
	 * @param launch the launch to watch
	 */
	public void addLaunch(ILaunch launch)
			throws CoreException, ServerAuthenticationException, ServerErrorUnknown, ProfileNotFound,
			ServerAlreadyRecordingSession, ServerConnectionFailedException, ServerHostUnknownException,
			ServerAuthenticationRequiresSSLException {

		if (launch == null) {
			throw new NotImplementedException("Null launch? [ErrorLocation-36]");
		}

		synchronized (recordedLaunches) {

			boolean record = launch.getLaunchConfiguration().getAttribute(Constants.PREF_SESSION_RECORD, false);
			String profile = launch.getLaunchConfiguration().getAttribute(Constants.PREF_SERVER_PROFILE,
					Constants.DEFAULT_SERVER_PROFILE);

			if (!record || profile.isEmpty()) {
				return;
			}

			final CountDownLatch countDownLatch = new CountDownLatch(1);
			try {
				new Job(StringResources.preparing_dynatrace_launch) {

					@Override
					protected IStatus run(final IProgressMonitor monitor) {
						try {
							SubMonitor.convert(monitor, 100);
							countDownLatch.await();
						} catch (InterruptedException ignored) {
							LogHelper.logOK("Await session recording started interrupted [ErrorLocation-47] " + ignored);
							// ignore
						}
						monitor.done();
						return Status.OK_STATUS;
					}
				}.schedule();

				startRecording(profile);
				recordedLaunches.put(launch, new RecordedLaunch(profile));
			} finally {
				countDownLatch.countDown();
			}
		}
	}

	public void stopRecording(ILaunch[] toStopLaunches) {

		synchronized (recordedLaunches) {
			Exception wasException = null;
			for (ILaunch toStop : toStopLaunches) {
				try {
					RecordedLaunch recordedLaunch = recordedLaunches.get(toStop);
					if (recordedLaunch == null || isBlank(recordedLaunch.profileName)) {
						continue;
					}
					stopRecording(recordedLaunch.profileName);
					recordedLaunches.remove(toStop);

				} catch (Exception e) {
					wasException = e; // continue with other stop attempts
					LogHelper.logError("An exception was thrown while Dynatrace session recording stop request was sent [ErrorLocation-48]", e);
				}
			}

			if (wasException != null) {
				TransientErrorPopupManager.logAndShowError(
						"An exception was thrown while Dynatrace session recording stop request was sent. Please view Eclipse Error Log for details. [ErrorLocation-49]");
				// Assuming any persistent errors will occur at session recording start and will be reported in detail then.
				// To keep duplication to a minimum only a cursory message is reported here.
			}
		}
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	public void stopRecordingAll() {
		synchronized(recordedLaunches) {
			Exception wasException = null;
			for (RecordedLaunch recordedLaunch : recordedLaunches.values()) {
				try {
					stopRecording(recordedLaunch.profileName);
					recordedLaunches.remove(recordedLaunch);

				} catch (Exception e) {
					wasException = e; // continue with other stop attempts
					LogHelper.logError("An exception was thrown while Dynatrace session recording stop request was sent [ErrorLocation-50]", e);
				}
			}

			if (wasException != null) {
				TransientErrorPopupManager.logAndShowError(
						"An error occurred while stopping Dynatrace session recording. Please view Eclipse Error Log for details. [ErrorLocation-51]");
				// Assuming any persistent errors will occur at session recording start and will be reported in detail then.
				// To keep duplication to a minimum only a cursory message is reported here.
			}
		}
	}



	private void startRecording(String profile)
			throws ProfileNotFound, ServerConnectionFailedException, ServerAlreadyRecordingSession,
			ServerErrorUnknown, ServerHostUnknownException, ServerAuthenticationException,
			ServerAuthenticationRequiresSSLException {

		restService.startSession(profile);
	}

	private void stopRecording(String profileName)
			throws ServerConnectionFailedException, ServerHostUnknownException, ServerErrorUnknown, ServerAuthenticationException,
			ServerAuthenticationRequiresSSLException {

		restService.stopSession(profileName);
	}
}
