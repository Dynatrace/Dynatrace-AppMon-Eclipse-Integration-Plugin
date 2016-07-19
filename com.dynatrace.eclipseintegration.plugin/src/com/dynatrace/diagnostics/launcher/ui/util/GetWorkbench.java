package com.dynatrace.diagnostics.launcher.ui.util;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Adam.Andrzejewski
 * @since 2016-04-13
 */
public class GetWorkbench {

	public static IWorkbenchWindow getWorkbenchWindow() {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			int workbenchWindowCount = PlatformUI.getWorkbench().getWorkbenchWindowCount();
			if (workbenchWindowCount > 0) {
				IWorkbenchWindow[] workbenchWindows = PlatformUI.getWorkbench().getWorkbenchWindows();
				activeWorkbenchWindow = workbenchWindows[0];
			}
		}
		return (activeWorkbenchWindow != null) ? activeWorkbenchWindow : null;
	}
}
