package com.transility.welloculus.bluetooth;

/**
 * Created by ImpetusDev on 4/15/2017.
 */
public class DeviceConnectException extends Exception {

    private String message = null;

    /**
     * Instantiates a new Device connect exception.
     */
    public DeviceConnectException() {
        super();
    }

    /**
     * Instantiates a new Device connect exception.
     *
     * @param message the message
     */
    public DeviceConnectException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Instantiates a new Device connect exception.
     *
     * @param cause the cause
     */
    public DeviceConnectException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}