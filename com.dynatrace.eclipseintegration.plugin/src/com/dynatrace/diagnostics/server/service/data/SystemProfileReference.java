package com.dynatrace.diagnostics.server.service.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Provide the information from the System Profile for streaming out as XML
 *
 * @author dominik.stadler
 */
@XmlRootElement(name = SystemProfileReference.SYSTEM_PROFILE)
public class SystemProfileReference extends ResponseReferenceBase {

    private static final String CONNECTED_AGENTS = "connectedagents"; //$NON-NLS-1$
    private static final String IS_RECORDING = "isrecording"; //$NON-NLS-1$
    private static final String SERVER_ALIAS = "serveralias"; //$NON-NLS-1$
    public static final String SYSTEM_PROFILE = "systemprofile"; //$NON-NLS-1$

    /**
     * Number of connected agents
     */
    @XmlAttribute(name = CONNECTED_AGENTS) private Integer connectedAgentCount;

    /**
     * Session recording is turned on currently.
     */
    @XmlAttribute(name = IS_RECORDING) private Boolean isRecording;

    /**
     * The alias name of the Dynatrace Server this system profile references to.
     */
    @XmlAttribute(name = SERVER_ALIAS) private String serverAlias;

    public SystemProfileReference() {
    }

    /**
     * Construct with only the system profile id
     *
     * @param id
     */
    public SystemProfileReference(String id) {
        this(id, null, Boolean.FALSE, null);
    }

    /**
     * Construct with all settings
     *
     * @param id the system profile ID
     * @param connectedAgentCount the number of connected agents
     * @param isRecording <code>true</code> if the system profile is currently recording
     * @param serverAlias the server alias this system profile is related to
     */
    public SystemProfileReference(String id, Integer connectedAgentCount, Boolean isRecording, String serverAlias) {
        this(id, connectedAgentCount, isRecording, serverAlias, null);
    }

    /**
     * Construct with all settings and the href that should be included for details
     *
     * @param id
     * @param connectedAgentCount
     * @param isRecording
     * @param href
     */
    public SystemProfileReference(String id, Integer connectedAgentCount, Boolean isRecording, String serverAlias, String href) {
        super(id, href);

        this.connectedAgentCount = connectedAgentCount;
        this.isRecording = isRecording;
        this.serverAlias = serverAlias;
    }
}
