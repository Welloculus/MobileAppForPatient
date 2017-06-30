package com.transility.welloculus.httpresponse;

import android.graphics.Bitmap;

import com.transility.welloculus.httpclient.Request;

/**
 * The type Base response.
 */
public abstract class BaseResponse {

	private int mCode;
	private String mStatus;
	private String mData;
	private Bitmap mBitmap;

	/**
	 * Instantiates a new Base response.
	 */
	public BaseResponse() {

	}

	/**
	 * Gets code.
	 *
	 * @return the code
	 */
	public int getmCode() {
		return mCode;
	}

	/**
	 * Sets code.
	 *
	 * @param mCode the m code
	 */
	public void setmCode(int mCode) {
		this.mCode = mCode;
	}

	/**
	 * Gets status.
	 *
	 * @return the status
	 */
	public String getmStatus() {
		return mStatus;
	}

	/**
	 * Sets status.
	 *
	 * @param mStatus the m status
	 */
	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	/**
	 * Gets data.
	 *
	 * @return the data
	 */
	public String getmData() {
		return mData;
	}

	/**
	 * Sets data.
	 *
	 * @param mData the m data
	 */
	public void setmData(String mData) {
		this.mData = mData;
	}

	/**
	 * Gets bitmap.
	 *
	 * @return the bitmap
	 */
	public Bitmap getmBitmap() {
		return mBitmap;
	}

	/**
	 * Sets bitmap.
	 *
	 * @param mBitmap the m bitmap
	 */
	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}

	/**
	 * Is successful boolean.
	 *
	 * @return the boolean
	 */
	public boolean isSuccessful() {
		return mCode >= 200 && mCode < 300;
	}

	/**
	 * Process response.
	 *
	 * @param arg the arg
	 */
	public final void processResponse(Object... arg) {
		if (isSuccessful()) {
			processResponseSuccess(arg);
		}
	}

	/**
	 * Process response success.
	 *
	 * @param arg the arg
	 */
	public abstract void processResponseSuccess(Object... arg);

	/**
	 * Gets response do.
	 *
	 * @param requestType the request type
	 * @return the response do
	 */
	public static BaseResponse getResponseDO(Request.RequestType requestType) {
		BaseResponse baseResponse = null;
		switch (requestType) {
			case GET_DEVICES:
				baseResponse = new DevicesListResponse();
				break;
			case POST_HEALTH_DATA:
				baseResponse = new PostHealthDataResponse();
				break;
			case GET_PAITENT_REPORT :
				baseResponse = new PaitentReportResponse();
				break;
			default:
				break;

		}
		return baseResponse;
	}

}
