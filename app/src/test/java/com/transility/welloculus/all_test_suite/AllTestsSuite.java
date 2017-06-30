package com.transility.welloculus.all_test_suite;

import com.transility.welloculus.adapter.DeviceListAdapterTest;
import com.transility.welloculus.adapter.UserAttributesAdapterTest;
import com.transility.welloculus.beans.CognitoErrorBeanTest;
import com.transility.welloculus.beans.DeviceResponseBeanTest;
import com.transility.welloculus.beans.HealthDataBeanTest;
import com.transility.welloculus.ui.AboutUsActivityTest;
import com.transility.welloculus.ui.ChangePasswordActivityTest;
import com.transility.welloculus.ui.DashboardActivityTest;
import com.transility.welloculus.ui.DeviceActivityTest;
import com.transility.welloculus.ui.ForgotPasswordActivityTest;
import com.transility.welloculus.ui.LoginActivityTest;
import com.transility.welloculus.ui.ReportsActivityTest;
import com.transility.welloculus.ui.SignUpActivityTest;
import com.transility.welloculus.utils.AppUtilityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite containing entry for all the test classes of welloculus
 * project.
 *
 * @author sneha.bansal
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SignUpActivityTest.class, LoginActivityTest.class, DashboardActivityTest.class, ForgotPasswordActivityTest.class, AppUtilityTest.class, ReportsActivityTest.class,
        DeviceActivityTest.class, DeviceListAdapterTest.class, UserAttributesAdapterTest.class,CognitoErrorBeanTest.class, DeviceResponseBeanTest.class, HealthDataBeanTest.class,
        AboutUsActivityTest.class,ChangePasswordActivityTest.class})
public class AllTestsSuite {

}
