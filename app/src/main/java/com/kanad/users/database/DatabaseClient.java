package com.kanad.users.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    public static final String USER_DB = "Users";
    private static DatabaseClient mInstance;
    private Context mContext;
    private AppDatabase appDatabase;

    private DatabaseClient(Context mContext) {
        this.mContext = mContext;
        
        appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, USER_DB).build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
