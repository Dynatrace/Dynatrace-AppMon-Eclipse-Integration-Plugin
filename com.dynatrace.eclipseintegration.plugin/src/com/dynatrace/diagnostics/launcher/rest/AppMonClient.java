package com.dynatrace.diagnostics.launcher.rest;

import static java.lang.String.format;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.dynatrace.diagnostics.codelink.core.client.Protocol;
import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ClientConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ClientHostUnknownException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerHostUnknownException;
import com.dynatrace.diagnostics.server.service.data.CodeLinkLookupResponse;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
class AppMonClient {

	final ResteasyClient restEasy;
	private final AppMonClientErrorResponses errorResponses = new AppMonClientErrorResponses();

	AppMonClient(ResteasyClient restEasy) {
		this.restEasy = restEasy;
	}

	@SuppressWarnings("SpellCheckingInspection")
	CodeLinkLookupResponse codeLinkConnect(
			long sessionId, String ideVersion, Protocol.Version pluginVersion, String project, String path)

			throws ClientConnectionFailedException, ClientHostUnknownException {

		Response response = null;
		try {
			MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
			formData.add("ideid", Integer.toString(Protocol.IDE_ECLIPSE));
			formData.add("ideversion", ideVersion);
			formData.add("major", Integer.toString(pluginVersion.getMajor()));
			formData.add("minor", Integer.toString(pluginVersion.getMinor()));
			formData.add("revision", Integer.toString(pluginVersion.getRevision()));
			formData.add("sessionid", Long.toString(sessionId));
			formData.add("activeproject", project);
			formData.add("projectpath", path);

			ResteasyWebTarget target = restEasy.target(getAppMonClientURL() + "codelink/connect/");
			response = target.request().post(Entity.form(formData));
			response.bufferEntity();
			logResponse(response);
			errorResponses.codeLinkConnectErrorResponseThenThrow(response);

			return response.readEntity(CodeLinkLookupResponse.class);

		} catch (ProcessingException restEasyException) {
			errorResponses.throwOnConnectionProblem(restEasyException);
			throw new IllegalStateException("The method above must always throw an exception [ErrorLocation-3]");
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	@SuppressWarnings("SpellCheckingInspection")
	void sendCodeLinkResponse(long sessionId, int responseCode)
			throws ServerConnectionFailedException, ClientConnectionFailedException, ClientHostUnknownException,
			ServerHostUnknownException {
		Response response = null;
		try {
			ResteasyWebTarget target = restEasy.target(getAppMonClientURL() + "codelink/response/");//$NON-NLS-1$
			MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
			formData.add("sessionid", Long.toString(sessionId));
			formData.add("responsecode", Integer.toString(responseCode));

			response = target.request().post(Entity.form(formData));
			response.bufferEntity();
			logResponse(response);
			errorResponses.sendCodeLinkResponseErrorResponseThenThrow(response);

		} catch (ProcessingException restEasyException) {
			errorResponses.throwOnConnectionProblem(restEasyException);
			throw new IllegalStateException("The method above must always throw an exception [ErrorLocation-4]");
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}


	static String getAppMonClientURL() {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		String clientURL = Constants.getDefaultString(prefStore, Constants.PREF_CLIENT_HOST, Constants.DEFAULT_CLIENT_HOST);
		boolean useSSL = Constants.getDefaultBoolean(prefStore, Constants.PREF_CLIENT_SSL, Constants.DEFAULT_CLIENT_SSL);
		int clientPort = Constants.getDefaultInt(prefStore, Constants.PREF_CLIENT_PORT,
				useSSL ? Constants.DEFAULT_CLIENT_PORT_SSL : Constants.DEFAULT_CLIENT_PORT_NO_SSL);

		return  (useSSL ? "https://" : "http://") + clientURL + ":" + clientPort + "/rest/management/";
	}

	private void logResponse(Response response) {
		LogHelper.logOK(format("Server response received [uri:%s] [status:%s] [headers:%s] [content:%s]",
				getAppMonClientURL(), response.getStatus(), response.getHeaders(), response.readEntity(String.class)));
	}
}
