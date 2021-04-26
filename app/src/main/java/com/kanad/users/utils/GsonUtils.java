package com.kanad.users.utils;


import com.google.gson.Gson;

public class GsonUtils {

    private static GsonUtils sInstance;

    private Gson mGson;

    private GsonUtils() {
        mGson = new Gson();
    }

    public static GsonUtils getInstance() {
        if (sInstance == null) {
            sInstance = new GsonUtils();
        }
        return sInstance;
    }

    public Gson getGson() {
        return mGson;
    }
}
