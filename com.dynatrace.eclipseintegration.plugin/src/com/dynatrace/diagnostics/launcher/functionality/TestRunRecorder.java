package com.dynatrace.diagnostics.launcher.functionality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;

import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.diagnostics.launcher.rest.RESTService;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ProfileNotFound;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationRequiresSSLException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerErrorUnknown;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerHostUnknownException;
import com.dynatrace.diagnostics.launcher.ui.errorpopup.TransientErrorPopupManager;
import com.dynatrace.diagnostics.server.service.data.TestRun;

/**
 * @author Adam.Andrzejewski
 * @author Michal.Weyer
 * @since 2016-03-23
 */
public class TestRunRecorder {

	private static final int TEST_RUN_FINISH_INTERVAL_CHECK = 1000;

	public static final String TESTRUN_ID_PROPERTY_NAME = "optionTestRunIdJava"; //$NON-NLS-1$

	private final RESTService restService;
	private Map<ILaunch, String> runningTestRuns = new ConcurrentHashMap<ILaunch, String>();

	public TestRunRecorder(RESTService restService) {
		this.restService = restService;
	}

	public String registerNewTestRun(ILaunch launch)
			throws ServerHostUnknownException, ServerConnectionFailedException, ServerErrorUnknown, ProfileNotFound,
			ServerAuthenticationException, ServerAuthenticationRequiresSSLException {
		String testRunId = restService.registerNewTestRun(getProfileName(launch));
		runningTestRuns.put(launch, testRunId);
		return testRunId;
	}

	public List<TestRun> finishTestRuns(ILaunch[] launches, ContainsLaunchTestCounts testCounts, ProgressMonitor progressMonitor)
			throws ServerHostUnknownException, ServerConnectionFailedException, InterruptedException, ServerErrorUnknown,
			ServerAuthenticationException, ServerAuthenticationRequiresSSLException {

		if (launches == null) {
			return Collections.emptyList();
		}

		List<TestRun> finishedTestRuns = new ArrayList<TestRun>();
		for (ILaunch iLaunch : launches) {
			String testRunId = runningTestRuns.remove(iLaunch);
			if (testRunId == null) {
				continue;
			}

			TestRun testRun = restService.getTestRun(testRunId, getProfileName(iLaunch));
			int expectedTestCount = testCounts.expectedTestCountsFor(iLaunch);
			while (!containsAsManyTests(testRun, expectedTestCount) && !progressMonitor.shouldAbort()) {
				progressMonitor.notifyRequestEmpty();
				Thread.sleep(TEST_RUN_FINISH_INTERVAL_CHECK);
				testRun = restService.getTestRun(testRunId, getProfileName(iLaunch));
			}


			if (containsAsManyTests(testRun, expectedTestCount)) {
				finishedTestRuns.add(testRun);
			} else if (testRun.numPassed + testRun.numFailed == 0) {
				TransientErrorPopupManager.logAndShowRunConfigError(
						"No test results available at test retrieval timeout."
						+ " Please verify agent name and profile name settings in Eclipse and AppMon Server. [ErrorLocation-94]",
						StringResources.link_url_no_testruns);
			} else {
				TransientErrorPopupManager.logAndShowError("Partial results presented"
						+ " - full results not available within testrun retrieval timeout [ErrorLocation-95]");
				finishedTestRuns.add(testRun);
			}
		}
		return finishedTestRuns;
	}

	private boolean containsAsManyTests(TestRun testRun, int expectedTestCount) {
		return (  testRun.numDegraded
				+ testRun.numFailed
				+ testRun.numImproved
				+ testRun.numInvalidated
				+ testRun.numPassed
				+ testRun.numVolatile == expectedTestCount);
	}

	private String getProfileName(ILaunch launch) {
		try {
			return launch.getLaunchConfiguration().getAttribute(Constants.PREF_SERVER_PROFILE, Constants.DEFAULT_SERVER_PROFILE);
		} catch (CoreException e) {
			throw new RuntimeException("Exception retrieving profile name [ErrorLocation-46]", e);
		}
	}

	public interface ContainsLaunchTestCounts {
		int expectedTestCountsFor(ILaunch launch);
	}

	public interface ProgressMonitor {
		void notifyRequestEmpty();
		boolean shouldAbort();
	}
}
