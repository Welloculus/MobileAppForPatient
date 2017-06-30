package com.transility.welloculus.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.transility.welloculus.R;

import com.transility.welloculus.utils.AppUtility;

/**
 * The type Forgot password activity.
 */
public class ForgotPasswordActivity extends BaseActivity {
    private EditText mEtPasswordInput;
    private EditText mEtCodeInput;
    private ViewSwitcher mViewSwitcher;
    /**
     * The M et username.
     */
    protected EditText mEtUsername;
    private Context mContext;
    private Button btnForgotPassword;
    private ForgotPasswordContinuation mForgotPasswordContinuation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_forgot_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initUI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });


    }

    /**
     * Do forgot password action.
     *
     * @param view the view
     */
    public void doForgotPasswordAction(View view) {
        if(isNetworkAvailable(mContext,getString(R.string.not_internet_connectivity))) {
        String userName = mEtUsername.getText().toString();
        TextView label = (TextView) findViewById(R.id.textViewUserIdLabel);
        if (TextUtils.isEmpty(userName)) {
            label.setText(mEtUsername.getHint() + " " + getString(R.string.cannot_be_empty));
            mEtUsername.setBackgroundResource(R.drawable.text_border_error);
            return;
        }

        label.setText("");
        showWaitDialog(getString(R.string.forgot_pass_title), mContext);
        //CognitoHelper.getPool().getUser(userName).forgotPasswordInBackground(forgotPasswordHandler);
        }
    }

    /**
     * Forgot password.
     *
     * @param view the view
     */
    public void forgotPassword(View view) {
        if(isNetworkAvailable(mContext,getString(R.string.not_internet_connectivity))) {
            getCode();
        }
    }


    private void getCode() {
        String newPassword = mEtPasswordInput.getText().toString();

        if (newPassword == null || newPassword.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewForgotPasswordUserIdMessage);
            label.setText(mEtPasswordInput.getHint() + " " + getString(R.string.cannot_be_empty));
            mEtPasswordInput.setBackgroundResource(R.drawable.text_border_error);
            return;
        }

        String verCode = mEtCodeInput.getText().toString();

        if (verCode == null || verCode.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewForgotPasswordCodeMessage);
            label.setText(mEtCodeInput.getHint() + " " + getString(R.string.cannot_be_empty));
            mEtCodeInput.setBackgroundResource(R.drawable.text_border_error);
            return;
        }

        showWaitDialog(getString(R.string.setting_password_message),mContext);
        mForgotPasswordContinuation.setPassword(newPassword);
        mForgotPasswordContinuation.setVerificationCode(verCode);
        mForgotPasswordContinuation.continueTask();
    }

    private void exit() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void initUI() {
        mEtUsername = (EditText) findViewById(R.id.editTextUserId);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcherForgotPassword);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        mViewSwitcher.showPrevious();


        mEtPasswordInput = (EditText) findViewById(R.id.editTextForgotPasswordPass);
        mEtPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewForgotPasswordUserIdLabel);
                    label.setText(mEtPasswordInput.getHint());
                    mEtPasswordInput.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewForgotPasswordUserIdMessage);
                label.setText(" ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewForgotPasswordUserIdLabel);
                    label.setText("");
                }
            }
        });

        mEtCodeInput = (EditText) findViewById(R.id.editTextForgotPasswordCode);
        mEtCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewForgotPasswordCodeLabel);
                    label.setText(mEtCodeInput.getHint());
                    mEtCodeInput.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewForgotPasswordCodeMessage);
                label.setText(" ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewForgotPasswordCodeLabel);
                    label.setText("");
                }
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
        alertDialog.setTitle("Message");
        final Intent changePssActivity = new Intent(this, LoginActivity.class);
        alertDialog.setMessage("Call your API for forget password here");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(changePssActivity);
                        finish();
                    }
                });
        alertDialog.show();
    }

    /**
     * The Forgot password handler.
     */
// ForgotPassword Callbacks
    ForgotPasswordHandler forgotPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            closeWaitDialog();
            showToastMessage(getString(R.string.pass_changed_successfully),mContext);
            exit();
//            mEtPasswordEd.setText("");
//            mEtPasswordEd.requestFocus();
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation forgotPasswordContinuation) {
            closeWaitDialog();
            getForgotPasswordCode(forgotPasswordContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            Log.e(AppUtility.TAG, e.getMessage());
            closeWaitDialog();
            //showDialogMessage(getString(R.string.forgot_pass_failed), CognitoHelper.formatException(e),mContext);
        }
    };

    private void getForgotPasswordCode(ForgotPasswordContinuation forgotPasswordContinuation) {
        mViewSwitcher.showNext();
        this.mForgotPasswordContinuation = forgotPasswordContinuation;
        String dest = forgotPasswordContinuation.getParameters().getDestination();
        String delMed = forgotPasswordContinuation.getParameters().getDeliveryMedium();
        TextView message = (TextView) findViewById(R.id.textViewForgotPasswordMessage);
        String textToDisplay = getString(R.string.code_sent_message) + " " + dest + " " + getString(R.string.code_sent_via) + " " + delMed;
        message.setText(textToDisplay);

    }
}
