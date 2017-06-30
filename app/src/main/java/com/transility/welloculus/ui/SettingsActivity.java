package com.transility.welloculus.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSettings;
import com.transility.welloculus.R;
import com.transility.welloculus.app.HealthCareApp;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.utils.Constants;
import com.transility.welloculus.utils.SharedPreferenceHelper;

import java.util.Map;

/**
 * The type Settings activity.
 */
public class SettingsActivity extends BaseActivity {
    private Switch mSmsSwitch;

    private Map<String, String> mSettings;
    private CognitoUserSettings mNewSettings;

    private AlertDialog mUserDialog;

    private boolean settingsChanged;
    /**
     * The M context.
     */
    private Context mContext;
    private TextView mTxtDataIntervalTitle, mTxtDataIntervalValue;
    private int mSyncInterval;
    private int mGreenColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        TextView main_title = (TextView) findViewById(R.id.settings_toolbar_title);
        main_title.setText(getString(R.string.settings_title));
        mGreenColor = ContextCompat.getColor(mContext, R.color.green);
        initUI();
        mSyncInterval = SharedPreferenceHelper.getSharedPreferenceInt(mContext, Constants.KEY_SYNC_INTERVAL, AppUtility.DEFAULT_SYNC_INTERVAL);
        mTxtDataIntervalValue.setText(String.format("%d", mSyncInterval));
        mTxtDataIntervalTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog();
            }
        });
        mTxtDataIntervalValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog();
            }
        });
    }

    private void showNumberPickerDialog() {
        RelativeLayout linearLayout = new RelativeLayout(mContext);
        final NumberPicker aNumberPicker = new NumberPicker(mContext);
        aNumberPicker.setMaxValue(AppUtility.MAX_INTERVAL);
        aNumberPicker.setMinValue(AppUtility.MIN_INTERVAL);
        aNumberPicker.setValue(mSyncInterval);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPickerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPickerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(aNumberPicker, numPickerParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(mContext.getString(R.string.select_interval_title));
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(mContext.getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                mSyncInterval = aNumberPicker.getValue();
                                SharedPreferenceHelper.setSharedPreferenceInt(mContext, Constants.KEY_SYNC_INTERVAL, aNumberPicker.getValue());
                                mTxtDataIntervalValue.setText(String.format("%d", mSyncInterval));
                                ((HealthCareApp) getApplication()).startPostDataService();
                            }
                        })
                .setNegativeButton(mContext.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(mGreenColor);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(mGreenColor);
    }


    private void updateSetting(String attribute, String value) {
        showWaitDialog(getString(R.string.change_mfa_setting), mContext);
        mNewSettings = new CognitoUserSettings();
        mNewSettings.setSettings(attribute, value);

    }


    private void toggleSwitch() {
        if (mSmsSwitch.isChecked()) {
            mSmsSwitch.setTextColor(Color.parseColor("#37A51C"));
        } else {
            mSmsSwitch.setTextColor(Color.parseColor("#E94700"));
        }
    }


    @Override
    protected void initUI() {

        AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
        alertDialog.setTitle("Message");
        final Intent changePssActivity = new Intent(this, DashboardActivity.class);
        alertDialog.setMessage("Call your API for setting device send timeout");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(changePssActivity);
                        finish();
                    }
                });
        alertDialog.show();

        mNewSettings = new CognitoUserSettings();
        settingsChanged = false;
       // mSmsSwitch = (Switch) findViewById(R.id.switchSettingsPhone);
        mTxtDataIntervalTitle = (TextView) findViewById(R.id.textViewDataIntervalTitle);
        mTxtDataIntervalValue = (TextView) findViewById(R.id.textViewDataIntervalValue);
        //mSettings = CognitoHelper.getUserDetails().getSettings().getSettings();

        if (mSmsSwitch != null) {
            mSmsSwitch.setClickable(true);
            mSmsSwitch.setChecked(false);
            mSmsSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSwitch();
                    if (mSmsSwitch.isChecked()) {
                        updateSetting(Constants.phoneNumber, Constants.SMS1);
                    } else {
                        updateSetting(Constants.phoneNumber, null);
                    }
                }
            });
        }
        if (mSettings != null) {
            if (mSettings.containsKey(Constants.phoneNumber) && mSmsSwitch != null) {
                mSmsSwitch.setClickable(true);
                if (mSettings.get(Constants.phoneNumber).contains(Constants.SMS) || mSettings.get(Constants.phoneNumber).contains(Constants.SMS1)) {
                    mSmsSwitch.setChecked(true);
                    mSmsSwitch.setTextColor(Color.parseColor("#37A51C"));
                } else {
                    mSmsSwitch.setChecked(false);
                    mSmsSwitch.setTextColor(Color.parseColor("#E94700"));
                }
            }
        }
    }

    private void exit() {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.refresh), settingsChanged);
        setResult(RESULT_OK, intent);
        finish();
    }
}
