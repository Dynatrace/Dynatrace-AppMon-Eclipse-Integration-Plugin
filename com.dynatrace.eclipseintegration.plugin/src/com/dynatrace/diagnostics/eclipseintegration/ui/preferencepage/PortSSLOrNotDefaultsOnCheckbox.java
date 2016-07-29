package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

/**
 * @author Michal.Weyer
 * @since 2016-04-06
 */
class PortSSLOrNotDefaultsOnCheckbox {

	private final Button sslCheckBox;
	private final Text portText;
	private final int defaultSSLPort;
	private final int defaultNonSSLPort;

	PortSSLOrNotDefaultsOnCheckbox(Button sslCheckBox, Text portText, int defaultSSLPort, int defaultNonSSLPort) {
		this.sslCheckBox = sslCheckBox;
		this.portText = portText;
		this.defaultSSLPort = defaultSSLPort;
		this.defaultNonSSLPort = defaultNonSSLPort;
	}


	void attachHandlers() {
		sslCheckBox.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateTextValue();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				updateTextValue();
			}
		});
	}

	private void updateTextValue() {
		boolean sslSelected = sslCheckBox.getSelection();
		if (sslSelected && isDefaultNonSSLPort()) {
			setDefaultSSLPort();
			return;
		}
		if (!sslSelected && isDefaultSSLPort()) {
			setDefaultNonSSLPort();
		}
	}


	private void setDefaultSSLPort() {
		portText.setText(Integer.toString(defaultSSLPort));
	}

	private void setDefaultNonSSLPort() {
		portText.setText(Integer.toString(defaultNonSSLPort));
	}

	private boolean isDefaultSSLPort() {
		try {
			int inputPortNbr = Integer.parseInt(portText.getText());
			return inputPortNbr == defaultSSLPort;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isDefaultNonSSLPort() {
		try {
			int inputPortNbr = Integer.parseInt(portText.getText());
			return inputPortNbr == defaultNonSSLPort;
		} catch (Exception e) {
			return false;
		}
	}
}
