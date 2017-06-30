package com.transility.welloculus.httpclient;

/**
 * The interface Url config.
 */
public interface UrlConfig {
    /**
     * The constant BASE_URL.
     */
    String BASE_URL = "https://gu4fki4e80.execute-api.eu-west-1.amazonaws.com/dev";
    String BASE_URL2 = "https://s8hvo0r5x6.execute-api.eu-west-1.amazonaws.com/dev";
    /**
     * The constant getAllDevices.
     */
    String getAllDevices = BASE_URL + "/device";
    String getAllDevices2 = BASE_URL2;
    /**
     * The constant getDeviceList.
     */
    String getDeviceList = getAllDevices +"?username=";
    /**
     * The constant postHealthData.
     */
    String postHealthData = BASE_URL + "/patient-health-data";
    /**
     * The constant getPaitentReport.
     */
    String getPaitentReport = BASE_URL+"/patient-health-data?";

    String updateUserProfile = BASE_URL+"/patient";


}