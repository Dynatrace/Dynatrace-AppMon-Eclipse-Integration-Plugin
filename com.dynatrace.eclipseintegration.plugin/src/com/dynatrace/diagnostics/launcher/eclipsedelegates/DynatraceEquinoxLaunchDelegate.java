package com.dynatrace.diagnostics.launcher.eclipsedelegates;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.pde.ui.launcher.EquinoxLaunchConfiguration;

import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.launcher.functionality.AgentConfigurationProvider;

public class DynatraceEquinoxLaunchDelegate extends EquinoxLaunchConfiguration {

	private AgentConfigurationProvider agentConfigurationProvider = new AgentConfigurationProvider();
	
	@Override
	public String[] getVMArguments(ILaunchConfiguration configuration) throws CoreException {
		String[] arguments = super.getVMArguments(configuration);
		return agentConfigurationProvider.getVMArgumentsWithAgent(arguments, configuration);
	}

	@Override
	public IVMRunner getVMRunner(ILaunchConfiguration configuration, String mode) throws CoreException {
		return super.getVMRunner(configuration, ILaunchManager.RUN_MODE);
	}
	@Override
	public String[] getEnvironment(ILaunchConfiguration configuration) throws CoreException {
		return agentConfigurationProvider.getPathExtension(super.getEnvironment(configuration));
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		Activator.getDefault().getLauncherCallbacks().onNewLaunch(DynatraceEquinoxLaunchDelegate.class, configuration, launch);
		super.launch(configuration, mode, launch, monitor);
	}
	
}
