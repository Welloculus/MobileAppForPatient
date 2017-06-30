package com.transility.welloculus.ui;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
public class SignUpActivityTest {


    private RegisterUser activity;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtGivenName;
    private EditText mEtEmail;
    private EditText mEtPhone;
    private EditText mEtFamilyName;
    private EditText mEtBirthdate;
    private Spinner mSpinGender;
    private EditText mEtCity;

    private final String TEST_USERNAME = "sneha04";
    private final String TEST_PASSWORD = "Qwerty@1234";
    private final String TEST_GIVEN_NAME = "sneha";
    private final String TEST_FAMILY_NAME = "sneha";
    private final String TEST_BIRTHDATE = "27/04/2017";
    private final String TEST_GENDER = "FEMALE";
    private final String TEST_EMAIL = "sneha.bansal@impetus.co.in";
    private final String TEST_PHONE_NUMBER = "+918103278037";
    private final String TEST_CITY = "INDORE";


    private Button mBtnSignUp;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(RegisterUser.class)
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
        mEtUsername = (EditText) activity.findViewById(R.id.editTextRegUserId);
        mEtPassword = (EditText) activity.findViewById(R.id.editTextRegUserPassword);
        mEtGivenName = (EditText) activity.findViewById(R.id.editTextRegGivenName);
        mEtEmail = (EditText) activity.findViewById(R.id.editTextRegEmail);
        mEtPhone = (EditText) activity.findViewById(R.id.editTextRegPhone);
        mEtFamilyName = (EditText) activity.findViewById(R.id.editTextFamilyName);
        mSpinGender = (Spinner) activity.findViewById(R.id.spintGender);
        mEtBirthdate = (EditText) activity.findViewById(R.id.editTextBirthDay);
        mEtCity = (EditText) activity.findViewById(R.id.editTextRegCity);
        mBtnSignUp = (Button) activity.findViewById(R.id.signUp);
    }

    // check if user name error message is getting displayed
    @Test
    public void testUserLogin_blankUserName() {
        mEtUsername.setText("");
        mBtnSignUp.performClick();
        TextView label = (TextView) activity.findViewById(R.id.textViewRegUserIdMessage);
        Assert.assertTrue("user name label is not been set", label.getText().length() > 1);
    }

    @Test
    public void testUserLogin_blankPassword() {
        mEtUsername.setText(TEST_USERNAME);
        mEtPassword.setText("");
        mBtnSignUp.performClick();
        TextView label = (TextView) activity.findViewById(R.id.textViewUserRegPasswordMessage);
        Assert.assertTrue("password is not been set", label.getText().length() > 1);
    }


    @Test
    public void testUserLogin_blankGivenName() {
        mEtUsername.setText(TEST_USERNAME);
        mEtPassword.setText(TEST_PASSWORD);
        mEtGivenName.setText("");
        mBtnSignUp.performClick();
        TextView label = (TextView) activity.findViewById(R.id.textViewRegGivenNameMessage);
        Assert.assertTrue("given name is not been set", label.getText().length() > 1);
    }

    @Test
    public void testUserLogin_blankFamilyName() {
        mEtUsername.setText(TEST_USERNAME);
        mEtPassword.setText(TEST_PASSWORD);
        mEtGivenName.setText(TEST_GIVEN_NAME);
        mEtFamilyName.setText("");
        mBtnSignUp.performClick();
        TextView label = (TextView) activity.findViewById(R.id.textViewFamilyNameMessage);
        Assert.assertTrue("family name is not been set", label.getText().length() > 1);
    }


    @Test
    public void testUserLogin_blankEmail() {
        mEtUsername.setText(TEST_USERNAME);
        mEtPassword.setText(TEST_PASSWORD);
        mEtGivenName.setText(TEST_GIVEN_NAME);
        mEtFamilyName.setText(TEST_FAMILY_NAME);
        mEtBirthdate.setText(TEST_BIRTHDATE);
        mEtEmail.setText("");
        mBtnSignUp.performClick();
        TextView label = (TextView) activity.findViewById(R.id.textViewRegEmailMessage);
        Assert.assertTrue("email is not been set", label.getText().length() > 1);
    }

    @Test
    public void testUserLogin_blankPhoneNumber() {
        mEtUsername.setText(TEST_USERNAME);
        mEtPassword.setText(TEST_PASSWORD);
        mEtGivenName.setText(TEST_GIVEN_NAME);
        mEtFamilyName.setText(TEST_FAMILY_NAME);
        mEtBirthdate.setText(TEST_BIRTHDATE);
        mEtEmail.setText(TEST_EMAIL);
        mEtPhone.setText("");
        mBtnSignUp.performClick();
        TextView label = (TextView) activity.findViewById(R.id.textViewRegPhoneMessage);
        Assert.assertTrue("phone number is not been set", label.getText().length() > 1);
    }

    @Test
    public void testUserLogin_blankCity() {
        mEtUsername.setText(TEST_USERNAME);
        mEtPassword.setText(TEST_PASSWORD);
        mEtGivenName.setText(TEST_GIVEN_NAME);
        mEtFamilyName.setText(TEST_FAMILY_NAME);
        mEtBirthdate.setText(TEST_BIRTHDATE);
        mEtEmail.setText(TEST_EMAIL);
        mEtPhone.setText(TEST_PHONE_NUMBER);
        mEtCity.setText("");
        mBtnSignUp.performClick();
        TextView label = (TextView) activity.findViewById(R.id.textViewRegCityLabel);
        Assert.assertTrue("city is not been set", label.getText().length() > 1);
    }


    @Test
    public void testSignUp() {
        mEtUsername.setText(TEST_USERNAME);
        mEtPassword.setText(TEST_PASSWORD);
        mEtGivenName.setText(TEST_GIVEN_NAME);
        mEtFamilyName.setText(TEST_FAMILY_NAME);
        mEtBirthdate.setText(TEST_BIRTHDATE);
        mEtEmail.setText(TEST_EMAIL);
        mEtPhone.setText(TEST_PHONE_NUMBER);
        mEtCity.setText(TEST_CITY);
        mBtnSignUp.performClick();
        TextView labelUserName = (TextView) activity.findViewById(R.id.textViewRegUserIdMessage);
        Assert.assertFalse("SignUp for same user has passed.", labelUserName.getText().length() > 1);
        try{
        Thread.sleep(5000);}catch (Exception e){

        }
    }


    @After
    public void tearDown() {
        activity = null;
    }
}
