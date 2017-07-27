package com.transility.welloculus.fora.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.taidoc.pclinklibrary.constant.PCLinkLibraryEnum.ABPMWriteType;
import com.transility.welloculus.R;
import com.transility.welloculus.fora.util.GuiUtils;
import com.transility.welloculus.fora.view.KeyboardAwareRelativeLayout;
import com.transility.welloculus.fora.widget.edittext.AEditText;
import com.transility.welloculus.fora.widget.edittext.InputFilterMinMax;
import com.taidoc.pclinklibrary.meter.AbstractMeter;
import com.taidoc.pclinklibrary.meter.TD3140;
import com.taidoc.pclinklibrary.meter.record.ABPMConfig;

/**
 * Full screen dialog fragment base.
 * 
 * @author Alvin Yang
 * 
 */
public class SetABPMConfigDialog extends DialogFragment {
	public static final String TAG = SetABPMConfigDialog.class.getSimpleName();

	public interface ABPMConfigListener {
		public void onResult(SetABPMConfigDialog dialog, List<ABPMConfig> configs);
	}
	
	// variables
	private ABPMConfigListener mHandler;
	private AbstractMeter mMeter;
	private ProgressDialog mProcessDialog = null;
	
	// views
	private KeyboardAwareRelativeLayout mRlRoot;
	private AEditText mEtData00;
	private AEditText mEtData01;
	private AEditText mEtData02;
	private AEditText mEtData10;
	private AEditText mEtData11;
	private AEditText mEtData12;
	private AEditText mEtData20;
	private AEditText mEtData21;
	private AEditText mEtData22;
	private AEditText mEtData30;
	private AEditText mEtData31;
	private AEditText mEtData32;
	private AEditText mEtData40;
	private AEditText mEtData41;
	private Button mBtnWrite;
	private Button mBtnClose;
	
	// listeners
	private KeyboardAwareRelativeLayout.OnKeyboardStateChangedListener mOnKeyboardStateChangedListener = new KeyboardAwareRelativeLayout.OnKeyboardStateChangedListener() {

        @Override
		public void onKeyboardStateChanged(int state) {
        	switch (state) {
			case KeyboardAwareRelativeLayout.KEYBOARD_STATE_HIDE://軟鍵盤隱藏
				mEtData00.clearFocus();
				mEtData01.clearFocus();
				mEtData02.clearFocus();
				mEtData10.clearFocus();
				mEtData11.clearFocus();
				mEtData12.clearFocus();
				mEtData20.clearFocus();
				mEtData21.clearFocus();
				mEtData22.clearFocus();
				mEtData30.clearFocus();
				mEtData31.clearFocus();
				mEtData32.clearFocus();
				mEtData40.clearFocus();
				mEtData41.clearFocus();
				break;
			case KeyboardAwareRelativeLayout.KEYBOARD_STATE_SHOW://軟鍵盤顯示
				break;
			default:
				break;
			}	
		}
    };
    
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			List<ABPMConfig> configs = null;
			if (v.getId() == R.id.btn_write) {
				List<List<Integer>> lists = new ArrayList<List<Integer>>();
				List<Integer> list = new ArrayList<Integer>();
				list.add(Integer.valueOf(mEtData00.getText().toString()));
				list.add(Integer.valueOf(mEtData01.getText().toString()));
				list.add(Integer.valueOf(mEtData02.getText().toString()));
				lists.add(list);
				
				list = new ArrayList<Integer>();
				list.add(Integer.valueOf(mEtData10.getText().toString()));
				list.add(Integer.valueOf(mEtData11.getText().toString()));
				list.add(Integer.valueOf(mEtData12.getText().toString()));
				lists.add(list);
				
				list = new ArrayList<Integer>();
				list.add(Integer.valueOf(mEtData20.getText().toString()));
				list.add(Integer.valueOf(mEtData21.getText().toString()));
				list.add(Integer.valueOf(mEtData22.getText().toString()));
				lists.add(list);
				
				list = new ArrayList<Integer>();
				list.add(Integer.valueOf(mEtData30.getText().toString()));
				list.add(Integer.valueOf(mEtData31.getText().toString()));
				list.add(Integer.valueOf(mEtData32.getText().toString()));
				lists.add(list);
				
				list = new ArrayList<Integer>();
				list.add(Integer.valueOf(mEtData40.getText().toString()));
				list.add(Integer.valueOf(mEtData41.getText().toString()));
				list.add(0);
				lists.add(list);
				
				try {
					// type0必須在最後才能寫
					configs = new ArrayList<ABPMConfig>();
					for (int i=0; i<lists.size(); i++) {
						list = new ArrayList<Integer>();
						list.addAll(lists.get(lists.size() - i - 1));
						
						if (((TD3140)mMeter).writeABPMConfig(list.get(0), list.get(1), list.get(2),
								ABPMWriteType.values()[lists.size() - i - 1])) {
							ABPMConfig config = new ABPMConfig(list.get(0), list.get(1), list.get(2),
									ABPMWriteType.values()[lists.size() - i - 1]);
							configs.add(0, config);
						}
					}
				}
				catch (Exception e) {
					configs = null;
				}
			}
			
