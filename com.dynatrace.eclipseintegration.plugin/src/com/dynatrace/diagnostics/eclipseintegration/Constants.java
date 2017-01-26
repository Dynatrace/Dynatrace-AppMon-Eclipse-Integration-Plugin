package com.dynatrace.diagnostics.eclipseintegration;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 *
 * @author michael.kumar
 */
public class Constants {

	public static final int DEFAULT_PORT = 30698;

	/**
	 * Property Dialog Constants
	 */
	public static final QualifiedName PROP_PROJECT_MARKER = new QualifiedName("dtdplugin", "participate"); //$NON-NLS-1$ //$NON-NLS-2$
	public static final String PROP_PROJECT_DEFAULT_MARKER_VALUE = "project.marker.default.value"; //$NON-NLS-1$

	/**
	 * Image Constants
	 */
	public static final String IMG_AGENT = "img.agent"; //$NON-NLS-1$
	public static final String IMG_COMPANY_LOGO = "img.company.logo"; //$NON-NLS-1$
	public static final String IMG_COMPANY_ICON = "img.company.icon"; //$NON-NLS-1$
	public static final String IMG_TEST_OK = "img.test.ok"; //$NON-NLS-1$
	public static final String IMG_TEST_FAILED = "img.test.failed"; //$NON-NLS-1$
	public static final String IMG_TEST_VOLATILE = "img.test.volatile"; //$NON-NLS-1$
	public static final String IMG_POPUP_ERROR = "img.popup.error"; //$NON-NLS-1$
	public static final String IMG_POPUP_CLOSE = "img.popup.close"; //$NON-NLS-1$
	public static final String IMG_WHERE_ICON = "img.dynatraceicon.where";

	/**
	 * Preference Dialog Constants
	 */
	public static final String PREF_REFRESH_WORKSPACE = "refreshWorkspace"; //$NON-NLS-1$
	public static final String PREF_SWITCH_TO_JAVA_BROWSING = "switchToJavabrowsing"; //$NON-NLS-1$

	public static final String IS_PREFSTORE_INITIALIZED = "isAlreadyInitialized"; //$NON-NLS-1$

	/**
	 * Look-up constants
	 */
	public static final byte FOUND = 0;
	public static final byte NOT_FOUND = 1;
	public static final byte ERROR = -1;

	// preferences dialog settings
	public static final String DEFAULT_SERVER_HOST = "localhost"; //$NON-NLS-1$
	public static final int DEFAULT_SERVER_REST_PORT_SSL = 8021;
	public static final int DEFAULT_SERVER_REST_PORT_NO_SSL = 8020;
	public static final int DEFAULT_PREF_TIMEOUT_TESTRUN_RETRIEVAL_SECONDS = 30;
	public static final String DEFAULT_PREF_TEST_CATEGORY = StringResources.preferences_global_test_category_unit;
	public static final String DEFAULT_SERVER_LOGIN = "admin"; //$NON-NLS-1$
	public static final String DEFAULT_SERVER_PASSWORD = "admin"; //$NON-NLS-1$
	public static boolean DEFAULT_SERVER_SSL = true;
	public static final String DEFAULT_COLLECTOR_HOST = "localhost";
	public static final int DEFAULT_COLLECTOR_LISTEN_TO_AGENTS_PORT = 9998;
	public static final String DEFAULT_CLIENT_HOST = "localhost";
	public static final int DEFAULT_CLIENT_PORT_NO_SSL = 8030;
	public static final int DEFAULT_CLIENT_PORT_SSL = 8031;
	public static final boolean DEFAULT_CLIENT_SSL = true;
	public static final boolean DEFAULT_ENABLE_CODE_LINK = true;
	public static final boolean DEFAULT_SWITCH_TO_JAVA_BROWSING_PERSPECTIVE = false;

	public static final String DEFAULT_AGENT_NAME = "eclipse"; //$NON-NLS-1$
	public static final String DEFAULT_SERVER_PROFILE = "Monitoring"; //$NON-NLS-1$

