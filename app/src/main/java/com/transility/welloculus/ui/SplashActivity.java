package com.transility.welloculus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.transility.welloculus.R;

/**
 * The type Splash activity.
 */
public class SplashActivity extends BaseActivity {
    private static int SPLASH_TIME_OUT = 3000;
    /**
     * The M context.
     */
    Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(mContext, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void initUI() {
        mContext = this;

    }
}
