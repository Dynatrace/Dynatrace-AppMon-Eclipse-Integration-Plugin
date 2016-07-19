package com.dynatrace.diagnostics.server.service.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "testRun" )
@XmlAccessorType(XmlAccessType.FIELD)
public class TestRunPostEntity {

	@XmlAttribute public String versionBuild;
	@XmlAttribute public String versionMajor;
	@XmlAttribute public String versionMilestone;
	@XmlAttribute public String versionMinor;
	@XmlAttribute public String versionRevision;
	@XmlAttribute public String marker;
	@XmlAttribute public String platform;
	@XmlAttribute public String loadTestName;
	@XmlAttribute public String category;

	@Override
	public String toString() {
		return "TestRunPostEntity [versionBuild=" + versionBuild + ", versionMajor=" + versionMajor
				+ ", versionMilestone=" + versionMilestone + ", versionMinor=" + versionMinor + ", versionRevision="
				+ versionRevision + ", marker=" + marker + ", platform=" + platform + ", loadTestName=" + loadTestName
				+ ", category=" + category + "]";
	}

}
