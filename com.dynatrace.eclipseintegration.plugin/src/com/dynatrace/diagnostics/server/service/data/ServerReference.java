package com.dynatrace.diagnostics.server.service.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author michael.kumar
 */

@XmlRootElement(name = "server")
public class ServerReference extends ResponseReferenceBase{

    public ServerReference() {
    }

    @XmlAttribute(name="name") private String name;

    @XmlAttribute(name="uid") private Integer uid;

    /**
     * @param serverName the server name
     * @param serverId the server ID
     */
    public ServerReference(String serverName, Integer serverId) {
        super(serverName); // because of backward compatibility to old CodeLink Plugins we have to set the name also as "id"
        this.name = serverName;
        this.uid = serverId;
    }
}
