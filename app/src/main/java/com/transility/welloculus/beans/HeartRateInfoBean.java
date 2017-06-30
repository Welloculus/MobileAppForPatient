package com.transility.welloculus.beans;

/**
 * Created by ImpetusDev on 4/5/2017.
 */
public class HeartRateInfoBean {
    private long time;
    private int heartRate;
    private String deviceID;
    private String deviceName;

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
     * Sets heart rate.
     *
     * @param heartRate the heart rate
     */
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
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
     * Gets heart rate.
     *
     * @return the heart rate
     */
    public int getHeartRate() {
        return heartRate;
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
     * @param heartRate  the heart rate
     * @param deviceID   the device id
     * @param deviceName the device name
     */
    public HeartRateInfoBean(long time, int heartRate, String deviceID, String deviceName) {
        this.time = time;
        this.heartRate = heartRate;
        this.deviceID = deviceID;
        this.deviceName = deviceName;
    }

    /**
     * Instantiates a new Heart rate info bean.
     */
    public HeartRateInfoBean() {

    }


}
