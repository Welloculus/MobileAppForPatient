package com.transility.welloculus.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.transility.welloculus.R;
import com.transility.welloculus.adapter.UserAttributesAdapter;
import com.transility.welloculus.utils.CalendarDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The type User profiles activity.
 */
public class UserProfilesActivity extends BaseActivity implements CalendarDialog.OnDateSetListener {
    private final String TAG = "UserProfileActivity";
    private Toolbar mToolbar;
    private AlertDialog mUserDialog;
    private String mUsername;
    /**
     * The M context.
     */
    Context mContext;
    /**
     * The M attribute to change.
     */
    String mAttributeToChange;
    /**
     * The M attribut old value.
     */
    String mAttributOldValue;
    /**
     * The M to date.
     */
    Calendar mToDate;
    /**
     * The M date btn format.
     */
    DateFormat mDateBtnFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_user_profile);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        // Set mToolbar for this screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        initUI();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Find which menu item was selected
        int menuItem = item.getItemId();
        if (menuItem == R.id.user_update_attribute) {
            showWaitDialog(getString(R.string.updating_message), mContext);
            getDetails();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    // Get mUser mDetails from CIP service
    private void getDetails() {
        final UserAttributesAdapter attributesAdapter = new UserAttributesAdapter(getApplicationContext(), mDateBtnFormat);
        final ListView attributesListView;
        attributesListView = (ListView) findViewById(R.id.listViewUserAttributes);
        attributesListView.setAdapter(attributesAdapter);
    }

    private void exit() {
        finish();
    }

    @Override
    protected void initUI() {
        mContext = this;
        getDetails();
        mDateBtnFormat = new SimpleDateFormat("dd/MM/yyyy");
        mToDate = Calendar.getInstance();
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        Log.d("date picked", "" + "Year: " + year + " Month: " + month + " Day: " + day);
        mToDate.set(Calendar.MONTH, month);
        mToDate.set(Calendar.DAY_OF_MONTH, day);
        mToDate.set(Calendar.YEAR, year);
        Date date = new Date(mToDate.getTimeInMillis());
        String newValue = "" + date.getTime();
        if (!newValue.equals(mAttributOldValue)) {
            showWaitDialog(getString(R.string.updating_message), mContext);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
