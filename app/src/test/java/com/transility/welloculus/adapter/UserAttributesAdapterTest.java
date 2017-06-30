package com.transility.welloculus.adapter;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoRefreshToken;
import com.transility.welloculus.BuildConfig;
import com.transility.welloculus.cognito.CognitoHelper;
import com.transility.welloculus.ui.UserProfilesActivity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.text.SimpleDateFormat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class UserAttributesAdapterTest {
    private ActivityController<UserProfilesActivity> activity;
    private CognitoUserSession cognitoUserSession;
    private Context mContexl;

    @Before
    public void setUp() {
        mContexl  = RuntimeEnvironment.application.getApplicationContext();
        CognitoHelper.init(mContexl);
        CognitoHelper.setUser("sneha03");
        cognitoUserSession  = Mockito.mock(CognitoUserSession.class);
        Mockito.when(cognitoUserSession.getRefreshToken()).thenReturn(new CognitoRefreshToken("refresh_token"));
        Mockito.when(cognitoUserSession.getAccessToken()).thenReturn(new CognitoAccessToken("access_token"));
        Mockito.when(cognitoUserSession.getIdToken()).thenReturn(new CognitoIdToken("id_token"));
        CognitoHelper.setCurrSession(cognitoUserSession);
        activity = Robolectric.buildActivity(UserProfilesActivity.class).create();

    }

    @Test
    public void test001CheckIfCountOfItemsIsCorrect(){
        CognitoHelper.setUser("sneha03");

        UserAttributesAdapter userAttributesAdapter = new UserAttributesAdapter(mContexl,new SimpleDateFormat("dd/mm/yyyy"));
        Assert.assertEquals("Count is incorrect", CognitoHelper.getItemCount(),userAttributesAdapter.getCount());

    }

    @After
    public void tearDown(){

        activity=null;
        CognitoHelper.setCurrSession(null);
        CognitoHelper.setUser(null);
    }
}