	public static final String[] WINDOWS_EXTENSIONS = new String[] { "*.dll" }; //$NON-NLS-1$
	public static final String[] LINUX_EXTENSIONS = new String[] { "*.so" };

	// preference storage and launch profile configuration keys
	public static final String PREF_AGENT_HOST = "dynatrace_agenthost"; //$NON-NLS-1$
	public static final String PREF_COLLECTOR_HOST = "dynatrace_collector_host"; //$NON-NLS-1$
	public static final String PREF_COLLECTOR_AGENT_PORT = "dynatrace_collector_agentport"; //$NON-NLS-1$
	public static final String PREF_SERVER_REST_PORT = "dynatrace_REST_port"; //$NON-NLS-1$
	public static final String PREF_TIMEOUT_TESTRUN_RETRIEVAL_SECONDS = "dynatrace_REST_testresults_get_timeout"; //$NON-NLS-1$
	public static final String PREF_TEST_CATEGORY_GLOBAL = "dynatrace_test_category_global";
	public static final String PREF_TEST_CATEGORY_PER_LAUNCH = "dynatrace_test_category_per_launch";
	public static final String PREF_AGENT_PATH = "dynatrace_agentpath"; //$NON-NLS-1$
	public static final String PREF_AGENT_NAME = "dynatrace_agentname"; //$NON-NLS-1$
	public static final String PREF_AGENT_PARAMS = "dynatrace_agentparams"; //$NON-NLS-1$

	public static final String PREF_SESSION_RECORD = "dynatrace_sessionrecord"; //$NON-NLS-1$
	public static final String PREF_SERVER_HOST = "serverhost";
	public static final String PREF_SERVER_PROFILE = "dynatrace_serverprofile"; //$NON-NLS-1$
	public static final String PREF_SERVER_NAME = "dynatrace_servername"; //$NON-NLS-1$
	public static final String PREF_SERVER_SSL = "dynatrace_serverssl"; //$NON-NLS-1$

	public static final String PREF_NODE_DYNATRACE_ECLIPSE_PLUGIN_PASSWORDS = "dynatrace-eclipse-plugin";
	public static final String PREF_SERVER_LOGIN = "dynatrace-server-login"; //$NON-NLS-1$
	public static final String PREF_SERVER_PASS = "dynatrace-server-pass"; //$NON-NLS-1$

	public static final String PREF_CLIENT_HOST = "dynatrace_client_host"; //$NON-NLS-1$
	public static final String PREF_CLIENT_PORT = "dynatrace_client_port"; //$NON-NLS-1$
	public static final String PREF_CLIENT_SSL = "dynatrace_client_via_ssl";
	public static final String PREF_ENABLE_CODELINK = "dynatrace_enable_codelink"; //$NON-NLS-1$
	public static final String PREF_SWITCH_TO_JAVA_BROWSING_PERSPECTIVE = "dynatrace_switch_to_java_browsing_perspective"; //$NON-NLS-1$

	public static final String DT_LAUNCH_GROUP = "com.dynatrace.run"; //$NON-NLS-1$
	public static final String DT_LAUNCH_MODE = "dynatrace"; //$NON-NLS-1$

	public static int getDefaultInt(IPreferenceStore prefStore, String preference, int defaultValue) {
		int value = prefStore.getInt(preference);
		if (value == 0) {
			value = prefStore.getDefaultInt(preference);
		}
		if (value == 0) {
			value = defaultValue;
		}
		return value;
	}

	public static String getDefaultString(IPreferenceStore prefStore, String preference, String defaultValue) {
		String value = prefStore.getString(preference);
		if (value == null || value.isEmpty()) {
			value = prefStore.getDefaultString(preference);
		}
		if (value == null || value.isEmpty()) {
			value = defaultValue;
		}
		return value;
	}

	public static boolean getDefaultBoolean(IPreferenceStore prefStore, String preference, boolean defaultValue) {
		String value = prefStore.getString(preference);
		if (value == null || value.isEmpty()) {
			value = prefStore.getDefaultString(preference);
		}
		if (value == null || value.isEmpty()) {
			return defaultValue;
		}
		return Boolean.valueOf(value);
	}
}
