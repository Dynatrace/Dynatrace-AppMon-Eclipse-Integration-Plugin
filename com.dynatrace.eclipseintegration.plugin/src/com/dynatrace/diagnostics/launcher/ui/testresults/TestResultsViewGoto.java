package com.dynatrace.diagnostics.launcher.ui.testresults;

import static java.lang.String.format;

import java.util.List;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.launcher.ui.util.GetWorkbench;
import com.dynatrace.diagnostics.server.service.data.TestRun;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
public class TestResultsViewGoto {

	public static void displayTestResultsView(List<TestRun> finishedTestRuns) throws PartInitException {
		final IWorkbenchWindow windowToUse = GetWorkbench.getWorkbenchWindow();
		if (windowToUse == null) {
			LogHelper.logError("Failed displaying TestResultsView, since windowToUse is null [ErrorLocation-89]");
			return;
		}

		IWorkbenchPage activePage = windowToUse.getActivePage();
		if (activePage == null) {
			LogHelper.logError("Failed displaying TestResultsView, since activePage is null [ErrorLocation-90]");
			return;
		}

		IViewPart view = activePage.showView(TestResultsView.VIEW_ID);
		if (!(view instanceof TestResultsView)) {
			LogHelper.logError(format("Failed displaying test results window. Window with [id:%s] expected to be a [%s] is [%s] [ErrorLocation-91]",
					TestResultsView.VIEW_ID, TestResultsView.class.getSimpleName(), view.getClass().getSimpleName()));
			return;
		}
		TestResultsView trView = (TestResultsView) view;
		trView.refresh(finishedTestRuns);
	}
}
