package com.dynatrace.diagnostics.launcher.ui.actions;

import org.eclipse.debug.ui.actions.OpenLaunchDialogAction;

import com.dynatrace.diagnostics.eclipseintegration.Constants;

public class OpenDynatraceConfigurations extends OpenLaunchDialogAction {

	public OpenDynatraceConfigurations() {
		super(Constants.DT_LAUNCH_GROUP);
	}
}
