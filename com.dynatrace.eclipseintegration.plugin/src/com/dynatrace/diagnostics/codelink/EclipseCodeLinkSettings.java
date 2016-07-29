package com.dynatrace.diagnostics.codelink;

import com.dynatrace.codelink.CodeLinkSettings;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;

public class EclipseCodeLinkSettings implements CodeLinkSettings {

	@Override
	public String getHost() {
		return Constants.getDefaultString(Activator.getDefault().getPreferenceStore(), Constants.PREF_CLIENT_HOST,
				Constants.DEFAULT_CLIENT_HOST);
	}

	@Override
	public int getPort() {
		int defaultPort = isSSL() ? Constants.DEFAULT_CLIENT_PORT_SSL : Constants.DEFAULT_CLIENT_PORT_NO_SSL;
		return Constants.getDefaultInt(Activator.getDefault().getPreferenceStore(), Constants.PREF_CLIENT_PORT,
				defaultPort);
	}

	@Override
	public boolean isEnabled() {
		return Constants.getDefaultBoolean(Activator.getDefault().getPreferenceStore(), Constants.PREF_ENABLE_CODELINK,
				Constants.DEFAULT_ENABLE_CODE_LINK);
	}

	@Override
	public boolean isSSL() {
		return Constants.getDefaultBoolean(Activator.getDefault().getPreferenceStore(), Constants.PREF_CLIENT_SSL,
				Constants.DEFAULT_CLIENT_SSL);
	}

	@Override
	public void setEnabled(boolean enabled) {
		Activator.getDefault().getPreferenceStore().setValue(Constants.PREF_ENABLE_CODELINK, enabled);
	}

}
