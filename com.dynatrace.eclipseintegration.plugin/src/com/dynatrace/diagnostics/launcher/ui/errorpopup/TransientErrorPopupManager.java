package com.dynatrace.diagnostics.launcher.ui.errorpopup;

import org.apache.commons.lang3.NotImplementedException;
import org.eclipse.swt.widgets.Display;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;

/**
 *
 */
public class TransientErrorPopupManager {

	private static final TransientErrorPopupManager INSTANCE = new TransientErrorPopupManager();
	private final StackPopupWindows stackPopupWindows = new StackPopupWindows();
	private final MoveWithParent moveWithParent = new MoveWithParent();
	private final FadeOut fadeOut = new FadeOut();
	private final MinimisedParentHide minimisedParentHide = new MinimisedParentHide();

	public static void logAndShowError(String errorMessage, Exception e) {
		LogHelper.logError(errorMessage, e);
		INSTANCE.displayErrorPopup(errorMessage, LinksShown.GLOBAL_DYNATRACE_CONFIG, StringResources.link_url_error_code);
	}

	public static void logAndShowError(String errorMessage) {
		LogHelper.logError(errorMessage, null);
		INSTANCE.displayErrorPopup(errorMessage, LinksShown.GLOBAL_DYNATRACE_CONFIG, StringResources.link_url_error_code);
	}

	public static void logAndShowRunConfigError(String errorMessage, String documentationLink) {
		LogHelper.logError(errorMessage);
		INSTANCE.displayErrorPopup(errorMessage, LinksShown.PER_RUN_DYNATRACE_CONFIG, documentationLink);
	}

	public static void logAndShowRunConfigError(String errorMessage, Exception e) {
		LogHelper.logError(errorMessage, e);
		INSTANCE.displayErrorPopup(errorMessage, LinksShown.PER_RUN_DYNATRACE_CONFIG, StringResources.link_url_error_code);
	}

	private enum LinksShown {
		GLOBAL_DYNATRACE_CONFIG,
		PER_RUN_DYNATRACE_CONFIG
	}

	private void displayErrorPopup(final String errorMessage, final LinksShown linksShown, final String documentationLink) {
		try {
			final Display display = Display.getDefault();
			display.asyncExec(new Runnable() {

				public void run() {
					final TransientErrorPopup shell = new TransientErrorPopup(errorMessage);
					if (LinksShown.GLOBAL_DYNATRACE_CONFIG.equals(linksShown)) {
						shell.showGlobalConfig();
					} else if (LinksShown.PER_RUN_DYNATRACE_CONFIG.equals(linksShown)) {
						shell.showRunConfig();
					} else {
						throw new NotImplementedException("displayErrorPopup for [linksShown:" + linksShown + "] [ErrorLocation-38]");
					}
					shell.setOnlineDocumentationLinkDestination(documentationLink);
					shell.layout();
					shell.pack();

					stackPopupWindows.displayOnTopOfPrev(shell);
					shell.setVisible(true); // doesn't grab focus

					fadeOut.fadeOutAfterDelay(shell, display);
					moveWithParent.handleParentMovement(shell);
					minimisedParentHide.hideWhileParentMinimised(shell);
				}
			});

		} catch (Exception e) {
			LogHelper.logError("[ErrorLocation-55]", e);
		}
	}
}
