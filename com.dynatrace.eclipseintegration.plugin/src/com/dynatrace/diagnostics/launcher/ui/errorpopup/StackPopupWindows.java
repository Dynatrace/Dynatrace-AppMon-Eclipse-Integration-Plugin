package com.dynatrace.diagnostics.launcher.ui.errorpopup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Michal.Weyer
 * @since 2016-04-15
 */
class StackPopupWindows {

	private final Object windowPositioningMonitor = new Object();
	private final List<WeakReference<Shell>> visibleWindows = new ArrayList<WeakReference<Shell>>();
	private Composite restackOnResized;

	void displayOnTopOfPrev(Shell shell) {
		synchronized (windowPositioningMonitor) {
			final Composite parentShell = shell.getParent();
			displayOverOthers(shell, parentShell);
			if (restackOnResized == null) {
				restackOnResized = parentShell;
				restackOnResized.addControlListener(new ControlAdapter() {
					@Override
					public void controlResized(ControlEvent e) {
						restackWindows();
					}
				});
			}
		}
	}

	private void displayOverOthers(Shell shell, Composite parentShell) {
		Point parentSize = parentShell.getSize();
		Point mySize = shell.getSize();
		Point myLocation = parentShell.getLocation();
		myLocation.x += parentSize.x - mySize.x - 15;
		myLocation.y += parentSize.y - mySize.y - 15;

		Point topMost = topMostWindowVisibleTopLeft();
		if (topMost != null) {
			myLocation.y = topMost.y - mySize.y - 5;
			if (myLocation.y < parentShell.getLocation().y) {
				myLocation.y = topMost.y;
			}
		}
		shell.setLocation(myLocation);
		visibleWindows.add(new WeakReference<Shell>(shell));
	}

	private Point topMostWindowVisibleTopLeft() {
		Point topMost = null;
		for (WeakReference<Shell> displayedWindow : visibleWindows) {
			Shell shell = displayedWindow.get();
			if (shell == null || shell.isDisposed()) {
				continue;
			}
			Point candidateLocation = shell.getLocation();
			if (topMost == null) {
				topMost = candidateLocation;
				continue;
			}

			if (candidateLocation.y < topMost.y) {
				topMost = candidateLocation;
			}
		}

		return topMost;
	}


	private void restackWindows() {
		List<WeakReference<Shell>> toLayoutList = new ArrayList<WeakReference<Shell>>(visibleWindows);
		visibleWindows.clear();

		for (WeakReference<Shell> layingOut : toLayoutList) {
			Shell shell = layingOut.get();
			if (shell == null || shell.isDisposed()) {
				continue;
			}
			displayOverOthers(shell, shell.getParent());
		}
	}
}
