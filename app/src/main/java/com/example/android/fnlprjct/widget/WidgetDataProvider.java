package com.example.android.fnlprjct.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.fnlprjct.MyQuery;
import com.example.android.fnlprjct.R;
import com.example.android.fnlprjct.data.MovieContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkyin on 24/6/2017.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = WidgetDataProvider.class.getSimpleName();

    Uri uri = MovieContract.MovieInfoEntry.CONTENT_URI;
    String[] mProjection = MyQuery.MovieInfo.PROJECTION;
    String selection = null;                                    // e.g. Quote.COLUMN_SYMBOL + "=?";
    String selectionArg[] = null;                               // e.g. new String[]{"1"};
    String sortOrder  = MovieContract.MovieInfoEntry.COL_POPULARITY + " ASC";  // e.g. "COLUMN_NAME ASC", DESC/ASC
    Cursor cursor = null;

    // Context --- the widget needs to get the package name to be associated with our app.
    // Intent --- this value is from the 'WidgetService.onGetViewFactory(Intent intent)'
    // WidgetDataProvider Constructor with (Context,Intent )  -- because we are accessing these data (Context,Intent )from the 'WidgetService.java'
    // Intent is used for example, opening up certain activities from tapping on the list-item.

//    List<String> collection = new ArrayList<>();
    Context context;
    Intent intent;

