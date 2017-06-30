package com.transility.welloculus.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.transility.welloculus.R;
import com.transility.welloculus.adapter.UserAttributesAdapter;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * The type New password.
 */
public class NewPassword extends BaseActivity {
    private EditText mEtNewPassword;
    private Button mBtnContinueSignIn;
    private AlertDialog mUserDialog;
    /**
     * The M date btn format.
     */
    DateFormat mDateBtnFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_NewPassword);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit(false);
                }
            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView main_title = (TextView) findViewById(R.id.newpassword_toolbar_title);
        main_title.setText(R.string.welcome);


        initUI();
    }

    @Override
    public void onBackPressed() {
        exit(false);
    }

    private void refreshItemsDisplayed() {
        final UserAttributesAdapter attributesAdapter =
                new UserAttributesAdapter(getApplicationContext(),mDateBtnFormat);
        final ListView displayListView;
        displayListView = (ListView) findViewById(R.id.listViewCurrentUserDetails);
        displayListView.setAdapter(attributesAdapter);
        displayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView data = (TextView) view.findViewById(R.id.editTextUserDetailInput);
                String attributeType = data.getHint().toString();
                String attributeValue = data.getText().toString();
                showAttributeDetail(attributeType, attributeValue);
            }
        });
    }

    private void showAttributeDetail(final String attributeType, final String attributeValue) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(attributeType);
        final EditText input = new EditText(NewPassword.this);
        input.setText(attributeValue);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        input.requestFocus();
        builder.setView(input);

        builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String newValue = input.getText().toString();
                    if (!newValue.equals(attributeValue)) {

                        refreshItemsDisplayed();
                    }
                    mUserDialog.dismiss();
                } catch (Exception e) {
                    Log.e(AppUtility.TAG, Log.getStackTraceString(e));
                }
            }
        });
        mUserDialog = builder.create();
        mUserDialog.show();
    }

    private boolean checkAttributes() {
        return true;
    }

    private void showDialogMessage(String title, String body, final boolean exit) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mUserDialog.dismiss();
                    if (exit) {
                        exit(false);
                    }
                } catch (Exception e) {
                    Log.e(AppUtility.TAG, Log.getStackTraceString(e));
                    exit(false);
                }
            }
        });
        mUserDialog = builder.create();
        mUserDialog.show();
    }

    private void exit(Boolean continueWithSignIn) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CONTINUESIGNINBTN, continueWithSignIn);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void initUI() {
        mDateBtnFormat = new SimpleDateFormat("dd/MM/yyyy");
        mEtNewPassword = (EditText) findViewById(R.id.editTextNewPassPass);
        mEtNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextChange(s);
            }

            private void beforeTextChange(CharSequence s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewNewPassPassLabel);
                    label.setText(mEtNewPassword.getHint());
                    mEtNewPassword.setBackgroundResource(R.drawable.text_border_selector);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doOnTextChanged();
            }

            private void doOnTextChanged() {
                TextView label = (TextView) findViewById(R.id.textViewNewPassPassMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                doAfterTextChange(s);
            }

            private void doAfterTextChange(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewNewPassPassLabel);
                    label.setText("");
                }
            }
        });

        mBtnContinueSignIn = (Button) findViewById(R.id.buttonNewPass);
        mBtnContinueSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserPassword = mEtNewPassword.getText().toString();
                if (newUserPassword != null) {

                    if (checkAttributes()) {
                        exit(true);
                    }
                }
                showDialogMessage(getString(R.string.error), getString(R.string.enter_all_required_attr), false);
            }
        });
        refreshItemsDisplayed();
    }
}
