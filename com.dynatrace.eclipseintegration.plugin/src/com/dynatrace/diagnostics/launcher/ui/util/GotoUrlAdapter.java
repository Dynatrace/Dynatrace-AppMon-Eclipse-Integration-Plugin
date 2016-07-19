package com.dynatrace.diagnostics.launcher.ui.util;

import java.net.URL;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;

/**
 * @author Michal.Weyer
 * @since 2016-04-22
 */
public final class GotoUrlAdapter extends SelectionAdapter {

	private final String url;

	public GotoUrlAdapter(String url) {
		this.url = url;
	}

	@Override
	public void widgetSelected(SelectionEvent selectionEvent) {
		try {
			IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
			browser.openURL(new URL(url));
		} catch (Exception e) {
			LogHelper.logError("Error navigating to URL [ErrorLocation-58]", e);
		}
	}
}
