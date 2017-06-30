package com.transility.welloculus.httpresponse;

import com.transility.welloculus.beans.ResponseBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * The type Post health data response.
 */
public class PostHealthDataResponse extends BaseResponse {

    /**
     * The Response bean.
     */
    ResponseBean responseBean;

    /**
     * Gets response bean.
     *
     * @return the response bean
     */
    public ResponseBean getResponseBean() {
        return responseBean;
    }

    /**
     * Sets response bean.
     *
     * @param responseBean the response bean
     */
    public void setResponseBean(ResponseBean responseBean) {
        this.responseBean = responseBean;
    }

    @Override
    public void processResponseSuccess(Object... arg) {
        //parsing logic goes here
        Type type = new TypeToken<ResponseBean>() {
        }.getType();
        responseBean = new Gson().fromJson(getmData(), type);
    }

}
