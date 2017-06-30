package com.transility.welloculus.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.transility.welloculus.R;
import com.transility.welloculus.adapter.DeviceListAdapter;
import com.transility.welloculus.app.receiver.IHealthCareReceiver;
import com.transility.welloculus.app.receiver.OnDeviceConnectListener;
import com.transility.welloculus.beans.DeviceInfoBean;
import com.transility.welloculus.beans.DeviceResponseBean;
import com.transility.welloculus.bluetooth.BluetoothHandler;
import com.transility.welloculus.bluetooth.DeviceConnectException;
import com.transility.welloculus.httpresponse.BaseResponse;
import com.transility.welloculus.httpresponse.DevicesListResponse;
import com.transility.welloculus.httpresponse.HttpResponseHandler;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.utils.Constants;

import java.util.ArrayList;

/**
 * Activity to show list of devices associated with the user.
 */
public class DeviceListActivity extends BaseActivity implements OnDeviceConnectListener, IHealthCareReceiver {
    protected RecyclerView mRecyclerView;
    private DeviceListAdapter mAdapter;
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    private IHealthCareReceiver healthCareReceiver;
    private Context mContext;
    private DeviceInfoBean mDeviceInfo;
    protected ArrayList<DeviceInfoBean> deviceInfoList = new ArrayList<>();
    private int mGreenColor;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list_actvity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;
        healthCareReceiver = this;
        initUI();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mGreenColor = ContextCompat.getColor(mContext, R.color.green);

        if (isNetworkAvailable(mContext, getString(R.string.not_internet_connectivity))) {

            fetchDeviceDetails();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void fetchDeviceDetails() {
        /*Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        String postData = "{\n" +
                "    \"event\" : {\n" +
                "        \"username\":" + " \"" + "sneha03" + "\" " +
                "    }\n" +
                "}";
        Request request = new Request().setUri(UrlConfig.BASE_URL2).setMethod(Request.Method.POST).setRequestType(Request.RequestType.GET_DEVICES).setPostData(postData);
        RetrieveDataFromServer retriveDataFromServer = new RetrieveDataFromServer(mContext.getApplicationContext()).setRequest(request, new GetDeviceListResponseHandler());
       retriveDataFromServer.execute();
        showWaitDialog(mContext.getString(R.string.fetching_device_list), mContext.getString(R.string.please_wait), mContext);*/
        MockGetDeviceListResponseHandler();
    }


    @Override
    public void onConnectClick(DeviceInfoBean deviceInfo) {
        mDeviceInfo = deviceInfo;
        performDeviceAction(deviceInfo);
    }

    @Override
    public void onConnectClick(DeviceInfoBean deviceInfo, View v) {
        //mDeviceInfo = deviceInfo;
        //performDeviceAction(deviceInfo);
    }

    /*
        @Override
        public void onConnectClick(DeviceInfoBean deviceInfo, View v) {
            //showWaitDialog(mContext.getString(R.string.fetching_device_list), mContext.getString(R.string.please_wait), mContext);
            // /mDeviceInfo = deviceInfo;
            //performDeviceAction(deviceInfo,v);
        }
    */
    @Override
    protected void initUI() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new DeviceListAdapter(mContext, deviceInfoList, this);
    }

