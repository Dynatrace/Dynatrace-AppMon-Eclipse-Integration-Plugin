package com.dynatrace.diagnostics.server.service.data;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Base class for sending XML responses back via REST.
 */
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name="response")
class ResponseBase {

	/**
	 * Defines the href where details for this object can be retrieved.
	 */
	@XmlAttribute(name="href") private String href;
	
	ResponseBase() {
	}

	/**
	 * Constructs the response object with the given href.
	 *
	 * @param href
	 */
	ResponseBase(String href) {
		this.href = href;
	}
}
