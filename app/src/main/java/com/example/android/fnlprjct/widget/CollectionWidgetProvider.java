package com.example.android.fnlprjct.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

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

//        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.collection_widget_provider);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        // tk add, copied from reference code at github
        // Set up the collection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);   // tky, setRemoteAdapter(context, views) is a helper method
        } else {
            setRemoteAdapterV11(context, views);
        }

        // +++++++++++++++++++++++++
        /*RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.sample_widget);
        // Load image for all appWidgetIds.
        Picasso picasso = Picasso.with(context);
        picasso.load(Data.URLS[new Random().nextInt(Data.URLS.length)]) //
            .placeholder(R.drawable.placeholder) //
            .error(R.drawable.error) //
            .transform(new GrayscaleTransformation(picasso)) //
            .into(updateViews, R.id.image, appWidgetIds);*/
        // ++++++++++++++++++++++++++
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultValue = context.getString(R.string.pref_movies_sortby_default_value);
        String string = sp.getString(context.getString(R.string.pref_movies_sort_key), defaultValue);
        Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > updateAppWidget ----- " + string);


        // Notifies the specified collection view in the specified AppWidget instance to invalidate its data.
        //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);

        // views.setEmptyView(R.id.widget_list, R.id.empty_view); // tky add

        // Instruct the widget manager to update the widget
        // Set the RemoteViews to use for the specified appWidgetIds.
        appWidgetManager.updateAppWidget(appWidgetId, views);


        // Notifies the specified collection view in the specified AppWidget instance to invalidate its data.
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
    }

    // auto-generated
    // tky comment,
    // starting sequence -- STEP 2
    // tky comment, the entry point is going to be calling 'onUpdate'
    //
    // Called in response to the ACTION_APPWIDGET_UPDATE and ACTION_APPWIDGET_RESTORED broadcasts when
    // this AppWidget provider is being asked to provide RemoteViews for a set of AppWidgets.
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onUpdate ");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > Loop ");
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
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
        Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onEnabled ");

        // Enter relevant functionality for when the first widget is created

    }

    // auto-generated
    // tky comment,
    // ending sequence -- STEP 1
    //
    // Only called once, when the last widget from this app is selected.
    @Override
    public void onDisabled(Context context) {
        Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onDisabled ");

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
        views.setRemoteAdapter(R.id.widget_list,
            new Intent(context, WidgetService.class));
    }

    // auto-generated
    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list,
            new Intent(context, WidgetService.class));
    }

    // not auto-generated
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onDeleted " + context.getPackageName());
        Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onDeleted " + context.getString(R.string.pref_movies_sort_key));

        //context.getp
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.pref_movies_sort_key), Context.MODE_PRIVATE);
       // SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.pref_movies_sort_key), MODE_PRIVATE);

        String defaultValue = context.getString(R.string.pref_movies_sortby_default_value);
//        String string = context.getString(R.string.pref_movies_sort_key);
//        String string = context.getString(R.string.pref_movies_sortby_ratings_label);

        String string = sp.getString(context.getString(R.string.pref_movies_sort_key), defaultValue);

        //String string = sp.getString(context.getString(R.string.pref_movies_sort_key), "xxx");
//        String string = sp.getString(context.getString(R.string.pref_movies_sortby_default_value), "xxx");
//        String string = sp.getString(context.getString(R.string.pref_movies_sortby_ratings_label), "xxx");

        Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onDeleted " + string);
    }

    // not auto-generated

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "00000000000000 INSIDE CollectionWidgetProvider > onReceive ");
    }
}

