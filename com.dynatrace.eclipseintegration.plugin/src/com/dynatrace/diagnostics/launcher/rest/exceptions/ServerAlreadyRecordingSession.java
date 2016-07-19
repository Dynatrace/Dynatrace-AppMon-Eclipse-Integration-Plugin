package com.dynatrace.diagnostics.launcher.rest.exceptions;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
public class ServerAlreadyRecordingSession extends ServerReportedReason {

	public ServerAlreadyRecordingSession(Response response, String throwSpotId) {
		super(response, throwSpotId);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.toString();
	}
}
