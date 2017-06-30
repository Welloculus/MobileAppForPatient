package com.transility.welloculus.ui;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.transility.welloculus.BuildConfig;
import com.transility.welloculus.R;
import com.transility.welloculus.cognito.CognitoHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by arpit.garg on 4/27/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class ForgotPasswordActivityTest {


    private ForgotPasswordActivity activity;
    private EditText mEtUsername;
    private ViewSwitcher mViewSwitcher;
    private final String TEST_USER = "sneha03";

    private Button btnForgotPassword;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(ForgotPasswordActivity.class)
                .create()
                .resume()
                .visible()
                .get();
        initUI();
        CognitoHelper.init(activity);
    }

    @Test
    public void testShouldNotBeNull() throws Exception {
        Assert.assertNotNull("activity is null.", activity);
    }

    public void initUI() {
        mEtUsername = (EditText) activity.findViewById(R.id.editTextUserId);
        mViewSwitcher = (ViewSwitcher) activity.findViewById(R.id.viewSwitcherForgotPassword);
        btnForgotPassword = (Button) activity.findViewById(R.id.btnForgotPassword);
    }

    @Test
    public void testForgotPassword_blankUserName() {
        mEtUsername.setText("");
        btnForgotPassword.performClick();
        TextView label = (TextView) activity.findViewById(R.id.textViewUserIdLabel);
        Assert.assertTrue("user name is not set", label.getText().length() > 1);
    }

    @Test
    public void testForgotPassword() {
        mEtUsername.setText(TEST_USER);
        btnForgotPassword.performClick();
        TextView label = (TextView) activity.findViewById(R.id.textViewUserIdLabel);
        Assert.assertFalse("username not exists.", label.getText().length() > 1);
    }


    @After
    public void tearDown() {
        CognitoHelper.setUser(null);
        activity = null;
    }
}
