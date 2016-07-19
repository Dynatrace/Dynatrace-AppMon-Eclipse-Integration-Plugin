package com.dynatrace.diagnostics.launcher.rest.exceptions;

import org.apache.http.HttpHost;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
public class ClientConnectionFailedException extends Exception {

	private final HttpHost host;

	public ClientConnectionFailedException(HttpHost host, String throwSpotId) {
		super("Failed connecting to [" + host + "] " + throwSpotId );
		this.host = host;
	}

	public HttpHost getHost() {
		return host;
	}
}
