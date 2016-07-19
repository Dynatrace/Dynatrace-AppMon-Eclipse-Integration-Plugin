package com.dynatrace.diagnostics.launcher.rest;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Form;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import com.dynatrace.diagnostics.codelink.core.client.Protocol.Version;
import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ClientConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ClientHostUnknownException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ProfileNotFound;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAlreadyRecordingSession;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationRequiresSSLException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerErrorUnknown;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerHostUnknownException;
import com.dynatrace.diagnostics.server.service.data.CodeLinkLookupResponse;
import com.dynatrace.diagnostics.server.service.data.TestRun;

/**
 * Responsibilities: <ol>
 * 		<li> facade for all REST communication
 * 		<li> subcomponent wiring
 * 		<li> REST response logging based on RESTEasy scaffolding. </ol>
 *
 *  @author Michal.Weyer
 */
public class RESTService {

	private final AppMonClient appMonClient;
	private final AppMonServer appMonServer;

	public RESTService() {
		ResteasyClient restEasy = new RESTClientSetup().createClient();
		restEasy.register(new ClientRequestFilter() {

			@Override
			public void filter(ClientRequestContext clientRequestContext) throws IOException {
				Object entity = clientRequestContext.getEntity();
				if (entity instanceof Form) {
					entity = ((Form) entity).asMap();
				}

				LogHelper.logOK("REST request outgoing: "
						+ clientRequestContext.getUri() + " "
						+ clientRequestContext.getMethod() + " "
						+ clientRequestContext.getHeaders()
						+ ((entity == null) ? "" : " " + entity));
			}
		});

		this.appMonClient = new AppMonClient(restEasy);
		this.appMonServer = new AppMonServer(restEasy);
	}

	public CodeLinkLookupResponse codeLinkConnect(
			long sessionId, String ideVersion, Version pluginVersion, String project, String path)
			throws ClientConnectionFailedException, ClientHostUnknownException	{

		return appMonClient.codeLinkConnect(sessionId, ideVersion, pluginVersion, project, path);
	}

	public void sendCodeLinkResponse(long sessionId, int responseCode)
			throws ServerConnectionFailedException, ClientConnectionFailedException, ClientHostUnknownException,
			ServerHostUnknownException {
		appMonClient.sendCodeLinkResponse(sessionId, responseCode);
	}

	public String startSession(String profileName)
			throws ProfileNotFound, ServerAlreadyRecordingSession, ServerConnectionFailedException,
			ServerHostUnknownException, ServerErrorUnknown, ServerAuthenticationException,
			ServerAuthenticationRequiresSSLException {

		return appMonServer.startSession(profileName);
	}

	public void stopSession(String profileName)
			throws ServerHostUnknownException, ServerConnectionFailedException, ServerErrorUnknown, ServerAuthenticationException,
			ServerAuthenticationRequiresSSLException {

		appMonServer.stopSession(profileName);
	}

	public String registerNewTestRun(String profileName)
			throws ServerHostUnknownException, ServerConnectionFailedException, ServerErrorUnknown, ProfileNotFound,
			ServerAuthenticationException, ServerAuthenticationRequiresSSLException {

		return appMonServer.registerNewTestRun(profileName);
	}

	public TestRun getTestRun(String testRunId, String profileName)
			throws ServerHostUnknownException, ServerConnectionFailedException, ServerErrorUnknown, ServerAuthenticationException,
			ServerAuthenticationRequiresSSLException {

		return appMonServer.getTestRun(testRunId, profileName);
	}
}
