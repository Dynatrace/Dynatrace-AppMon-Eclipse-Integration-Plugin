package com.dynatrace.diagnostics.launcher;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;

/**
 * @author Michal.Weyer
 * @since 2016-03-23
 */
class DoNothingListenerLaunches implements ILaunchesListener2 {

	@Override
	public void launchesTerminated(ILaunch[] launches) {
		// does nothing
	}

	@Override
	public void launchesRemoved(ILaunch[] launches) {
		// does nothing
	}

	@Override
	public void launchesAdded(ILaunch[] launches) {
		// does nothing
	}

	@Override
	public void launchesChanged(ILaunch[] launches) {
		// does nothing
	}
}
