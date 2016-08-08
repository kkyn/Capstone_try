package com.example.android.myproject_2;

import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by kkyin on 2/4/2016.
 */
public class OptionalDependencies {
    private Context context;

    public OptionalDependencies(Context context) {
        this.context = context;
    }

    public void initialize() {
        Stetho.initializeWithDefaults(context);
    }
}
