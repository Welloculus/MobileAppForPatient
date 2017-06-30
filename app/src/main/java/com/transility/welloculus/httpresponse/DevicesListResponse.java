package com.transility.welloculus.httpresponse;

import android.util.Log;

import com.transility.welloculus.beans.DeviceResponseBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * The type Devices list response.
 */
public class DevicesListResponse extends BaseResponse {

    /**
     * The Device response.
     */
    DeviceResponseBean deviceResponse;

    /**
     * Gets device response.
     *
     * @return the device response
     */
    public DeviceResponseBean getDeviceResponse() {
        return deviceResponse;
    }

    /**
     * Sets device response.
     *
     * @param commonResponse the common response
     */
    public void setDeviceResponse(DeviceResponseBean commonResponse) {
        this.deviceResponse = commonResponse;
    }

    @Override
    public void processResponseSuccess(Object... arg) {
        //parsing logic goes here

        Type type = new TypeToken<DeviceResponseBean>() {
        }.getType();
        deviceResponse = new Gson().fromJson(getmData(), type);
        Log.d("deviceList "," "+ deviceResponse);
    }

}
