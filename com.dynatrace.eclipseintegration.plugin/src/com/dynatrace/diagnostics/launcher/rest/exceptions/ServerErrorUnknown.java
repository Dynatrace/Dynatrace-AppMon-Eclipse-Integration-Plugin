package com.dynatrace.diagnostics.launcher.rest.exceptions;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Server returned an error for which no specific client-side handling
 * is not necessary, possible or needed.
 *
 * @author Michal.Weyer
 * @since 2016-04-12
 */
public class ServerErrorUnknown extends ServerReportedReason {

	private final String profileName;

	public ServerErrorUnknown(Response response, String profileName, String throwSpotId) {
		super(response, throwSpotId);
		this.profileName = profileName;
	}

	public String getProfileName() {
		return profileName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("profileName", profileName)
				.appendSuper(super.toString())
				.toString();
	}
}
