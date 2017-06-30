package com.transility.welloculus.httpresponse;

/**
 * The interface Http response handler.
 */
public interface HttpResponseHandler {

	/**
	 * Handle response.
	 *
	 * @param baseResponse the base response
	 */
	public void handleResponse(BaseResponse baseResponse);

	/**
	 * Handle error.
	 *
	 * @param baseResponse the base response
	 */
	public void handleError(BaseResponse baseResponse);

}
