package com.transility.welloculus.httpclient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.transility.welloculus.R;
import com.transility.welloculus.httpresponse.BaseResponse;
import com.transility.welloculus.httpresponse.HttpResponseHandler;

/**
 * The type Retrieve data from server.
 */
public class RetrieveDataFromServer extends AsyncTask<Object, Void, BaseResponse> {

    private Request mRequest;
    private BaseResponse mBaseResponse;
    private HttpResponseHandler mResponseHandler;
   // private final static String TAG = CoreHttpClient.class.getSimpleName();
    private Context mContext;

    /**
     * Instantiates a new Retrieve data from server.
     *
     * @param context the context
     */
    public RetrieveDataFromServer(Context context) {
        this.mContext = context;
    }

    /**
     * Sets request.
     *
     * @param request         the request
     * @param responseHandler the response handler
     * @return the request
     */
    public RetrieveDataFromServer setRequest(Request request, HttpResponseHandler responseHandler) {
        this.mRequest = request;
        this.mResponseHandler = responseHandler;
        return this;
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Sets context.
     *
     * @param context the context
     */
    public void setContext(Context context) {
        this.mContext = context;
    }


    @Override
    protected void onPreExecute() {
        mRequest.getRequestType().setAsWorking(true);
        Log.d("track", mRequest.getRequestType() + " is working");
        super.onPreExecute();
    }

    @Override
    protected BaseResponse doInBackground(Object... arg0) {/*
        //CoreHttpClient coreHttpClient = new CoreHttpClient();
        mBaseResponse = coreHttpClient.execute(mRequest);
        try {
            mBaseResponse.processResponse(arg0);
        } catch (Exception e) {
            mBaseResponse.setmStatus(mContext.getString(R.string.internal_error));
           // Log.e(TAG, Log.getStackTraceString(e));
        }
        return mBaseResponse;
    */
    return null;
    }

    @Override
    protected void onPostExecute(BaseResponse baseResponse) {
        try {
            mRequest.getRequestType().setAsWorking(false);
            Log.d("track", mRequest.getRequestType() + " finish");
            if (baseResponse.isSuccessful() && baseResponse.getmData() != null && baseResponse.getmData().length() > 0) {
                mResponseHandler.handleResponse(baseResponse);
            } else if (baseResponse.isSuccessful() && baseResponse.getmBitmap() != null) {
                mResponseHandler.handleResponse(baseResponse);
            } else {
                Log.e("mResponse", "code " + baseResponse.getmCode() + " for " + mRequest.getUri() + " Status " + baseResponse.getmStatus());
                mResponseHandler.handleError(baseResponse);
            }
        } catch (Exception e) {
            //Log.e(TAG, Log.getStackTraceString(e));
            baseResponse.setmStatus(mContext.getString(R.string.internal_error));
            mResponseHandler.handleError(baseResponse);
        }
        super.onPostExecute(baseResponse);
    }

}