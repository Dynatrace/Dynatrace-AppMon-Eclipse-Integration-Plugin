package com.dynatrace.diagnostics.server.service.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "testRun" )
@XmlAccessorType(XmlAccessType.FIELD)
public class TestRun {

	@XmlAttribute public String category;
	@XmlAttribute public String creationMode;
	@XmlAttribute public String href;
	@XmlAttribute public String id;

	@XmlAttribute public int numDegraded;
	@XmlAttribute public int numFailed;
	@XmlAttribute public int numImproved;
	@XmlAttribute public int numInvalidated;
	@XmlAttribute public int numPassed;
	@XmlAttribute public int numVolatile;

	@XmlAttribute public String platform;
	@XmlAttribute public String startTime;
	@XmlAttribute public String systemProfile;

	@XmlElement
	public List<TestResult> testResult;

	@Override
	public String toString() {
		return "TestRun [category=" + category + ", creationMode=" + creationMode + ", href=" + href + ", id=" + id
				+ ", numDegraded=" + numDegraded + ", numFailed=" + numFailed + ", numImproved=" + numImproved
				+ ", numInvalidated=" + numInvalidated + ", numPassed=" + numPassed + ", numVolatile=" + numVolatile
				+ ", platform=" + platform + ", startTime=" + startTime + ", systemProfile=" + systemProfile
				+ ", testResult=" + testResult + "]";
	}
}
