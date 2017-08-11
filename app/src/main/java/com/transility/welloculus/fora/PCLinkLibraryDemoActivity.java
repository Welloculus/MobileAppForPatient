/*
 * Copyright (C) 2012 TaiDoc Technology Corporation. All rights reserved.
 */

package com.transility.welloculus.fora;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.taidoc.pclinklibrary.android.bluetooth.util.BluetoothUtil;
import com.transility.welloculus.fora.constant.PCLinkLibraryDemoConstant;
import com.transility.welloculus.fora.interfaces.CustomAlertOnClickListener;
import com.transility.welloculus.fora.preference.MeterPreferenceDialog;
import com.transility.welloculus.fora.util.GuiUtils;
import com.taidoc.pclinklibrary.util.LogUtil;
import com.transility.welloculus.R;

public class PCLinkLibraryDemoActivity extends FragmentActivity {
	private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
	private final String TAG = PCLinkLibraryDemoActivity.class.getSimpleName();
	
	// Views
	private LinearLayout mPL2303Layout;
	private LinearLayout mBTLayout;
	private Spinner mBaudRate;
	private Button mPL2303Connect;
	
    private Spinner mSelectedMeter;
    private Button mConnect;
    private Button mBLEPair;
    private RadioGroup mMeterTransferType;
    private TextView mCopyright;
    
    private MeterPreferenceDialog meterDialog;
    
    private BroadcastReceiver detachReceiver;
    
    private boolean mBLEMode;
    private boolean mKNVMode;
    private int mPairedMeterCount;
    private Map<String, String> mPairedMeterNames;
    private Map<String, String> mPairedMeterAddrs;
    
    private static final String ACTION_USB_PERMISSION = "com.taidoc.pclinklibrary.demo.USB_PERMISSION";
    
    public Dialog permissionDialog;
    public int needRequestPermission;
    
