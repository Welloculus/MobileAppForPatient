package com.transility.welloculus.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.transility.welloculus.R;

/**
 * The type Change password activity.
 */
public class ChangePasswordActivity extends BaseActivity {


    private EditText mEtCurrPassword;
    private EditText mEtNewPassword;
    private EditText mEtConfirmNewPassword;
    private Button mBtnChange;
    private AlertDialog mUserDialog;
    /**
     * The M context.
     */
    Context mContext ;
//    Context context =  getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChangePass);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView main_title = (TextView) findViewById(R.id.change_password_toolbar_title);
        main_title.setText(getString(R.string.change_pass_title));

        initUI();

    }

    private void changePassword() {
        String cPass = mEtCurrPassword.getText().toString();

        if(cPass == null || cPass.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewChangePassCurrPassMessage);
            label.setText(mEtCurrPassword.getHint()+" "+getString(R.string.cannot_be_empty));
            mEtCurrPassword.setBackgroundResource(R.drawable.text_border_error);
            return;
        }

        String nPass = mEtNewPassword.getText().toString();

        if(nPass == null || nPass.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewChangePassNewPassMessage);
            label.setText(mEtNewPassword.getHint()+" "+getString(R.string.cannot_be_empty));
            mEtNewPassword.setBackgroundResource(R.drawable.text_border_error);
            return;
        }

        String nConfirmPass = mEtConfirmNewPassword.getText().toString();

        if(nConfirmPass == null || nConfirmPass.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewConfirmPassNewPassMessage);
            label.setText(mEtConfirmNewPassword.getHint()+" "+getString(R.string.cannot_be_empty));
            mEtConfirmNewPassword.setBackgroundResource(R.drawable.text_border_error);
            return;
        }

        if(!nPass.equals(nConfirmPass)){

            TextView label = (TextView) findViewById(R.id.textViewChangePassNewPassMessage);
            label.setText(mEtNewPassword.getHint()+" "+getString(R.string.password_did_not_match));
            mEtNewPassword.setBackgroundResource(R.drawable.text_border_error);

            label = (TextView) findViewById(R.id.textViewConfirmPassNewPassMessage);
            label.setText(mEtConfirmNewPassword.getHint()+" "+getString(R.string.password_did_not_match));
            mEtConfirmNewPassword.setBackgroundResource(R.drawable.text_border_error);

            return;
        }

        showWaitDialog(getString(R.string.changing_pass),mContext);
    }

    @Override
    protected void initUI() {

        AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
        alertDialog.setTitle("Message");
        final Intent changePssActivity = new Intent(this, DashboardActivity.class);
        alertDialog.setMessage("Call your API to change password here");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(changePssActivity);
                        finish();
                    }
                });
        alertDialog.show();


        mContext = this;
        mEtCurrPassword = (EditText) findViewById(R.id.editTextChangePassCurrPass);
        mEtCurrPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewChangePassCurrPassLabel);
                    label.setText(mEtCurrPassword.getHint());
                    mEtCurrPassword.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewChangePassCurrPassMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewChangePassCurrPassLabel);
                    label.setText("");
                }
            }
        });


        mEtNewPassword = (EditText) findViewById(R.id.editTextChangePassNewPass);
        mEtNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewChangePassNewPassLabel);
                    label.setText(mEtNewPassword.getHint());
                     mEtNewPassword.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewChangePassNewPassMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewChangePassNewPassLabel);
                    label.setText("");
                }
            }
        });

        mEtConfirmNewPassword = (EditText) findViewById(R.id.editTextConfirmPassNewPass);

        mEtConfirmNewPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmPassNewPassLabel);
                    label.setText(mEtConfirmNewPassword.getHint());
                    mEtNewPassword.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewConfirmPassNewPassMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmPassNewPassLabel);
                    label.setText("");
                }
            }
        });

        mBtnChange = (Button) findViewById(R.id.change_pass_button);
        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
                //insertValue();
            }
        });
    }




}
