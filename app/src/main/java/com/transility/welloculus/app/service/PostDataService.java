package com.transility.welloculus.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.transility.welloculus.R;
import com.transility.welloculus.beans.HealthRecordBean;
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
import com.transility.welloculus.utils.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PostDataService extends BroadcastReceiver {
    private Context context;
    private static final String keyString = "wellie";
    public static final String tempProviderId = "123";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        int lastSyncedRow = SharedPreferenceHelper.getSharedPreferenceInt(context, Constants.LAST_SYNCED_ROW, 0);
        List<HealthRecordBean> healthInfo = DBHelper.getInstance(context.getApplicationContext()).getHealthRecordsAfterRow(lastSyncedRow);
        if (healthInfo != null && healthInfo.size() > 0) {
            sendDataToServer(context.getApplicationContext(), healthInfo);
        }
    }

    private void sendDataToServer(Context context, List<HealthRecordBean> healthInfo) {
        try {
            Map<String, JSONObject> devicePostData = createDevicePostData(healthInfo);
            Set<String> keyset = devicePostData.keySet();
            for (String key : keyset) {
                JSONObject dataTypeJsonObject = devicePostData.get(key);
                dataTypeJsonObject = encryptDataObject(dataTypeJsonObject);
                Request request = new Request().setUri(UrlConfig.postHealthData).setMethod(Request.Method.POST).setRequestType(Request.RequestType.POST_HEALTH_DATA).setPostData(dataTypeJsonObject.toString());
                RetrieveDataFromServer retriveDataFromServer = new RetrieveDataFromServer(context.getApplicationContext()).setRequest(request, new SendDataResponseHandler());
                retriveDataFromServer.execute();
            }
            int lastSyncedRow = healthInfo.get(healthInfo.size()-1).getRecordId();
            SharedPreferenceHelper.setSharedPreferenceInt(context, Constants.LAST_SYNCED_ROW, lastSyncedRow);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private JSONObject encryptDataObject(JSONObject dataTypeJsonObject) throws JSONException {
        JSONArray dataArray = dataTypeJsonObject.getJSONArray(Constants.DATA);
        //TODO Saurav, add code to  encrypt dataArray here
        return dataTypeJsonObject;
    }


    private Map<String, JSONObject> createDevicePostData(List<HealthRecordBean> healthRecordsList) throws JSONException {
        Map<String, JSONObject> healthDataJson = new HashMap<>();
        for (int i = 0; i < healthRecordsList.size(); i++) {
            HealthRecordBean healthRecord = healthRecordsList.get(i);
            String dataType = healthRecord.getDataType();
            if (!healthDataJson.containsKey(dataType)) {
                String date = new SimpleDateFormat("YYYY-MM-DD").format(new Date());
                JSONObject dataTypeJsonObject =  new JSONObject();
                dataTypeJsonObject.put(Constants.DATA_TYPE,dataType);
                dataTypeJsonObject.put(Constants.DEVICE_ID, healthRecord.getDeviceID());
                dataTypeJsonObject.put(Constants.DEVICE_NAME, healthRecord.getDeviceName());
                dataTypeJsonObject.put(Constants.USER_DEVICE_ID, healthRecord.getUserDeviceID());
                dataTypeJsonObject.put(Constants.PROVIDER_ID, tempProviderId);
                dataTypeJsonObject.put(Constants.DATE, date);
                dataTypeJsonObject.put(Constants.DATA, new JSONArray());
                healthDataJson.put(dataType, dataTypeJsonObject);
            }
            JSONArray dataArray = healthDataJson.get(dataType).getJSONArray(Constants.DATA);
            JSONObject itemJson = new JSONObject(healthRecord.getData());
            itemJson.put(Constants.TIME, healthRecord.getTime());
            dataArray.put(itemJson);
        }
        return healthDataJson;
    }


    public static SecretKey getSecretEncryptionKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] key = (keyString).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    public static byte[] encryptText(JSONArray plainText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.toString().getBytes());
        return byteCipherText;
    }


    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }


    private class SendDataResponseHandler implements HttpResponseHandler {
        private int minHeartRate = 0;
        private int maxHeartRate = 0;
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
                    Toast.makeText(context.getApplicationContext(), "" + responseBean.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
