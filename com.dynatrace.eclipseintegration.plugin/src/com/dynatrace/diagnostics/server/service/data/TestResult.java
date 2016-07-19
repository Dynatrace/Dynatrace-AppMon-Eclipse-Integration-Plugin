package com.dynatrace.diagnostics.server.service.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestResult {

	@XmlAttribute
	public String name;
	@XmlAttribute(name = "package")
	public String pckg;
	@XmlAttribute
	public String status;

	@XmlElement(name = "measure")
	public List<Measure> measures;

	@Override
	public String toString() {
		return "TestResult [name=" + name + ", pckg=" + pckg + ", status=" + status + ", measures=" + measures + "]";
	}


}
