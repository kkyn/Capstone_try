package com.example.android.fnlprjct;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by kkyin on 31/5/2017.
 */

public class MyRqstQ {

    private static final String LOG_TAG = MyRqstQ.class.getSimpleName();

    private static MyRqstQ instance;

    private RequestQueue rqstQ;

    public static synchronized MyRqstQ getInstance(Context context) {
        Log.d(LOG_TAG, "---- get instance of MyRqstQ ----");
        if (instance == null) {
            instance = new MyRqstQ(context.getApplicationContext());
        }
        return instance;
    }

    private MyRqstQ(Context getApplicationContext) {
        rqstQ = Volley.newRequestQueue(getApplicationContext);
        rqstQ.start(); // ??

    }

    public RequestQueue getRequestQueue() {
        Log.d(LOG_TAG, "---- get Request Q, rqstQ ----");
        return rqstQ;
    }

    /*private RequestQueue getRequestQueue() {
        if (rqstQ == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            rqstQ = Volley.newRequestQueue(cntxt.getApplicationContext());
        }
        return rqstQ;
    }*/
    ///---------------------------------------
//    // private static /*final*/ MyRqstQ instance;  /*= new MyRqstQ(context);*/
//
//    private static MyRqstQ instance;
//    private static Context cntxt;
//
//    private RequestQueue rqstQ;
//
//    public static synchronized MyRqstQ getInstance(Context context) {
//
//        return instance = new MyRqstQ(context);
//    }
//
//    private MyRqstQ(Context context) {
//        cntxt = context;
//        rqstQ = getRequestQueue();
//    }
//
//    private RequestQueue getRequestQueue() {
//        if (rqstQ == null) {
//            // getApplicationContext() is key, it keeps you from leaking the
//            // Activity or BroadcastReceiver if someone passes one in.
//            rqstQ = Volley.newRequestQueue(cntxt.getApplicationContext());
//        }
//        return rqstQ;
//    }
}
