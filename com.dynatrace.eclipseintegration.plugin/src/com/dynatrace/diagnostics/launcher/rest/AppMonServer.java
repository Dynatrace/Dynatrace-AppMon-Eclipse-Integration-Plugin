package com.dynatrace.diagnostics.launcher.rest;

import static java.lang.String.format;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ProfileNotFound;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAlreadyRecordingSession;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationRequiresSSLException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerErrorUnknown;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerHostUnknownException;
import com.dynatrace.diagnostics.server.service.data.TestRun;
import com.dynatrace.diagnostics.server.service.data.TestRunPostEntity;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
class AppMonServer {

	private final ResteasyClient restEasy;
	private final AppMonServerErrorResponses errorResponses = new AppMonServerErrorResponses();

	AppMonServer(ResteasyClient restEasy) {
		this.restEasy = restEasy;
	}

	@SuppressWarnings("SpellCheckingInspection")
	String startSession(String profileName)
			throws ProfileNotFound, ServerErrorUnknown, ServerAlreadyRecordingSession,
			ServerHostUnknownException, ServerConnectionFailedException, ServerAuthenticationException,
			ServerAuthenticationRequiresSSLException {

		Response response = null;
		try {
			ResteasyWebTarget target = restEasy.target(getAppMonServerURL() + "profiles/" + profileName + "/startrecording");
			MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
			formData.add("recordingOption", "all");
			formData.add("isSessionLocked", "false");
			formData.add("isTimeStampAllowed", "false");
			String sessionName = profileName + ' ' + DateFormat.getDateTimeInstance().format(new Date());
			formData.add("description", sessionName);
			formData.add("presentableName", sessionName);

			basicAuthentication(target);
			response = target.request(MediaType.TEXT_XML_TYPE).acceptLanguage(Locale.ENGLISH).post(Entity.form(formData));
			response.bufferEntity();
			logResponse(target.getUri(), response);
			errorResponses.startSessionErrorResponseThenThrow(response, profileName);

			return sessionName;

		} catch (ProcessingException restEasyException) {
			errorResponses.categorizeConnectionProblemRethrow(restEasyException);
			throw new IllegalStateException("The method above must always throw an exception [ErrorLocation-8]");
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	@SuppressWarnings("SpellCheckingInspection")
	void stopSession(String profileName)
			throws ServerHostUnknownException, ServerConnectionFailedException, ServerErrorUnknown,
			ServerAuthenticationException, ServerAuthenticationRequiresSSLException {

		Response response = null;
		try {
			ResteasyWebTarget target = restEasy.target( getAppMonServerURL() + "profiles/" + profileName + "/stoprecording");
			basicAuthentication(target);
			response = target.request(MediaType.TEXT_XML_TYPE).acceptLanguage(Locale.ENGLISH).get();
			response.bufferEntity();
			logResponse(target.getUri(), response);
			errorResponses.stopSessionErrorResponseThenThrow(response, profileName);

		} catch (ProcessingException restEasyException) {
			errorResponses.categorizeConnectionProblemRethrow(restEasyException);
			throw new IllegalStateException("The method above must always throw an exception [ErrorLocation-9]");
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	String registerNewTestRun(String profileName)
			throws ServerHostUnknownException, ServerConnectionFailedException, ServerErrorUnknown, ProfileNotFound,
			ServerAuthenticationException, ServerAuthenticationRequiresSSLException {

		Response response = null;
		try {
			ResteasyWebTarget target = restEasy.target( getAppMonServerURL() + "profiles/" + profileName + "/testruns");
			TestRunPostEntity toPost = new TestRunPostEntity();
			toPost.category = "unit";
			Calendar now = Calendar.getInstance();
			toPost.versionMajor = String.valueOf(now.get(Calendar.YEAR));
			toPost.versionMinor = String.valueOf(now.get(Calendar.MONTH) + 1);
			toPost.versionRevision = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
			toPost.versionBuild = new SimpleDateFormat("HH:mm:ss").format(now.getTime());

			basicAuthentication(target);
			response = target.request(MediaType.APPLICATION_XML_TYPE).acceptLanguage(Locale.ENGLISH).post(Entity.entity(toPost, MediaType.TEXT_XML_TYPE));
			response.bufferEntity();
			logResponse(target.getUri(), response);
			errorResponses.registerNewTestRunErrorResponseThenThrow(response, profileName);

			TestRun testRun = response.readEntity(TestRun.class);
			return testRun.id;

		} catch (ProcessingException restEasyException) {
			errorResponses.categorizeConnectionProblemRethrow(restEasyException);
			throw new IllegalStateException("The method above must always throw an exception [ErrorLocation-10]");
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	TestRun getTestRun(String testRunId, String profileName)
			throws ServerHostUnknownException, ServerConnectionFailedException, ServerErrorUnknown,
			ServerAuthenticationException, ServerAuthenticationRequiresSSLException {

		Response response = null;
		try {
			ResteasyWebTarget target = restEasy.target( getAppMonServerURL() + "profiles/" + profileName + "/testruns/" + testRunId);
			basicAuthentication(target);

			response = target.request(MediaType.APPLICATION_XML_TYPE).acceptLanguage(Locale.ENGLISH).get();
			response.bufferEntity();
			logResponse(target.getUri(), response);
			errorResponses.getTestRunErrorResponseThenThrow(response, profileName);

			return response.readEntity(TestRun.class);

		} catch (ProcessingException restEasyException) {
			errorResponses.categorizeConnectionProblemRethrow(restEasyException);
			throw new IllegalStateException("The method above must always throw an exception [ErrorLocation-11]");
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	private void logResponse(URI address, Response response) {
		LogHelper.logOK(format("Server response received [uri:%s] [status:%s] [headers:%s] [content:%s]",
				address.toString(), response.getStatus(), response.getHeaders(), response.readEntity(String.class)));
	}

	private void basicAuthentication(ResteasyWebTarget target) {
		try {
			ISecurePreferences preferenceStoreSecure = SecurePreferencesFactory.getDefault();
			String serverLogin = preferenceStoreSecure.get(Constants.PREF_SERVER_LOGIN, Constants.DEFAULT_SERVER_LOGIN);
			String serverPass = preferenceStoreSecure.get(Constants.PREF_SERVER_PASS, Constants.DEFAULT_SERVER_PASSWORD);
			target.register(new BasicAuthentication(serverLogin, serverPass));
		} catch (StorageException e) {
			throw new RuntimeException("Exception while retrieving login/password from secure storage for REST request. [ErrorLocation-12]", e);
		}
	}

	private static String getAppMonServerURL() {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		String serverURL = Constants.getDefaultString(prefStore, Constants.PREF_SERVER_HOST, Constants.DEFAULT_SERVER_HOST);
		boolean useSSL = Constants.getDefaultBoolean(prefStore, Constants.PREF_SERVER_SSL, Constants.DEFAULT_SERVER_SSL);
		int serverPort = Constants.getDefaultInt(prefStore, Constants.PREF_SERVER_REST_PORT,
				useSSL ? Constants.DEFAULT_SERVER_REST_PORT_SSL : Constants.DEFAULT_SERVER_REST_PORT_NO_SSL);

		return  (useSSL ? "https://" : "http://") + serverURL + ":" + serverPort + "/rest/management/";
	}
}
