package com.transility.welloculus.ui;

import android.view.View;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoRefreshToken;
import com.transility.welloculus.BuildConfig;
import com.transility.welloculus.R;
import com.transility.welloculus.cognito.CognitoHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class ReportsActivityTest {

    private ReportsActivity activity;
    private CognitoUserSession cognitoUserSession;


    @Before
    public void setUp(){
        CognitoHelper.setUser("sneha03");
        cognitoUserSession  = Mockito.mock(CognitoUserSession.class);
        Mockito.when(cognitoUserSession.getRefreshToken()).thenReturn(new CognitoRefreshToken("refresh_token"));
        Mockito.when(cognitoUserSession.getAccessToken()).thenReturn(new CognitoAccessToken("access_token"));
        Mockito.when(cognitoUserSession.getIdToken()).thenReturn(new CognitoIdToken("id_token"));
        CognitoHelper.setCurrSession(cognitoUserSession);
        activity = Robolectric.buildActivity(ReportsActivity.class)
                .create()
                .resume()
                .visible()
                .get();
    }
    @Test
    public void testActivityNotNull() throws Exception { Assert.assertNotNull("activity is null.", activity);}

    @Test
    public void testProperView() throws Exception {

        if(!(activity.paintentReportMap.size()>0))
        {        Assert.assertTrue("Message is not been displayed",activity.findViewById(R.id.device_message).getVisibility()== View.VISIBLE);
            Assert.assertTrue("Report is  been displayed",activity.findViewById(R.id.report_container).getVisibility()== View.INVISIBLE);
        }
        else{
            Assert.assertTrue("Report is not been displayed",activity.findViewById(R.id.report_container).getVisibility()== View.VISIBLE);
            Assert.assertTrue("Message is  been displayed",activity.findViewById(R.id.device_message).getVisibility()== View.GONE);
        }
    }

    @Test
    public void testProperViewWhenReportIsAvail_FOR_BLUETOOTH() throws Exception {
        CognitoHelper.setUser("sneha03");
        Calendar mFromDate = Calendar.getInstance();
        mFromDate.set(Calendar.DATE,13);
        mFromDate.set(Calendar.MONTH,3);
        mFromDate.set(Calendar.YEAR,2017);
        mFromDate.set(Calendar.MINUTE, 0);
        mFromDate.set(Calendar.SECOND, 0);
        mFromDate.set(Calendar.HOUR_OF_DAY, 0);

        Calendar mToDate  = Calendar.getInstance();
        mToDate.set(Calendar.DATE,14);
        mFromDate.set(Calendar.MONTH,3);
        mFromDate.set(Calendar.YEAR,2017);
        mToDate.set(Calendar.MINUTE, 0);
        mToDate.set(Calendar.SECOND, 0);
        mToDate.set(Calendar.HOUR_OF_DAY, 0);

        activity.getReport(mFromDate.getTimeInMillis(),mToDate.getTimeInMillis());

        if(!(activity.paintentReportMap.size()>0))
        {        Assert.assertTrue("Message is not been displayed",activity.findViewById(R.id.device_message).getVisibility()== View.VISIBLE);
            Assert.assertTrue("Report is  been displayed",activity.findViewById(R.id.report_container).getVisibility()== View.INVISIBLE);
        }
        else{
            Assert.assertTrue("Report is not been displayed",activity.findViewById(R.id.report_container).getVisibility()== View.VISIBLE);
            Assert.assertTrue("Message is  been displayed",activity.findViewById(R.id.device_message).getVisibility()== View.GONE);
        }
    }

    @Test
    public void testProperViewWhenReportIsAvail_FOR_IOT() throws Exception {
        CognitoHelper.setUser("arpit");
        Calendar mFromDate = Calendar.getInstance();
        mFromDate.set(Calendar.DATE,13);
        mFromDate.set(Calendar.MONTH,3);
        mFromDate.set(Calendar.YEAR,2017);
        mFromDate.set(Calendar.MINUTE, 0);
        mFromDate.set(Calendar.SECOND, 0);
        mFromDate.set(Calendar.HOUR_OF_DAY, 0);

        Calendar mToDate  = Calendar.getInstance();
        mToDate.set(Calendar.DATE,14);
        mFromDate.set(Calendar.MONTH,3);
        mFromDate.set(Calendar.YEAR,2017);
        mToDate.set(Calendar.MINUTE, 0);
        mToDate.set(Calendar.SECOND, 0);
        mToDate.set(Calendar.HOUR_OF_DAY, 0);

        activity.getReport(mFromDate.getTimeInMillis(),mToDate.getTimeInMillis());

        if(!(activity.paintentReportMap.size()>0))
        {        Assert.assertTrue("Message is not been displayed",activity.findViewById(R.id.device_message).getVisibility()== View.VISIBLE);
            Assert.assertTrue("Report is  been displayed",activity.findViewById(R.id.report_container).getVisibility()== View.INVISIBLE);
        }
        else{
            Assert.assertTrue("Report is not been displayed",activity.findViewById(R.id.report_container).getVisibility()== View.VISIBLE);
            Assert.assertTrue("Message is  been displayed",activity.findViewById(R.id.device_message).getVisibility()== View.GONE);
        }
    }

    @Test
    public void testProperViewWhenReportIsAvail_FOR_RANDOMUSER() throws Exception {
        CognitoHelper.setUser("");
        Calendar mFromDate = Calendar.getInstance();
        mFromDate.set(Calendar.DATE,13);
        mFromDate.set(Calendar.MONTH,3);
        mFromDate.set(Calendar.YEAR,2017);
        mFromDate.set(Calendar.MINUTE, 0);
        mFromDate.set(Calendar.SECOND, 0);
        mFromDate.set(Calendar.HOUR_OF_DAY, 0);

        Calendar mToDate  = Calendar.getInstance();
        mToDate.set(Calendar.DATE,14);
        mFromDate.set(Calendar.MONTH,3);
        mFromDate.set(Calendar.YEAR,2017);
        mToDate.set(Calendar.MINUTE, 0);
        mToDate.set(Calendar.SECOND, 0);
        mToDate.set(Calendar.HOUR_OF_DAY, 0);

        activity.getReport(mFromDate.getTimeInMillis(),mToDate.getTimeInMillis());

        if(!(activity.paintentReportMap.size()>0))
        {        Assert.assertTrue("Message is not been displayed",activity.findViewById(R.id.device_message).getVisibility()== View.VISIBLE);
            Assert.assertTrue("Report is  been displayed",activity.findViewById(R.id.report_container).getVisibility()== View.INVISIBLE);
        }
        else{
            Assert.assertTrue("Report is not been displayed",activity.findViewById(R.id.report_container).getVisibility()== View.VISIBLE);
            Assert.assertTrue("Message is  been displayed",activity.findViewById(R.id.device_message).getVisibility()== View.GONE);
        }
    }

    @After
    public void tearDown(){
        activity=null;
        CognitoHelper.setUser(null);
    }
}
