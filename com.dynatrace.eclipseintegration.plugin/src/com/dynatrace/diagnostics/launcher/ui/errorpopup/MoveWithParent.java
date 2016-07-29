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
class MoveWithParent {

	private final List<WeakReference<Shell>> managedChildren = new ArrayList<WeakReference<Shell>>();
	private Point prevParentPosition = null;

	void handleParentMovement(Shell childShell) {
		final Composite parentShell = childShell.getParent();
		prevParentPosition = parentShell.getLocation();
		parentShell.addControlListener(new ControlAdapter() {
			@Override
			public void controlMoved(ControlEvent e) {
				moveAllToNewParentLocation(parentShell);
				prevParentPosition = parentShell.getLocation();
			}
		});

		managedChildren.add(new WeakReference<Shell>(childShell));
	}

	private void moveAllToNewParentLocation(Composite parentShell) {
		int xOffset = parentShell.getLocation().x - prevParentPosition.x;
		int yOffset = parentShell.getLocation().y - prevParentPosition.y;

		for (WeakReference<Shell> childOfMoved : managedChildren) {
			Shell popup = childOfMoved.get();
			if (popup == null || popup.isDisposed()) {
				continue;
			}

			Point prev = popup.getLocation();
			popup.setLocation(new Point(prev.x + xOffset, prev.y + yOffset));
		}
	}
}