			mHandler.onResult(SetABPMConfigDialog.this, configs);
		}
	};
	
	private AEditText.AEditTextListener mAEditTextListener = new AEditText.AEditTextListener() {
		
		@Override
		public void onImeBack(AEditText ctrl, String text) {
			GuiUtils.setKeypadVisibility(getActivity(), ctrl, View.INVISIBLE);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_FullScreenDialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.set_abpm_config, container, false);
		
		findViews(view);
		setListeners();
		init();
		
		setCancelable(false);
		
		getDialog().setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK)) {
					if (event.getAction()!=KeyEvent.ACTION_DOWN) {
						mBtnClose.performClick();
						return true;
					}
				}
				
				return false;
			}
		});
		
		return view;
	}
	
    public static SetABPMConfigDialog newInstance(ABPMConfigListener handler, AbstractMeter meter) {
    	SetABPMConfigDialog f = new SetABPMConfigDialog();
    	f.mHandler = handler;
    	f.mMeter = meter;
    	
    	return f;
    }
    
	private void findViews(View view) {
		mRlRoot = (KeyboardAwareRelativeLayout) view.findViewById(R.id.root);
		mEtData00 = (AEditText) view.findViewById(R.id.et_data00);
		mEtData01 = (AEditText) view.findViewById(R.id.et_data01);
		mEtData02 = (AEditText) view.findViewById(R.id.et_data02);
		mEtData10 = (AEditText) view.findViewById(R.id.et_data10);
		mEtData11 = (AEditText) view.findViewById(R.id.et_data11);
		mEtData12 = (AEditText) view.findViewById(R.id.et_data12);
		mEtData20 = (AEditText) view.findViewById(R.id.et_data20);
		mEtData21 = (AEditText) view.findViewById(R.id.et_data21);
		mEtData22 = (AEditText) view.findViewById(R.id.et_data22);
		mEtData30 = (AEditText) view.findViewById(R.id.et_data30);
		mEtData31 = (AEditText) view.findViewById(R.id.et_data31);
		mEtData32 = (AEditText) view.findViewById(R.id.et_data32);
		mEtData40 = (AEditText) view.findViewById(R.id.et_data40);
		mEtData41 = (AEditText) view.findViewById(R.id.et_data41);
		mBtnWrite = (Button) view.findViewById(R.id.btn_write);
		mBtnClose = (Button) view.findViewById(R.id.btn_close);
	}
	
	private void setListeners() {
		mRlRoot.setOnKeyboardStateChangedListener(mOnKeyboardStateChangedListener);
		mEtData00.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData01.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData02.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData10.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData11.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData12.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData20.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData21.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData22.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData30.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData31.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData32.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData40.setOnEditTextImeBackListener(mAEditTextListener);
		mEtData41.setOnEditTextImeBackListener(mAEditTextListener);
		mBtnWrite.setOnClickListener(mOnClickListener);
		mBtnClose.setOnClickListener(mOnClickListener);
	}
	
	private void init() {
		InputFilterMinMax filter = new InputFilterMinMax(0, 255);
		mEtData00.setFilters(new InputFilterMinMax[] {filter});
		mEtData01.setFilters(new InputFilterMinMax[] {filter});
		mEtData02.setFilters(new InputFilterMinMax[] {filter});
		mEtData10.setFilters(new InputFilterMinMax[] {filter});
		mEtData11.setFilters(new InputFilterMinMax[] {filter});
		mEtData12.setFilters(new InputFilterMinMax[] {filter});
		mEtData20.setFilters(new InputFilterMinMax[] {filter});
		mEtData21.setFilters(new InputFilterMinMax[] {filter});
		mEtData22.setFilters(new InputFilterMinMax[] {filter});
		mEtData30.setFilters(new InputFilterMinMax[] {filter});
		mEtData31.setFilters(new InputFilterMinMax[] {filter});
		mEtData32.setFilters(new InputFilterMinMax[] {filter});
		mEtData40.setFilters(new InputFilterMinMax[] {filter});
		mEtData41.setFilters(new InputFilterMinMax[] {filter});
		
		showProcessDialog();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final List<ABPMConfig> configs = new ArrayList<ABPMConfig>();
				try {
					for (int i=0; i<ABPMWriteType.values().length; i++) {
						ABPMConfig config = ((TD3140)mMeter).readABPMConfig(
								ABPMWriteType.values()[ABPMWriteType.values().length - i - 1]);
						if (config != null) {
							configs.add(0, config);
						}
					}
				}
				catch (Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							dimissProcessDialog();
							mBtnClose.performClick();
						}
					});
					return;
				}
				
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						dimissProcessDialog();
						mEtData00.setText(String.valueOf(configs.get(0).getData0()));
						mEtData01.setText(String.valueOf(configs.get(0).getData1()));
						mEtData02.setText(String.valueOf(configs.get(0).getData2()));
						mEtData10.setText(String.valueOf(configs.get(1).getData0()));
						mEtData11.setText(String.valueOf(configs.get(1).getData1()));
						mEtData12.setText(String.valueOf(configs.get(1).getData2()));
						mEtData20.setText(String.valueOf(configs.get(2).getData0()));
						mEtData21.setText(String.valueOf(configs.get(2).getData1()));
						mEtData22.setText(String.valueOf(configs.get(2).getData2()));
						mEtData30.setText(String.valueOf(configs.get(3).getData0()));
						mEtData31.setText(String.valueOf(configs.get(3).getData1()));
						mEtData32.setText(String.valueOf(configs.get(3).getData2()));
						mEtData40.setText(String.valueOf(configs.get(4).getData0()));
						mEtData41.setText(String.valueOf(configs.get(4).getData1()));
					}
				});
			}
		}).start();
	}
	
	private void showProcessDialog() {
		if (mProcessDialog == null) {
			mProcessDialog = ProgressDialog.show(getActivity(), null,
	                getString(R.string.abpm_get_config), true);
	        mProcessDialog.setCancelable(false);
		}
	}
	
	private void dimissProcessDialog() {
        if (mProcessDialog != null) {
            mProcessDialog.dismiss();
            mProcessDialog = null;
        } /* end of if */
    }
}
