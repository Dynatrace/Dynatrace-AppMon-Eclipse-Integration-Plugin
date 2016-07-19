package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * @author Michal.Weyer
 * @since 2016-05-18
 */
public class TimeoutDigitsOnly {

	private final Text timeoutText;

	TimeoutDigitsOnly(Text timeoutText) {
		this.timeoutText = timeoutText;
	}

	void attachHandlers() {
		timeoutText.setTextLimit(4);
		timeoutText.addVerifyListener(new VerifyListener() {
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
