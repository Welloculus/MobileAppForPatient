package com.transility.welloculus.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for device info parameters.
 *
 * @author arpit.garg
 */
public class DeviceInfoBean {

    private String device_state;

    private String device_udi;

    private String device_name;

    private String device_type;

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    private String start_date;

    public Boolean getIn_use() {
        return in_use;
    }

    public void setIn_use(Boolean in_use) {
        this.in_use = in_use;
    }

    private Boolean in_use;

    public void setHealth_data(ArrayList<HealthDataBean> health_data) {
        this.health_data = health_data;
    }

    private ArrayList<HealthDataBean> health_data;

    /**
     * Gets device state.
     *
     * @return the device state
     */
    public String getDevice_state() {
        return device_state;
    }

    /**
     * Gets device udi.
     *
     * @return the device udi
     */
    public String getDevice_udi() {
        return device_udi;
    }

    /**
     * Gets device name.
     *
     * @return the device name
     */
    public String getDevice_name() {
        return device_name;
    }

    /**
     * Instantiates a new Device info bean.
     *
     * @param device_state the device state
     * @param device_udi   the device udi
     * @param device_name  the device name
     * @param device_type  the device type
     */
    public DeviceInfoBean(String device_state, String device_udi, String device_name, String device_type) {
        this.device_state = device_state;
        this.device_udi = device_udi;
        this.device_name = device_name;
        this.device_type = device_type;
    }

    public DeviceInfoBean(String device_state, String device_udi, String device_name, String device_type, Boolean in_use) {
        this.device_state = device_state;
        this.device_udi = device_udi;
        this.device_name = device_name;
        this.device_type = device_type;
        this.in_use= in_use;
    }

    public DeviceInfoBean(String device_state, String device_udi, String device_name, String device_type, Boolean in_use , String deviceStartDate) {
        this.start_date = deviceStartDate;
        this.device_state = device_state;
        this.device_udi = device_udi;
        this.device_name = device_name;
        this.device_type = device_type;
        this.in_use= in_use;
    }

    /**
     * Gets device type.
     *
     * @return the device type
     */
    public String getDevice_type() {
        return device_type;
    }

    /**
     * Instantiates a new Device info bean.
     *
     * @param device_udi  the device udi
     * @param device_name the device name
     */
    public DeviceInfoBean(String device_udi, String device_name) {
        this.device_name = device_name;
        this.device_udi = device_udi;
    }

    /**
     * Gets health data.
     *
     * @return the health data
     */
    public List<HealthDataBean> getHealth_data() {
        return health_data;
    }



}