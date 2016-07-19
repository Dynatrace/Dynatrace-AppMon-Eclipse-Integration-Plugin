package com.dynatrace.diagnostics.server.service.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="profiles")
public class SystemProfiles extends ResponseBase {

	@XmlElementRefs({ @XmlElementRef(type = SystemProfileReference.class)})
	public List<SystemProfileReference> systemProfiles = new ArrayList<SystemProfileReference>(4);
	
	public SystemProfiles() {
	}
	
	public SystemProfiles(List<SystemProfileReference> systemProfiles) {
		this.systemProfiles = systemProfiles;
	}
	
}
