package com.dynatrace.diagnostics.launcher.functionality;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;

import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;

/**
 * Provides methods to modify command lines to inject the dT agent into startup
 *
 * @author michael.kumar
 *
 */
public class AgentConfigurationProvider {

	private static final String DEBUG_SWITCH = "-Xdebug";

	/**
	 * is only needed for JVMPI injection, i.e. for java 1.4. and currently unused
	 *
	 * @param env the current environment
	 * @return the environment with added/updated path variable
	 */
	public String[] getPathExtension(String[] env) {
		return env;
	}

	/**
	 * constructs the agent parameters and appends them to the current vm parameters
	 *
	 * @param configuration the current launch configuration which can be used to override the global defaults.
	 * @param quotePath if the agent path should be quoted or not. quoting the path (to escape spaces in the path) is unnecessary
	 *        if the launcher
	 *        accepts the vmargs as array but is needed if the vmargs are returned as a single string.
	 * @return vm parameters with agent
	 */
	private String getAgentString(ILaunchConfiguration configuration, boolean quotePath) {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();

		String serverHost = Constants.getDefaultString(prefStore, Constants.PREF_COLLECTOR_HOST, Constants.DEFAULT_COLLECTOR_HOST);
		int port = Constants.getDefaultInt(prefStore, Constants.PREF_COLLECTOR_AGENT_PORT, Constants.DEFAULT_COLLECTOR_LISTEN_TO_AGENTS_PORT);
		String agentLibraryPath = Constants.getDefaultString(prefStore, Constants.PREF_AGENT_PATH, "");

		String confAgentName = "";
		String confAdditionalParameters = "";
		if (configuration != null) {
			try {
				confAgentName = configuration.getAttribute(Constants.PREF_AGENT_NAME, "");
				confAdditionalParameters = configuration.getAttribute(Constants.PREF_AGENT_PARAMS, "");
			} catch (CoreException e) {
				throw new IllegalStateException("Exception when retrieving Dynatrace launch configuration [ErrorLocation-1]");
			}
		}
		String agentName = isNotEmpty(confAgentName) ? confAgentName : Constants.getDefaultString(prefStore, Constants.PREF_AGENT_NAME, Constants.DEFAULT_AGENT_NAME);
		String params = isNotEmpty(confAdditionalParameters) ? confAdditionalParameters : Constants.getDefaultString(prefStore, Constants.PREF_AGENT_PARAMS, "");

		if (port == 0 || serverHost.isEmpty() || agentLibraryPath.isEmpty()) {
			throw new IllegalArgumentException("Agent is not configured correctly! [ErrorLocation-2]");
		}

		StringBuilder arguments = new StringBuilder();

		arguments.append("-agentpath:");
		if (quotePath) {
			arguments.append("\"");
		}
		arguments.append(agentLibraryPath);
		if (quotePath) {
			arguments.append("\"");
		}
		arguments.append("=");
		arguments.append("name=");
		arguments.append(agentName);
		arguments.append(",server=");
		arguments.append(serverHost);
		if (port != Constants.DEFAULT_COLLECTOR_LISTEN_TO_AGENTS_PORT) {
			arguments.append(":").append(port);
		}
		if (!params.isEmpty()) {
			if (!params.startsWith(",")) {
				arguments.append(',');
			}
			arguments.append(params);
		}
		return arguments.toString();
	}

	/**
	 * cleans the current argument list from problematic VM parameters like debug switches, gc parameters etc.
	 *
	 * @param arguments the current arguments
	 * @return cleaned argument string
	 */
	private String sanitizeArguments(String arguments) {
		arguments = arguments.replace(DEBUG_SWITCH, "");
		return arguments;
	}

	/**
	 * cleans the current argument list from problematic VM parameters like debug switches, gc parameters etc.
	 *
	 * @param arguments the current arguments
	 * @return cleaned argument string
	 */
	private String[] sanitizeArguments(String[] arguments) {
		List<String> sanitized = new ArrayList<String>();
		for (String arg : arguments) {
			if (!arg.equals(DEBUG_SWITCH)) {
				sanitized.add(arg);
			}
		}
		return sanitized.toArray(new String[sanitized.size()]);
	}

	public String getVMArgumentsWithAgent(String vmArgs, ILaunchConfiguration configuration) {
		String sanitizedArguments = sanitizeArguments(vmArgs);
		sanitizedArguments += " " + getAgentString(configuration, true);
		return sanitizedArguments;
	}

	public String[] getVMArgumentsWithAgent(String[] vmArgs, ILaunchConfiguration configuration) {
		String[] sanitizedArguments = sanitizeArguments(vmArgs);
		String[] temp = new String[vmArgs.length + 1];
		System.arraycopy(sanitizedArguments, 0, temp, 0, vmArgs.length);
		temp[vmArgs.length] = getAgentString(configuration, false);
		return temp;
	}
}
