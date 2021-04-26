package com.kanad.users.activities;

import android.os.Bundle;
import android.os.Handler;

import com.kanad.users.R;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getName();

    private static final int SPLASH_TIME_OUT = 2000;

    Runnable runnable = () -> {
        nextActivityTo(LoginActivity.class);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initContext(SplashActivity.this);

        new Handler().postDelayed(runnable, SPLASH_TIME_OUT);

    }

}