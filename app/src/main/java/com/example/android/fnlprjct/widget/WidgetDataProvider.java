package com.example.android.fnlprjct.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.fnlprjct.MyApplication;
import com.example.android.fnlprjct.MyQuery;
import com.example.android.fnlprjct.R;
import com.example.android.fnlprjct.Utility;
import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;


public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = WidgetDataProvider.class.getSimpleName();

    private Uri uri = MovieInfoEntry.CONTENT_URI;
    private Cursor cursor = null;

    // Context --- the widget needs to get the package name to be associated with our app.
    // Intent --- this value is from the 'WidgetService.onGetViewFactory(Intent intent)'
    // WidgetDataProvider Constructor with (Context,Intent )  -- because we are accessing these data (Context,Intent )from the 'WidgetService.java'
    // Intent is used for example, opening up certain activities from tapping on the list-item.

    private Context context;
    private Intent intent;


    public WidgetDataProvider(Context context, Intent intent) { // passing the context and get the intent
        this.context = context;
        this.intent = intent;
    }

    // Called when your factory is first constructed.
    @Override
    public void onCreate() {

    }

    // Called when notifyDataSetChanged() is triggered on the remote adapter.
    // ? call only on the 1st addition of appWidgets ??
    // ? when this -> ( appWidgetManager.updateAppWidget(appWidgetId, views); ) is first called ??
    @Override
    public void onDataSetChanged() {

        //-------------------------------
        String sortMoviesBy = Utility.getPreferredSortSequence(MyApplication.getAppContext());
        String searchYear = Utility.getPreferredYear(MyApplication.getAppContext());
        String[] projection;
        String selection;
        String selectionArg[];
        String sortOrder;

        if (sortMoviesBy.equals(context.getResources().getString(R.string.pref_value_movies_sortby_default))) {

            projection = MyQuery.MovieInfo.PROJECTION;    // pick the selected columns
            selection = MovieInfoEntry.COL_YEAR + "=?";
            selectionArg = new String[]{searchYear};
            sortOrder = MovieInfoEntry.COL_VOTE_COUNT + " DESC";

        }
        else {
            projection = MyQuery.MovieInfo.PROJECTION;
            selection = MovieInfoEntry.COL_YEAR + "=?";
            selectionArg = new String[]{searchYear};
            sortOrder = MovieInfoEntry.COL_VOTE_AVERAGE + " DESC";
        }

        //-------------------------------
        if (cursor != null) {
            cursor.close();
        }

        ContentResolver contentResolver = context.getContentResolver();
        cursor = contentResolver.query(uri, projection, selection, selectionArg, sortOrder);
        //-- or --
        //cursor = context.getContentResolver().query(uri, projection, selection, selectionArg, sortOrder);
        //--------------------------------

    }

    // Called when the last RemoteViewsAdapter that is associated with this factory is unbound.
    @Override
    public void onDestroy() {

        if(cursor != null){
            cursor.close();
        }
    }

    @Override
    public int getCount() {

        if ( cursor == null ) {return 0;}
        else {return cursor.getCount();}

    }

    @Override
    public RemoteViews getViewAt(int position) {

        // -------------------------------------
        cursor.moveToPosition(position);
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.grid_item);

        // -- viewId, text --
        remoteView.setTextViewText(R.id.textview_id, cursor.getString(MyQuery.MovieInfo.COL_MOVIE_TITLE));

        return remoteView;
        // -------------------------------------

    }

    @Override
    public RemoteViews getLoadingView() {

        return new RemoteViews(context.getPackageName(), R.layout.grid_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1; // we only have 1 type of view in our widget, so we add '1', formerly --0--
    }

    @Override
    public long getItemId(int position) {
        if (cursor == null) return 0;
        else return cursor.getPosition();
    }

    @Override
    public boolean hasStableIds() {
        // we are not adding or deleting from our static-data
        // so for now we have stableIds
        return true; // -- false--
    }

}