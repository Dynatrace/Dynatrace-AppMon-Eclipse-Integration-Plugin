package com.dynatrace.diagnostics.launcher.ui.errorpopup;

import java.lang.ref.WeakReference;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Michal.Weyer
 * @since 2016-04-15
 */
class FadeOut {

	private volatile WeakReference<Shell> mouseOverShell;

	void fadeOutAfterDelay(final TransientErrorPopup shell, final Display display) {
		shell.addMouseTrackListener(new MouseTrackListener() {
			@Override
			public void mouseEnter(MouseEvent e) {
				preventFadeout(shell);
			}

			@Override
			public void mouseExit(MouseEvent e) {
				if (shell.getBounds().contains(shell.getLocation().x + e.x, shell.getLocation().y + e.y)) {
					return;
				}
				resumeFadeout();
			}
			@Override
			public void mouseHover(MouseEvent e) {

			}
		});

		final int fadeOutAfterSeconds = 10;
		display.timerExec(fadeOutAfterSeconds * 1000, new Runnable() {
			@Override
			public void run() {
				if (shell.isDisposed() ) {
					return;
				}

				if (mouseHovering(shell)) {
					display.timerExec(fadeOutAfterSeconds * 1000, this);
					return;
				}

				shell.setAlpha(shell.getAlpha() > 20 ? shell.getAlpha() - 20 : 0);
				if (shell.getAlpha() == 0) {
					shell.close();
					return;
				}

				display.timerExec(100, this);
			}
		});
	}



	private boolean mouseHovering(TransientErrorPopup shell) {
		return mouseOverShell != null && mouseOverShell.get() == shell;
	}

	private void preventFadeout(TransientErrorPopup shell) {
		if (shell.isDisposed()) {
			return;
		}
		shell.setAlpha(255);
		mouseOverShell = new WeakReference<Shell>(shell);
	}

	private void resumeFadeout() {
		mouseOverShell = null;
	}
}
