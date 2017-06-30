package com.transility.welloculus.bluetooth.zephyr;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.transility.welloculus.BuildConfig;
import com.transility.welloculus.utils.AppUtility;

import zephyr.android.HxMBT.BTClient;
import zephyr.android.HxMBT.ConnectListenerImpl;
import zephyr.android.HxMBT.ConnectedEvent;
import zephyr.android.HxMBT.ZephyrPacketArgs;
import zephyr.android.HxMBT.ZephyrPacketEvent;
import zephyr.android.HxMBT.ZephyrPacketListener;
import zephyr.android.HxMBT.ZephyrProtocol;

/**
 * Class to manage Zephyr Connection.
 *
 * @author arpit.garg
 */
public class ZephyrConnectListener extends ConnectListenerImpl {
    private Handler handler;
    private final int PACKET_HR_SPD_DIST = 0x26;
    private final int HEART_RATE = 0x100;
    private final int INSTANT_SPEED = 0x101;
    private HRSpeedDistPacketInfo HRSpeedDistPacket = new HRSpeedDistPacketInfo();

    /**
     * Instantiates a new Zephyr connect listener.
     *
     * @param handler     the handler
     * @param _NewHandler the new handler
     */
    public ZephyrConnectListener(Handler handler, Handler _NewHandler) {
        super(handler, null);
        this.handler = _NewHandler;
    }

    public void Connected(ConnectedEvent<BTClient> eventArgs) {
        if (BuildConfig.DEBUG) {
            Log.v(AppUtility.TAG, String.format("Connected to BioHarness %s.", eventArgs.getSource().getDevice().getName()));
        }
        //Creates a new ZephyrProtocol object and passes it the BTComms object
        ZephyrProtocol zephyrProtocol = new ZephyrProtocol(eventArgs.getSource().getComms());
        zephyrProtocol.addZephyrPacketEventListener(new ZephyrPacketListener() {
            public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
                ZephyrPacketArgs msg = eventArgs.getPacket();
                byte CRCFailStatus;
                byte rcvdBytes;


                //CRCFailStatus = msg.getCRCStatus();
                //rcvdBytes = msg.getNumRvcdBytes();
                if (PACKET_HR_SPD_DIST == msg.getMsgID()) {


                    byte[] data = msg.getBytes();

                    //***************Displaying the Heart Rate********************************
                    int HRate = Math.abs(HRSpeedDistPacket.GetHeartRate(data));
                    Message text1 = handler.obtainMessage(HEART_RATE);
                    Bundle b1 = new Bundle();
                    b1.putInt(AppUtility.EXTRAS_HEART_RATE, HRate);
                    text1.setData(b1);
                    handler.sendMessage(text1);
                    if (BuildConfig.DEBUG) {
                        Log.v(AppUtility.TAG, "Heart Rate is " + HRate);
                    }
                    //***************Displaying the Instant Speed********************************
                    double InstantSpeed = HRSpeedDistPacket.GetInstantSpeed(data);

                    text1 = handler.obtainMessage(INSTANT_SPEED);
                    if (BuildConfig.DEBUG) {
                        b1.putDouble("InstantSpeed", InstantSpeed);
                    }
                    text1.setData(b1);
                    handler.sendMessage(text1);
                    if (BuildConfig.DEBUG) {
                        Log.v(AppUtility.TAG, "Instant Speed is " + InstantSpeed);
                    }
                }
            }
        });
    }

}