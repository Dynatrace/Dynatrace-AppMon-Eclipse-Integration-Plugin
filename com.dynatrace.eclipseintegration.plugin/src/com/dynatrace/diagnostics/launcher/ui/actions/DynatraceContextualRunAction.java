package com.dynatrace.diagnostics.launcher.ui.actions;

import org.eclipse.debug.ui.actions.ContextualLaunchAction;

import com.dynatrace.diagnostics.eclipseintegration.Constants;

/**
 * The action for the "run with Dynatrace as" context menu.
 * The actual configuration for the run targets can be found in plugin.xml
 * @author michael.kumar
 *
 */
public class DynatraceContextualRunAction  extends ContextualLaunchAction {

	public DynatraceContextualRunAction() {
		super(Constants.DT_LAUNCH_MODE);
	}
}