//    private void initData() {
//        collection.clear();
//        for (int i=0; i<=10; i++) {
//            collection.add("ListView Item " + i);
//        }
//    }


    public WidgetDataProvider(Context context, Intent intent) { // passing the context and get the intent
        this.context = context;
        this.intent = intent;
    }

    // Called when your factory is first constructed.
    @Override
    public void onCreate() {
//        initData();
        Log.d(LOG_TAG, "22222222222222 INSIDE WidgetDataProvider > onCreate ");

    }

    // Called when notifyDataSetChanged() is triggered on the remote adapter.
    // ? call only on the 1st addition of appWidgets ??
    // ? when this -> ( appWidgetManager.updateAppWidget(appWidgetId, views); ) is first called ??
    @Override
    public void onDataSetChanged() {
//        initData();
        Log.d(LOG_TAG, "22222222222222 INSIDE WidgetDataProvider > onDataSetChanged ");

        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.pref_movies_sort_key), Context.MODE_PRIVATE);
        String defaultValue = context.getString(R.string.pref_movies_sortby_default_value);
        String string = sp.getString(context.getString(R.string.pref_movies_sort_key), defaultValue);
        Log.d(LOG_TAG, "22222222222222 INSIDE WidgetDataProvider > onDataSetChanged " + string);

        if (cursor != null) {
            cursor.close();
        }
        cursor = context.getContentResolver().query(uri, mProjection, selection, selectionArg, sortOrder);


        /*Uri uri = MovieContract.MovieInfoEntry.CONTENT_URI;
        uri = ContentUris.withAppendedId(uri, mItemId);
        Uri mUri = uri;
        String[] mProjection = MyQuery.MovieInfo.PROJECTION;*/
    }

    // Called when the last RemoteViewsAdapter that is associated with this factory is unbound.
    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "22222222222222 INSIDE WidgetDataProvider > onDestroy ");
    }

    @Override
    public int getCount() {
        if ( null == cursor ) return 0;
        int count = cursor.getCount();
        Log.d(LOG_TAG, "22222222222222 INSIDE WidgetDataProvider > getCount " + count);
        return cursor.getCount();
        //return collection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        //return null;
//        cursor.moveToPosition(position);
//        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.gridview);
//        try {
//            Bitmap b = Picasso.with(context)
//            .load(cursor.getString(MyQuery.MovieInfo.COL_POSTERLINK))
//            // .placeholder(R.drawable.sample_1)
//            //.error(R.drawable.sample_2)
//           // .fit()
//            .transform(new Croptransform()).get(); //
//           // .into(remoteView);
//            remoteView.setImageViewBitmap(R.id.gridview_item, b);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return remoteView;
        // -------------------------------------
        cursor.moveToPosition(position);
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.gridview);
        Uri imageUri = Uri.parse(cursor.getString(MyQuery.MovieInfo.COL_POSTERLINK));
        try {
            Bitmap b = Picasso.with(context).load(imageUri)
               // .resizeDimen(50,50)
                .get();
         //   Bitmap b = Picasso.with(context).load(cursor.getString(MyQuery.MovieInfo.COL_POSTERLINK)).get();
            remoteView.setImageViewBitmap(R.id.gridview_item, b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return remoteView;

        // ---------------------------------------

///--        cursor.moveToPosition(position);

///--        RemoteViews remoteView = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);

        //RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.gridview);

///--        String surii = cursor.getString(MyQuery.MovieInfo.COL_POSTERLINK);
///--        Log.d(LOG_TAG, "----"+ surii);

        /*Uri urii = Uri.parse(cursor.getString(MyQuery.MovieInfo.COL_POSTERLINK));
        Log.d(LOG_TAG, "----"+urii.toString());*/

//        remoteView.setImageViewUri(R.id.gridview_item, cursor.getString(MyQuery.MovieInfo.COL_POSTERLINK));
//        remoteView.setImageViewUri(R.id.gridview_item, Uri.parse(cursor.getString(MyQuery.MovieInfo.COL_POSTERLINK)));

        /*Picasso.with(context)
            .load(cursor.getString(MyQuery.MovieInfo.COL_POSTERLINK))
            .placeholder(R.drawable.sample_1)
            .error(R.drawable.sample_2)
            .fit()
            .into((Target) remoteView);*/

//        return remoteView;
        // ++++++++++++++++++++++++++++++++++++++++++
            /*Picasso.with(context)
                .load(mCursor.getString(Main_Fragment.COLUMN_POSTERLINK))
                .placeholder(R.drawable.sample_1)
                .error(R.drawable.sample_0)
//                .resizeDimen(200,0) // no good
//                .resize(2000,500)   // --A-- //  .resize(600,200)
//                .onlyScaleDown()    // --A--
//                .centerInside()     // --A--
                .fit()                 // --B--
                //  .centerCrop()

                .into(viewHolder.poster_imageview);*/
        // ++++++++++++++++++++++++++++++++++++++++++

        /*remoteView.setTextViewText(android.R.id.text1, cursor.getString(MyQuery.MovieInfo.COL_RELEASEDATE));
        remoteView.setTextColor(android.R.id.text1, Color.BLACK);
        return remoteView;*/

       // cursor.moveToPosition(position);
        // moviereleasedate.setText(cursor.getString(MyQuery.MovieInfo.COL_RELEASEDATE));
        /*RemoteViews remoteView = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
        remoteView.setTextViewText(android.R.id.text1, cursor.getString(MyQuery.MovieInfo.COL_RELEASEDATE));
        remoteView.setTextColor(android.R.id.text1, Color.BLACK);
        return remoteView;*/

        /*RemoteViews remoteView = new RemoteViews(context.getPackageName(),
            android.R.layout.simple_list_item_1);
        remoteView.setTextViewText(android.R.id.text1, collection.get(position));
        // this is where you will putting you data, and so we're just going to be sending a color
        // here right now.
        remoteView.setTextColor(android.R.id.text1, Color.BLACK);
        return remoteView;*/
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // we only have 1 type of view in our widget, so we add '1', formerly --0--
    }

    @Override
    public long getItemId(int position) {
        return position;   // --0--
    }

    @Override
    public boolean hasStableIds() {
        // we are not adding or deleting from our static-data
        // so for now we have stableIds
        return true; // -- false--
    }

    public class Croptransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            //return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            //return null;
            return "square()";
        }
    }
}
