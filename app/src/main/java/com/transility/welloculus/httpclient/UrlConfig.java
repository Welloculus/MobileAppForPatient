package com.transility.welloculus.httpclient;

/**
 * The interface Url config.
 */
public interface UrlConfig {
    /**
     * The constant BASE_URL.
     */
    String BASE_URL = "http://ndimp.impetus.co.in:6046/iotGateway/healthRecords/addHealthRecords";
    String BASE_URL2 = "http://ndimp.impetus.co.in:6046/iotGateway/healthRecords/addHealthRecords";
    /**
     * The constant getAllDevices.
     */
    String getAllDevices = BASE_URL;
    String getAllDevices2 = BASE_URL2;
    /**
     * The constant getDeviceList.
     */
    String getDeviceList = getAllDevices;
    /**
     * The constant postHealthData.
     */
    String postHealthData = BASE_URL;

    /**
     * The constant getPaitentReport.
     */
    String getPaitentReport = BASE_URL;

    String updateUserProfile = BASE_URL;


}