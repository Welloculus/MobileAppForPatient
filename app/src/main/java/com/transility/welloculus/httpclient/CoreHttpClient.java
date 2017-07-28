package com.transility.welloculus.httpclient;

import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.util.IOUtils;
import com.transility.welloculus.httpresponse.BaseResponse;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.httpclient.Request;
import com.transility.welloculus.httpclient.UrlConfig;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * The type Core http client.
 */
public class CoreHttpClient implements UrlConfig {
    private static final String UTF_8 = "UTF-8";
    private static final String TAG = CoreHttpClient.class.getSimpleName();
    private static final int CONNECTION_TIME_OUT = 15 * 1000;
    private HttpURLConnection mCon;

    /**
     * Execute base response.
     *
     * @param req the req
     * @return the base response
     */
    public BaseResponse execute(Request req) {
        String errorMessage;
        errorMessage = "";
        URL obj = null;
        BaseResponse baseResponse = BaseResponse.getResponseDO(req.getRequestType());

        //init url
        Log.i(TAG, "request url " + req.getUri());
        InputStream inputStream = null;
        try {
            obj = new URL(req.getUri());
            mCon = (HttpURLConnection) obj.openConnection();
            mCon.setConnectTimeout(CONNECTION_TIME_OUT);
           // mCon.setRequestProperty("Authorization", "" + CognitoHelper.getCurrSession().getIdToken().getJWTToken());
            mCon.setRequestProperty("Content-Type", "application/json");

            switch (req.getMethod()) {
                case GET:
                    mCon.setRequestMethod("GET");
                    break;
                case POST:
                    Log.d(TAG, "setting post");
                    mCon.setRequestMethod("POST");
                    addPostData(req);
                    break;
                default:
                    Log.d("Unknown method =", "" + req.getMethod());
                    return baseResponse;
            }
            int responseCode = mCon.getResponseCode();
            Log.v(TAG, "\nSending " + req.getMethod() + " request to URL : " + req.getUri());
            Log.v(TAG, "ResponseBean Code : " + responseCode);
            inputStream = mCon.getInputStream();
            String response = IOUtils.toString(inputStream);
            //print result
            Log.v(TAG, baseResponse.toString());
            baseResponse.setmCode(responseCode);
            //set response data
            baseResponse.setmData(response);

            Log.v(TAG, "ResponseBean data : " + response);

        } catch (Exception e) {
            errorMessage = e.getMessage();
            baseResponse.setmStatus(errorMessage);
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                   // Log.e(AppUtility.TAG, Log.getStackTraceString(e));
                }
            }
            // make connection close
            if (mCon != null) {
                mCon.disconnect();
            }

        }
        return baseResponse;
    }

    private void addPostData(Request req) throws IOException {
        String postData = generatePostData(req);
        if (!TextUtils.isEmpty(postData)) {
            writePostData(postData);
        }
    }


    private void writePostData(String postData) throws IOException {
        this.mCon.setDoOutput(true);
        this.mCon.setDoInput(true);
        OutputStream os = this.mCon.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, UTF_8));
        Log.v(TAG, "postData : " + postData);
        writer.write(postData);
        writer.flush();
        writer.close();
    }


    private String getHttpUrlQuery(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), UTF_8));
        }

        return result.toString();
    }


    private String generatePostData(Request request) throws UnsupportedEncodingException {
        String postData = null;
        if (request.getParams().size() > 0) {
            postData = getHttpUrlQuery(request.getParams());
        } else if (!TextUtils.isEmpty(request.getPostData())) {
            postData = request.getPostData();
        }
        return postData;
    }

}