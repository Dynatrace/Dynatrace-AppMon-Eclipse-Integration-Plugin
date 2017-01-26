package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_CLIENT_HOST;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_CLIENT_PORT_NO_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_CLIENT_PORT_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_CLIENT_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_COLLECTOR_HOST;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_COLLECTOR_LISTEN_TO_AGENTS_PORT;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_ENABLE_CODE_LINK;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_PREF_TIMEOUT_TESTRUN_RETRIEVAL_SECONDS;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_SERVER_HOST;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_SERVER_LOGIN;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_SERVER_PASSWORD;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_SERVER_REST_PORT_NO_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_SERVER_REST_PORT_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_SERVER_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_SWITCH_TO_JAVA_BROWSING_PERSPECTIVE;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_AGENT_PATH;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_CLIENT_HOST;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_CLIENT_PORT;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_CLIENT_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_COLLECTOR_AGENT_PORT;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_COLLECTOR_HOST;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_ENABLE_CODELINK;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_SERVER_HOST;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_SERVER_LOGIN;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_SERVER_PASS;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_SERVER_REST_PORT;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_SERVER_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_SWITCH_TO_JAVA_BROWSING_PERSPECTIVE;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_TIMEOUT_TESTRUN_RETRIEVAL_SECONDS;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.PREF_TEST_CATEGORY_GLOBAL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_PREF_TEST_CATEGORY;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.getDefaultBoolean;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.getDefaultInt;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.getDefaultString;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.launcher.functionality.TestRunCategoryCombo;

/**
 * @author Michal.Weyer
 * @since 2016-04-06
 */
class Configuration {

	private DesignerGenerated view;
	private ILog logger = Activator.getDefault().getLog();

	Configuration(DesignerGenerated view) {
		this.view = view;
	}

	void loadConfigurationIntoFields() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		ISecurePreferences preferenceStoreSecure = SecurePreferencesFactory.getDefault();

