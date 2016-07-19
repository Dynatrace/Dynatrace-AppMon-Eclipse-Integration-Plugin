package com.dynatrace.diagnostics.launcher.ui.errorpopup;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.launcher.ui.actions.OpenDynatraceConfigurations;
import com.dynatrace.diagnostics.launcher.ui.util.GetWorkbench;

/**
 * @author Michal.Weyer
 * @since 2016-04-15
 */
class ShowErrorLog {

	static void showErrorLog() {
		IWorkbenchWindow workbenchWindow = GetWorkbench.getWorkbenchWindow();
		if (workbenchWindow == null) {
			LogHelper.logError("Failed showing Error Log, workbench window not found [ErrorLocation-52]");
			return;
		}
		IWorkbenchPage activePage = workbenchWindow.getActivePage();
		if (activePage == null) {
			LogHelper.logError("Failed showing Error Log, activePage window not found [ErrorLocation-53]");
			return;
		}

		try {
			activePage.showView("org.eclipse.pde.runtime.LogView");
		} catch (PartInitException e) {
			LogHelper.logError("Failed showing Error Log, showView() threw exception [ErrorLocation-54]", e);
		}
	}

	static void showDynatraceGlobalConfigurationWindows() {
		PreferencesUtil.createPreferenceDialogOn(
				GetWorkbench.getWorkbenchWindow().getShell(),
				"com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage.EclipseIntegrationPreferencePage",
				null, null).open();
	}

	static void showDynatraceLaunchConfigWindow() {
		new OpenDynatraceConfigurations().run();
	}
}
