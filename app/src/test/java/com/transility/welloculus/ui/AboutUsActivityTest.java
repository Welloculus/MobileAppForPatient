package com.transility.welloculus.ui;

import com.transility.welloculus.BuildConfig;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class AboutUsActivityTest {
    private AboutUsActivity activity;
    private ActivityController<AboutUsActivity> activityController;


    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(AboutUsActivity.class).create();
        activity = activityController.start().resume().visible().get();
    }

    @Test
    public void testIfActivityCreated(){
        Assert.assertNotNull("activity has not been created",activity);
    }
}
