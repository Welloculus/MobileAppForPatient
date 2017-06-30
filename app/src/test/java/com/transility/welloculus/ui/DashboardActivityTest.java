package com.transility.welloculus.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.transility.welloculus.BuildConfig;
import com.transility.welloculus.R;
import com.transility.welloculus.cognito.CognitoHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.util.ActivityController;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class DashboardActivityTest {

    private ActivityController<DashboardActivity> activityController;
    private Context context;
    private DashboardActivity activity;
    CognitoHelper cognitoHelper;

    // Device connect and Report button of Dashboard Screen.
    LinearLayout btnDeviceConnect, btnGetReport;
    private final String TEST_USER = "sneha03";

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application.getBaseContext();
        CognitoHelper.init(context);
        CognitoHelper.setUser(TEST_USER);

        activityController = Robolectric.buildActivity(DashboardActivity.class).create();
        activity = activityController.start().resume().visible().get();
        btnDeviceConnect = (LinearLayout) activity.findViewById(R.id.btnDeviceConnect);
        btnGetReport = (LinearLayout) activity.findViewById(R.id.btn_get_reports);

    }


    @Test
    public void testDeviceClick() {
        btnDeviceConnect.performClick();
        Intent startedIntent = Shadows.shadowOf(activity).getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        junit.framework.Assert.assertEquals("deviceList has not been launched",DeviceListActivity2.class, shadowIntent.getIntentClass());
    }

    @Test
    public void testReportClick() {
        btnGetReport.performClick();
        Intent startedIntent = Shadows.shadowOf(activity).getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        Assert.assertEquals( "reportList activity ",ReportsActivity.class, shadowIntent.getIntentClass());
    }

    @After
    public void tearDown(){
        activity=null;
        CognitoHelper.setUser(null);
    }

}