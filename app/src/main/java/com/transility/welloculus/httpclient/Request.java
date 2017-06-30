package com.transility.welloculus.httpclient;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Request.
 */
public class Request {

    private Method method;
    private String uri;
    private Map<String, String> params;
    private RequestType requestType;
    private String postData;

    /**
     * Instantiates a new Request.
     */
    public Request() {
        params = new HashMap<String, String>();
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Gets post data.
     *
     * @return the post data
     */
    public String getPostData() {
        return postData;
    }

    /**
     * Sets post data.
     *
     * @param postData the post data
     * @return the post data
     */
    public Request setPostData(String postData) {
        this.postData = postData;
        return this;
    }

    /**
     * Sets method.
     *
     * @param method the method
     * @return the method
     */
    public Request setMethod(Method method) {
        this.method = method;

        return this;
    }

    /**
     * Gets uri.
     *
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets uri.
     *
     * @param uri the uri
     * @return the uri
     */
    public Request setUri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * Gets params.
     *
     * @return the params
     */
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * Sets params.
     *
     * @param params the params
     * @return the params
     */
    public Request setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    /**
     * Add param request.
     *
     * @param name  the name
     * @param value the value
     * @return the request
     */
    public Request addParam(String name, String value) {
        params.put(name, value);
        return this;
    }

    /**
     * The enum Method.
     */
    public enum Method {
        /**
         * Get method.
         */
        GET, /**
         * Post method.
         */
        POST

    }

    /**
     * The enum Request type.
     */
    public  enum RequestType {
        /**
         * Simple response request type.
         */
        SIMPLE_RESPONSE, /**
         * Get devices request type.
         */
        GET_DEVICES , /**
         * Post health data request type.
         */
        POST_HEALTH_DATA, /**
         * Get paitent report request type.
         */
        GET_PAITENT_REPORT,

        UPDATE_PROFILE;

        private boolean isWorking = false;

        /**
         * Sets as working.
         *
         * @param isWorking the is working
         */
        public void setAsWorking(boolean isWorking) {
            this.isWorking = isWorking;
        }

        /**
         * Is working boolean.
         *
         * @return the boolean
         */
        public boolean isWorking() {
            return this.isWorking;
        }

    }

    /**
     * Gets request type.
     *
     * @return the request type
     */
    public RequestType getRequestType() {
        return requestType;
    }

    /**
     * Sets request type.
     *
     * @param requestType the request type
     * @return the request type
     */
    public Request setRequestType(RequestType requestType) {
        this.requestType = requestType;
        return this;
    }

}