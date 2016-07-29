package com.dynatrace.diagnostics.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.launcher.functionality.SessionRecorder;
import com.dynatrace.diagnostics.launcher.functionality.TestRunRecorder;
import com.dynatrace.sdk.server.DynatraceClient;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
public class LauncherCallbacks {

	private final SessionRecorder sessionRecorder;
	private final LaunchNew launchNew;
	private final LaunchTerminated launchTerminated;

	private ILaunchesListener2 launchListener;
	private IWorkbenchListener workbenchListener;
	private final TestRunListener testRunListener;

	public LauncherCallbacks(DynatraceClient client) {
		this.sessionRecorder = new SessionRecorder(client);
		TestRunRecorder testRunRecorder = new TestRunRecorder(client);

		launchNew = new LaunchNew(sessionRecorder, testRunRecorder);
		launchTerminated = new LaunchTerminated(sessionRecorder, testRunRecorder);
		launchListener = onLaunchComplete();
		workbenchListener = postWorkbenchShutdown();

		testRunListener = new TestRunListener() {
			@Override
			public void sessionFinished(ITestRunSession uncastSession) {
				TestRunSession session = (TestRunSession) uncastSession;
				int totalCount = session.getTotalCount();
				ILaunch launch = session.getLaunch();

				launchTerminated.setTestCount(launch, totalCount);
			}
		};
	}

	public void attach() {
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(launchListener);
		PlatformUI.getWorkbench().addWorkbenchListener(workbenchListener);
		JUnitCore.addTestRunListener(testRunListener);
	}

	public void detach() {
		if (launchListener == null) {
			DebugPlugin.getDefault().getLaunchManager().removeLaunchListener(launchListener);
		}
		if (workbenchListener != null) {
			PlatformUI.getWorkbench().removeWorkbenchListener(workbenchListener);
		}
		JUnitCore.removeTestRunListener(testRunListener);
	}

	public ILaunchConfiguration onNewLaunch(Class<? extends LaunchConfigurationDelegate> launchDelegateClass,
			ILaunchConfiguration configuration, ILaunch launch) throws CoreException {

		ILaunchConfigurationWorkingCopy workingCopy = configuration.getWorkingCopy();

		launchNew.handle(launchDelegateClass, launch, workingCopy);
		return workingCopy;
	}

	private DoNothingListenerLaunches onLaunchComplete() {
		return new DoNothingListenerLaunches() {
			@Override
			public void launchesTerminated(final ILaunch[] launches) {
				Job onLaunchCompleteJob = launchTerminated.createProcess(launches);
				onLaunchCompleteJob.setUser(true);
				onLaunchCompleteJob.schedule();
			}
		};
	}

	private DoNothingListenerWorkbench postWorkbenchShutdown() {
		return new DoNothingListenerWorkbench() {
			@Override
			public void postShutdown(IWorkbench workbench) {
				try {
					sessionRecorder.stopRecordingAll();
				} catch (Exception e) {
					LogHelper.logError(
							"Exception when stopping session recording on workbench shutdown [ErrorLocation-74]", e);
				}
			}
		};
	}

}
