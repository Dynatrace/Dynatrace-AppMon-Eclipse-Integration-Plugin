package com.dynatrace.diagnostics.server.service.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author michael.kumar
 */

@XmlRootElement(name="servers")
public class ServersResponse extends ResponseBase {

    @XmlElementRefs({ @XmlElementRef(type = ServerReference.class)})
    public List<ServerReference> servers = new ArrayList<ServerReference>();

    public ServersResponse() {
    }

    public ServersResponse(List<ServerReference> servers) {
        this.servers = servers;
    }

}
