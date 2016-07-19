package com.dynatrace.diagnostics.server.service.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "measure")
@XmlAccessorType(XmlAccessType.FIELD)
public class Measure {

	@XmlAttribute
	public String metricGroup;
	@XmlAttribute
	public String name;

	@XmlAttribute
	public int numDegradedRuns;
	@XmlAttribute
	public int numFailingOrInvalidatedRuns;
	@XmlAttribute
	public int numImprovedRuns;
	@XmlAttribute
	public int numValidRuns;
	@XmlAttribute
	public double value;
	@XmlAttribute
	public double violationPercentage;

	@XmlAttribute
	public String unit;

	@Override
	public String toString() {
		return "Measure [metricGroup=" + metricGroup + ", name=" + name + ", numDegradedRuns=" + numDegradedRuns
				+ ", numFailingOrInvalidatedRuns=" + numFailingOrInvalidatedRuns + ", numImprovedRuns="
				+ numImprovedRuns + ", numValidRuns=" + numValidRuns + ", value=" + value + ", violationPercentage="
				+ violationPercentage + ", unit=" + unit + "]";
	}

}
