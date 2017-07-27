package com.transility.welloculus.fora.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.transility.welloculus.R;

/**
 * Full screen dialog fragment base.
 * 
 * @author Jay Lee
 * 
 */
public class FullScreenDialogFragmentBase extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_FullScreenDialog);
    }
}
