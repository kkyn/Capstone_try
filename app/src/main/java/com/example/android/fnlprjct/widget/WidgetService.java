package com.example.android.fnlprjct.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by kkyin on 24/6/2017.
 */
    // tky comment,
    // POINT TO NOTE : Since we are doing a 'collection'-widget,
    // we need a RemoteViewsService and RemoteViewsService.RemoteViewsFactory classes.
    //
    // at Androidmanifest.xml add :
    // <service android:name=".widget.WidgetService"
    //             android:permission="android:permission.BIND_REMOTEVIEWS"/>
    //
public class WidgetService extends RemoteViewsService {

    private static final String LOG_TAG = WidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) { // 'intent' is passed into the RemoteViewsFactory

        Log.d(LOG_TAG, "11111111111111 INSIDE WIDGETSERVICE > onGetViewFactory ");

        RemoteViewsFactory rfctry = new WidgetDataProvider(this, intent);

        return rfctry;
    }
}
