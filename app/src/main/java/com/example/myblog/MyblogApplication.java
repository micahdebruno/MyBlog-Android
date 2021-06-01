package com.example.myblog;

import android.app.Application;
import android.content.Context;

public class MyblogApplication extends Application {

    private static MyblogApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyblogApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
