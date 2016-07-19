package com.dynatrace.diagnostics.launcher.ui.errorpopup;

import java.lang.ref.WeakReference;

import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Michal.Weyer
 * @since 2016-04-26
 */
class MinimisedParentHide {

	private WeakReference<TransientErrorPopup> popupShell;

	void hideWhileParentMinimised(TransientErrorPopup shell) {
		this.popupShell = new WeakReference<TransientErrorPopup>(shell);
		Shell parent = (Shell) shell.getParent();
		if (parent.getMinimized()) {
			popupShell.get().setVisible(false);
		}

		parent.addShellListener(new ShellAdapter() {
			@Override
			public void shellIconified(ShellEvent e) {
				TransientErrorPopup transientErrorPopup = popupShell.get();
				if (transientErrorPopup == null) {
					return;
				}
				if (transientErrorPopup.isDisposed()) {
					return;
				}
				transientErrorPopup.setVisible(false);
			}

			@Override
			public void shellDeiconified(ShellEvent e) {
				TransientErrorPopup transientErrorPopup = popupShell.get();
				if (transientErrorPopup == null) {
					return;
				}
				if (transientErrorPopup.isDisposed()) {
					return;
				}
				transientErrorPopup.setVisible(true);
			}
		});
	}
}
