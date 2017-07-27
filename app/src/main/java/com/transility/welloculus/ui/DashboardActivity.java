package com.transility.welloculus.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.transility.welloculus.R;
import com.transility.welloculus.app.HealthCareApp;
import com.transility.welloculus.bluetooth.BluetoothHandler;
import com.transility.welloculus.db.DBHelper;
import com.transility.welloculus.fora.PCLinkLibraryDemoActivity;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.utils.Constants;

/**
 * The type Dashboard activity.
 */
public class DashboardActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "DashboardActivity";

    private NavigationView mNavDrawer;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private AlertDialog mUserDialog;


    // Cognito mUser objects
    private CognitoUser mUser;

    // User mDetails
    private String mUsername;
    private LinearLayout mllDeviceConnect;
    private LinearLayout mllAllDeviceConnect;
    private LinearLayout mllGetReports;
    private LinearLayout mllForaDevices;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set mToolbar for this screen
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle("");
        //TextView main_title = (TextView) findViewById(R.id.main_toolbar_title);
        //main_title.setText(getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        initUI();
        init();
        View navigationHeader = mNavDrawer.getHeaderView(0);
        TextView navHeaderSubTitle = (TextView) navigationHeader.findViewById(R.id.textViewNavUserSub);
        navHeaderSubTitle.setText(mUsername);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }


    @Override
    public void onBackPressed() {
        exit(false);
    }


    // Handle when the a navigation item is selected
    private void setNavDrawer() {
        mNavDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                performAction(item);
                return true;
            }
        });
    }

    // Perform the action for the selected navigation item
    private void performAction(MenuItem item) {
        // Close the navigation drawer
        mDrawer.closeDrawers();

        // Find which item was selected
        switch (item.getItemId()) {
            case R.id.nav_user_change_password:
                // Change password
                changePassword();
                break;
            /*case R.id.nav_user_verify_attribute:
                // Confirm new mUser
                // confirmUser();
                attributesVerification();
                break;*/
            case R.id.nav_user_user_profiles:
                // Show mUser settings
                showUserProfile();
                break;
            case R.id.nav_user_settings:
                // Show mUser settings
                showSettings();
                break;
            case R.id.nav_user_sign_out:
                // Sign out from this account
                signOut();
                break;
            case R.id.nav_user_about:
                // For the inquisitive
                Intent aboutAppActivity = new Intent(this, AboutUsActivity.class);
                startActivity(aboutAppActivity);
                break;
            default:
                break;
        }
    }

    // Show mUser MFA Settings
    private void showSettings() {
        Intent userSettingsActivity = new Intent(this, SettingsActivity.class);
        startActivityForResult(userSettingsActivity, Constants.settingReq);
    }

    // Show mUser MFA Settings
    private void showUserProfile() {
        Intent userSettingsActivity = new Intent(this, ProfileActivity.class);
        startActivityForResult(userSettingsActivity, Constants.userProfieReq);
    }

    // Change mUser password
    private void changePassword() {
        Intent changePssActivity = new Intent(this, ChangePasswordActivity.class);
        startActivity(changePssActivity);
    }

    // Verify attributes
    private void attributesVerification() {
        //Intent attrbutesActivity = new Intent(this, VerifyActivity.class);
        //startActivityForResult(attrbutesActivity, Constants.verifyReq);
    }

    // Sign out mUser
    private void signOut() {
        //mUser.signOut();

        exit(true);
    }

    // Initialize this activity
    private void init() {
        //mUsername = CognitoHelper.getCurrUser();
        //mUser = CognitoHelper.getPool().getUser(mUsername);
        if (isNetworkAvailable(mContext, getString(R.string.not_internet_connectivity))) {
           // getDetails();
        }
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
                    // Log failure
                    Log.e(AppUtility.TAG, Log.getStackTraceString(e));
                    if (exit) {
                        exit(false);
                    }
                }
            }
        });
        mUserDialog = builder.create();
        mUserDialog.show();
    }

    private void exit(boolean dosignOut) {

        if (dosignOut) {
            //open login screen
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra(Constants.SOURCE, TAG);
            startActivity(intent);
            // user is logged out delete all data.
            DBHelper.getInstance(getApplicationContext()).deleteAllData();
        }

        finish();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.btnDeviceConnect:
                intent = new Intent(mContext, DeviceListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_get_reports:
                intent = new Intent(mContext, ReportsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_get_alldevice:
                /*intent = new Intent(mContext, AllDeviceListActivity.class);
                startActivity(intent);*/
                AlertDialog alertDialog = new AlertDialog.Builder(DashboardActivity.this).create();
                alertDialog.setTitle("Message");
                alertDialog.setMessage("Call your device List here");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            case R.id.btn_fora_devices:
                intent = new Intent(mContext, PCLinkLibraryDemoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initUI() {
        mllDeviceConnect = (LinearLayout) findViewById(R.id.btnDeviceConnect);
        mllDeviceConnect.setOnClickListener(this);
        mllAllDeviceConnect = (LinearLayout) findViewById(R.id.btn_get_alldevice);
        mllAllDeviceConnect.setOnClickListener(this);
        mllGetReports = (LinearLayout) findViewById(R.id.btn_get_reports);
        mllGetReports.setOnClickListener(this);
        mllForaDevices = (LinearLayout) findViewById(R.id.btn_fora_devices);
        mllForaDevices.setOnClickListener(this);
        mContext = this;
        // Set navigation drawer for this screen
        mDrawer = (DrawerLayout) findViewById(R.id.user_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavDrawer = (NavigationView) findViewById(R.id.nav_view);
        setNavDrawer();
    }


    // Get mUser mDetails from CIP service
    private void getDetails() {
        //CognitoHelper.getPool().getUser(mUsername).getDetailsInBackground(detailsHandler);
    }

    /**
     * The Details handler.
     */
   /* GetDetailsHandler detailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
            CognitoHelper.setUserDetails(cognitoUserDetails);
        }

        @Override
        public void onFailure(Exception exception) {
            showDialogMessage(getString(R.string.could_not_fetch_user_data), CognitoHelper.formatException(exception), true);
        }
    };*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BluetoothHandler.getInstance().isConnected()) {
            BluetoothHandler.getInstance().disconnect(getApplicationContext());
        }
        ((HealthCareApp) getApplication()).stopPostDataService();
    }
}

