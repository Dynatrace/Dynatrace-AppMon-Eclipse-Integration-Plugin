package com.dynatrace.diagnostics.server.service.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * REST response for codelink lookup which is sent from dT client to eclipse integration plugin
 *
 * @author michael.kumar
 * Date: 12.10.2009
 */

@XmlRootElement(name="codeLinkLookup")
public class CodeLinkLookupResponse extends ResponseBase {

    @XmlAttribute(name="versionMatched")
    public boolean versionMatched;

    @XmlAttribute(name="timedOut")
    public boolean timedOut;

    @XmlAttribute(name="className")
    public String className;

    @XmlAttribute(name="methodName")
    public String methodName;

    @XmlAttribute(name="sessionId")
    public long sessionId;

    @XmlElementWrapper(name="attributes")
    @XmlElements(@XmlElement(name="attribute", type=String.class))
    public String[] arguments;

}
