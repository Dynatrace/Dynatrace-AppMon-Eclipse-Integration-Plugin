package com.dynatrace.diagnostics.launcher.rest;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import java.net.UnknownHostException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.apache.http.conn.HttpHostConnectException;

import com.dynatrace.diagnostics.launcher.rest.exceptions.ProfileNotFound;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAlreadyRecordingSession;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerAuthenticationRequiresSSLException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerErrorUnknown;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ServerHostUnknownException;
import com.dynatrace.diagnostics.server.service.data.ErrorResponse;

/**
 * Handles all server-side error responses. Each method does all of the following:
 * (1) returns normally on non-error response
 * (2) throws an exception on error response.
 *
 * @author Michal.Weyer
 * @since 2016-04-12
 */
class AppMonServerErrorResponses {

	@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
	void categorizeConnectionProblemRethrow(ProcessingException restEasyException)
			throws ServerHostUnknownException, ServerConnectionFailedException {

		int causeDepth = ExceptionUtils.indexOfType(restEasyException, HttpHostConnectException.class);
		if (causeDepth != -1) {
			HttpHostConnectException httpHostConnectException =
					(HttpHostConnectException) ExceptionUtils.getThrowableList( restEasyException).get(causeDepth);
			throw new ServerConnectionFailedException(httpHostConnectException.getHost(), "[ErrorLocation-13]");
		}

		causeDepth = ExceptionUtils.indexOfType(restEasyException, UnknownHostException.class);
		if (causeDepth != -1) {
			UnknownHostException exception = (UnknownHostException) ExceptionUtils.getThrowableList(restEasyException).get(causeDepth);
			throw new ServerHostUnknownException(exception.getMessage() + "[ErrorLocation-15]");
		}

		throw new RuntimeException("Exception thrown when attempting to connect to the AppMon Server REST service [ErrorLocation-14]", restEasyException);
	}

	@SuppressWarnings("SpellCheckingInspection")
	void startSessionErrorResponseThenThrow(Response response, String profileName)
			throws ProfileNotFound, ServerAuthenticationException,
			ServerAlreadyRecordingSession, ServerErrorUnknown, ServerAuthenticationRequiresSSLException {

		if (response.getStatus() == HttpStatus.SC_OK) {
			return;
		}
		throwIfLoginFailed(response);
		throwIfSSLRequired(response);

		ErrorResponse errorResponse = parseEntity(response);
		switch (response.getStatus()) {
			case HttpStatus.SC_NOT_FOUND:
				throw new ProfileNotFound(response, profileName, "[ErrorLocation-16]");
			case HttpStatus.SC_FORBIDDEN:
				if (containsIgnoreCase(errorResponse.reason,
						"Session recording is not allowed if continuous transaction storage is enabled")
					|| containsIgnoreCase(errorResponse.reason,
						"Die Aufnahme von Sitzungen ist nicht erlaubt, wenn die laufende Transaktionsspeicherung aktiv ist")) {
					throw new ServerAlreadyRecordingSession(response, "[ErrorLocation-17]");
				}
		}

		throw new ServerErrorUnknown(response, profileName, "[ErrorLocation-18]");
	}

	void stopSessionErrorResponseThenThrow(Response response, String profileName)
			throws ServerErrorUnknown, ServerAuthenticationException, ServerAuthenticationRequiresSSLException {

		if (response.getStatus() == HttpStatus.SC_OK) {
			return;
		}
		throwIfLoginFailed(response);
		throwIfSSLRequired(response);

		throw new ServerErrorUnknown(response, profileName, "[ErrorLocation-19]");
	}

	void registerNewTestRunErrorResponseThenThrow(Response response, String profileName)
			throws ServerErrorUnknown, ProfileNotFound, ServerAuthenticationException, ServerAuthenticationRequiresSSLException {

		if (response.getStatus() == HttpStatus.SC_CREATED) {
			return;
		}
		throwIfLoginFailed(response);
		throwIfSSLRequired(response);

		String responseContent = response.readEntity(String.class);
		if (response.getStatus() == HttpStatus.SC_NOT_FOUND) {
			if (responseContent.contains("System profile") || responseContent.contains("AgentGroup")) {
				throw new ProfileNotFound(response, profileName, "[ErrorLocation-20]");
			}
		}

		throw new ServerErrorUnknown(response, profileName, "[ErrorLocation-21]");
	}

	void getTestRunErrorResponseThenThrow(Response response, String profileName)
			throws ServerErrorUnknown, ServerAuthenticationException, ServerAuthenticationRequiresSSLException {

		if (response.getStatus() == HttpStatus.SC_OK) {
			return;
		}
		throwIfLoginFailed(response);
		throwIfSSLRequired(response);

		throw new ServerErrorUnknown(response, profileName, "[ErrorLocation-22]");
	}



	private void throwIfLoginFailed(Response response) throws ServerAuthenticationException {
		String responseBody = response.readEntity(String.class);

		if (containsIgnoreCase(responseBody, "user name or password is incorrect")
				|| containsIgnoreCase(responseBody, "weil die Zugangsdaten nicht korrekt sind")) {
			throw new ServerAuthenticationException(response, "[ErrorLocation-23]");
		}

		if (response.getStatus() == HttpStatus.SC_UNAUTHORIZED
				&& (responseBody.contains("Web service login failed for user")
					|| responseBody.contains("Die Anmeldung am Webinterface mit dem Benutzernamen"))) {
			throw new ServerAuthenticationException(response, "[ErrorLocation-24]");
		}
	}

	@SuppressWarnings("SpellCheckingInspection")
	private void throwIfSSLRequired(Response response) throws ServerAuthenticationRequiresSSLException {
		String responseBody = response.readEntity(String.class);
		if (response.getStatus() == HttpStatus.SC_FORBIDDEN
				&& (responseBody.contains("It is forbidden to send authentication data using HTTP. Use HTTPS")
					|| responseBody.contains("Authentifizierung mit unsicherem HTTP ist nicht erlaubt")
					|| responseBody.contains("bertragung von Authentifizierungsdaten mit HTTP ist nicht erlaubt"))) {

			throw new ServerAuthenticationRequiresSSLException(response, "[ErrorLocation-25]");
		}
	}

	private ErrorResponse parseEntity(Response response) {
		ErrorResponse errorResponse = new ErrorResponse();
		MediaType mediaType = response.getMediaType();
		if (MediaType.TEXT_XML_TYPE.equals(mediaType) || MediaType.APPLICATION_XML_TYPE.equals(mediaType)) {
			errorResponse = response.readEntity(ErrorResponse.class);
		} else {
			errorResponse.reason = response.readEntity(String.class);
		}
		return errorResponse;
	}
}
