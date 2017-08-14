package com.example.android.fnlprjct;

import android.app.Application;
import android.content.Context;

//import com.facebook.stetho.Stetho;


public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        new OptionalDependencies(this).initialize();

        MyApplication.context = getApplicationContext();

        //Stetho.initializeWithDefaults(this);

    }

    public static Context getAppContext(){
        return MyApplication.context;
    }

}
