package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.diagnostics.launcher.ui.util.GotoUrlAdapter;

/**
 * @author Michal.Weyer
 * @since 2016-04-06
 */
class Links {

	private final DesignerGenerated view;

	Links(DesignerGenerated view) {
		this.view = view;
	}

	void initLinks() {
		view.helpAndTroubleshootingLink.addSelectionListener(new GotoUrlAdapter(StringResources.preferences_global_link_help_url));
		view.dynatraceLink.addSelectionListener(new GotoUrlAdapter(StringResources.preferences_global_link_dynatrace_url));
	}
}
