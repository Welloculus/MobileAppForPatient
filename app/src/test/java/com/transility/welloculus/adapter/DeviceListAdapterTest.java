package com.transility.welloculus.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoRefreshToken;
import com.transility.welloculus.BuildConfig;
import com.transility.welloculus.R;
import com.transility.welloculus.app.receiver.OnDeviceConnectListener;
import com.transility.welloculus.beans.DeviceInfoBean;
import com.transility.welloculus.cognito.CognitoHelper;
import com.transility.welloculus.ui.DeviceListActivity;
import com.transility.welloculus.utils.AppUtility;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

/**
 * Test case to check the adapters.
 *
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class DeviceListAdapterTest {

    private ActivityController<DeviceListActivity> activity;
    private CognitoUserSession cognitoUserSession;

    @Before
    public void setUp() {

        CognitoHelper.setUser("sneha03");
        cognitoUserSession  = Mockito.mock(CognitoUserSession.class);
        Mockito.when(cognitoUserSession.getRefreshToken()).thenReturn(new CognitoRefreshToken("refresh_token"));
        Mockito.when(cognitoUserSession.getAccessToken()).thenReturn(new CognitoAccessToken("access_token"));
        Mockito.when(cognitoUserSession.getIdToken()).thenReturn(new CognitoIdToken("id_token"));
        CognitoHelper.setCurrSession(cognitoUserSession);

        activity = Robolectric.buildActivity(DeviceListActivity.class).create();

    }

        @Test
        public void test001CheckIfCountOfItemsIsCorrect(){
            CognitoHelper.setUser("sneha03");
            ArrayList<DeviceInfoBean> devicesList = new ArrayList<>();

            DeviceInfoBean deviceInfo1=new DeviceInfoBean("device_state1","device_udi1","device_name1","bluetooth");
            devicesList.add(deviceInfo1);
            DeviceInfoBean deviceInfoBean2=new DeviceInfoBean("device_state2","device_udi3","device_name2","IOT");
            devicesList.add(deviceInfoBean2);
            DeviceListAdapter deviceAdapter=new DeviceListAdapter(RuntimeEnvironment.application.getApplicationContext(),devicesList,new OnDeviceConnectListener(){
                @Override
                public void onConnectClick(DeviceInfoBean deviceInfo) {

                }
                @Override
                public void onConnectClick(DeviceInfoBean deviceInfo, View v) {

                }
            });

            Assert.assertEquals("Count is incorrect",devicesList.size(),deviceAdapter.getItemCount());

        }


    @Test
    public void test002CheckIfUserDataisCorrectShown_HEARTRATE(){
        CognitoHelper.setUser("sneha03");
        ArrayList<DeviceInfoBean> devicesList=new ArrayList<>();
        DeviceInfoBean deviceInfo1=new DeviceInfoBean("device_state1","device_udi1","device_name1", AppUtility.BLUETOOTH);
        devicesList.add(deviceInfo1);
        DeviceInfoBean deviceInfoBean2=new DeviceInfoBean("device_state2","device_udi3","device_name2",AppUtility.IOT);
        devicesList.add(deviceInfoBean2);
        DeviceListAdapter deviceAdapter=new DeviceListAdapter(RuntimeEnvironment.application.getApplicationContext(),devicesList,new OnDeviceConnectListener(){
            @Override
            public void onConnectClick(DeviceInfoBean deviceInfo) {

            }
            @Override
            public void onConnectClick(DeviceInfoBean deviceInfo, View v) {

            }
        });

        RecyclerView reportsScreenRv= (RecyclerView) activity.get().findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.get());
        reportsScreenRv.setAdapter(deviceAdapter);
        reportsScreenRv.setLayoutManager(layoutManager);

        DeviceListAdapter.ListItemViewHolder reportsRecyclerViewHolder =deviceAdapter.onCreateViewHolder(reportsScreenRv,0);
        deviceAdapter.onBindViewHolder(reportsRecyclerViewHolder,0);

        Assert.assertEquals("Heart Rate",reportsRecyclerViewHolder.txtDeviceAttr.getText());

    }

    @Test
    public void test002CheckIfUserDataisCorrectShown_TEMPERATURE(){
        CognitoHelper.setUser("arpit");
        ArrayList<DeviceInfoBean> devicesList=new ArrayList<>();
        DeviceInfoBean deviceInfo1=new DeviceInfoBean("device_state1","device_udi1","device_name1", AppUtility.BLUETOOTH);
        devicesList.add(deviceInfo1);
        DeviceInfoBean deviceInfoBean2=new DeviceInfoBean("device_state2","device_udi3","device_name2",AppUtility.IOT);
        devicesList.add(deviceInfoBean2);
        DeviceListAdapter deviceAdapter=new DeviceListAdapter(RuntimeEnvironment.application.getApplicationContext(),devicesList,new OnDeviceConnectListener(){
            @Override
            public void onConnectClick(DeviceInfoBean deviceInfo) {

            }
            @Override
            public void onConnectClick(DeviceInfoBean deviceInfo, View v) {

            }
        });

        RecyclerView reportsScreenRv= (RecyclerView) activity.get().findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.get());
        reportsScreenRv.setAdapter(deviceAdapter);
        reportsScreenRv.setLayoutManager(layoutManager);

        DeviceListAdapter.ListItemViewHolder reportsRecyclerViewHolder =deviceAdapter.onCreateViewHolder(reportsScreenRv,1);
        deviceAdapter.onBindViewHolder(reportsRecyclerViewHolder,1);

        Assert.assertEquals("Room Tempreture",reportsRecyclerViewHolder.txtDeviceAttr.getText());

    }

        @After
        public void tearDown(){

            activity=null;
            CognitoHelper.setCurrSession(null);
            CognitoHelper.setUser(null);
        }
    }