		try {
			view.serverText.setText(Constants.getDefaultString(preferenceStore, PREF_SERVER_HOST, DEFAULT_SERVER_HOST));
			view.serverRESTViaSSLCheck.setSelection( getDefaultBoolean(preferenceStore, PREF_SERVER_SSL, DEFAULT_SERVER_SSL));
			view.serverRESTPortText.setText(
					String.valueOf(getDefaultInt(preferenceStore, PREF_SERVER_REST_PORT,
							view.serverRESTViaSSLCheck.getSelection() ? DEFAULT_SERVER_REST_PORT_SSL : DEFAULT_SERVER_REST_PORT_NO_SSL))); // keep below serverRestViaSSLCheck
			view.loginText.setText( preferenceStoreSecure.get(PREF_SERVER_LOGIN, DEFAULT_SERVER_LOGIN));
			view.passwordText.setText( preferenceStoreSecure.get(PREF_SERVER_PASS, DEFAULT_SERVER_PASSWORD));
			view.testRetrievalTimeoutText.setText( String.valueOf(getDefaultInt(preferenceStore,
					PREF_TIMEOUT_TESTRUN_RETRIEVAL_SECONDS, DEFAULT_PREF_TIMEOUT_TESTRUN_RETRIEVAL_SECONDS)));
			view.testCategoryCombo.setText( Constants.getDefaultString(preferenceStore, PREF_TEST_CATEGORY_GLOBAL, DEFAULT_PREF_TEST_CATEGORY));

			view.agentLibraryText.setText(String.valueOf(getDefaultString(preferenceStore, PREF_AGENT_PATH, "")));
			view.collectorHost.setText( Constants.getDefaultString(preferenceStore, PREF_COLLECTOR_HOST, DEFAULT_COLLECTOR_HOST));
			view.collectorAgentConnectionPortText.setText(
					String.valueOf(getDefaultInt(preferenceStore, PREF_COLLECTOR_AGENT_PORT,
							DEFAULT_COLLECTOR_LISTEN_TO_AGENTS_PORT)));

			view.enableCodeLinkCheck.setSelection( getDefaultBoolean(preferenceStore, PREF_ENABLE_CODELINK, DEFAULT_ENABLE_CODE_LINK));
			view.clientHostText.setText( getDefaultString(preferenceStore, PREF_CLIENT_HOST, DEFAULT_CLIENT_HOST));
			view.clientViaSSLCheck.setSelection( getDefaultBoolean(preferenceStore, PREF_CLIENT_SSL, DEFAULT_CLIENT_SSL));

			view.clientPortText.setText(
					String.valueOf(getDefaultInt(preferenceStore, PREF_CLIENT_PORT,
							view.clientViaSSLCheck.getSelection() ? DEFAULT_CLIENT_PORT_SSL : DEFAULT_CLIENT_PORT_NO_SSL))); // keep below view.clientViaSSLCheck
			view.switchToJavaBrowsingCheck.setSelection(
					getDefaultBoolean(preferenceStore, PREF_SWITCH_TO_JAVA_BROWSING_PERSPECTIVE, DEFAULT_SWITCH_TO_JAVA_BROWSING_PERSPECTIVE));

		} catch (StorageException e) {
			logger.log(LogHelper.createErrorStatus("Error loading secure configuration-preferences (login and passwords). [ErrorLocation-44]", e));
		}
	}

	@SuppressWarnings("ConstantConditions")
	void performDefaults() {
		view.serverText.setText( DEFAULT_SERVER_HOST);
		view.loginText.setText( DEFAULT_SERVER_LOGIN);
		view.passwordText.setText( DEFAULT_SERVER_PASSWORD);
		view.testRetrievalTimeoutText.setText( Integer.toString(DEFAULT_PREF_TIMEOUT_TESTRUN_RETRIEVAL_SECONDS));
		view.testCategoryCombo.setText( DEFAULT_PREF_TEST_CATEGORY );
		view.testCategoryCombo.select(0);
		view.collectorHost.setText( DEFAULT_COLLECTOR_HOST);
		view.collectorAgentConnectionPortText.setText( String.valueOf(DEFAULT_COLLECTOR_LISTEN_TO_AGENTS_PORT));
		view.clientHostText.setText( DEFAULT_CLIENT_HOST);
		view.serverRESTViaSSLCheck.setSelection( DEFAULT_SERVER_SSL);
		view.clientViaSSLCheck.setSelection( DEFAULT_CLIENT_SSL);
		view.serverRESTPortText.setText( String.valueOf(DEFAULT_SERVER_SSL ? DEFAULT_SERVER_REST_PORT_SSL : DEFAULT_SERVER_REST_PORT_NO_SSL));
		view.clientPortText.setText( String.valueOf( DEFAULT_CLIENT_SSL ? DEFAULT_CLIENT_PORT_SSL : DEFAULT_CLIENT_PORT_NO_SSL));
		view.switchToJavaBrowsingCheck.setSelection( DEFAULT_SWITCH_TO_JAVA_BROWSING_PERSPECTIVE);

		view.enableCodeLinkCheck.setSelection( DEFAULT_ENABLE_CODE_LINK);
		view.enableCodeLinkCheck.notifyListeners(SWT.Selection, null);
	}

	void saveConfiguration() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		ISecurePreferences preferenceStoreSecure = SecurePreferencesFactory.getDefault();

		try {
			preferenceStore.setValue(PREF_SERVER_HOST, view.serverText.getText());
			preferenceStore.setValue(PREF_AGENT_PATH, view.agentLibraryText.getText());
			preferenceStore.setValue(PREF_COLLECTOR_HOST, view.collectorHost.getText());
			preferenceStore.setValue(PREF_COLLECTOR_AGENT_PORT, view.collectorAgentConnectionPortText.getText());
			preferenceStore.setValue(PREF_SERVER_SSL, String.valueOf(view.serverRESTViaSSLCheck.getSelection()));
			preferenceStore.setValue(PREF_SERVER_REST_PORT, view.serverRESTPortText.getText());
			preferenceStoreSecure.put(PREF_SERVER_LOGIN, view.loginText.getText(), false);
			preferenceStoreSecure.put(PREF_SERVER_PASS, view.passwordText.getText(), true);
			String timeoutText = view.testRetrievalTimeoutText.getText();
			preferenceStore.setValue(PREF_TIMEOUT_TESTRUN_RETRIEVAL_SECONDS, timeoutText.isEmpty() ? 1 : Integer.parseInt(timeoutText));
			preferenceStore.setValue(PREF_TEST_CATEGORY_GLOBAL, view.testCategoryCombo.getText());
			preferenceStore.setValue(PREF_ENABLE_CODELINK, String.valueOf(view.enableCodeLinkCheck.getSelection()));
			preferenceStore.setValue(PREF_CLIENT_HOST, view.clientHostText.getText());
			preferenceStore.setValue(PREF_CLIENT_SSL, String.valueOf(view.clientViaSSLCheck.getSelection()));
			preferenceStore.setValue(PREF_CLIENT_PORT, view.clientPortText.getText());
			preferenceStore.setValue(PREF_SWITCH_TO_JAVA_BROWSING_PERSPECTIVE, view.switchToJavaBrowsingCheck.getSelection());

			InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).flush();

		} catch (Exception e) {
			logger.log(LogHelper.createErrorStatus("Error storing secure configuration-preferences (login and passwords). [ErrorLocation-45]", e));
		}
	}
}
