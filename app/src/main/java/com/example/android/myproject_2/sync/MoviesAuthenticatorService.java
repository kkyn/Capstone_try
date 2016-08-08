package com.example.android.myproject_2.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kkyin on 14/7/2016.
 */
public class MoviesAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private MoviesAuthenticator moviesAuthenticator;

    @Override
    public void onCreate() {
        //super.onCreate();
        //Create a new authenticator object
        moviesAuthenticator = new MoviesAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //return null;
        return moviesAuthenticator.getIBinder();
    }
}
