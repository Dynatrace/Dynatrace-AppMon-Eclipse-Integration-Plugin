package com.dynatrace.diagnostics.launcher.eclipsedelegates;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.launcher.functionality.AgentConfigurationProvider;

public class DynatraceJavaLaunchDelegate extends JavaLaunchDelegate{

	private AgentConfigurationProvider agentConfigurationProvider = new AgentConfigurationProvider();

	@Override
	public IVMRunner getVMRunner(ILaunchConfiguration configuration, String mode) throws CoreException {
		return super.getVMRunner(configuration, ILaunchManager.RUN_MODE);
	}

	@Override
	public String getVMArguments(ILaunchConfiguration configuration)
			throws CoreException {
		assert agentConfigurationProvider != null;
		String arguments = super.getVMArguments(configuration);
		return agentConfigurationProvider.getVMArgumentsWithAgent(arguments, configuration);
	}

	@Override
	public String[] getEnvironment(ILaunchConfiguration configuration) throws CoreException {
		return agentConfigurationProvider.getPathExtension(super.getEnvironment(configuration));
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, final ILaunch launch, IProgressMonitor monitor) throws CoreException {
		ILaunchConfiguration configToUse = Activator.getDefault().getLauncherCallbacks().onNewLaunch(DynatraceJavaLaunchDelegate.class, configuration,
				launch);
		super.launch(configToUse, mode, launch, monitor);
	}

}
