package com.dynatrace.diagnostics.launcher;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;

/**
 * @author Michal.Weyer
 * @since 2016-03-23
 */
class DoNothingListenerWorkbench implements IWorkbenchListener {

	@Override
	public void postShutdown(IWorkbench workbench) {
		// does nothing
	}

	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		return true;
	}
}
