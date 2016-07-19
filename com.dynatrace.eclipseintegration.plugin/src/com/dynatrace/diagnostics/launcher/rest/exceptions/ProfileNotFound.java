package com.dynatrace.diagnostics.launcher.rest.exceptions;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Michal.Weyer
 * @since 2016-04-12
 */
public class ProfileNotFound extends ServerReportedReason {

	private final String profileName;

	public ProfileNotFound(Response response, String profileName, String throwSpotId) {
		super(response, throwSpotId);
		this.profileName = profileName;
	}

	public String getProfileName() {
		return profileName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("profileName", profileName)
				.toString();
	}
}
