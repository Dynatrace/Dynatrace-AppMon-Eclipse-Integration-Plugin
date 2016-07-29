package com.dynatrace.diagnostics.launcher;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.JUnitLaunchCheck;
import com.dynatrace.diagnostics.launcher.functionality.SessionRecorder;
import com.dynatrace.diagnostics.launcher.functionality.TestRunRecorder;
import com.dynatrace.diagnostics.launcher.ui.errorpopup.TransientErrorPopupManager;
import com.dynatrace.sdk.server.exceptions.ServerConnectionException;
import com.dynatrace.sdk.server.exceptions.ServerResponseException;

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
			sessionRecorder.addLaunch(launch); // only DT delegates report, so
												// anything here has dtAgent
		} catch (Exception e) {
			reportSessionError(e);
		}

		if (JUnitLaunchCheck.isJUnitLaunch(launchDelegateClass)) {
			try {
				String testRunID = testRunRecorder.registerNewTestRun(launch);
				workingCopy.setAttribute(Constants.PREF_AGENT_PARAMS,
						com.dynatrace.diagnostics.launcher.functionality.TestRunRecorder.TESTRUN_ID_PROPERTY_NAME + "="
								+ testRunID);
			} catch (Exception e) {
				reportTestrunError(e);
			}
		}
	}

	private void reportSessionError(Exception e) {
		if (e instanceof ServerResponseException || e instanceof ServerConnectionException
				|| e instanceof IllegalArgumentException) {
			TransientErrorPopupManager.logAndShowError(e.getMessage());
			return;
		}
		TransientErrorPopupManager.logAndShowError(
				"Unexpected error occurred while attempting to record Dynatrace session. Please view Eclipse Error Log for details."
						+ " Failed recording session [ErrorLocation-67]",
				e);

	}

	private void reportTestrunError(Exception e) {
		if (e instanceof ServerResponseException || e instanceof ServerConnectionException
				|| e instanceof IllegalArgumentException) {
			TransientErrorPopupManager.logAndShowError(e.getMessage());
			return;
		}
		TransientErrorPopupManager.logAndShowRunConfigError(
				"Unexpected error occurred while attempting to JUnit test runs. Please view Eclipse Error Log for details. [ErrorLocation-73]",
				e);
	}
}
