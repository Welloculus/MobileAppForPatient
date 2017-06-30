package com.transility.welloculus.utils;

/**
 * Created by sneha.bansal on 4/25/2017.
 * Test Cases of {@link com.transility.welloculus.utils.AppUtility}
 */

import android.content.Context;

import com.transility.welloculus.BuildConfig;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class AppUtilityTest {
    Context context;
    @Before
    public void setUp()
    {
        context = ShadowApplication.getInstance().getApplicationContext();;
    }

    /*Assertion to be find*/
    @Test
    public void testSendBroadCast_emptyMessage(){
        AppUtility.sendNotification(context,null,"",false);

    }

    @Test
    public void testSendBroadCast_notCritical(){
        AppUtility.sendNotification(context,"title","message",false);
    }

    @Test
    public void testSendBroadCast_critical(){
        AppUtility.sendNotification(context,"","",true);
    }

    @Test
    public void testisHearRateCritical_critical(){
        boolean status = AppUtility.isHearRateCritical(80);
        Assert.assertTrue("heartrate should be critical",status);
    }

    @Test
    public void testisHearRateCritical_normal(){
        boolean status = AppUtility.isHearRateCritical(100);
        Assert.assertFalse("heartrate should not be critical",status);
    }

    @Test
    public void testisEmailValid(){
        String email = "sneha.bansa@impetus.co.in";
        Assert.assertTrue("email id is not valid",AppUtility.isEmailValid(email));
        email = "sneha@gmail.com";
        Assert.assertTrue("email id is not valid",AppUtility.isEmailValid(email));
        email = "sneha";
        Assert.assertFalse("email id is  valid",AppUtility.isEmailValid(email));
    }
}
