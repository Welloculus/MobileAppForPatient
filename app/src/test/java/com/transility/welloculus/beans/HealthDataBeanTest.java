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
 * Test Cases of {@link HealthDataBeanTest}
 *
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class HealthDataBeanTest {
    HealthDataBean healthDataBean;

    @Before
    public void setUpDeviceInfo() {
        healthDataBean = new HealthDataBean();
    }

    @Test
    public void testsetEntry_created(){
        String entry_created = "entry_created";
        healthDataBean.setEntry_created(entry_created);
        Assert.assertEquals("service name is not same",entry_created,healthDataBean.getEntry_created());
    }

    @Test
    public void testgetEntry_created(){
        String entry_created = "entry_created";
        healthDataBean.setEntry_created(entry_created);
        Assert.assertEquals("service name is not same",entry_created,healthDataBean.getEntry_created());
    }

    @Test
    public void testsetValue(){
        String value = "value";
        healthDataBean.setValue(value);
        Assert.assertEquals("service name is not same",value,healthDataBean.getValue());
    }

    @Test
    public void testgetValue(){
        String value = "value";
        healthDataBean.setValue(value);
        Assert.assertEquals("service name is not same",value,healthDataBean.getValue());
    }

    @Test
    public void testsetKey(){
        String key = "key";
        healthDataBean.setKey(key);
        Assert.assertEquals("service name is not same",key,healthDataBean.getKey());
    }

    @Test
    public void testgetKey(){
        String key = "key";
        healthDataBean.setKey(key);
        Assert.assertEquals("service name is not same",key,healthDataBean.getKey());
    }

    @Test
    public void testtoString(){
        String key = "key";
        healthDataBean.setKey(key);
        Assert.assertNotNull("to string returned null",healthDataBean.toString());
    }

}
