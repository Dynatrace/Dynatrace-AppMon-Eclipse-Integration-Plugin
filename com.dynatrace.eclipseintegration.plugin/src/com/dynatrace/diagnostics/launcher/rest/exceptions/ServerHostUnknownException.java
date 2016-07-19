package com.dynatrace.diagnostics.launcher.rest.exceptions;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
public class ServerHostUnknownException extends Exception {

	private final String hostName;

	public ServerHostUnknownException(String hostName) {
		super("Failed connecting to [hostName:" + hostName + "]");
		this.hostName = hostName;
	}

	public String getHostName() {
		return hostName;
	}
}
