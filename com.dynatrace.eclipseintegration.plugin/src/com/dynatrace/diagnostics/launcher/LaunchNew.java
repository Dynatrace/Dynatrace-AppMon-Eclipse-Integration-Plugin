package com.dynatrace.diagnostics.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.JUnitLaunchCheck;
import com.dynatrace.diagnostics.launcher.functionality.SessionRecorder;
import com.dynatrace.diagnostics.launcher.functionality.TestRunRecorder;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ProfileNotFound;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAlreadyRecordingSession;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationRequiresSSLException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerErrorUnknown;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerHostUnknownException;
import com.dynatrace.diagnostics.launcher.ui.errorpopup.TransientErrorPopupManager;

/**
 * @author Michal.Weyer
 * @since 2016-04-13
 */
class LaunchNew {

	private SessionRecorder sessionRecorder;
	private TestRunRecorder testRunRecorder;

	LaunchNew(SessionRecorder sessionRecorder, TestRunRecorder testRunRecorder) {
		this.sessionRecorder = sessionRecorder;
		this.testRunRecorder = testRunRecorder;
	}

	void handle(Class<? extends LaunchConfigurationDelegate> launchDelegateClass, ILaunch launch,
			ILaunchConfigurationWorkingCopy workingCopy) {

		try {
			sessionRecorder.addLaunch(launch); // only DT delegates report, so anything here has dtAgent
		} catch (Exception e) {
			reportSessionError(e);
		}

		if (JUnitLaunchCheck.isJUnitLaunch(launchDelegateClass)) {
			try {
				String testRunID = testRunRecorder.registerNewTestRun(launch);
				workingCopy.setAttribute(Constants.PREF_AGENT_PARAMS,
						com.dynatrace.diagnostics.launcher.functionality.TestRunRecorder.TESTRUN_ID_PROPERTY_NAME + "=" +
								testRunID);
			} catch (Exception e) {
				reportTestrunError(e);
			}
		}
	}


	private void reportSessionError(Exception e) {
		if (e instanceof ServerAlreadyRecordingSession) {
			TransientErrorPopupManager.logAndShowRunConfigError(
					"Dynatrace Server is already recording a session, per-launch session couldn't be started [ErrorLocation-59]", e);
			// if continuous session recording enabled test runs will recorded, so continue to test recording
			return;
		}
		if (e instanceof ProfileNotFound) {
			TransientErrorPopupManager.logAndShowRunConfigError(
					((ProfileNotFound) e).getProfileName() + " - profile not found on Dynatrace Serve, failed recording session [ErrorLocation-60]", e);
			return;
		}
		if (e instanceof ServerErrorUnknown) {
			TransientErrorPopupManager.logAndShowError(
					"Dynatrace Server returned an error response or failed - see Error Log view in Eclipse for details [ErrorLocation-61]", e);
			// possibly the test runs will still record?
			return;
		}
		if (e instanceof CoreException) {
			TransientErrorPopupManager.logAndShowError(
					"Exception while retrieving launch configuration data, Dynatrace AppMon session will not be recorded [ErrorLocation-62]", e);
			return;
		}
		if (e instanceof ServerConnectionFailedException) {
			TransientErrorPopupManager.logAndShowError(
					((ServerConnectionFailedException) e).getHost() +
							" - failed connecting to Dynatrace Server while starting DT session [ErrorLocation-63]", e);
			return;
		}
		if (e instanceof ServerHostUnknownException) {
			TransientErrorPopupManager.logAndShowError(
					((ServerHostUnknownException) e).getHostName() + " - Dynatrace server host couldn't be resolved, failed recording session [ErrorLocation-64]", e);
			return;
		}
		if (e instanceof ServerAuthenticationException) {
			TransientErrorPopupManager.logAndShowError("Authentication error while logging into Dynatrace Server, failed recording session [ErrorLocation-65]", e);
			return;
		}
		if (e instanceof ServerAuthenticationRequiresSSLException) {
			TransientErrorPopupManager.logAndShowError(
					"Dynatrace Server configured to require SSL if authorization data provided - login failed, failed recording session [ErrorLocation-66]", e);
			return;
		}
		TransientErrorPopupManager.logAndShowError(
				"Unexpected error occurred while attempting to record Dynatrace session. Please view Eclipse Error Log for details."
				+ " Failed recording session [ErrorLocation-67]",
				e);

	}

	private void reportTestrunError(Exception e) {

		if (e instanceof ProfileNotFound) {
			TransientErrorPopupManager.logAndShowRunConfigError(
					((ProfileNotFound) e).getProfileName() + " -  profile not found on Dynatrace Server, failed recording test run [ErrorLocation-68]", e);
			return;
		}
		if (e instanceof ServerConnectionFailedException) {
			TransientErrorPopupManager.logAndShowError(
					((ServerConnectionFailedException) e).getHost() +
							" - failed connecting to Dynatrace Server while recording JUnit test run [ErrorLocation-69]", e);
			return;
		}
		if (e instanceof ServerHostUnknownException) {
			TransientErrorPopupManager.logAndShowError(
					((ServerHostUnknownException) e).getHostName() + " - Dynatrace server host couldn't be resolved, failed recording test run [ErrorLocation-70]", e);
			return;
		}
		if (e instanceof ServerAuthenticationException) {
			TransientErrorPopupManager.logAndShowError("Authentication error while logging into Dynatrace Server, failed recording test run [ErrorLocation-71]", e);
			return;
		}
		if (e instanceof ServerAuthenticationRequiresSSLException) {
			TransientErrorPopupManager.logAndShowError(
					"Dynatrace Server configured to require SSL if authorization data provided - login failed, failed recording test run [ErrorLocation-72]", e);
			return;
		}
		TransientErrorPopupManager.logAndShowRunConfigError(
				"Unexpected error occurred while attempting to JUnit test runs. Please view Eclipse Error Log for details. [ErrorLocation-73]", e);
	}
}
