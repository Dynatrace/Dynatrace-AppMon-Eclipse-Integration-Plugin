package com.dynatrace.diagnostics.launcher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.diagnostics.launcher.functionality.SessionRecorder;
import com.dynatrace.diagnostics.launcher.functionality.TestRunRecorder;
import com.dynatrace.diagnostics.launcher.ui.errorpopup.TransientErrorPopupManager;
import com.dynatrace.diagnostics.launcher.ui.testresults.TestResultsViewGoto;
import com.dynatrace.sdk.server.exceptions.ServerConnectionException;
import com.dynatrace.sdk.server.exceptions.ServerResponseException;
import com.dynatrace.sdk.server.testautomation.models.TestRun;

/**
 * @author Michal.Weyer
 * @since 2016-04-13
 */
class LaunchTerminated {

	private final SessionRecorder sessionRecorder;
	private final TestRunRecorder testRunRecorder;
	private final Map<WeakReference<ILaunch>, Integer> JUnitLaunch_testCount = new ConcurrentHashMap<WeakReference<ILaunch>, Integer>();

	LaunchTerminated(SessionRecorder sessionRecorder, TestRunRecorder testRunRecorder) {
		this.sessionRecorder = sessionRecorder;
		this.testRunRecorder = testRunRecorder;
	}

	Job createProcess(ILaunch[] launches) {
		return new WrapUpLaunch(launches);
	}

	void setTestCount(ILaunch launch, int totalCount) {
		JUnitLaunch_testCount.put(new WeakReference<ILaunch>(launch), totalCount);
	}

	private final class WrapUpLaunch extends Job {

		private final ILaunch[] launches;
		private volatile boolean operationAborted;

		WrapUpLaunch(ILaunch[] launches) {
			super(StringResources.parsing_test_results);
			this.launches = launches;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			final int TOTAL_WORK = 100;
			final int SESSION_RETRIEVAL_WORK_AMT = 30;

			monitor.beginTask("Stopping session recording, retrieving test results from AppMon...", TOTAL_WORK);
			monitor.worked(1);

			try {
				sessionRecorder.stopRecording(launches);
				monitor.worked(SESSION_RETRIEVAL_WORK_AMT - 1);

				final List<TestRun> finishedTestRuns = new ArrayList<TestRun>();
				finishedTestRuns.addAll(testRunRecorder.finishTestRuns(launches, launchTestCounts,
						new TestRetrieverMonitor(new SubProgressMonitor(monitor, 70))));
				if (finishedTestRuns.isEmpty()) {
					return Status.CANCEL_STATUS;
				}

				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						try {
							if (finishedTestRuns.isEmpty()) {
								return;
							}
							TestResultsViewGoto.displayTestResultsView(finishedTestRuns);

						} catch (PartInitException e) {
							LogHelper.logError(
									"Failed to show the test results view after finishing test run [ErrorLocation-41]",
									e);
						}
					}
				});
				return Status.OK_STATUS;

			} catch (InterruptedException e) {
				TransientErrorPopupManager.logAndShowError(
						"Retrieving Dynatrace JUnit test runs results interrupted, results won't be retrieved [ErrorLocation-42]",
						e);
			} catch (ServerConnectionException e) {
				logDontPopup(e);
			} catch (ServerResponseException e) {
				logDontPopup(e);
			} finally {
				monitor.done();
			}
			return Status.CANCEL_STATUS;
		}

		@Override
		protected void canceling() {
			super.canceling();
			operationAborted = true;
		}

		private void logDontPopup(Exception e) {
			LogHelper.logError("Error during Dynatrace launch complete handling [ErrorLocation-43]", e);
			// no popup since assuming this will have occurred at launch start
		}

		private final class TestRetrieverMonitor implements TestRunRecorder.ProgressMonitor {
			private final SubProgressMonitor subMonitor;
			private final long startInstant = System.currentTimeMillis();
			private long prevReportedInstant = -1;

			private TestRetrieverMonitor(SubProgressMonitor subMonitor) {
				this.subMonitor = subMonitor;
				subMonitor.beginTask("Retrieving test run data", (int) (timeoutMs() / 1000));
			}

			@Override
			public void notifyRequestEmpty() {
				if (prevReportedInstant == -1) {
					prevReportedInstant = System.currentTimeMillis();
					return;
				}
				subMonitor.worked((int) ((System.currentTimeMillis() - prevReportedInstant) / 1000));
				prevReportedInstant = System.currentTimeMillis();
			}

			@Override
			public boolean shouldAbort() {
				return operationAborted || (System.currentTimeMillis() > startInstant + timeoutMs());
			}

			private long timeoutMs() {
				IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
				return preferenceStore.getInt(Constants.PREF_TIMEOUT_TESTRUN_RETRIEVAL_SECONDS) * 1000;
			}
		}
	}

	private final TestRunRecorder.ContainsLaunchTestCounts launchTestCounts = new TestRunRecorder.ContainsLaunchTestCounts() {
		@Override
		public int expectedTestCountsFor(ILaunch launch) {
			for (WeakReference<ILaunch> iLaunchWeakReference : JUnitLaunch_testCount.keySet()) {
				if (launch.equals(iLaunchWeakReference.get())) {
					return JUnitLaunch_testCount.get(iLaunchWeakReference);
				}
			}
			throw new IllegalStateException("No test run count found. [ErrorLocation-33]");
		}
	};
}
