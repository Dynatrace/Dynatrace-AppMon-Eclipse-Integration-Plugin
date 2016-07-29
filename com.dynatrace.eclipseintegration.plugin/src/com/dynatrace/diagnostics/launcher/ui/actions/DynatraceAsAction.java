package com.dynatrace.diagnostics.launcher.ui.actions;

import org.eclipse.debug.ui.actions.LaunchShortcutsAction;

import com.dynatrace.diagnostics.eclipseintegration.Constants;

public class DynatraceAsAction extends LaunchShortcutsAction {
	public DynatraceAsAction() {
		super(Constants.DT_LAUNCH_GROUP);
	}
}
