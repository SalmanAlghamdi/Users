package com.kanad.users;

import android.app.Application;

import com.kanad.users.webservices.ApiManager;

public class MyApplication extends Application {
    private static MyApplication singleton;
    public static ApiManager apiManager;

    public MyApplication() {
        singleton = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = ApiManager.getInstance();
        singleton = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
