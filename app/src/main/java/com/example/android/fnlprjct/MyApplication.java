package com.example.android.fnlprjct;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
//import com.facebook.stetho.Stetho; // tky commented

/**
 * Created by kkyin on 19/3/2016.
 */
public class MyApplication extends Application {

    // tky add july17 2017
    private static Context context;

    // analytics stuff, (com.google.android.gms.analytics)
    private static GoogleAnalytics sGoogleAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        new OptionalDependencies(this).initialize();

        // tky add july17 2017
        MyApplication.context = getApplicationContext();

//        Stetho.initializeWithDefaults(this); /// tky commented

        // analytics stuff
        sGoogleAnalytics = GoogleAnalytics.getInstance(this);
    }

    // tky add july17 2017
    public static Context getAppContext(){
        return MyApplication.context;
    }

    synchronized public Tracker getDefaultTracker(){

        if (sTracker == null) {
            sTracker = sGoogleAnalytics.newTracker(R.xml.global_tracker);
        }
        return sTracker;
    }
}
