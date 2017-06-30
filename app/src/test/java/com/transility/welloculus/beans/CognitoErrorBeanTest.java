package com.transility.welloculus.beans;

import com.transility.welloculus.BuildConfig;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 *
 * Test Cases of {@link CognitoErrorBean}
 *
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)

public class CognitoErrorBeanTest {

    CognitoErrorBean cognitoErrorBean;

    @Before
    public void setUpDeviceInfo() {
        cognitoErrorBean = new CognitoErrorBean();
    }
    
    @Test
    public void testsetServiceName(){
        String serviceName = "serviceName";
        cognitoErrorBean.setServiceName(serviceName);
        Assert.assertEquals("service name is not same",serviceName,cognitoErrorBean.getServiceName());
    }

    @Test
    public void testgetServiceName(){
        String serviceName = "serviceName";
        cognitoErrorBean.setServiceName(serviceName);
        Assert.assertEquals("service name is not same",serviceName,cognitoErrorBean.getServiceName());
    }

    @Test
    public void testsetMessage(){
        String message = "message";
        cognitoErrorBean.setMessage(message);
        Assert.assertEquals("message name is not same",message,cognitoErrorBean.getMessage());
    }

    @Test
    public void testgetMessage(){
        String message = "message";
        cognitoErrorBean.setMessage(message);
        Assert.assertEquals("message name is not same",message,cognitoErrorBean.getMessage());
    }

    @Test
    public void testsetStatusCode(){
        String statusCode = "statusCode";
        cognitoErrorBean.setStatusCode(statusCode);
        Assert.assertEquals("service name is not same",statusCode,cognitoErrorBean.getStatusCode());
    }

    @Test
    public void testgetStatusCode(){
        String statusCode = "statusCode";
        cognitoErrorBean.setStatusCode(statusCode);
        Assert.assertEquals("service name is not same",statusCode,cognitoErrorBean.getStatusCode());
    }

    @Test
    public void testsetErrorCode(){
        String errorCode = "errorCode";
        cognitoErrorBean.setErrorCode(errorCode);
        Assert.assertEquals("errorCode is not same",errorCode,cognitoErrorBean.getErrorCode());
    }

    @Test
    public void testgetErrorCode(){
        String errorCode = "errorCode";
        cognitoErrorBean.setErrorCode(errorCode);
        Assert.assertEquals("errorCode  is not same",errorCode,cognitoErrorBean.getErrorCode());
    }

    @Test
    public void testsetRequestId(){
        String requestId = "requestId";
        cognitoErrorBean.setRequestId(requestId);
        Assert.assertEquals("requestId is not same",requestId,cognitoErrorBean.getRequestId());
    }

    @Test
    public void testgetRequestId(){
        String requestId = "requestId";
        cognitoErrorBean.setRequestId(requestId);
        Assert.assertEquals("requestId  is not same",requestId,cognitoErrorBean.getRequestId());
    }
}
