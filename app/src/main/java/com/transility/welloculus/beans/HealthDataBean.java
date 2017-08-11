package com.transility.welloculus.beans;

import org.json.JSONObject;

/**
 * The type Health data bean.
 */
public class HealthDataBean {

    private long entryCreated;

    private JSONObject dataMap = new JSONObject();

    public JSONObject getDataMap() {
        return dataMap;
    }

    public void setDataMap(JSONObject dataMap) {
        this.dataMap = dataMap;
    }

    public long getEntryCreated() {
        return entryCreated;
    }

    public void setEntryCreated(long entryCreated) {
        this.entryCreated = entryCreated;
    }
}
