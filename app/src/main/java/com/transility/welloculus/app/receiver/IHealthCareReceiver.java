package com.transility.welloculus.app.receiver;

import com.transility.welloculus.beans.DeviceInfoBean;

/**
 * The interface Health care receiver.
 */
public interface IHealthCareReceiver {
    /**
     * On heart rate received.
     *
     * @param heartRate  the heart rate
     * @param deviceInfo the device info
     */
    void onHeartRateReceived(int heartRate, DeviceInfoBean deviceInfo);

    /**
     * Is connected.
     *
     * @param connect the connect
     */
    void isConnected(boolean connect);
}
