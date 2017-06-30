package com.transility.welloculus.beans;

import java.util.List;

/**
 * Created by sneha.bansal on 3/31/2017.
 */
public class PatientReportBean {

    private List<DeviceInfoBean> device_details;

    private PatientDetailsBean patient_details;

    /**
     * Gets device details.
     *
     * @return the device details
     */
    public List<DeviceInfoBean> getDevice_details()
    {
        return device_details;
    }

    /**
     * Sets device details.
     *
     * @param device_details the device details
     */
    public void setDevice_details(List<DeviceInfoBean> device_details)
    {
        this.device_details = device_details;
    }

    /**
     * Gets paitent details.
     *
     * @return the paitent details
     */
    public PatientDetailsBean getPaitentDetails()
    {
        return patient_details;
    }

    /**
     * Sets paitent details.
     *
     * @param patientDetails the patient details
     */
    public void setPaitentDetails (PatientDetailsBean patientDetails)
    {
        this.patient_details = patientDetails;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [device_details = "+ device_details +", PatientDetails = "+ patient_details +"]";
    }
}
