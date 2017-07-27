/*
 * Copyright (C) 2012 TaiDoc Technology Corporation. All rights reserved.
 */

package com.transility.welloculus.fora.util;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.transility.welloculus.fora.PCLinkLibraryDemoActivity;
import com.transility.welloculus.R;
import com.transility.welloculus.fora.constant.PCLinkLibraryDemoConstant;
import com.transility.welloculus.fora.interfaces.CustomAlertOnClickListener;
/**
 * GUI Utility
 * 
 * @author Jay Lee
 */
public class GuiUtils {
    /**
     * 跳轉到 Home Activity
     * 
     * @param sourceActivityInstance
     *            source activity instance
     */
    public static void goToPCLinkLibraryHomeActivity(Activity sourceActivityInstance) {
        goToSpecifiedActivity(sourceActivityInstance, PCLinkLibraryDemoActivity.class, null);
    }

    /**
     * 跳轉到特定的Activity
     * 
     * @param sourceActivityInstance
     *            source activity instance
     * @param targetActivityClass
     *            target activity class
     */
    public static void goToSpecifiedActivity(Activity sourceActivityInstance,
            Class<? extends Activity> targetActivityClass) {
        goToSpecifiedActivity(sourceActivityInstance, targetActivityClass, null);
    }

    /**
     * 跳轉到特定的Activity
     * 
     * @param sourceActivityInstance
     *            source activity instance
     * @param targetActivityClass
     *            target activity class
     * @param bundleOfSourceActivity
     *            bundle content of source activity
     */
    public static void goToSpecifiedActivity(Activity sourceActivityInstance,
            Class<? extends Activity> targetActivityClass, Bundle bundleOfSourceActivity) {
        Intent intent = new Intent();
        if (bundleOfSourceActivity != null) {
            intent.putExtras(bundleOfSourceActivity);
        }
        intent.setClass(sourceActivityInstance, targetActivityClass);
        sourceActivityInstance.startActivity(intent);
        sourceActivityInstance.finish();
    }

    public static float convertTypeValueToPixel(Context context, float value, int typedValue) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(typedValue, value, r.getDisplayMetrics());
        return px;
    }
    
    public static void setKeypadVisibility(Context context, EditText inputNote, int visibility) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        switch (visibility) {
            case View.VISIBLE:
                // 開啟鍵盤
                imm.showSoftInput(inputNote, InputMethodManager.SHOW_IMPLICIT);
                break;
            case View.GONE:
            case View.INVISIBLE:
                // 關閉鍵盤
                imm.hideSoftInputFromWindow(inputNote.getWindowToken(), 0);
                break;
        } /* end of switch */
    }
    
    public static Dialog showConfirmDialog(Activity activity, boolean cancelable, String title, String message,
            final CustomAlertOnClickListener backHandler) {

        Dialog dialog = new Dialog(activity, R.style.CustomDialog);
        dialog.setContentView(R.layout.confirm_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(cancelable);

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tv_content);
        Button btnBack = (Button) dialog.findViewById(R.id.btn_confirm);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        tvContent.setText(message);

        dialog.show();

        final Dialog inDialog = dialog;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backHandler != null) {
                    backHandler.onClick(inDialog, v);
                }
            }
        });

        return dialog;
	}
    
    /**
     * 取得螢幕解析度
     * 
     * @param context
     *            Context
     * @return The screen height and width map.
     */
    @SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
    public static HashMap<String, Integer> getScreenResolution(Context context) {
        int Measuredwidth = 0;
        int Measuredheight = 0;
        Point size = new Point();
        WindowManager windowsManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // MARK 如果未來支援只超過HONEYCOMB_MR2時，把相容以下版本移除
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            windowsManager.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        } else {
            Display d = windowsManager.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        } /* end of if */

        HashMap<String, Integer> screenResolutionMap = new HashMap<String, Integer>();
        screenResolutionMap.put(PCLinkLibraryDemoConstant.SCREEN_HEIGHT, Measuredheight);
        screenResolutionMap.put(PCLinkLibraryDemoConstant.SCREEN_WIDTH, Measuredwidth);
        return screenResolutionMap;
    }
    
    public static int getStatusBarHeight(Context context) {
        return Math.round(convertTypeValueToPixel(context, 25f, TypedValue.COMPLEX_UNIT_DIP));
    }
}
