package com.transility.welloculus.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.transility.welloculus.R;
import com.transility.welloculus.beans.HeartRateInfoBean;
import com.transility.welloculus.beans.ResponseBean;
import com.transility.welloculus.db.DBHelper;
import com.transility.welloculus.httpclient.Request;
import com.transility.welloculus.httpclient.RetrieveDataFromServer;
import com.transility.welloculus.httpclient.UrlConfig;
import com.transility.welloculus.httpresponse.BaseResponse;
import com.transility.welloculus.httpresponse.HttpResponseHandler;
import com.transility.welloculus.httpresponse.PostHealthDataResponse;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by arpit.garg on 4/5/2017.
 */
public class PostDataService extends BroadcastReceiver {
    private final String KEY_ITEMS = "data";
    private final String KEY_PROVIDERID = "provider_id";
    private final String KEY_HEART_RATE = "heart_rate";
    private final String KEY_HEALTH_VITAL_KEY = "data";
    private final String KEY_ENTRY_CREATED = "time";
    private Context context;
    private int minHeartRate = 0;
    private int maxHeartRate = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        HashMap<String, List<HeartRateInfoBean>> healthInfo = (HashMap<String, List<HeartRateInfoBean>>) DBHelper.getInstance(context.getApplicationContext()).getHealthInfo();
        if (healthInfo != null && healthInfo.size() > 0) {
            sendDataToServer(context.getApplicationContext(), healthInfo);
        }
    }

    private void sendDataToServer(Context context, HashMap<String, List<HeartRateInfoBean>> healthInfo) {
        Iterator it = healthInfo.entrySet().iterator();
        JSONObject postJsonObject = new JSONObject();
        try {
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                List<HeartRateInfoBean> healthInfoData = (List<HeartRateInfoBean>) pair.getValue();
                String deviceId = (String) pair.getKey();
                minHeartRate = AppUtility.CRITICAL_MAX_HEART_RATE;
                maxHeartRate = 0;
                if (healthInfoData != null) {

                    postJsonObject.put(KEY_PROVIDERID, "123");
                    postJsonObject.put("user_id", "1");
                    postJsonObject.put("device_id", "HXM-Zypher");
                    postJsonObject.put("user_device_id", deviceId);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String output = dateFormat.format(date);
                    System.out.println(output);
                    postJsonObject.put("date", output);

//                    postJsonObject.put("data_type", "heart_rate");

                    postJsonObject.put(KEY_HEART_RATE, createDevicePostData(deviceId, healthInfoData));
                }
                it.remove();
            }

        } catch (JSONException e) {
            Log.e(AppUtility.TAG, Log.getStackTraceString(e));
        }
        String postData = postJsonObject.toString();
        Log.v(AppUtility.TAG, " json : " + postData);
        Request request = new Request().setUri(UrlConfig.postHealthData).setMethod(Request.Method.POST).setRequestType(Request.RequestType.POST_HEALTH_DATA).setPostData(postData);
        RetrieveDataFromServer retriveDataFromServer = new RetrieveDataFromServer(context.getApplicationContext()).setRequest(request, new SendDataResponseHandler());
        retriveDataFromServer.execute();
    }

    private JSONArray createDevicePostData(String deviceId, List<HeartRateInfoBean> healthInfoData) throws JSONException {
        JSONObject deviceObject = new JSONObject();
        JSONArray dataArray = new JSONArray();
        for (int i = 0; i < healthInfoData.size(); i++) {
            JSONObject healthJson = new JSONObject();
            HeartRateInfoBean heartRateInfo = healthInfoData.get(i);

            if (heartRateInfo.getHeartRate() > maxHeartRate) {
                maxHeartRate = heartRateInfo.getHeartRate();
            }

            if (heartRateInfo.getHeartRate() < minHeartRate) {
                minHeartRate = heartRateInfo.getHeartRate();
            }
            JSONObject itemJson = new JSONObject();
//            healthJson.put(KEY_HEART_RATE, heartRateInfo.getHeartRate());
//            itemJson.put(KEY_HEALTH_VITAL_KEY, healthJson);
            itemJson.put(KEY_HEART_RATE, heartRateInfo.getHeartRate());
            itemJson.put(KEY_ENTRY_CREATED, heartRateInfo.getTime());
            dataArray.put(itemJson);
        }
//        deviceObject.put(KEY_HEART_RATE, dataArray);
        return dataArray;
    }

    private class SendDataResponseHandler implements HttpResponseHandler {
        @Override
        public void handleResponse(BaseResponse baseResponse) {
            PostHealthDataResponse postHealthDataResponse = (PostHealthDataResponse) baseResponse;
            if (postHealthDataResponse != null && postHealthDataResponse.getResponseBean() != null) {
                ResponseBean responseBean = postHealthDataResponse.getResponseBean();
                if (responseBean.getSuccess() && responseBean.getStatus() == Constants.HTTP_SUCCESS) {
                    DBHelper.getInstance(context).deleteAllData();
                    Log.e(AppUtility.TAG, "handleResponse :" + baseResponse.getmData());
                    Log.e(AppUtility.TAG, "handleResponse : " + baseResponse.getmStatus());
                    String message = String.format("Max Heart Rate : %s , Min Heart Rate : %s", maxHeartRate, minHeartRate);
                    boolean critical = AppUtility.isHearRateCritical(maxHeartRate) || AppUtility.isHearRateCritical(minHeartRate);
                    AppUtility.sendNotification(context.getApplicationContext(), context.getString(R.string.health_data_is_posted_to_server), message, critical);
                } else {
                    Toast.makeText(context.getApplicationContext(), ""+responseBean.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void handleError(BaseResponse baseResponse) {
            Log.e(AppUtility.TAG, "handleError : " + baseResponse);
            Log.e(AppUtility.TAG, "handleError : " + baseResponse.getmStatus());
        }
    }
}
