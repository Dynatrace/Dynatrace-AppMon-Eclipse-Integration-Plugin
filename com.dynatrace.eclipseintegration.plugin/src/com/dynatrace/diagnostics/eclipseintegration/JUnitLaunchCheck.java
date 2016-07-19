package com.dynatrace.diagnostics.eclipseintegration;

import static java.util.Arrays.asList;

import java.util.List;

import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.jdt.junit.launcher.JUnitLaunchConfigurationDelegate;

import com.dynatrace.diagnostics.launcher.eclipsedelegates.DynatraceJunitLaunchDelegate;
import com.dynatrace.diagnostics.launcher.eclipsedelegates.DynatraceJunitPluginLaunchDelegate;

public class JUnitLaunchCheck {


	private static final List<Class<? extends JUnitLaunchConfigurationDelegate>> JUNIT_LAUNCH_CLASSES = asList(DynatraceJunitLaunchDelegate.class, DynatraceJunitPluginLaunchDelegate.class);

	@SuppressWarnings({ "unchecked", "SuspiciousMethodCalls" })
	public static boolean isJUnitLaunch(Class<? extends LaunchConfigurationDelegate> launchDelegateClass) {
		return JUNIT_LAUNCH_CLASSES.contains(launchDelegateClass);
	}
}
