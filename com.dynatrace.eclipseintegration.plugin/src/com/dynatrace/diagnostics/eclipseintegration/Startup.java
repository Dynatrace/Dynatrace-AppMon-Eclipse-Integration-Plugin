package com.dynatrace.diagnostics.eclipseintegration;

import org.eclipse.ui.IStartup;

/**
 * @author Michal.Weyer
 * @since 2016-04-22
 */
public class Startup implements IStartup {

	/**
	 * Hook called when eclipse started up
	 *
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 * @author markus.poechtrager
	 */
	@Override
	public void earlyStartup() {
		Activator.getDefault().startCodeLink();
	}
}
