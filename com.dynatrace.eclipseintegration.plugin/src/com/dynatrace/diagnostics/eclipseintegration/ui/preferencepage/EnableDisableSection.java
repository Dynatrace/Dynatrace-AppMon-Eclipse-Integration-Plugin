package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

/**
 * @author Michal.Weyer
 * @since 2016-04-06
 */
class EnableDisableSection {

	private final Button enableSectionCheck;
	private final List<Control> controls;

	EnableDisableSection(Button enableSectionCheck, List<Control> controls) {
		this.enableSectionCheck = enableSectionCheck;
		this.controls = controls;
	}

	EnableDisableSection attachHandlers() {
		enableSectionCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableDisableSection();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				enableDisableSection();
			}
		});
		return this;
	}

	void updateVisibility() {
		enableDisableSection();
	}

	private void enableDisableSection() {
		boolean selection = enableSectionCheck.getSelection();
		if (selection) {
			for (Control control : controls) {
				control.setEnabled(true);
			}
			return;
		}

		for (Control control : controls) {
			control.setEnabled(false);
		}
	}
}
