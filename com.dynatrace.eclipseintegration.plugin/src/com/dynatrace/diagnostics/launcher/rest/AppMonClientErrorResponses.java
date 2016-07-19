package com.dynatrace.diagnostics.launcher.rest;

import java.net.UnknownHostException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.HttpHostConnectException;

import com.dynatrace.diagnostics.launcher.rest.exceptions.ClientConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ClientHostUnknownException;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
class AppMonClientErrorResponses {

	void codeLinkConnectErrorResponseThenThrow(Response response) {

	}

	void sendCodeLinkResponseErrorResponseThenThrow(Response response) {

	}

	void throwOnConnectionProblem(ProcessingException restEasyException) throws ClientConnectionFailedException, ClientHostUnknownException {

		int causeDepth = ExceptionUtils.indexOfType(restEasyException, HttpHostConnectException.class);
		if (causeDepth != -1) {
			HttpHostConnectException httpHostConnectException =
					(HttpHostConnectException) ExceptionUtils.getThrowableList( restEasyException).get(causeDepth);
			throw new ClientConnectionFailedException(httpHostConnectException.getHost(), "[ErrorLocation-5]");
		}

		causeDepth = ExceptionUtils.indexOfType(restEasyException, UnknownHostException.class);
		if (causeDepth != -1) {
			UnknownHostException exception = (UnknownHostException) ExceptionUtils.getThrowableList(restEasyException).get(causeDepth);
			throw new ClientHostUnknownException(exception.getMessage(), "[ErrorLocation-6]");
		}

		throw new RuntimeException("Exception thrown when attempting to connect to the AppMon Server REST service [ErrorLocation-7]", restEasyException);
	}
}
