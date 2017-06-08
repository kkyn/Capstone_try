package com.example.android.fnlprjct;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;

/**
 * Created by kkyin on 7/6/2017.
 */

public class NewFragment extends Fragment
                    implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String LOG_TAG = NewFragment.class.getSimpleName();

    private static final String[] MOVIEINFO_COLUMN_PROJECTION_POPULARITY =
        new String[]{
            MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
            , MovieInfoEntry.COL_MV_ID
            , MovieInfoEntry.COL_POPULARITY + " as sort_column"
            , MovieInfoEntry.COL_POSTERLINK
            //   , MovieInfoEntry.COL_BACKDROP_PATH
        };

    // state the columns of data I want
    private static final String[] MOVIEINFO_COLUMN_PROJECTION_VOTEAVERAGE =
        new String[]{
            MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
            , MovieInfoEntry.COL_MV_ID
            , MovieInfoEntry.COL_VOTE_AVERAGE + " as sort_column"
            , MovieInfoEntry.COL_POSTERLINK
            , MovieInfoEntry.COL_BACKDROP_PATH
        };

    private static final String[] MOVIEINFO_COLUMN_PROJECTION_FAVOURITES =
        new String[]{
            MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
            , MovieInfoEntry.COL_MV_ID
            , MovieInfoEntry.COL_VOTE_AVERAGE + " as sort_column"
//            ,MovieInfoEntry.COL_FAVOURITES
            , MovieInfoEntry.COL_POSTERLINK
            , MovieInfoEntry.COL_BACKDROP_PATH
        };

    private Cursor cursor;
    private ViewPager viewPager;
    private VPAdapter vpAdapter;

    public NewFragment(){
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onCreate  ) ) ) ) ) ) ");
        Log.d(LOG_TAG, " ( ( ( ( ( ( (  Before getLoaderManager().initLoader  ) ) ) ) ) ) ");
        this.getLoaderManager().initLoader(2,null,this);
        Log.d(LOG_TAG, " ( ( ( ( ( ( (  After getLoaderManager().initLoader  ) ) ) ) ) ) ");

        FragmentManager fmgr = getFragmentManager();
        vpAdapter = new VPAdapter(fmgr);
        // vpAdapter = new VPAdapter(fmgr, cursor);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onCreate  ) ) ) ) ) ) ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.page_view, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onCreateView  ) ) ) ) ) ) ");

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onViewCreated  ) ) ) ) ) ) ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewPager.setAdapter(vpAdapter);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onActivityCreated  ) ) ) ) ) ) ");

    }

    //--- Callback methods for "LoaderManager.LoaderCallbacks<Cursor>" ---
    //--------------------------------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortMoviesBy = Utility.getPreferredSortSequence(getContext());

        Uri uri;
        String[] projection;
        String selection;
        String[] selectionArg;
        String sortOrder;

        uri = MovieInfoEntry.CONTENT_URI;

        if (sortMoviesBy.equals(getString(R.string.pref_movies_sortby_default_value))) {
            projection = MOVIEINFO_COLUMN_PROJECTION_POPULARITY;
            selection = null;
            selectionArg = null;
            sortOrder = MovieInfoEntry.COL_POPULARITY + " DESC";

        } else if (sortMoviesBy.equals(getString(R.string.pref_movies_sortby_ratings))) {
            projection = MOVIEINFO_COLUMN_PROJECTION_VOTEAVERAGE;
            selection = null;
            selectionArg = null;
            sortOrder = MovieInfoEntry.COL_VOTE_AVERAGE + " DESC";

        } else {  // sortMoviesBy.equals(getString(R.string.pref_movies_sortby_favourites))
            projection = MOVIEINFO_COLUMN_PROJECTION_FAVOURITES;
            selection = MovieInfoEntry.COL_FAVOURITES + "=?";
            selectionArg = new String[]{"1"};
            sortOrder = MovieInfoEntry.COL_VOTE_AVERAGE + " DESC";
        }

        CursorLoader cursorLoader =
            new CursorLoader(   getActivity(),           // context
                                uri,                     // uri
                                projection,              // projection
                                selection,               // selection
                                selectionArg,            // selectionArg
                                sortOrder                // sortOrder
                            );
        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onCreateLoader  ) ) ) ) ) ) ");

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        cursor = data;

//        FragmentManager fmgr = getFragmentManager();
//
//        vpAdapter = new VPAdapter(fmgr);
//        //vpAdapter = new VPAdapter(fmgr, cursor);
//
//        viewPager.setAdapter(vpAdapter);

        vpAdapter.swapCursor(cursor);
        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onLoadFinished  ) ) ) ) ) ) "  + cursor.getCount());

        //cursor.close();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        vpAdapter.swapCursor(null);
            //cursor=null;
    }
    //---------- End -----------------------------------------------------
}
