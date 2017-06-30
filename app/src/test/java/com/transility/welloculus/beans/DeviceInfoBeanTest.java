package com.transility.welloculus.beans;

import com.transility.welloculus.BuildConfig;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 *
 * Test Cases of {@link DeviceInfoBean2}
 *
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)

public class DeviceInfoBeanTest {
    private DeviceInfoBean2 deviceInfo;
    private String device_state = "connected";
    private String device_udi = "123456";
    private String device_name = "HXM123456";
    private String device_type = "bluetooth";

    @Before
    public void setUpDeviceInfo() {
        deviceInfo = new DeviceInfoBean2(device_state, device_udi, device_name, device_type);
    }

    @Test
    public void testNotNullValues() {
        Assert.assertNotNull("Device name is null.", deviceInfo.getDevice_name());
        Assert.assertNotNull("Device id is null.", deviceInfo.getDevice_udi());
        Assert.assertNotNull("Device state is null.", deviceInfo.getDevice_state());
        Assert.assertNotNull("Device type is null.", deviceInfo.getDevice_type());
    }

    @Test
    public void testDeviceName() {
        Assert.assertEquals("Device name has changed.",device_name, deviceInfo.getDevice_name());
    }

    @Test
    public void testDeviceId() {
        Assert.assertEquals("Device id has changed.",device_udi, deviceInfo.getDevice_udi());
    }

    @Test
    public void testDeviceType() {
        Assert.assertEquals("Device type has changed.",device_type, deviceInfo.getDevice_type());
    }

    @Test
    public void testDeviceState() {
        Assert.assertEquals("Device state has changed.",device_state, deviceInfo.getDevice_state());
    }
}


