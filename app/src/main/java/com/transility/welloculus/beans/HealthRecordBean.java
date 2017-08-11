package com.transility.welloculus.beans;

/**
 * Created by ImpetusDev on 4/5/2017.
 */
public class HealthRecordBean {
    private int recordId;
    private long time;
    private String data;
    private String dataType;
    private String deviceID;
    private String userDeviceID;
    private String deviceName;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Sets device id.
     *
     * @param deviceID the device id
     */
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * Gets device name.
     *
     * @return the device name
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * Sets device name.
     *
     * @param deviceName the device name
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * Gets device id.
     *
     * @return the device id
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * Instantiates a new Heart rate info bean.
     *
     * @param time       the time
     * @param dataType      the type of data
     * @param data          Single record of data
     * @param deviceID   the device id
     * @param deviceName the device name
     */
    public HealthRecordBean(long time, String dataType, String data, String deviceID, String deviceName) {
        this.time = time;
        this.dataType = dataType;
        this.data = data;
        this.deviceID = deviceID;
        this.deviceName = deviceName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUserDeviceID() {
        return userDeviceID;
    }

    public void setUserDeviceID(String userDeviceID) {
        this.userDeviceID = userDeviceID;
    }

    /**
     * Instantiates a new Heart rate info bean.
     */
    public HealthRecordBean() {

    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
