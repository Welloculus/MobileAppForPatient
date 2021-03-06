package com.transility.welloculus.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.transility.welloculus.R;
import com.transility.welloculus.db.DBHelper;
import com.transility.welloculus.httpclient.Request;
import com.transility.welloculus.httpclient.RetrieveDataFromServer;
import com.transility.welloculus.httpclient.UrlConfig;
import com.transility.welloculus.httpresponse.BaseResponse;
import com.transility.welloculus.httpresponse.HttpResponseHandler;
import com.transility.welloculus.utils.AppUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class HeartRateReceiver extends BroadcastReceiver {
    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.ctx = context;
        DBHelper dbHelper = DBHelper.getInstance(context);
        if (intent != null) {
            String action = intent.getAction();
            if (action == AppUtility.BROADCAST_NEW_DATA_ACTION && intent.hasExtra(AppUtility.EXTRAS_DATA_TYPE)) {
                String dataType = intent.getStringExtra(AppUtility.EXTRAS_DATA_TYPE);
                String data = intent.getStringExtra(AppUtility.EXTRAS_HEALTH_DATA);
                long time = intent.getLongExtra(AppUtility.EXTRAS_HEART_RATE_LOG_TIME, Calendar.getInstance().getTimeInMillis());
                String deviceId = intent.getStringExtra(AppUtility.EXTRAS_DEVICE_ID);
                String deviceName = intent.getStringExtra(AppUtility.EXTRAS_DEVICE_NAME);


                if (data != null) {
                    dbHelper.insertHealthData(deviceId, deviceName, time, dataType, data);
                }
            }
        }
    }

    public void SendRequest(String deviceId, String deviceName, long time, int data) {
        String url = UrlConfig.getPaitentReport;
        String postData = "{\n" +
                "        \"providerId\":\"123\",\n" +
                "    \"event\" : {\n" +
                "        \"username\":\"sneha03\",\n" +
                "        \"device_id\":" + " \"" + deviceId + "\", " +
                "        \"device_name\":" + " \"" + deviceName + "\", " +
                "        \"time\":" + " \"" + time + "\", " +
                "        \"data\":" + " \"" + data + "\", " +
                "        \"inUse\":\"true\",\n" +
                "    }\n" +
                "}";
        Log.e(AppUtility.TAG, "data..... " + postData);

        Request request = new Request().setUri(url).setMethod(Request.Method.POST).setRequestType(Request.RequestType.GET_DEVICES).setPostData(postData);
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        request.setParams(map);
        RetrieveDataFromServer retriveDataFromServer = new RetrieveDataFromServer(ctx.getApplicationContext()).setRequest(request, new GetPaitentReportResponseHandler());
        retriveDataFromServer.execute();

    }

    private class GetPaitentReportResponseHandler implements HttpResponseHandler {

        @Override
        public void handleResponse(BaseResponse response) {
            Log.e(AppUtility.TAG, "handleResponse..... ");
            Log.e(AppUtility.TAG, "response " + response);
            Log.e(AppUtility.TAG, "response " + response.getmStatus());
        }

        @Override
        public void handleError(BaseResponse response) {
            Log.e(AppUtility.TAG, "handleError..... ");
            Log.e(AppUtility.TAG, "response " + response);
            Log.e(AppUtility.TAG, "response " + response.getmStatus());
        }
    }


}

