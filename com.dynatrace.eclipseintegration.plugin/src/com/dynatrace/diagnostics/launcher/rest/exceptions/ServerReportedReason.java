package com.dynatrace.diagnostics.launcher.rest.exceptions;

import static java.lang.String.format;

import javax.ws.rs.core.Response;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
class ServerReportedReason extends Exception {

	ServerReportedReason(Response response, String throwSpotId) {
		super(format(
				"Server returned an error response " +
				"[httpResponseCode:%d] [responseBody:%s] %s",
				response.getStatus(), response.readEntity(String.class), throwSpotId));

	}
}
