package com.transility.welloculus.ui;

import android.content.Intent;
import android.widget.TextView;

import com.transility.welloculus.BuildConfig;
import com.transility.welloculus.R;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowIntent;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class )
public class LoginActivityTest {


    private LoginActivity activity;
    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(LoginActivity.class)
                .create()
                .resume()
                .visible()
                .get();
    }

    @Test
    public void testShouldNotBeNull() throws Exception {
        Assert.assertNotNull("activity is null.", activity);
    }
    // check if user name error message is getting displayed
    @Test
    public void testUserLogin_blankUserName() {
        activity.mEtUsername.setText("");
        activity.mEtPasswordEd.setText("QWERTY");
        TextView label = (TextView) activity.findViewById(R.id.textViewUserIdMessage);
        Assert.assertFalse("user name lable is been set before signup",label.getText().length()>1);
        activity.signInUser();
        Assert.assertTrue("user name lable is not been set",label.getText().length()>1);
    }

    //check if password error message is displayed or
    @Test
    public void testUserLogin_blankPassword() {
        activity.mEtUsername.setText("sneha03");
        activity.mEtPasswordEd.setText("");
        TextView label = (TextView) activity.findViewById(R.id.textViewUserPasswordMessage);
        Assert.assertFalse("password lable is been set before signup",label.getText().length()>1);
        activity.signInUser();
        Assert.assertTrue("password lable is not been set",label.getText().length()>1);
    }

    @Test
    public void testUserLogin() {
        activity.mEtUsername.setText("sneha03");
        activity.mEtPasswordEd.setText("qwerty");

        activity.signInUser();
        TextView labelUserName = (TextView) activity.findViewById(R.id.textViewUserIdMessage);
        Assert.assertFalse("user name lable is been set after signin",labelUserName.getText().length()>1);

        TextView labelPassword = (TextView) activity.findViewById(R.id.textViewUserPasswordMessage);
        Assert.assertFalse("password lable is been set after signin",labelPassword.getText().length()>1);

    }

    @Test
    public  void testsignUp(){
        activity.signUp(null);
        Intent startedIntent = Shadows.shadowOf(activity).getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        Assert.assertEquals("register user has not been launched",RegisterUser.class, shadowIntent.getIntentClass());
    }

    @Test
    public void testaboutApp(){
        activity.forgotPassword(null);
        Intent startedIntent = Shadows.shadowOf(activity).getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        Assert.assertEquals("forgot password has not been launched",ForgotPasswordActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void testlogin(){
        activity.logIn(null);
        TextView labelUserName = (TextView) activity.findViewById(R.id.textViewUserIdMessage);
        Assert.assertTrue("user name lable is notbeen set after signin",labelUserName.getText().length()>1);
    }

    @Test
    public void testlogin_user(){
        activity.mEtUsername.setText("sneha03");
        activity.mEtPasswordEd.setText("Qwerty@1234");
        activity.logIn(null);
        TextView labelUserName = (TextView) activity.findViewById(R.id.textViewUserIdMessage);
        Assert.assertTrue("user name lable is notbeen set after signin",labelUserName.getText().length()>1);
    }


    @After
    public void tearDown() {
    activity = null;
    }
}
