package com.transility.welloculus.ui;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoRefreshToken;
import com.transility.welloculus.BuildConfig;
import com.transility.welloculus.cognito.CognitoHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by sneha.bansal on 4/28/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class DeviceActivityTest {

    private DeviceListActivity2 activity;
    private CognitoUserSession cognitoUserSession;
    private Context context;


    @Before
    public void setUp() {

        context = RuntimeEnvironment.application.getBaseContext();
        CognitoHelper.setUser("sneha03");
        cognitoUserSession  = Mockito.mock(CognitoUserSession.class);
        Mockito.when(cognitoUserSession.getRefreshToken()).thenReturn(new CognitoRefreshToken("refresh_token"));
        Mockito.when(cognitoUserSession.getAccessToken()).thenReturn(new CognitoAccessToken("access_token"));
        Mockito.when(cognitoUserSession.getIdToken()).thenReturn(new CognitoIdToken("id_token"));
        CognitoHelper.setCurrSession(cognitoUserSession);
        activity = Robolectric.buildActivity(DeviceListActivity2.class)
                .create()
                .resume()
                .visible()
                .get();
    }

    @Test
    public void testShouldNotBeNull() throws Exception {
        Assert.assertNotNull("activity is null.", activity);
    }

    @Test
    public void testDeviceListShouldNotbeZero() throws Exception {
        Assert.assertTrue("list size  is zero.", activity.deviceInfoList.size()>0);
    }

    @Test
    public void testDeviceListShouldbeZero() throws Exception {
        CognitoHelper.setUser("");
        Assert.assertTrue("list size  is zero.", activity.deviceInfoList.size()>0);
    }


    @After
    public void tearDown(){
        activity=null;
        CognitoHelper.setUser(null);
    }
}
