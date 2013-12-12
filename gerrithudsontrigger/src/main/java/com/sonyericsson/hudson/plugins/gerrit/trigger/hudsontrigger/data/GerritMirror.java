package com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer;
import com.sonyericsson.hudson.plugins.gerrit.trigger.Messages;
import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl;
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.ReplicationConfig;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import net.sf.json.JSONObject;

/**
 * Represents a Gerrit mirror after which we wait for replication events.
 * @author Mathieu Wang &lt;mathieu.wang@ericsson.com&gt;
 *
 */
public class GerritMirror extends AbstractDescribableImpl<GerritMirror> {
    private static final Logger logger = LoggerFactory.getLogger(GerritMirror.class);
    private static final String HOSTNAME_JSON_KEY = "hostName";
    private static final String DISPLAYNAME_JSON_KEY = "displayName";
    private static final String TIMEOUT_JSON_KEY = "timeout";

    private String hostName;
    private String displayName;
    private int timeout;

    /**
     * Standard Constructor.
     * @param hostName the host name of the mirror
     * @param displayName the name that will be displayed in job config, if mirror config in jobs is enabled.
     * @param timeout maximum time we wait for a replication event.
     */
    public GerritMirror(String hostName, String displayName, int timeout) {
        this.hostName = hostName;
        this.displayName = displayName;
        this.timeout = timeout;
    }

    /**
     * Getter for host name.
     * @return the host name
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Getter for display name.
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Getter for time-out.
     * @return the time-out
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Creates a GerritMirror from a JSONObject.
     * @param obj the JSONObject.
     * @return a GerritMirror.
     */
    public static GerritMirror createGerritMirrorFromJSON(JSONObject obj) {
        String hostName = obj.getString(HOSTNAME_JSON_KEY);
        String displayName = obj.getString(DISPLAYNAME_JSON_KEY);
        int timeout = obj.getInt(TIMEOUT_JSON_KEY);
        return new GerritMirror(hostName, displayName, timeout);
    }

    /**
     * Get a specific Gerrit mirror associated to a server, using its display name.
     * @param serverName the Gerrit server.
     * @param displayName the display name of the mirror.
     * @return the Gerrit mirror that has the given display name.
     */
    public static GerritMirror getGerritMirror(String serverName, String displayName) {
        GerritServer server = PluginImpl.getInstance().getServer(serverName);
        if (server != null) {
            ReplicationConfig replicationConfig = server.getConfig().getReplicationConfig();
            if (replicationConfig.isEnableReplication()) {
                for (GerritMirror mirror : replicationConfig.getGerritMirrors()) {
                    if (mirror.getDisplayName().equals(displayName)) {
                        return mirror;
                    }
                }
            }
        } else {
            logger.warn(Messages.CouldNotFindServer(serverName));
        }
        return null;
    }

    /**
     * The Descriptor for a GerritMirror.
     */
    @Extension
    public static class GerritMirrorDescriptor extends Descriptor<GerritMirror> {
        @Override
        public String getDisplayName() {
            return "";
        }
    }
}
