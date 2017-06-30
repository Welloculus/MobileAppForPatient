package com.transility.welloculus.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.transility.welloculus.R;
import com.transility.welloculus.ui.DashboardActivity;

public class AppUtility implements Constants {

    /**
     *
     * @param context
     * @param title
     * @param message
     * @param isCritical
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void sendNotification(Context context, String title, String message, boolean isCritical) {
        Intent broadCastIntent = new Intent(context, DashboardActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), broadCastIntent, 0);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_icon)
                .setContentIntent(pIntent)
                .setAutoCancel(true).setContentTitle("" + title);
        if (!TextUtils.isEmpty(message)) {
            mBuilder.setContentText(message);
        }
        if (isCritical) {
            mBuilder.setVibrate(new long[]{100, 250}).setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setColor(Color.RED);
        } else {
            mBuilder.setColor(Color.GREEN);
        }
        mBuilder.setContentIntent(pIntent);
        mNotificationManager.notify(0, mBuilder.build());
    }

    /**
     *
     * @param heartRate in integer value
     * @return true if heartrate is critical.
     */
    public static boolean isHearRateCritical(int heartRate) {
        boolean critical = false;
        if (heartRate > CRITICAL_MAX_HEART_RATE || heartRate < CRITICAL_MIN_HEART_RATE) {
            critical = true;
        }
        return critical;
    }

    /**
     *
     * @param  email in string format.
     * @return true if email is valid.
     */
    public static boolean isEmailValid(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") || email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+");
    }

}
