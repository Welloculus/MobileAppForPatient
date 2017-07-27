package com.transility.welloculus.fora.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetUtils {
	public static boolean isOnLine(Context context) {
        boolean haveInternet = false;
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mConnectivityManager != null && mNetworkInfo != null
                    && mNetworkInfo.isConnectedOrConnecting()) {
                haveInternet = true;
            } /* end of if */
        } catch (Exception e) {
            haveInternet = false;
        } /* end of try-catch */

        return haveInternet;
    }
}
