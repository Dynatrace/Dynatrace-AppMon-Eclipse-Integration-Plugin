package com.dynatrace.diagnostics.launcher.eclipsedelegates;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.pde.ui.launcher.EclipseApplicationLaunchConfiguration;

import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.launcher.functionality.AgentConfigurationProvider;

public class DynatraceEclipseLaunchDelegate extends EclipseApplicationLaunchConfiguration {
	
	private AgentConfigurationProvider launchConfigurationProvider = new AgentConfigurationProvider();

	@Override
	public String[] getVMArguments(ILaunchConfiguration configuration) throws CoreException {
		String[] arguments = super.getVMArguments(configuration);
		return launchConfigurationProvider.getVMArgumentsWithAgent(arguments, configuration);
	}

	@Override
	public IVMRunner getVMRunner(ILaunchConfiguration configuration, String mode) throws CoreException {
		return super.getVMRunner(configuration, ILaunchManager.RUN_MODE);
	}

	@Override
	public String[] getEnvironment(ILaunchConfiguration configuration) throws CoreException {
		return launchConfigurationProvider.getPathExtension(super.getEnvironment(configuration));
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		Activator.getDefault().getLauncherCallbacks().onNewLaunch(DynatraceEclipseLaunchDelegate.class, configuration, launch);
		super.launch(configuration, mode, launch, monitor);
	}
	
}
