package com.example.android.fnlprjct.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class MAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private MAccountAuthenticator mvAccntAuthntctr;

    @Override
    public void onCreate() {
        //super.onCreate();
        //Create a new authenticator object
        mvAccntAuthntctr = new MAccountAuthenticator(this);
    }

    // 'Service' returns a subclass of 'AbstractAccountAuthenticator'
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mvAccntAuthntctr.getIBinder();
    }
}
