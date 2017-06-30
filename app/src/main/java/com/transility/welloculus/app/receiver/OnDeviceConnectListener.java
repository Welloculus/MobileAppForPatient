package com.transility.welloculus.app.receiver;

import android.view.View;

import com.transility.welloculus.beans.DeviceInfoBean;

/**
 * The interface On device connect listener.
 */
public interface OnDeviceConnectListener {
    /**
     * On connect click.
     *
     * @param deviceInfo the device info
     */
    void onConnectClick(DeviceInfoBean deviceInfo);

    void onConnectClick(DeviceInfoBean deviceInfo, View v);

}