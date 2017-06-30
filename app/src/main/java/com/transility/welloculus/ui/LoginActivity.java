package com.transility.welloculus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.transility.welloculus.R;
import com.transility.welloculus.utils.Constants;

/**
 * The type Login activity.
 */
public class LoginActivity extends BaseActivity {
    private final String TAG = "LoginActivity";
    private Toolbar mtoolbar;
    /**
     * The M et username.
     */
    protected EditText mEtUsername;
    /**
     * The M et password ed.
     */
    protected EditText mEtPasswordEd;
    private MultiFactorAuthenticationContinuation mMultiFactorAuthenticationContinuation;
    private NewPasswordContinuation mNewPasswordContinuation;
    private String mUsername;
    private String mPassword;
    /**
     * The M txt user label.
     */
    TextView mTxtUserLabel, /**
     * The M txt password label.
     */
    mTxtPasswordLabel;
    /**
     * The M context.
     */
    Context mContext;
    /**
     * The M login button.
     */
    Button mLoginButton;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        mtoolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mtoolbar.setTitle("");
        setSupportActionBar(mtoolbar);

        initUI();
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null && bundle.containsKey(Constants.SOURCE)) {
            //dont do anything
        } else {

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_signin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Find which menu item was selected
        int menuItem = item.getItemId();
        switch (menuItem)
        {
            case R.id.user_about_app:
                aboutApp();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode",""+requestCode);
        Log.e("resultCode",""+resultCode);
        handleCallBack(requestCode,resultCode,data);

    }

    private void handleCallBack(int requestCode,int resultCode,Intent data){
        switch (requestCode) {
            case Constants.signUpReq:
                signUpReqCallBack(resultCode,data);
                break;
            case Constants.confirmUser:
                // Confirm register user result
                confirmUserCallBack(resultCode,data);
                break;
            case Constants.launchUserReq:
                // User
                launchUserReq(resultCode,data);
                break;
            case Constants.mfaReq:
                //MFA result
                mfaReqCallBack(resultCode,data);
                break;
            case Constants.firstTimeSignReq:
                //new mPassword result
                firstTimeSignReqCallBack(resultCode,data);
        }
    }

    private void firstTimeSignReqCallBack(int resultCode,Intent data){
        closeWaitDialog();;
        Boolean continueSignIn = false;
        if (resultCode == RESULT_OK) {
            continueSignIn = data.getBooleanExtra(Constants.CONTINUESIGNIN, false);
        }
        if (continueSignIn) {
           // continueWithFirstTimeSignIn();
        }
    }

    private void mfaReqCallBack(int resultCode,Intent data){
        closeWaitDialog();;
        if (resultCode == RESULT_OK) {
            String code = data.getStringExtra(Constants.MFCODE);
            mfaRequestCallBack(code);
        }
    }

    private void signUpReqCallBack(int resultCode,Intent data){
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra(Constants.NAME);
            String userPasswd = data.getStringExtra(Constants.EXTRAS_PASS);
            if (!name.isEmpty()) {
                mEtUsername.setText(name);
                mEtPasswordEd.setText("");
                mEtPasswordEd.requestFocus();
            }
            if (!userPasswd.isEmpty()) {
                mEtPasswordEd.setText(userPasswd);
            }
            if (!name.isEmpty() && !userPasswd.isEmpty()) {
                mUsername = name;
                mPassword = userPasswd;

            }}
    }

    private  void launchUserReq(int resultCode,Intent data){
        if (resultCode == RESULT_OK) {
            clearInput();
            String name = data.getStringExtra(Constants.TODO);
            if (name != null) {
                if (!name.isEmpty()) {
                    name.equals(getString(R.string.exit));
                    onBackPressed();
                }
            }
        }
    }
    private void confirmUserCallBack(int resultCode,Intent data){
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra(Constants.NAME);
            if (!name.isEmpty()) {
                mEtUsername.setText(name);
                mEtPasswordEd.setText("");
                mEtPasswordEd.requestFocus();
            }
        }
    }

    private void mfaRequestCallBack(String code){
        if (code != null) {
            if (code.length() > 0) {
                showWaitDialog(getString(R.string.signing_message),getString(R.string.please_wait),mContext);
                mMultiFactorAuthenticationContinuation.setMfaCode(code);
                mMultiFactorAuthenticationContinuation.continueTask();
            } else {
                mEtPasswordEd.setText("");
                mEtPasswordEd.requestFocus();
            }
        }
    }

    /**
     * Sign up.
     *
     * @param view the view
     */
