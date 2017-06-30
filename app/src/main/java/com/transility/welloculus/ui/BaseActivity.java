package com.transility.welloculus.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.transility.welloculus.R;
import com.transility.welloculus.utils.AppUtility;

import java.text.DateFormat;
import java.util.Date;

/**
 * The type Base activity.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mWaitDialog;
    private AlertDialog mUserDialog;

    /**
     * Init ui.
     */
    protected abstract void initUI();


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * Common methode created to show toast message
     *
     * @param message the message
     */
    protected void showToastMessage(String message,Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Common method been created for showing wait progress bar
     *
     * @param message the message
     * @param context the context
     */
    protected void showWaitDialog(String message, Context context) {
        closeWaitDialog();
        mWaitDialog = new ProgressDialog(context); // this = YourActivity
        //mWaitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mWaitDialog.setMessage(message);
        mWaitDialog.setCancelable(false);
        mWaitDialog.show();

    }

    /**
     * Common method been created for showing wait progress bar
     *
     * @param title   the title
     * @param message the message
     * @param context the context
     */
    protected void showWaitDialog(String title, String message, Context context) {

        closeWaitDialog();
        mWaitDialog = new ProgressDialog(context); // this = YourActivity
        mWaitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mWaitDialog.setMessage(message);
        mWaitDialog.setIndeterminate(true);
        mWaitDialog.setTitle(title);
        mWaitDialog.setCancelable(false);
        mWaitDialog.show();

    }

    /**
     * closes the wait progress bar
     */
    public void closeWaitDialog() {
        try {
            if (mWaitDialog != null && mWaitDialog.isShowing()){
                mWaitDialog.dismiss();
                mWaitDialog = null;
            }


        } catch (Exception e) {
            Log.e(AppUtility.TAG, Log.getStackTraceString(e));
        }
    }

    /**
     * method check is newtwork is available or not
     *
     * @param context the context
     * @param message the message
     * @return boolean
     */
    public boolean isNetworkAvailable(Context context, String message) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        // display message
        if (!isConnected) {
            showToastMessage(message,context);
        }

        return isConnected;
    }

    /**
     * Common method to show message in alert box.
     *
     * @param title the title
     * @param body  the body
     */
    public void showDialogMessage(String title, String body,Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setTitle(title).setMessage(body).setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mUserDialog.dismiss();
                } catch (Exception e) {
                    Log.e(AppUtility.TAG, Log.getStackTraceString(e));
                }
            }
        });
        mUserDialog = builder.create();
        mUserDialog.show();
    }

    /**
     * Common method to show date in specified view.
     *
     * @param time       the time
     * @param viewID     the view id
     * @param dateFormat the date format
     */
    public void displayDate(long time, int viewID, DateFormat dateFormat) {
        TextView tv = (TextView) findViewById(viewID);
        Date date = new Date(time);
        String fromdate = dateFormat.format(date);
        tv.setText(fromdate);
    }

}
