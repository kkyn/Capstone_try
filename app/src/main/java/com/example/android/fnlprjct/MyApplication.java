package com.example.android.fnlprjct;

import android.app.Application;
import android.content.Context;

//import com.facebook.stetho.Stetho; // tky commented

/**
 * Created by kkyin on 19/3/2016.
 */
public class MyApplication extends Application {

    // tky add july17 2017
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        new OptionalDependencies(this).initialize();

        // tky add july17 2017
        MyApplication.context = getApplicationContext();

//        Stetho.initializeWithDefaults(this); /// tky commented
    }

    // tky add july17 2017
    public static Context getAppContext(){
        return MyApplication.context;
    }
}
