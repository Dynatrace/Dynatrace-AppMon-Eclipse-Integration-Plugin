package com.dynatrace.diagnostics.eclipseintegration;

import java.security.Principal;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.preference.IPreferenceStore;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.sdk.server.ServerConfiguration;

public class EclipseServerConfiguration implements ServerConfiguration {

	@Override
	public String getPassword() {
		ISecurePreferences preferenceStoreSecure = SecurePreferencesFactory.getDefault();
		try {
			return preferenceStoreSecure.get(Constants.PREF_SERVER_PASS, Constants.DEFAULT_SERVER_PASSWORD);
		} catch (StorageException e) {
			LogHelper.logError("Could not retrieve password from the secure store.");
			return null;
		}
	}

	@Override
	public Principal getUserPrincipal() {
		return this;
	}

	@Override
	public String getName() {
		ISecurePreferences preferenceStoreSecure = SecurePreferencesFactory.getDefault();
		try {
			return preferenceStoreSecure.get(Constants.PREF_SERVER_LOGIN, Constants.DEFAULT_SERVER_LOGIN);
		} catch (StorageException e) {
			LogHelper.logError("Could not retrieve username from the secure store.");
			return null;
		}
	}

	@Override
	public String getHost() {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		return Constants.getDefaultString(prefStore, Constants.PREF_SERVER_HOST, Constants.DEFAULT_SERVER_HOST);
	}

	@Override
	public int getPort() {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		return Constants.getDefaultInt(prefStore, Constants.PREF_SERVER_REST_PORT,
				isSSL() ? Constants.DEFAULT_SERVER_REST_PORT_SSL : Constants.DEFAULT_SERVER_REST_PORT_NO_SSL);
	}

	@Override
	public int getTimeout() {
		// this is a client connection timeout. We don't have any user dependent
		// settings for that, thus we hard-code a 0, which is infinite timeout
		// closing session can actually take a long time
		return 0;
	}

	@Override
	public boolean isSSL() {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		return Constants.getDefaultBoolean(prefStore, Constants.PREF_SERVER_SSL, Constants.DEFAULT_SERVER_SSL);
	}

	@Override
	public boolean isValidateCertificates() {
		return false;
	}

}
