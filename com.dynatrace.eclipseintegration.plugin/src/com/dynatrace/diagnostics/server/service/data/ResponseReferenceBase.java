package com.dynatrace.diagnostics.server.service.data;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name="responseref")
public class ResponseReferenceBase extends ResponseBase {

    private static final String ID = "id"; //$NON-NLS-1$

    @XmlAttribute(name = ID)
	public String id;

	ResponseReferenceBase() {
	}

	ResponseReferenceBase(String id) {
		this(id, null);
	}

	ResponseReferenceBase(String id, String href) {
		super(href);
		this.id = id;
	}

}
