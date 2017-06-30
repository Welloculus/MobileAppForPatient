package com.transility.welloculus.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.transility.welloculus.app.service.PostDataService;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.utils.Constants;
import com.transility.welloculus.utils.SharedPreferenceHelper;

/**
 * Created by arpit.garg on 4/5/2017.
 */
public class HealthCareApp extends Application {

    private PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * Start post data service.
     */
    public void startPostDataService() {
        try {
            int dataInterval = SharedPreferenceHelper.getSharedPreferenceInt(getApplicationContext(), Constants.KEY_SYNC_INTERVAL, AppUtility.DEFAULT_SYNC_INTERVAL);
            //Create a new PendingIntent and add it to the AlarmManager
            Intent intent = new Intent(getApplicationContext(), PostDataService.class);
            pendingIntent =
                    PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager =
                    (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
            int intervalInMinutes = dataInterval * AppUtility.TIME_ONE_MINUTE;
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, intervalInMinutes,
                    intervalInMinutes, pendingIntent);

        } catch (Exception e) {
            Log.e(AppUtility.TAG, Log.getStackTraceString(e));
        }
    }

    /**
     * Stop post data service.
     */
    public void stopPostDataService() {
        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }


}