    // Listeners
    private OnItemSelectedListener mSelectedMeterOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
				long id) {
			
			TextView view = (TextView)selectedItemView;
			if (view != null) {
				int indexOfBLE = isBLEMeter(view.getText().toString());
				if (indexOfBLE != -1) {
					mMeterTransferType.check(R.id.meterBtType2RadioButton);
					for (View v : mMeterTransferType.getTouchables()) {
						v.setEnabled(false);
					}
					
					mBLEMode = true;
					mKNVMode = view.getText().toString().toLowerCase().contains("knv v125");
				}
				else {
					for (int i=0; i<mMeterTransferType.getChildCount(); i++) {
						View v = mMeterTransferType.getChildAt(i);
						v.setEnabled(true);
					}
					
					mBLEMode = false;
					mKNVMode = false;
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parentView) {
		}
	};
	
    private Button.OnClickListener mConnectOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// 設定選擇裝置
			// Set the selection device
            if (mSelectedMeter.getSelectedItem() != null) {
            	String deviceMac = mSelectedMeter.getSelectedItem().toString().split("/")[1];
                if (!"".equals(deviceMac)) {
                    // Set pair device mac address
                    SharedPreferences settings = getSharedPreferences(
                            PCLinkLibraryDemoConstant.SHARED_PREFERENCES_NAME, 0);

                    settings.edit()
                            .putString(PCLinkLibraryDemoConstant.PAIR_METER_MAC_ADDRESS,
                                    deviceMac).commit();

                    int resId = mMeterTransferType.getCheckedRadioButtonId();
                    
                    // 設定BT Transfer Type
					// Set BT Transfer Type
                    if (resId == R.id.meterBtType1RadioButton) {
                        // Type One
                    	settings.edit()
                                .putString(PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE,
                                        PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE_ONE)
                                .putBoolean(PCLinkLibraryDemoConstant.BLE_MODE, false)
                                .putBoolean(PCLinkLibraryDemoConstant.KNV_MODE, false)
                                .commit();
                    } else {
                        // Type Two
                        settings.edit()
                                .putString(PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE,
                                        PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE_TWO)
                                .putBoolean(PCLinkLibraryDemoConstant.BLE_MODE, mBLEMode)
                                .putBoolean(PCLinkLibraryDemoConstant.KNV_MODE, mKNVMode).commit();
                    } /* end of if */

                    // Go to the test command activity
                    if (mKNVMode) {
                    	GuiUtils.goToSpecifiedActivity(PCLinkLibraryDemoActivity.this,
                    			PCLinkLibraryCommuTestActivityForKNV.class);
                    }
                    else {
                    	GuiUtils.goToSpecifiedActivity(PCLinkLibraryDemoActivity.this,
                    			PCLinkLibraryCommuTestActivity.class);
                    }
                } /* end of if */
            } else {
                new AlertDialog.Builder(PCLinkLibraryDemoActivity.this)
                        .setMessage(R.string.bluetooth_need_to_pair)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Go to the setting page
                                startActivityForResult(new Intent(
                                        android.provider.Settings.ACTION_BLUETOOTH_SETTINGS), 0);
                            };
                        }).show();
            } /* end of if */
		}
    };
    
    private Button.OnClickListener mBLEPairOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			ShowMeterDialog();
		}
    };
    
    private Button.OnClickListener mPL2303ConnectOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {			
			SharedPreferences settings = getSharedPreferences(
                    PCLinkLibraryDemoConstant.SHARED_PREFERENCES_NAME, 0);
			
			settings.edit()
            .putString(PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE,
                    PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE)
            .commit();
			
			settings.edit()
            .putString(PCLinkLibraryDemoConstant.BAUDRATE,
                    mBaudRate.getSelectedItem().toString())
            .commit();
			
			GuiUtils.goToSpecifiedActivity(PCLinkLibraryDemoActivity.this,
                    PCLinkLibraryCommuTestActivity.class);
		}
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_paired_device_and_type);
        
        findViews();
        setListener();

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (needRequestPermission == 0) {
				needRequestPermission = !checkPermissions() ? 1 : 2;
				if (needRequestPermission == 1) {
					requestPermissions();
				}
			}
		}
    }
    
    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkPermissions() {
        // Android M Permission check 
        int result = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions() {
        // Android M Permission check 

        if (permissionDialog == null) {
            permissionDialog = GuiUtils.showConfirmDialog(PCLinkLibraryDemoActivity.this, false,
                    getString(R.string.confirm), getString(R.string.error_location_not_supported), new CustomAlertOnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, View view) {
                            // 若有設定cancelable則有可能不由button按下,所以只會有一次,
                            // 若是由按鈕按下,則會進入二次,所以只要在dialog會null(dismiss呼叫)時,
                            // 再執行目的程式即可
							
							// If there is a set cancelable may not press the button, so only once,
                            // If the button is pressed, it will enter the second, so as long as the dialog will be null (dismiss call)
                            // execute the target program again
							
                            if (dialog != null) {
                                dialog.dismiss();
                                permissionDialog = null;
                                requestPermissions(new String[] {
                                		Manifest.permission.ACCESS_COARSE_LOCATION },
                                        PERMISSION_REQUEST_COARSE_LOCATION);
                            }
                        }
                    });
        }
    }

    @Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		//super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    	
    	switch (requestCode) {
        case PERMISSION_REQUEST_COARSE_LOCATION:
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "coarse location permission granted");
                
                if (needRequestPermission == 1) {
        			needRequestPermission = 2;
        		}
            }
            else {
                GuiUtils.showConfirmDialog(PCLinkLibraryDemoActivity.this, false,
                        getString(R.string.confirm), getString(R.string.error_location_not_supported2), new CustomAlertOnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, View view) {
                                // 若有設定cancelable則有可能不由button按下,所以只會有一次,
                                // 若是由按鈕按下,則會進入二次,所以只要在dialog會null(dismiss呼叫)時,
                                // 再執行目的程式即可
								
								// If there is a set cancelable may not press the button, so only once,
								// If the button is pressed, it will enter the second, so as long as the dialog will be null (dismiss call)
								// execute the target program again
                                if (dialog != null) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                        });
            }
            return;
    	}
    }
    
    /**
     * 清除配對設定
     */
    private void clearSharePreferences() {
        // set pair device mac address
        SharedPreferences settings = getSharedPreferences(
                PCLinkLibraryDemoConstant.SHARED_PREFERENCES_NAME, 0);
        settings.edit().remove(PCLinkLibraryDemoConstant.PAIR_METER_MAC_ADDRESS).commit();
    }

    /**
     * 設定此Activity會用到的View
     */
    private void findViews() {
    	mPL2303Layout = (LinearLayout) findViewById(R.id.ll_top);
    	mBTLayout = (LinearLayout) findViewById(R.id.ll_body);
    	
    	mBaudRate = (Spinner) findViewById(R.id.spBaudRate);
    	mPL2303Connect = (Button) findViewById(R.id.btnPL2303ConnectMeter);
    	
        mSelectedMeter = (Spinner) findViewById(R.id.pairDeviceList);
        mConnect = (Button) findViewById(R.id.btnConnectMeter);
        mBLEPair = (Button) findViewById(R.id.btnPair);
        mMeterTransferType = (RadioGroup) findViewById(R.id.meterBtTransferTypeRadioGroup);
    }

    /**
     * 設定此Activity會用到的Listener
	 * Set the Listener to use this Activity
     */
    private void setListener() {
    	mConnect.setOnClickListener(mConnectOnClickListener);
    	mBLEPair.setOnClickListener(mBLEPairOnClickListener);
    	mSelectedMeter.setOnItemSelectedListener(mSelectedMeterOnItemSelectedListener);
    	mPL2303Connect.setOnClickListener(mPL2303ConnectOnClickListener);
    }
    
    /**
     * 搜尋已配對的Bluetooth裝置，並將找到的裝置顯示在UI上面
	 * 
	 * Search for paired Bluetooth devices and display the found devices on the UI
     */
    public void queryBluetoothDevicesAndSetToUi() {

        new Thread(new Runnable() {
            private Handler handler = new Handler();
            private ProgressDialog processDialog;

            @Override
            public void run() {
                Looper.prepare();
                try {
                    final List<String> remoteDeviceNameList = new ArrayList<String>();
                    final ArrayAdapter<String> selectedMeterSpinnerAdapter = new ArrayAdapter<String>(
                            PCLinkLibraryDemoActivity.this, android.R.layout.simple_spinner_item,
                            remoteDeviceNameList);
                    selectedMeterSpinnerAdapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            processDialog = ProgressDialog.show(PCLinkLibraryDemoActivity.this,
                                    null, getString(R.string.start_search_meter), true);
                            mSelectedMeter.setAdapter(selectedMeterSpinnerAdapter);
                        }
                    });

                    List<BluetoothDevice> remoteDeviceList = BluetoothUtil.getPairedDevices();

                    for (int i = 0; i < remoteDeviceList.size(); i++) {
                        String remoteDeviceName = remoteDeviceList.get(i).getName() + "/"
                                + remoteDeviceList.get(i).getAddress();
                        remoteDeviceNameList.add(remoteDeviceName);
                    } /* end of for */
                    
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        	int adapterCount = initBLEMeters();
                        	
                        	adapterCount += selectedMeterSpinnerAdapter.getCount();
                        	
                        	if (adapterCount > 0) {
                                selectedMeterSpinnerAdapter.notifyDataSetChanged();                                
                                mSelectedMeter.setSelection(0);                                
                                mSelectedMeterOnItemSelectedListener.onItemSelected(mSelectedMeter,
                                		mSelectedMeter.getChildAt(0), 0, 0);
                            } else {
                            	// 如果手機不支援ble, 則要求user一定要在設定頁先設定
								// If the phone does not support ble, then the user must be set in the set page first
                            	if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                            		showNeeToPairMessageDialog();
                            	}
                            } /* end of if */
                        }
                    }, 1000);
                } catch (Exception e) {
                    LogUtil.error(PCLinkLibraryDemoActivity.class, e.getMessage(), e);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showErrorMessageDialog();
                        }
                    });
                } finally {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (processDialog != null) {
                                processDialog.dismiss();
                            } /* end of if */
                        }
                    });
                }
                Looper.loop();
            }

            /**
             * 顯示錯誤訊息
			 * Displays an error message
             */
            private void showErrorMessageDialog() {
                new AlertDialog.Builder(PCLinkLibraryDemoActivity.this)
                        .setMessage(R.string.bluetooth_occur_error)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Back to the home menu
                                GuiUtils.goToSpecifiedActivity(PCLinkLibraryDemoActivity.this,
                                        PCLinkLibraryDemoActivity.class);
                            };
                        }).show();
            }

            /**
             * 顯示錯誤訊息
			 * Displays an error message
             */
            private void showNeeToPairMessageDialog() {
                new AlertDialog.Builder(PCLinkLibraryDemoActivity.this)
                        .setMessage(R.string.bluetooth_need_to_pair)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Go to the setting page
                                startActivityForResult(new Intent(
                                        android.provider.Settings.ACTION_BLUETOOTH_SETTINGS), 0);
                            };
                        }).show();
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (detachReceiver != null) {
			unregisterReceiver(detachReceiver);
			detachReceiver = null;			
		}
	}

	/**
     * 當接收到usb時,會由onResume進入
	 * When receiving usb, it will be entered by onResume
     */
	@Override
	protected void onResume() {
		super.onResume();
		        
        boolean fromPl2303 = false;
		Bundle bundle = getIntent().getExtras();
		if(bundle != null && bundle.containsKey(PCLinkLibraryDemoConstant.FromPL2303)) {
			fromPl2303 = bundle.getBoolean(PCLinkLibraryDemoConstant.FromPL2303); 
		}
		
		// 由usb attach呼叫
		//Called by usb attach
		if(getIntent().getAction() != null && getIntent().getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED) ||
				fromPl2303 == true) {
			initDetachedUsbListener();
	        initBaudRate();
			initUI(true);
		}
		else {
			initUI(false);
			this.queryBluetoothDevicesAndSetToUi();
		}
		
		clearSharePreferences();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private void initDetachedUsbListener() {
		if (detachReceiver == null) {
	    	detachReceiver = new BroadcastReceiver() {
	
				@Override
				public void onReceive(Context context, Intent intent) {
					initUI(false);
					queryBluetoothDevicesAndSetToUi();
				}
	        };
	
	        IntentFilter filter = new IntentFilter();
	        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
	        registerReceiver(detachReceiver, filter);
		}
    }
	
	private void initBaudRate() {
		ArrayAdapter<CharSequence> adapter = 
				ArrayAdapter.createFromResource(this, R.array.BaudRate_Var, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mBaudRate.setAdapter(adapter);
		mBaudRate.setSelection(1);
	}
	
 	private void initUI(boolean isPL2303) {
		mPL2303Layout.setVisibility(isPL2303 ? View.VISIBLE : View.GONE);
    	mBTLayout.setVisibility(!isPL2303 ? View.VISIBLE : View.GONE);
    	
    	if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			//mBLEPair.setVisibility(View.GONE);
    		mBLEPair.setEnabled(false);
        }
    	else {
    		mBLEPair.setEnabled(true);
    	}
	}
 	
 	private int isBLEMeter(final String str) {
		String nameKey;
		String addrKey;
		String nameValue;
		String addrValue;
	    for (int i=0; i<mPairedMeterCount; i++) {
	    	nameKey = PCLinkLibraryDemoConstant.BLE_PAIRED_METER_NAME_ + String.valueOf(i);
			addrKey = PCLinkLibraryDemoConstant.BLE_PAIRED_METER_ADDR_ + String.valueOf(i);
			nameValue = mPairedMeterNames.get(nameKey);
			addrValue = mPairedMeterAddrs.get(addrKey);
		    String remoteDeviceName = nameValue + "/" + addrValue;
		    
		    if (str.equals(remoteDeviceName)) {
		    	return i;
		    }
	    }
	    
	    return -1;
 	}
 	
 	private int initBLEMeters() {
 		int adapterCount = 0;
 		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
 			 
 		    mPairedMeterNames = new HashMap<String, String>();
 		    mPairedMeterAddrs = new HashMap<String, String>();
		    mPairedMeterCount = MeterPreferenceDialog.getPairedMeters(this, mPairedMeterNames, mPairedMeterAddrs);
		    
		    ArrayAdapter<String> selectedMeterSpinnerAdapter = (ArrayAdapter<String>)mSelectedMeter.getAdapter();
		    adapterCount = selectedMeterSpinnerAdapter.getCount();
 			String nameKey;
			String addrKey;
			String nameValue;
			String addrValue;
		    for (int i=0; i<mPairedMeterCount; i++) {
		    	nameKey = PCLinkLibraryDemoConstant.BLE_PAIRED_METER_NAME_ + String.valueOf(i);
				addrKey = PCLinkLibraryDemoConstant.BLE_PAIRED_METER_ADDR_ + String.valueOf(i);
				nameValue = mPairedMeterNames.get(nameKey);
				addrValue = mPairedMeterAddrs.get(addrKey);
			    String remoteDeviceName = nameValue + "/" + addrValue;
			    
			    if (selectedMeterSpinnerAdapter != null) {
			    	boolean find_flag = false;
				    for (int j=0; j<selectedMeterSpinnerAdapter.getCount(); j++) {
		 				String remoteDeviceName2 = selectedMeterSpinnerAdapter.getItem(j);
		 				if (remoteDeviceName.equals(remoteDeviceName2)) {
		 					find_flag = true;
		 					break;
		 				}
		 			}
				    if (!find_flag) {
					    adapterCount++;
	 					selectedMeterSpinnerAdapter.add(remoteDeviceName);
				    }
			    }
		    }
		}
 		
 		return adapterCount;
	}
 	
 	private void ShowMeterDialog() {
    	meterDialog = MeterPreferenceDialog.newInstance();
    	meterDialog.show(getSupportFragmentManager(), "FRAGMENT_SETTING_METER");
    }
    
    public String getCurrentVersionName() {
        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            return "";
        } /* end of if */
    }
    
    public int getCurrentVersionCode() {
        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            return -1;
        } /* end of if */
    }
}