// App methods
    // Register user - start process
    public void signUp(View view) {
        signUpNewUser();
    }

    /**
     * Log in.
     *
     * @param view the view
     */
// Login if a user is already present
    public void logIn(View view) {
        if(isNetworkAvailable(mContext,getString(R.string.not_internet_connectivity))) {
            signInUser();
        }
    }

    /**
     * Forgot password.
     *
     * @param view the view
     */
// Forgot mPassword processing
    public void forgotPassword(View view) {
        forgotpasswordUser();
    }

    private void signUpNewUser() {
        Intent registerActivity = new Intent(this, RegisterUser.class);
        startActivityForResult(registerActivity, Constants.signUpReq);
    }

    private void aboutApp(){
        Intent aboutAppActivity = new Intent(this, AboutUsActivity.class);
        startActivity(aboutAppActivity);
    }

    /**
     * Sign in user.
     */
    protected void signInUser() {
        mLoginButton.setEnabled(false);


        mUsername = mEtUsername.getText().toString();
        if (mUsername == null || mUsername.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewUserIdMessage);
            label.setText(mEtUsername.getHint() + " " + getString(R.string.cannot_be_empty));
            mEtUsername.setBackgroundResource(R.drawable.text_border_error);
            mLoginButton.setEnabled(true);
            closeWaitDialog();;
            return;
        }


        mPassword = mEtPasswordEd.getText().toString();
        if (mPassword == null || mPassword.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewUserPasswordMessage);
            label.setText(mEtPasswordEd.getHint() + " " + getString(R.string.cannot_be_empty));
            mEtPasswordEd.setBackgroundResource(R.drawable.text_border_error);
            mLoginButton.setEnabled(true);
            closeWaitDialog();;
            return;
        }

        if(!(mPassword.equals("abc") && mUsername.equals("abc"))){
            TextView label = (TextView) findViewById(R.id.textViewUserPasswordMessage);
            label.setText(" " + getString(R.string.credentials_did_not_match));
            mLoginButton.setEnabled(true);
            return;
        }
        launchUser();


    }

    private void forgotpasswordUser() {
        Intent intent = new Intent(mContext, ForgotPasswordActivity.class);
        startActivityForResult(intent, Constants.forgotPassReq);
    }

    private void launchUser() {
        Intent userActivity = new Intent(this, DashboardActivity.class);
        userActivity.putExtra(Constants.NAME, mUsername);
        startActivityForResult(userActivity, Constants.launchUserReq);
        finish();
    }



    private void clearInput() {
        if (mEtUsername == null) {
            mEtUsername = (EditText) findViewById(R.id.editTextUserId);
        }

        if (mEtPasswordEd == null) {
            mEtPasswordEd = (EditText) findViewById(R.id.editTextUserPassword);
        }

        mEtUsername.setText("");
        mEtUsername.requestFocus();
        mEtUsername.setBackgroundResource(R.drawable.text_border_selector);
        mEtPasswordEd.setText("");
        mEtPasswordEd.setBackgroundResource(R.drawable.text_border_selector);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void initUI() {
        mLoginButton = (Button)findViewById(R.id.buttonLogIn);
        mEtUsername = (EditText) findViewById(R.id.editTextUserId);
        mEtPasswordEd = (EditText) findViewById(R.id.editTextUserPassword);
        mTxtUserLabel = (TextView) findViewById(R.id.textViewUserIdLabel);
        mTxtPasswordLabel = (TextView) findViewById(R.id.textViewUserPasswordLabel);

        mEtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    mTxtUserLabel.setText(getString(R.string.Username));
                    mEtUsername.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTxtUserLabel.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mTxtUserLabel.setText("");
                }
            }
        });

        mEtPasswordEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    mTxtPasswordLabel.setText(getString(R.string.Password));
                    mEtPasswordEd.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTxtPasswordLabel.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mTxtPasswordLabel.setText("");
                }
            }
        });
    }

}
