package com.example.android.myproject_2;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.myproject_2.data.MovieContract.X_MovieInfoEntry;
import com.example.android.myproject_2.sync.MoviesSyncAdapter;

/**
 * A placeholder fragment (Main_Fragment) containing a simple view.
 */
public class Main_Fragment extends Fragment
                        implements LoaderManager.LoaderCallbacks<Cursor>
                                , SharedPreferences.OnSharedPreferenceChangeListener
                             //   ,Movie_RecyclerViewAdapter.OnItemClickHandler_0
{
    // constructor
    public Main_Fragment() {

    }

    public static final String LOG_TAG = Main_Fragment.class.getSimpleName();
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    private RecyclerView                mRecyclerView;
    private Movie_RecyclerViewAdapter movie_Adapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private long itemID = 0;
    private Uri mUri;

    private static final int MOVIE_FRAGMENT_ID = 0; // constant definition

    static final int COLUMN_POPULAR_ID;
    static final int COLUMN_MV_ID;
    static final int COLUMN_KEY_ID;
    static final int COLUMN_POSTERLINK;
    static final int COLUMN_BACKDROP_PATH;

    static{
        COLUMN_POPULAR_ID = 0;
        COLUMN_MV_ID = 1;
        COLUMN_KEY_ID = 2;
        COLUMN_POSTERLINK = 3;
        COLUMN_BACKDROP_PATH = 4;
    }

    // state the columns of data I want
    private static final String[] X_MOVIE_INFO_PROJECTION_1 = new String[]{

            X_MovieInfoEntry.TABLE_NAME + "." + X_MovieInfoEntry._ID
            ,X_MovieInfoEntry.COL_MV_ID
            ,X_MovieInfoEntry.COL_VOTE_AVERAGE + " as sort_column"
            ,X_MovieInfoEntry.COL_POSTERLINK
            ,X_MovieInfoEntry.COL_BACKDROP_PATH
    };

    private static final String[] X_MOVIE_INFO_PROJECTION = new String[]{

            X_MovieInfoEntry.TABLE_NAME + "." + X_MovieInfoEntry._ID
            ,X_MovieInfoEntry.COL_MV_ID
            ,X_MovieInfoEntry.COL_POPULARITY + " as sort_column"
            ,X_MovieInfoEntry.COL_POSTERLINK
            ,X_MovieInfoEntry.COL_BACKDROP_PATH
    };
    private static final String[] X_MOVIE_INFO_PROJECTION_2 = new String[]{

            X_MovieInfoEntry.TABLE_NAME + "." + X_MovieInfoEntry._ID
            ,X_MovieInfoEntry.COL_MV_ID
            ,X_MovieInfoEntry.COL_VOTE_AVERAGE + " as sort_column"
//            ,X_MovieInfoEntry.COL_FAVOURITES
            ,X_MovieInfoEntry.COL_POSTERLINK
            ,X_MovieInfoEntry.COL_BACKDROP_PATH
    };
    private static final String SELECTED_INDEX = "selected_index";

    //-------------------------
    //--- Interface stuff -----
    //-------------------------
    CallBackListener mCallBackListener;

    // Container Activity must implement this interface
    public interface CallBackListener {

         void onItemSelectedInRecyclerView(Uri mUri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallBackListener = (CallBackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CallBackListener");
        }
    }

    //---------------------------------
    //--- Fragment Lifecycle Stuff ----
    //---------------------------------

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStop() {
        super.onStop(); //Log.d(LOG_TAG, "---- 6 onStop() --");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();  //Log.d(LOG_TAG, "---- 7 onDestroyView() --");
    }

    @Override // --- 1a ----
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);  //Log.d(LOG_TAG, "---- 1a onViewCreated(View view, @Nullable Bundle savedInstanceState) --");
    }

    @Override // --- 0 ----
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);    //Log.d(LOG_TAG, "---- 0 onCreate() --");
    }

    @Override // --- 2 ----
    public void onActivityCreated(Bundle savedInstanceState) {

        // returns a loader-object, there is no need to keep it around.
        // LoaderManager will take of the details.
        // Loaders are uniquely identified, e.g. POPULAR_LOADER_ID
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        getLoaderManager().initLoader(MOVIE_FRAGMENT_ID, null, this);
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        super.onActivityCreated(savedInstanceState);

    }

    @Override   // --- 3 ---
    public void onStart() {
        super.onStart();
    }

    @Override //---- 4 ----
    public void onResume() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sp.registerOnSharedPreferenceChangeListener(this);

        super.onResume();

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        getLoaderManager().restartLoader(MOVIE_FRAGMENT_ID, null, this); //  help maintain position ??
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    }

    @Override //--- 5 ----
    public void onPause() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sp.unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }

    void myRestartLoaderCode(){

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        getLoaderManager().restartLoader(MOVIE_FRAGMENT_ID, null, this);
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    }

    //---------------------------------------------------------------------------------------------
    //------ SETTING FOR implementation of SharedPreferences.OnSharedPreferenceChangeListener -----
    //---------------------------------------------------------------------------------------------
    // For SharedPreferences.OnSharedPreferenceChangeListener
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if ( key.equals(getString(R.string.pref_movies_sort_key)) ) {

            getLoaderManager().restartLoader(MOVIE_FRAGMENT_ID, null, this);

            MoviesSyncAdapter.syncImmediately(getContext());
        }

    }

    //--------------------------------------------------------------
    //-- LoaderManager.LoaderCallbacks<Cursor> --
    //----------- LoaderCursor Stuff (begin) -----------------------
    //--------------------------------------------------------------
    /*
    * Callback that's invoked when the system has initialized the Loader and
    * is ready to start the query. This usually happens when initLoader() is
    * called. The loaderID argument contains the ID value passed to the
    * initLoader() call.
    */
    // @param int id --- refers to MOVIE_LOADER
    // @param Bundle args ---
    @Override   // --- 1 ---
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortMoviesBy = Utility.getPreferredSortSequence(getContext());
        Uri uri;
        String[] projection;

        String selection;
        String[] selectionArg ;

        uri = X_MovieInfoEntry.CONTENT_URI;

        String sortOrder;
        if (sortMoviesBy.equals(getString(R.string.pref_movies_sortby_default_value))) {
            sortOrder       = X_MovieInfoEntry.COL_POPULARITY + " DESC";
            projection      = X_MOVIE_INFO_PROJECTION;
            selection       = null;
            selectionArg    = null;
        } else if (sortMoviesBy.equals(getString(R.string.pref_movies_sortby_ratings))) {
            sortOrder       = X_MovieInfoEntry.COL_VOTE_AVERAGE + " DESC";
            projection      = X_MOVIE_INFO_PROJECTION_1;
            selection       = null;
            selectionArg = null;
        } else {  // sortMoviesBy.equals(getString(R.string.pref_movies_sortby_favourites))
            sortOrder       = X_MovieInfoEntry.COL_VOTE_AVERAGE + " DESC";
            projection      = X_MOVIE_INFO_PROJECTION_2;
            selection       = X_MovieInfoEntry.COL_FAVOURITES + "=?";
            selectionArg    = new String[]{"1"};
        }

        //
        // https://developer.android.com/reference/android/content/CursorLoader.html
        // CursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
        //
        return new CursorLoader(
                getActivity(),           // context
                uri,                     // uri
                projection,              // projection
                selection,               // selection
                selectionArg,            // selectionArg
                sortOrder                // sortOrder
        );
    }

    // Called when a previously created loader has finished its load.
    /*
    * Defines the callback that CursorLoader calls
    * when it's finished its query
    */
    @Override //--- 5 ---
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        Log.d(LOG_TAG, "----   onLoadFinished(), loader ID : " + loader.getId() + " --");
        if (cursor == null) {Log.d(LOG_TAG, "----   onLoadFinished(), cursor,NULL --");}
        else                {Log.d(LOG_TAG, "----   onLoadFinished(), cursor,NOT NULL -- cursor count : " + cursor.getCount() + " --");

        }

        // import a new set of cursor structured/found in 'Cursor' into the RecyclerViewAdapter.
        /*
         * Moves the query results into the adapter, causing the
         * RecyclerView fronting this adapter to re-display
        */
        movie_Adapter.swapCursor(cursor);

        if (mPosition != RecyclerView.NO_POSITION) {
            mRecyclerView.smoothScrollToPosition(mPosition);
        }
    }

    // Called when a previously created loader is being reset, and thus making its data unavailable.
    /*
    * Invoked when the CursorLoader is being reset. For example, this is
    * called if the data in the provider changes and the Cursor becomes stale.
    */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        /* Clears out the adapter's reference to the Cursor.
        * This prevents memory leaks.
        */
        movie_Adapter.swapCursor(null);
    }
    //--------------------------------------------------------------
    //----------- LoaderCursor Stuff (End) -------------------------
    //--------------------------------------------------------------

    //-------------------------------------------------
    //----------- OPTIONS MENU Stuff (Begin)-----------
    // ------------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //if (id == com.example.android.myproject_2.R.id.most_popular) {
        if (id==R.id.most_popular){

            //------------------------------------------------
            // get the file, SharedPreferences
            // Gets a SharedPreferences instance that points to the default file
            // that is used by the preference framework in the given context.
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

            // Create a new Editor for these preferences, through which you can make modifications to
            // the data in the preferences and atomically commit those changes back to the SharedPreferences object.
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Set a String value in the preferences editor, to be written back once commit() or apply() are called.
            editor.putString(getString(R.string.pref_movies_sort_key), getString(R.string.pref_movies_sortby_default_value));

            editor.apply();

            return true;
            //------------------------------------------------

        }
        return super.onOptionsItemSelected(item);

    }
    //------------------------------------------------
    //--------------- OPTIONS MENU Stuff (End)--------
    //------------------------------------------------

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != RecyclerView.NO_POSITION) {
            outState.putInt(SELECTED_INDEX, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

   /* private long getNewPosition(int position) {
        return movie_Adapter.getItemId(position);
    }*/


    /* When the system is ready for the Fragment to appear, this displays
     * the Fragment's View
     */
    @Override //--- 1 ---
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView.LayoutManager mLayoutManager;

        //return super.onCreateView(inflater, container, savedInstanceState);

        //************************************************
        // tky comment ....
        // Implementation the interface, 'NAME'/OnItemClickHandler_0
        // with the method-name/onItemClick_0 found within the interface declaration.
        Movie_RecyclerViewAdapter.OnItemClickHandler_0 ref_onItemClickHandler_0 =

                new Movie_RecyclerViewAdapter.OnItemClickHandler_0() {
                    @Override
                    public void onItemClick_0(Movie_RecyclerViewAdapter.MovieViewHolder viewHolder) {

                        mPosition = viewHolder.getAdapterPosition();

                        itemID = movie_Adapter.getItemId(mPosition);
                        mUri = X_MovieInfoEntry.CONTENT_URI;
                        mUri = ContentUris.withAppendedId(mUri, itemID);

                        mCallBackListener.onItemSelectedInRecyclerView(mUri);
                        //-- or --
                        // ((CallBackListener) getActivity()).onItemSelectedInRecyclerView(tryUri);

                    }

        };

        movie_Adapter = new Movie_RecyclerViewAdapter(getContext(), ref_onItemClickHandler_0);
        //************************************************

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_id4_movies);

        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        mLayoutManager = new GridLayoutManager(getContext(), 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(movie_Adapter);
        mRecyclerView.setHasFixedSize(true);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(SELECTED_INDEX)) {
            mPosition = savedInstanceState.getInt(SELECTED_INDEX);
        }

        return rootView;
    }

}