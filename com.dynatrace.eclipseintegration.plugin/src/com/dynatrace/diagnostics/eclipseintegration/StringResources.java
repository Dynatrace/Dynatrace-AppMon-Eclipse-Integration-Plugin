package com.dynatrace.diagnostics.eclipseintegration;

import org.eclipse.osgi.util.NLS;

public final class StringResources extends NLS {

	static {
		NLS.initializeMessages("plugin", StringResources.class);
	}

	private StringResources() {
		// Do not instantiate
	}

	public static String javaproject_global_title;
	public static String preferences_global_slogan_build_optimize_test;

	public static String preferences_global_serverGroup;
	public static String preferences_global_server_label;
	public static String preferences_global_server_REST_port_label;
	public static String preferences_global_login_label;
	public static String preferences_global_password_label;
	public static String preferences_global_test_timeout;
	public static String preferences_global_test_category;
	public static String preferences_global_test_category_unit;
	public static String preferences_global_test_category_performance;
	public static String preferences_global_server_via_ssl_label;

	public static String preferences_global_agentGroup;
	public static String preferences_global_agentLibraryLabel;
	public static String preferences_global_agentLibraryBrowseLabel;
	public static String preferences_global_collector_host_label;
	public static String preferences_global_collector_agent_port_label;

	public static String preferences_global_CodeLinkGroup;
	public static String preferences_global_client_host;
	public static String preferences_global_client_port;
	public static String preferences_global_client_via_ssl_label;
	public static String preferences_global_enable_code_link;
	public static String preferences_global_switch_to_Java_Browsing_Perspective;

	public static String preferences_global_link_help;
	public static String preferences_global_link_help_url;
	public static String preferences_global_link_dynatrace;
	public static String preferences_global_link_dynatrace_url;

	public static String link_url_no_testruns;
	public static String link_url_error_code;

	public static String javaproject_properties_page1_enableChk;

	public static String preferences_page1_serverSettingsGroup;
	public static String preferences_page1_autostartServerChk;
	public static String preferences_page1_autostartServerChk_hint;

	public static String preferences_page1_lookupOptionsGroup;
	public static String preferences_page1_switchToBrowsingChk;

	public static String searchview_description;

	public static String server_startup_error;

	public static String runDescription;

	public static String preferences_page2_agent;
	public static String preferences_page2_agent_lib;
	public static String preferences_page2_agent_name;
	public static String preferences_page2_agent_additional_params;
	public static String preferences_page2_agent_lib_browse;

	public static String preferences_page2_session_record;
	public static String preferences_page2_session_record_label;
	public static String preferences_test_category_label;
	public static String preferences_page2_server;
	public static String preferences_page2_server_profile;
	public static String preferences_page2_refresh;

	public static String launchtab_message;
	public static String launchtab_name;

	// these strings are used directly in the xml configuration of the plugin,
	// they are only referenced here to avoid unused message warnings.
	public static String launchtabgroup_java;
	public static String launchtabgroup_osgi;
	public static String launchtabgroup_eclipse;
	public static String launchtabgroup_junit;
	public static String launchtabgroup_junitplugin;

	public static String launchshortcut_java;
	public static String launchshortcut_osgi;
	public static String launchshortcut_eclipse;
	public static String launchshortcut_junit;
	public static String launchshortcut_junitplugin;

	public static String launch_tab_button_label;
	public static String launch_with_agent;
	public static String launch_with_agent_as;
	public static String launch_with_agent_history;
	public static String launch_with_agent_configurations;

	public static String preparing_dynatrace_launch;
	public static String parsing_test_results;

	public static String testResultsView_testName;
	public static String testResultsView_testStatus;
	public static String testResultsView_metricGroup;
	public static String testResultsView_metricName;
	public static String testResultsView_metricValue;
	public static String testResultsView_metricUnit;
	public static String testResultsView_noResults;
	public static String testResultsView_moreInformation;
}
