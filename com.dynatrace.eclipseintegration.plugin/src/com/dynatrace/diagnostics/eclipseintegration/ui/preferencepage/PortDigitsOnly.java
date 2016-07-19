package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * @author Michal.Weyer
 * @since 2016-04-06
 */
class PortDigitsOnly {

	private final Text portText;

	PortDigitsOnly(Text portText) {
		this.portText = portText;
	}

	void attachHandlers() {
		portText.setTextLimit(5);
		portText.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				for (char c : e.text.toCharArray()) {
					if (!Character.isDigit(c)) {
						e.doit = false;
						return;
					}
				}
			}
		});
	}
}
