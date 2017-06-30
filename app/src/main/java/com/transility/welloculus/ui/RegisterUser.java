package com.transility.welloculus.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.transility.welloculus.R;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.utils.CalendarDialog;
import com.transility.welloculus.utils.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The type Register user.
 */
public class RegisterUser extends BaseActivity implements CalendarDialog.OnDateSetListener {
    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtGivenName;
    private EditText mEtEmail;
    private EditText mEtPhone;
    private EditText mEtFamilyName;
    private EditText mEtBirthdate;
    private Spinner mSpinGender;
    private EditText mEtCity;


    private Button mBtnSignUp;
    private AlertDialog muserDialog;
    private String mUsernameInput;
    private String mUserPasswd;
    private Context mContext;
    /**
     * The M to date.
     */
    Calendar mToDate;
    /**
     * The M date btn format.
     */
    DateFormat mDateBtnFormat;
    /**
     * The M scroll view.
     */
    ScrollView mScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // get back to main screen
            String value = extras.getString(Constants.TODO);
            if (value.equals(getString(R.string.exit))) {
                onBackPressed();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Register);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView main_title = (TextView) findViewById(R.id.signUp_toolbar_title);
        main_title.setText(getString(R.string.sign_up_title));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initUI();
    }

    /**
     * The Sign up handler.
     */
    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Check signUpConfirmationState to see if the user is already confirmed
            closeWaitDialog();
            if (signUpConfirmationState) {
                // User is already confirmed
                showDialogMessage(getString(R.string.signup_successful), mUsernameInput + " " + getString(R.string.confirmed), true);
            } else {

            }
        }

        @Override
        public void onFailure(Exception exception) {
            closeWaitDialog();
            TextView label = (TextView) findViewById(R.id.textViewRegUserIdMessage);
            label.setText(getString(R.string.signup_failed));
            mEtUsername.setBackgroundResource(R.drawable.text_border_error);

        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.confirmSignUp && resultCode == RESULT_OK) {
            String name = null;
            if (data.hasExtra(Constants.NAME)) {
                name = data.getStringExtra(Constants.NAME);
            }
            exit(name, mUserPasswd);
        }
    }

    private void showDialogMessage(String title, String body, final boolean exit) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setTitle(title).setMessage(body).setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    muserDialog.dismiss();
                    if (exit) {
                        exit(mUsernameInput);
                    }
                } catch (Exception e) {
                    Log.e(AppUtility.TAG, Log.getStackTraceString(e));
                    if (exit) {
                        exit(mUsernameInput);
                    }
                }
            }
        });
        muserDialog = builder.create();
        muserDialog.show();
    }

    private void exit(String userName) {
        exit(userName, null);
        finish();
    }

    private void exit(String userName, String passKey) {
        Intent intent = new Intent();
        if (userName == null) {
            userName = "";
        }
        if (passKey == null) {
            passKey = "";
        }
        intent.putExtra(Constants.NAME, userName);
        intent.putExtra(Constants.EXTRAS_PASS, passKey);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void initUserName() {
        mEtUsername = (EditText) findViewById(R.id.editTextRegUserId);
        mEtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserIdLabel);
                    label.setText(mEtUsername.getHint());
                    mEtUsername.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegUserIdMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserIdLabel);
                    label.setText("");
                }
            }
        });
    }

    private void initPassword() {
        mEtPassword = (EditText) findViewById(R.id.editTextRegUserPassword);
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserPasswordLabel);
                    label.setText(mEtPassword.getHint());
                    mEtPassword.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewUserRegPasswordMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserPasswordLabel);
                    label.setText("");
                }
            }
        });
    }

    private void initGivenName() {
        mEtGivenName = (EditText) findViewById(R.id.editTextRegGivenName);
        mEtGivenName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegGivenNameLabel);
                    label.setText(mEtGivenName.getHint());
                    mEtGivenName.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegGivenNameMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegGivenNameLabel);
                    label.setText("");
                }
            }
        });
    }

    private void initEmail() {
        mEtEmail = (EditText) findViewById(R.id.editTextRegEmail);
        mEtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText(mEtEmail.getHint());
                    mEtEmail.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegEmailMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText("");
                }
            }
        });
    }


    @Override
    protected void initUI() {
        mScrollView = (ScrollView) findViewById(R.id.scroll_register_user);

        initUserName();
        initPassword();
        initGivenName();
        initEmail();
        initPhone();
        initFamilyName();
        initGenderSpinner();
        initBirthDate();
        initCity();
        initButtonSignUp();

        AlertDialog alertDialog = new AlertDialog.Builder(RegisterUser.this).create();
        alertDialog.setTitle("Message");
        final Intent changePssActivity = new Intent(this, LoginActivity.class);
        alertDialog.setMessage("Call your API for Register your User");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(changePssActivity);
                        finish();
                    }
                });
        alertDialog.show();
    }

    private void initButtonSignUp() {
        mBtnSignUp = (Button) findViewById(R.id.signUp);
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUpAction();
            }
        });
    }

    private void performSignUpAction() {

        if (isNetworkAvailable(mContext, getString(R.string.not_internet_connectivity))) {
            // Read user data and register
            CognitoUserAttributes userAttributes = new CognitoUserAttributes();

            mUsernameInput = mEtUsername.getText().toString();
            if (mUsernameInput.isEmpty()) {
                TextView view = (TextView) findViewById(R.id.textViewRegUserIdMessage);
                view.setText(mEtUsername.getHint() + " " + getString(R.string.cannot_be_empty));
                mEtUsername.setBackgroundResource(R.drawable.text_border_error);
                scrollView(mEtUsername);
                return;
            }

            String userpasswordInput = mEtPassword.getText().toString();
            mUserPasswd = userpasswordInput;
            if (userpasswordInput.isEmpty()) {
                TextView view = (TextView) findViewById(R.id.textViewUserRegPasswordMessage);
                view.setText(mEtPassword.getHint() + " " + getString(R.string.cannot_be_empty));
                mEtPassword.setBackgroundResource(R.drawable.text_border_error);
                scrollView(mEtPassword);
                return;
            }

            String userInput = mEtGivenName.getText().toString();
            if (userInput.isEmpty()) {
                TextView view = (TextView) findViewById(R.id.textViewRegGivenNameMessage);
                view.setText(mEtGivenName.getHint() + " " + getString(R.string.not_valid));
                mEtGivenName.setBackgroundResource(R.drawable.text_border_error);
                scrollView(mEtGivenName);
                return;
            } else {

            }

            userInput = mEtFamilyName.getText().toString();
            if (userInput.isEmpty()) {
                TextView view = (TextView) findViewById(R.id.textViewFamilyNameMessage);
                view.setText(mEtFamilyName.getHint() + " " + getString(R.string.not_valid));
                mEtFamilyName.setBackgroundResource(R.drawable.text_border_error);
                scrollView(mEtFamilyName);
                return;
            } else {

            }

            userInput = (String) mSpinGender.getSelectedItem();
            if (!userInput.isEmpty()) {

            }

            String dateString = mEtBirthdate.getText().toString();
            Date date = null;
            try {
                date = mDateBtnFormat.parse(dateString);
            } catch (ParseException e) {
                Log.e(AppUtility.TAG, Log.getStackTraceString(e));
                date = null;
            }

            if (!TextUtils.isEmpty(dateString) && date != null) {

            }

            userInput = mEtEmail.getText().toString();
            if (userInput.isEmpty() && !AppUtility.isEmailValid(userInput)) {
                TextView view = (TextView) findViewById(R.id.textViewRegEmailMessage);
                view.setText(mEtEmail.getHint() + " " + getString(R.string.not_valid));
                mEtEmail.setBackgroundResource(R.drawable.text_border_error);
                scrollView(mEtEmail);
                return;
            } else {

            }

            userInput = mEtPhone.getText().toString();
            if (userInput.isEmpty()) {
                TextView view = (TextView) findViewById(R.id.textViewRegPhoneMessage);
                view.setText(mEtPhone.getHint() + " " + getString(R.string.not_valid));
                mEtPhone.setBackgroundResource(R.drawable.text_border_error);
                scrollView(mEtPhone);
                return;
            } else {

            }

            userInput = mEtCity.getText().toString();
            if (userInput.isEmpty()) {
                TextView view = (TextView) findViewById(R.id.textViewRegCityMessage);
                view.setText(mEtCity.getHint() + " " + getString(R.string.not_valid));
                mEtCity.setBackgroundResource(R.drawable.text_border_error);
                scrollView(mEtCity);
                return;
            } else {

            }


            showWaitDialog(getString(R.string.signup_message), mContext);


        }
    }

    private void initCity() {
        mEtCity = (EditText) findViewById(R.id.editTextRegCity);
        mEtCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegCityLabel);
                    label.setText(mEtCity.getHint());
                    mEtCity.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegEmailMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText("");
                }
            }

        });
    }

    private void initBirthDate() {
        mEtBirthdate = (EditText) findViewById(R.id.editTextBirthDay);
        mEtBirthdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewBirthdayLabel);
                    label.setText(mEtBirthdate.getHint());
                    mEtBirthdate.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegEmailMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = mEtBirthdate.getText().toString();
                if (text.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText("");
                }
            }
        });
        mDateBtnFormat = new SimpleDateFormat("dd/MM/yyyy");
        mToDate = Calendar.getInstance();
        displayDate(mToDate.getTimeInMillis(), mEtBirthdate.getId(), mDateBtnFormat);
        mEtBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
    }

    private void initGenderSpinner() {
        mSpinGender = (Spinner) findViewById(R.id.spintGender);
        mSpinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initFamilyName() {
        mEtFamilyName = (EditText) findViewById(R.id.editTextFamilyName);
        mEtFamilyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewFamiltyNameLabel);
                    label.setText(mEtFamilyName.getHint());
                    mEtFamilyName.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegEmailMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText("");
                }
            }
        });
    }

    private void initPhone() {
        mEtPhone = (EditText) findViewById(R.id.editTextRegPhone);
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegPhoneLabel);
                    label.setText(mEtPhone.getHint() + " " + getString(R.string.number_label));
                    mEtPhone.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegPhoneMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegPhoneLabel);
                    label.setText("");
                }
            }
        });
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        Log.d("date picked", "" + "Year: " + year + " Month: " + month + " Day: " + day);
        mToDate.set(Calendar.MONTH, month);
        mToDate.set(Calendar.DAY_OF_MONTH, day);
        mToDate.set(Calendar.YEAR, year);
        displayDate(mToDate.getTimeInMillis(), R.id.editTextBirthDay, mDateBtnFormat);
    }

    private void showCalendar() {
        new CalendarDialog(this, this).showDatePicker();
    }

    private void scrollView(final View v) {

        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, v.getBottom());
            }
        });

        mScrollView.smoothScrollTo(0, v.getBottom());
    }
}
