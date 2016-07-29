package com.dynatrace.diagnostics.launcher.ui.actions;

import org.eclipse.debug.internal.ui.actions.RelaunchLastAction;

import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;

@SuppressWarnings("restriction")
public class DynatraceRunLastAction extends RelaunchLastAction {

	@Override
	public String getLaunchGroupId() {
		return Constants.DT_LAUNCH_GROUP;
	}

	@Override
	public String getMode() {
		return Constants.DT_LAUNCH_MODE;
	}

	@Override
	protected String getCommandId() {
		return "org.eclipse.debug.ui.commands.RunLast"; //$NON-NLS-1$
	}

	@Override
	protected String getDescription() {
		return StringResources.runDescription;
	}

	@Override
	protected String getText() {
		return StringResources.launch_with_agent;
	}

	@Override
	protected String getTooltipText() {
		return "";
	}


}
