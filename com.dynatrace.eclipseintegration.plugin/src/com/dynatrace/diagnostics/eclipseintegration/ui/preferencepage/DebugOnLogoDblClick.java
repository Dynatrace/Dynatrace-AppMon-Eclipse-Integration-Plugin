package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;

/**
 * @author Michal.Weyer
 * @since 2016-04-06
 */
class DebugOnLogoDblClick {

	private final DesignerGenerated view;

	DebugOnLogoDblClick(DesignerGenerated view) {
		this.view = view;
	}

	void debugOnLogoDblClick() {
		view.companyLogoImg.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				// enter debug mode on mouse click on company image
				// middle button
				if (e.button == 2) {
					boolean debugVal = !Activator.getDefault().isDebug();
					Activator.getDefault().setDebug(debugVal);
					if (debugVal) {
						LogHelper.logInfo("debug on");
					}
					else {
						LogHelper.logInfo("debug off");
					}
				}
			}
		});
	}
}
