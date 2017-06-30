package com.transility.welloculus.ui;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.transility.welloculus.BuildConfig;
import com.transility.welloculus.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class )
public class ChangePasswordActivityTest {

    private ChangePasswordActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(ChangePasswordActivity.class)
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
    public void testChangePassword_blankCurrentPassword() {
        EditText mEtCurrPassword = (EditText) activity.findViewById(R.id.editTextChangePassCurrPass);
        EditText mEtNewPassword = (EditText) activity.findViewById(R.id.editTextChangePassNewPass);
        EditText mEtConfirmNewPassword = (EditText) activity.findViewById(R.id.editTextConfirmPassNewPass);
        Button mBtnChange = (Button) activity.findViewById(R.id.change_pass_button);

        mEtCurrPassword.setText("");
        mEtNewPassword.setText("QWERTY");
        mEtConfirmNewPassword.setText("QWERTY");

        TextView label = (TextView) activity.findViewById(R.id.textViewChangePassCurrPassMessage);
        Assert.assertFalse("current password lable is been set before change password",label.getText().length()>1);
        mBtnChange.performClick();
        Assert.assertTrue("current password lable is not been set",label.getText().length()>1);
    }

    @Test
    public void testChangePassword_blankNewPassword() {
        EditText mEtCurrPassword = (EditText) activity.findViewById(R.id.editTextChangePassCurrPass);
        EditText mEtNewPassword = (EditText) activity.findViewById(R.id.editTextChangePassNewPass);
        EditText mEtConfirmNewPassword = (EditText) activity.findViewById(R.id.editTextConfirmPassNewPass);
        Button mBtnChange = (Button) activity.findViewById(R.id.change_pass_button);

        mEtCurrPassword.setText("QWERTY");
        mEtNewPassword.setText("");
        mEtConfirmNewPassword.setText("QWERTY");

        TextView label = (TextView) activity.findViewById(R.id.textViewChangePassNewPassMessage);
        Assert.assertFalse("new password lable is been set before change password",label.getText().length()>1);
        mBtnChange.performClick();
        Assert.assertTrue("new password lable is not been set",label.getText().length()>1);
    }

    @Test
    public void testChangePassword_blankConfirmPassword() {
        EditText mEtCurrPassword = (EditText) activity.findViewById(R.id.editTextChangePassCurrPass);
        EditText mEtNewPassword = (EditText) activity.findViewById(R.id.editTextChangePassNewPass);
        EditText mEtConfirmNewPassword = (EditText) activity.findViewById(R.id.editTextConfirmPassNewPass);
        Button mBtnChange = (Button) activity.findViewById(R.id.change_pass_button);

        mEtCurrPassword.setText("QWERTY");
        mEtNewPassword.setText("QWERTY");
        mEtConfirmNewPassword.setText("");

        TextView label = (TextView) activity.findViewById(R.id.textViewConfirmPassNewPassMessage);
        Assert.assertFalse("confirm password lable is been set before change password",label.getText().length()>1);
        mBtnChange.performClick();
        Assert.assertTrue("confirm password lable is not been set",label.getText().length()>1);
    }

    @Test
    public void testChangePassword_password_does_not_match() {
        EditText mEtCurrPassword = (EditText) activity.findViewById(R.id.editTextChangePassCurrPass);
        EditText mEtNewPassword = (EditText) activity.findViewById(R.id.editTextChangePassNewPass);
        EditText mEtConfirmNewPassword = (EditText) activity.findViewById(R.id.editTextConfirmPassNewPass);
        Button mBtnChange = (Button) activity.findViewById(R.id.change_pass_button);

        mEtCurrPassword.setText("QWERTY");
        mEtNewPassword.setText("QWERTY");
        mEtConfirmNewPassword.setText("QWERTY1");

        TextView label = (TextView) activity.findViewById(R.id.textViewConfirmPassNewPassMessage);
        Assert.assertFalse("confirm password lable is been set before change password",label.getText().length()>1);
        mBtnChange.performClick();
        Assert.assertTrue("confirm password lable is not been set",label.getText().length()>1);
    }


}
