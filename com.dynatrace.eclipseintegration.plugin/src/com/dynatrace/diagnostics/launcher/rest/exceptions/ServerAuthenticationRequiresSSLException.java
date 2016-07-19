package com.dynatrace.diagnostics.launcher.rest.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Michal.Weyer
 * @since 2016-04-13
 */
public class ServerAuthenticationRequiresSSLException extends ServerReportedReason {

	public ServerAuthenticationRequiresSSLException(Response response, String throwSpotId) {
		super(response, throwSpotId);
	}
}
