package com.example.android.myproject_2;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by kkyin on 19/3/2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}