package com.transility.welloculus.beans;


public class CognitoErrorBean {
    /**
     * The Message.
     */
    String message;

    /**
     * Sets service name.
     *
     * @param serviceName the service name
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Gets serviceName.
     *
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }
    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets status code.
     *
     * @param statusCode the status code
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


    /**
     * Gets status code.
     *
     * @return the status code
     */
    public String getStatusCode() {
        return statusCode;
    }
    /**
     * Gets error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets error code.
     *
     * @param errorCode the error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Sets request id.
     *
     * @param requestId the request id
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    /**
     * Gets requestId.
     *
     * @return the requestId
     */
    public String getRequestId() {
        return requestId;
    }
    /**
     * The Service name.
     */
    String serviceName;
    /**
     * The Status code.
     */
    String statusCode;
    /**
     * The Error code.
     */
    String errorCode;
    /**
     * The Request id.
     */
    String requestId;

}
