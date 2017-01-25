package com.dynatrace.diagnostics.launcher.functionality;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.internal.core.Preferences;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.diagnostics.launcher.ui.errorpopup.TransientErrorPopupManager;
import com.dynatrace.sdk.server.DynatraceClient;
import com.dynatrace.sdk.server.exceptions.ServerConnectionException;
import com.dynatrace.sdk.server.exceptions.ServerResponseException;
import com.dynatrace.sdk.server.testautomation.TestAutomation;
import com.dynatrace.sdk.server.testautomation.models.CreateTestRunRequest;
import com.dynatrace.sdk.server.testautomation.models.TestCategory;
import com.dynatrace.sdk.server.testautomation.models.TestRun;

/**
 * @author Adam.Andrzejewski
 * @author Michal.Weyer
 * @since 2016-03-23
 */
public class TestRunRecorder {

	private static final int TEST_RUN_FINISH_INTERVAL_CHECK = 1000;

	public static final String TESTRUN_ID_PROPERTY_NAME = "optionTestRunIdJava"; //$NON-NLS-1$

	private final TestAutomation testAutomation;
	private Map<ILaunch, String> runningTestRuns = new ConcurrentHashMap<ILaunch, String>();

	public TestRunRecorder(DynatraceClient client) {
		this.testAutomation = new TestAutomation(client);
	}

	public String registerNewTestRun(ILaunch launch) throws ServerConnectionException, ServerResponseException {
		Calendar now = Calendar.getInstance();
		CreateTestRunRequest request = new CreateTestRunRequest(getProfileName(launch),
				new SimpleDateFormat("HH:mm:ss").format(now.getTime()));
		request.setVersionMajor(String.valueOf(now.get(Calendar.YEAR)));
		request.setVersionMinor(String.valueOf(now.get(Calendar.MONTH)) + 1);
		request.setVersionRevision(String.valueOf(now.get(Calendar.DAY_OF_MONTH)));

		TestCategory configuredTestCategory = TestCategory.UNIT;
		String testCategoryName =
			Constants.getDefaultString(Activator.getDefault().getPreferenceStore(),
				Constants.PREF_TEST_CATEGORY,
				Constants.DEFAULT_PREF_TEST_CATEGORY);
		try {
			for (TestCategory enumValue : TestCategory.values()) {
				if (enumValue.name().equalsIgnoreCase(testCategoryName)) {
					configuredTestCategory = enumValue;
					break;
				}
			}
		} catch (Exception e) {
			LogHelper.createErrorStatus(
					"Exception when retrieving test category for test run registration. "
					+ "Test category name configured: [" + testCategoryName + "]", e);
		}
		request.setCategory(configuredTestCategory);

		LogHelper.logInfo("Registering a testrun: " + request.toString());
		TestRun tr = testAutomation.createTestRun(request);
		runningTestRuns.put(launch, tr.getId());
		return tr.getId();
	}

	public List<TestRun> finishTestRuns(ILaunch[] launches, ContainsLaunchTestCounts testCounts,
			ProgressMonitor progressMonitor)
			throws InterruptedException, ServerConnectionException, ServerResponseException {

		if (launches == null) {
			return Collections.emptyList();
		}

		List<TestRun> finishedTestRuns = new ArrayList<TestRun>();
		for (ILaunch iLaunch : launches) {
			String testRunId = runningTestRuns.remove(iLaunch);
			if (testRunId == null) {
				continue;
			}
			LogHelper.logInfo("Fetching test run results for: " + testRunId);
			TestRun testRun = testAutomation.fetchTestRun(getProfileName(iLaunch), testRunId);
			int expectedTestCount = testCounts.expectedTestCountsFor(iLaunch);
			while (!containsAsManyTests(testRun, expectedTestCount) && !progressMonitor.shouldAbort()) {
				progressMonitor.notifyRequestEmpty();
				Thread.sleep(TEST_RUN_FINISH_INTERVAL_CHECK);
				testRun = testAutomation.fetchTestRun(getProfileName(iLaunch), testRunId);
			}

			if (containsAsManyTests(testRun, expectedTestCount)) {
				finishedTestRuns.add(testRun);
			} else if (testRun.getTestResults().isEmpty()) {
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
		return (testRun.getTestResults().size() == expectedTestCount);
	}

	private String getProfileName(ILaunch launch) {
		try {
			return launch.getLaunchConfiguration().getAttribute(Constants.PREF_SERVER_PROFILE,
					Constants.DEFAULT_SERVER_PROFILE);
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