    /**
     * connect to the bluetooth
     *
     * @param deviceInfo
     */
    private void connectToBluetooth(final DeviceInfoBean deviceInfo) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                switch (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    case PackageManager.PERMISSION_DENIED:
                        performPermissionDeniedAction(deviceInfo);
                        break;
                    case PackageManager.PERMISSION_GRANTED:
                        startDeviceConnection(deviceInfo, healthCareReceiver);
                        break;
                    default:
                        break;
                }
            } else {
                startDeviceConnection(deviceInfo, healthCareReceiver);
            }

        } catch (RuntimeException e) {
            showToastMessage(e.getMessage(), mContext);
            Log.e(AppUtility.TAG, Log.getStackTraceString(e));
        }

    }

    private void performPermissionDeniedAction(DeviceInfoBean deviceInfo) {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION);
        } else {
            startDeviceConnection(deviceInfo, healthCareReceiver);
        }
    }

    /**
     * start the device connection
     *
     * @param deviceInfo
     * @param receiver
     */
    private void startDeviceConnection(final DeviceInfoBean deviceInfo, IHealthCareReceiver receiver) {
        try {
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                showBluetoothDisabledDialog();
            } else {
                BluetoothHandler.getInstance().connectToBluetooth(mContext, deviceInfo, receiver);
            }

        } catch (DeviceConnectException e) {
            Log.e(AppUtility.TAG, Log.getStackTraceString(e));
            showToastMessage(e.getMessage(), mContext);
        }
    }

    private void showBluetoothDisabledDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.bluetooth_is_disabled))
                .setMessage(mContext.getString(R.string.bluetooth_should_be_enabled))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentOpenBluetoothSettings = new Intent();
                        intentOpenBluetoothSettings.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivity(intentOpenBluetoothSettings);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showToastMessage(mContext.getString(R.string.bluetooth_should_be_enabled), mContext);
                    }
                })
                .setIcon(R.drawable.app_icon)
                .show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(mGreenColor);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(mGreenColor);
    }

    /**
     * disconnect the device
     */
    private void disconnectDevice() {
        BluetoothHandler.getInstance().disconnect(mContext);
    }

    @Override
    public void onHeartRateReceived(int heartRate, DeviceInfoBean deviceInfo) {
        Log.v(AppUtility.TAG, "heartRate : " + heartRate);
    }


    @Override
    public void isConnected(boolean connect) {
        if (connect) {
            showToastMessage(mContext.getString(R.string.device_connected), mContext);
        } else {
            showToastMessage(mContext.getString(R.string.device_disconnected), mContext);
        }
        mAdapter = new DeviceListAdapter(mContext, deviceInfoList, this);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_COARSE_LOCATION:
                boolean isPermitted = false;
                for (int gr : grantResults) {
                    // Check if request is granted or not
                    if (gr == PackageManager.PERMISSION_GRANTED) {
                        isPermitted = true;
                    }
                }
                if (isPermitted) {
                    if (mDeviceInfo != null) {
                        connectToBluetooth(mDeviceInfo);
                    }
                } else {
                    showToastMessage(mContext.getString(R.string.bluetooth_requires_location_permission), mContext);
                }
                break;
            default:
                return;
        }
    }


    /**
     * perform the device disconnect action
     */
    private void performDeviceDisconnect(final DeviceInfoBean deviceInfo) {

        if (!BluetoothHandler.getInstance().getConnectedDevice().getDevice_udi().equalsIgnoreCase(mDeviceInfo.getDevice_udi())) {
            AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getString(R.string.another_device_already_connected))
                    .setMessage(mContext.getString(R.string.confirm_proceed_connect_new_device))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            disconnectDevice();
                            connectToBluetooth(deviceInfo);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.drawable.app_icon)
                    .show();
            Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(mGreenColor);
            Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(mGreenColor);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                    .setTitle(R.string.disconnect_device_tittle)
                    .setMessage(R.string.disconnect_device_message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            disconnectDevice();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.drawable.app_icon)
                    .show();
            Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(mGreenColor);
            Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(mGreenColor);
        }
    }

    /**
     * perform the device connect and disconnect action
     */
    private void performDeviceAction(DeviceInfoBean deviceInfo) {
        if (BluetoothHandler.getInstance().isConnected()) {
            performDeviceDisconnect(deviceInfo);
        } else {
            performDeviceConnect(deviceInfo);
        }
    }

    /**
     * perform device disconnect action
     */
    private void performDeviceConnect(DeviceInfoBean deviceInfo) {
        connectToBluetooth(deviceInfo);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DeviceList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private void  MockGetDeviceListResponseHandler() {
        //ArrayList<DeviceInfoBean> deviceList1 = (ArrayList<DeviceInfoBean>) response.getResponseContent();
        ArrayList<DeviceInfoBean> deviceList1 = new ArrayList<DeviceInfoBean>();
        DeviceInfoBean deviceMock = new DeviceInfoBean("true", "00:07:80:5A:3A:26", "zephyr", "Bluetooth", true , "03-05-2017");
        deviceList1.add(deviceMock);
        ArrayList<DeviceInfoBean> deviceList = new ArrayList<DeviceInfoBean>();
        for (DeviceInfoBean g : deviceList1) {
            System.out.println(g.getDevice_state());
            if (g.getDevice_state() == "true") {
                deviceList.add(g);
            }
        }
        if (deviceList == null) {
            deviceInfoList.clear();
        } else {
            deviceInfoList.addAll(deviceList);
        }

            prepareDeviceInfoData();
    }
    private void prepareDeviceInfoData() {
        mAdapter.notifyDataSetChanged();
    }

    private class GetDeviceListResponseHandler implements HttpResponseHandler {
        @Override
        public void handleResponse(BaseResponse baseResponse) {

            DevicesListResponse deviceListResponse = (DevicesListResponse) baseResponse;

            DeviceResponseBean response = deviceListResponse.getDeviceResponse();

            if (response != null) {
                if (response.getSuccess() && (response.getStatus() == Constants.HTTP_SUCCESS)) {
                    //ArrayList<DeviceInfoBean> deviceList1 = (ArrayList<DeviceInfoBean>) response.getResponseContent();
                    ArrayList<DeviceInfoBean> deviceList1 = new ArrayList<DeviceInfoBean>();
                    DeviceInfoBean deviceMock = new DeviceInfoBean("true", "00:07:80:5A:3A:26", "zephyr", "Bluetooth", true);
                    deviceList1.add(deviceMock);
                    ArrayList<DeviceInfoBean> deviceList = new ArrayList<DeviceInfoBean>();
                    for (DeviceInfoBean g : deviceList1) {
                        System.out.println(g.getDevice_state());
                        if (g.getDevice_state() == "true") {
                            deviceList.add(g);
                        }
                    }
                    if (deviceList == null) {
                        deviceInfoList.clear();
                    } else {
                        deviceInfoList.addAll(deviceList);
                    }
                    if (deviceInfoList.isEmpty()) {
                        showNoDeviceFound(getString(R.string.no_device_found), false);
                        showToastMessage(mContext.getString(R.string.no_device_found), mContext);
                    } else {
                        showNoDeviceFound("", true);
                        prepareDeviceInfoData();
                    }
                } else {
                    showNoDeviceFound(getString(R.string.no_device_found), false);
                    showToastMessage(response.getErrorMessage(), mContext);
                }

                Log.e(AppUtility.TAG, " success : " + response.getSuccess() + " status : " + response.getStatus());
            } else {
                showNoDeviceFound(getString(R.string.no_device_found), false);
                showToastMessage(mContext.getString(R.string.internal_error), mContext);
            }
            closeWaitDialog();

        }

        private void prepareDeviceInfoData() {
            mAdapter.notifyDataSetChanged();
        }

        private void showNoDeviceFound(String message, boolean showDeviceList) {
            TextView tv = (TextView) findViewById(R.id.device_message);
            tv.setVisibility(showDeviceList ? View.GONE : View.VISIBLE);
            tv.setText(message);
        }

        @Override
        public void handleError(BaseResponse baseResponse) {
            closeWaitDialog();
            Log.e(AppUtility.TAG, "response " + baseResponse);
            Log.e(AppUtility.TAG, "response " + baseResponse.getmStatus());
            showToastMessage(baseResponse.getmStatus(), mContext);
        }
    }


}
