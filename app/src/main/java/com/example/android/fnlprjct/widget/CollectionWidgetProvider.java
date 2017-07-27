package com.example.android.fnlprjct.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.fnlprjct.Main_Activity;
import com.example.android.fnlprjct.R;

// tky comment,
// (A) initial code are auto-generated when use File > New > Widget > App Widget, wizard

/**
 * Implementation of App Widget functionality.
 */
public class CollectionWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = CollectionWidgetProvider.class.getSimpleName();

    // auto-generated
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.collection_widget_provider);

        // ***************************************************************
        // click event handler for the title, launches the app when the user clicks on Title.
        // Create an Intent to launch Main_Activity
        Intent titleIntent = new Intent(context, Main_Activity.class);

        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);

        // Attach an on-click listener with PendingIntent to the 'widget_layout_main'
        remoteViews.setOnClickPendingIntent(R.id.widget_layout_main, titlePendingIntent);
        // ***************************************************************

        // Set up the collection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, remoteViews);   // tky, setRemoteAdapter(context, views) is a helper method
        } else {
            setRemoteAdapterV11(context, remoteViews);
        }

        // Notifies the specified collection-view in the specified AppWidget instance to invalidate its data.
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);

        // Instruct the widget-manager to update the widget.
        // Set the RemoteViews to use for the specified appWidgetIds.
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    // auto-generated
    // starting sequence -- STEP 2
    // The entry point is going to be calling 'onUpdate'
    //
    // Called in response to the ACTION_APPWIDGET_UPDATE and ACTION_APPWIDGET_RESTORED broadcasts when
    // this AppWidget provider is being asked to provide RemoteViews for a set of AppWidgets.
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        /*Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onUpdate ");*/

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            /*Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > Loop ");*/

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        // Very important!!
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    // auto-generated
    // tky comment,
    // starting sequence -- STEP 1
    // Only called once, when the first widget from this app is selected.
    // Called when the user 'select' the appwidget from the list of widgets
    // and the framework sends a broadcast event, ACTION_APPWIDGET_ENABLED.
    //
    // Context: The Context in which this receiver is running.
    // Called in response to the ACTION_APPWIDGET_ENABLED broadcast when the a AppWidget for this provider is instantiated.

    @Override
    public void onEnabled(Context context) {
        /*Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onEnabled ");*/

        // Enter relevant functionality for when the first widget is created

    }

    // auto-generated
    // Ending sequence -- STEP 1
    // Only called once, when the last widget from this app is selected.
    @Override
    public void onDisabled(Context context) {
        /*Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onDisabled ");*/

        // Enter relevant functionality for when the last widget is disabled
    }

    // auto-generated
    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(
            R.id.widget_list, new Intent(context, WidgetService.class));
    }

    // auto-generated
    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(
            0, R.id.widget_list, new Intent(context, WidgetService.class));
    }

    // not auto-generated
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        /*Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onDeleted " + context.getPackageName());
        Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onDeleted " + context.getString(R.string.pref_movies_sort_key));*/

    }

    // not auto-generated
    // ------------------------------------------------
    // Receiver of broadcast-message from invoked method,
    //    e.g. Utility.BroadcastMessage(getApplicationContext() context)
    //
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Toast.makeText(context, "IN on RECEIVE", Toast.LENGTH_LONG).show();

        String action = intent.getAction();

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {

            ComponentName componentName = new ComponentName(context, CollectionWidgetProvider.class);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(componentName);

            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}

