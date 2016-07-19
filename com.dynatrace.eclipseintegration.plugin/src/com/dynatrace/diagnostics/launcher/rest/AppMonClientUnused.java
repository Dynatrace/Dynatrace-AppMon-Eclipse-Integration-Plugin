package com.dynatrace.diagnostics.launcher.rest;

import java.util.ArrayList;
import java.util.List;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.dynatrace.diagnostics.server.service.data.ServerReference;
import com.dynatrace.diagnostics.server.service.data.ServersResponse;
import com.dynatrace.diagnostics.server.service.data.SystemProfileReference;
import com.dynatrace.diagnostics.server.service.data.SystemProfiles;

/**
 * May prove useful in the future (?). Separated out from live code.
 *
 * @author Michal.Weyer
 * @since 2016-04-12
 */
class AppMonClientUnused extends AppMonClient {

	AppMonClientUnused(ResteasyClient restEasy) {
		super(restEasy);
	}

	@SuppressWarnings("unused") // someday?
	public List<String> getServers() {
		ResteasyWebTarget target = restEasy.target(getAppMonClientURL() + "servers");//$NON-NLS-1$
		ServersResponse response = target.request().get(ServersResponse.class);
		List<String> servers = new ArrayList<String>();
		if (response != null) {
			for (ServerReference server : response.servers) {
				servers.add(server.id);
			}
		}
		return servers;
	}

	@SuppressWarnings("unused") // someday?
	public List<String> getProfiles(String server) {
		ResteasyWebTarget target = restEasy.target(getAppMonClientURL() + "profiles/" + server);//$NON-NLS-1$
		SystemProfiles response = target.request().get(SystemProfiles.class);
		List<String> profiles = new ArrayList<String>();
		if (response != null) {
			for (SystemProfileReference profile : response.systemProfiles) {
				profiles.add(profile.id);
			}
		}
		return profiles;
	}
}
