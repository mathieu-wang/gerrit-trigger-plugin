package com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data;

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
        String hostName = obj.getString("hostName");
        String displayName = obj.getString("displayName");
        int timeout = obj.getInt("timeout");
        return new GerritMirror(hostName, displayName, timeout);
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
