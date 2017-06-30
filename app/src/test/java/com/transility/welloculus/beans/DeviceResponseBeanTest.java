package com.transility.welloculus.beans;

import com.transility.welloculus.BuildConfig;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

/**
 *
 * Test Cases of {@link DeviceResponseBean}
 *
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class DeviceResponseBeanTest {

    DeviceResponseBean deviceResponseBean;

    @Before
    public void setUpDeviceInfo() {
        deviceResponseBean = new DeviceResponseBean();
    }

    @Test
    public void testsetServiceName(){
         DeviceInfoBean2 deviceInfo;
         String device_state = "connected";
         String device_udi = "123456";
         String device_name = "HXM123456";
         String device_type = "bluetooth";
        ArrayList<DeviceInfoBean2> responseContent = new ArrayList<>();
        deviceInfo = new DeviceInfoBean2(device_state, device_udi, device_name, device_type);
        responseContent.add(deviceInfo);
        deviceResponseBean.setResponseContent(responseContent);
        Assert.assertEquals("responseContent size is not same",responseContent.size(), deviceResponseBean.getResponseContent().size());
    }

    @Test
    public void testgetServiceName(){
        DeviceInfoBean2 deviceInfo;
        String device_state = "connected";
        String device_udi = "123456";
        String device_name = "HXM123456";
        String device_type = "bluetooth";
        ArrayList<DeviceInfoBean2> responseContent = new ArrayList<>();
        deviceInfo = new DeviceInfoBean2(device_state, device_udi, device_name, device_type);
        responseContent.add(deviceInfo);
        deviceResponseBean.setResponseContent(responseContent);
        Assert.assertEquals("responseContent size is not same",responseContent.size(), deviceResponseBean.getResponseContent().size());
    }


    @Test
    public void testtoString(){
        String errorMessage = "errorMessage";
        String errorCode = "errorCode";
        deviceResponseBean.setErrorMessage(errorMessage);
        deviceResponseBean.setErrorCode(errorCode);
        Assert.assertEquals("error name is not same",errorMessage, deviceResponseBean.getErrorMessage());
        Assert.assertEquals("error code is not same",errorCode, deviceResponseBean.getErrorCode());

    }

}
