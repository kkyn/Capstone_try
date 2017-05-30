package com.example.android.fnlprjct.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kkyin on 14/7/2016.
 */
public class MoviesAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private MoviesAccountAuthenticator moviesAccountAuthenticator;

    @Override
    public void onCreate() {
        //super.onCreate();
        //Create a new authenticator object
        moviesAccountAuthenticator = new MoviesAccountAuthenticator(this);
    }

    // 'Service' returns a subclass of 'AbstractAccountAuthenticator'
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //return null;
        return moviesAccountAuthenticator.getIBinder();
    }
}
