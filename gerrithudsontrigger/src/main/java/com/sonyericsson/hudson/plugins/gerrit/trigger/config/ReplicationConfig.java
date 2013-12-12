package com.sonyericsson.hudson.plugins.gerrit.trigger.config;

import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritMirror;

/**
 * Each instance of this class holds data needed to trigger builds on replication events,
 * and is associated to one specific GerritServer.
 * A GerritServer can have at most one ReplicationConfig in its Config object.
 *
 * @author Mathieu Wang &lt;mathieu.wang@ericsson.com&gt;
 *
 */
public class ReplicationConfig {
    private static final String ENABLE_REPLICATION_JSON_KEY = "enableReplication";
    private static final String MIRRORS_JSON_KEY = "mirrors";
    private static final String ENABLE_MIRROR_SELECTION_IN_JOBS_JSON_KEY = "enableMirrorSelectionInJobs";

    private boolean enableReplication;
    private List<GerritMirror> mirrors;
    private boolean enableMirrorSelectionInJobs;

    /**
     * Standard constructor.
     * @param enableReplication whether to allow waiting on replication events.
     * @param mirrors the GerritMirror objects.
     * @param enableMirrorSelectionInJobs whether to allow mirror selection in jobs.
     */
    private ReplicationConfig(boolean enableReplication,
                                List<GerritMirror> mirrors,
                                boolean enableMirrorSelectionInJobs) {
        this.enableReplication = enableReplication;
        this.mirrors = mirrors;
        this.enableMirrorSelectionInJobs = enableMirrorSelectionInJobs;
    }

    /**
     * Copy constructor.
     * @param config the ReplicationConfig object to be copied, never null.
     */
    public ReplicationConfig(ReplicationConfig config) {
        enableReplication = config.isEnableReplication();
        if (config.getGerritMirrors() != null) {
            mirrors = new LinkedList<GerritMirror>();
            for (GerritMirror mirror : config.getGerritMirrors()) {
                mirrors.add(new GerritMirror(mirror.getHostName(), mirror.getDisplayName(), mirror.getTimeout()));
            }
        }
        enableMirrorSelectionInJobs = config.isEnableMirrorSelectionInJobs();
    }

    /**
     * Default constructor.
     */
    public ReplicationConfig() {
        this(false, new LinkedList<GerritMirror>(), false);
    }

    /**
     * If we enable waiting on replication events.
     *
     * @return true if so.
     */
    public boolean isEnableReplication() {
        return enableReplication;
    };

    /**
     * Get the list of GerritMirror objects.
     * @return the list.
     */
    public List<GerritMirror> getGerritMirrors() {
        return mirrors;
    }

    /**
     * Whether mirror selection in enabled in job config.
     * @return true if so.
     */
    public boolean isEnableMirrorSelectionInJobs() {
        return enableMirrorSelectionInJobs;
    }

    /**
     * Create a ReplicationConfig object from JSON.
     * @param formData the JSON data.
     * @return the ReplicationConfig object.
     */
    public static ReplicationConfig createReplicationConfigFromJSON(JSONObject formData) {
        ReplicationConfig replicationConfig;
        List<GerritMirror> mirrors = new LinkedList<GerritMirror>();

        boolean enableReplication = formData.has(ENABLE_REPLICATION_JSON_KEY);
        if (enableReplication) {
            JSONObject replicationConfigAsJSON = formData.getJSONObject(ENABLE_REPLICATION_JSON_KEY);
            Object mirrorsAsJSON = replicationConfigAsJSON.get(MIRRORS_JSON_KEY);
            if (mirrorsAsJSON instanceof JSONArray) {
                for (Object jsonObject : (JSONArray)mirrorsAsJSON) {
                    mirrors.add(GerritMirror.createGerritMirrorFromJSON((JSONObject)jsonObject));
                }
            } else if (mirrorsAsJSON instanceof JSONObject) {
                mirrors.add(GerritMirror.createGerritMirrorFromJSON((JSONObject)mirrorsAsJSON));
            }
            boolean enableMirrorSelectionInJobsAsJSON
                = replicationConfigAsJSON.getBoolean(ENABLE_MIRROR_SELECTION_IN_JOBS_JSON_KEY);
            replicationConfig = new ReplicationConfig(true, mirrors, enableMirrorSelectionInJobsAsJSON);
        } else {
            replicationConfig = new ReplicationConfig();
        }
        return replicationConfig;
    }
}
