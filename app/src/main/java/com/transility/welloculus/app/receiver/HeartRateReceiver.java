package com.transility.welloculus.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.transility.welloculus.R;
import com.transility.welloculus.db.DBHelper;
import com.transility.welloculus.utils.AppUtility;

import java.util.Calendar;


public class HeartRateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        if (intent != null) {
            String action = intent.getAction();
            if (action == AppUtility.BROADCAST_HEART_RATE_ACTION && intent.hasExtra(AppUtility.EXTRAS_HEART_RATE)) {
                int heartRate = intent.getIntExtra(AppUtility.EXTRAS_HEART_RATE, 0);
                long time = intent.getLongExtra(AppUtility.EXTRAS_HEART_RATE_LOG_TIME, Calendar.getInstance().getTimeInMillis());
                String deviceId = intent.getStringExtra(AppUtility.EXTRAS_DEVICE_ID);
                String deviceName = intent.getStringExtra(AppUtility.EXTRAS_DEVICE_NAME);

                if (heartRate > 0) {
                    AppUtility.sendNotification(context, String.format("%s - %s : %d", deviceName, context.getString(R.string.heart_rate_is), heartRate), "", AppUtility.isHearRateCritical(heartRate));
                    dbHelper.insertHealthData(deviceId, deviceName, time, heartRate);
                } else {
                    Context applicationContext = context.getApplicationContext();
                    Toast.makeText(applicationContext, applicationContext.getString(R.string.zephyr_invalid_data) + "" + heartRate, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}

