package com.dynatrace.diagnostics.launcher.ui.tabgroups;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.pde.ui.launcher.JUnitTabGroup;

public class JUnitPluginTabGroup extends JUnitTabGroup {
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		super.createTabs(dialog, mode);
		List<ILaunchConfigurationTab> tabList = new ArrayList<ILaunchConfigurationTab>();
		for (ILaunchConfigurationTab tab : getTabs()) {
			tabList.add(tab);
		}

		tabList.add(new DynatraceAgentConfigurationTab());
		
		setTabs(tabList.toArray(new ILaunchConfigurationTab[tabList.size()]));
	}
}
