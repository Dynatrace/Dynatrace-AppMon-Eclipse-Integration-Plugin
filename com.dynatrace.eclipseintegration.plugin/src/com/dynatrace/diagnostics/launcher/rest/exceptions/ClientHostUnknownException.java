package com.dynatrace.diagnostics.launcher.rest.exceptions;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
public class ClientHostUnknownException extends Exception {

	private final String hostName;

	public ClientHostUnknownException(String hostName, String throwSpotId) {
		super("Failed connecting to [hostName:" + hostName + "] " + throwSpotId);
		this.hostName = hostName;
	}

	public String getHostName() {
		return hostName;
	}
}
