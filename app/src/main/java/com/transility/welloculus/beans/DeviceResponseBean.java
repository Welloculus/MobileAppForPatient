package com.transility.welloculus.beans;

import java.util.List;

/**
 * Created by sneha.bansal on 4/11/2017.
 */
public class DeviceResponseBean extends ResponseBean {

    private List<DeviceInfoBean> responseContent;

    /**
     * Gets response content.
     *
     * @return the response content
     */
    public List<DeviceInfoBean> getResponseContent ()
    {
        return responseContent;
    }

    /**
     * Sets response content.
     *
     * @param responseContent the response content
     */
    public void setResponseContent (List<DeviceInfoBean> responseContent)
    {
        this.responseContent = responseContent;
    }

    @Override
    public String toString() {
        return "ClassPojo [errorMessage = " + errorMessage + ", responseCode = " + responseCode + ", status = " + status + ", responseContent = " + responseContent + ", errorCode = " + errorCode + ", success = " + success + "]";
    }

}
