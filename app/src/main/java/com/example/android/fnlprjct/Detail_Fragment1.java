package com.example.android.fnlprjct;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.android.fnlprjct.adapter.DetailRcyclrVw_Adapter;
import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;
import com.example.android.fnlprjct.data.MovieContract.MovieReviewEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Detail_Fragment1 extends Fragment
                             implements
                              SharedPreferences.OnSharedPreferenceChangeListener
                            , View.OnClickListener
{

    // Required empty public constructor
    public Detail_Fragment1() {
        //setHasOptionsMenu(true);
    }

    // tky comment ---------------------------------------------
    // + Used by the caller, MyFrgmntPgrAdptr/FragmentStatePagerAdapter
    // + To create a new instance of ArticleDetailFragment/Fragment,
    //    providing "num" as an argument.
    //---------------------------------------------------------------
    public static Detail_Fragment1 newInstance(int itemId) {

        Bundle arguments = new Bundle();
               arguments.putInt(ITEMID_KEY, itemId);

        Detail_Fragment1 fragment = new Detail_Fragment1();
                         fragment.setArguments(arguments);

        return fragment;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private static final String LOG_TAG = Detail_Fragment1.class.getSimpleName();
    private static final String ITEMID_KEY = "ItemID_Key";

    static final String DETAIL_URI = "URI";

    private static final int DETAIL_MOVIE_LOADER = 3;
    private static final int MOVIE_REVIEW_LOADER = 4;

    private Uri mUri;
    private Uri uri;
    private int mItemId;

    //static final String MOVIE_ID = "MovieID";
    private String videoId;

    private DetailRcyclrVw_Adapter detailsAdapter;
    private String targetSharedElementTransition;
    public boolean isFavouriteEnabled = false;

    private AnimatedVectorDrawable emptyHeart;
    private AnimatedVectorDrawable fillHeart;

    /*@BindView(R.id.movietitle_tv) TextView movietitle;*/
    @BindView(R.id.moviethumbnail_iv) DynamicHeightNetworkImageView  moviethumbnail;
    @BindView(R.id.moviereleasedate_tv) TextView moviereleasedate;
    @BindView(R.id.movieratings_tv) TextView movieratings;
    @BindView(R.id.moviesynopsis_tv) TextView moviesynopsis;
    @BindView(R.id.movievideo_tv) ImageView movievideo;
    @BindView(R.id.favourite_btn) ImageView moviefavourite; // ImageView > ImageButton > FloatingActionButton
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.moviedetails_ryclrv) RecyclerView recyclerView;

    //-----------------------------------------------------------
    //-- for FetchMoviesDbAsyncTask_1 ... AsyncTask<...> Stuff ----
    //-----------------------------------------------------------
    public class FetchComplete implements FetchMoviesDbAsyncTask.OnAsyncTaskCompletedListener {

        @Override
        public void onAsyncTaskCompleted(String[] result) {

            if (result != null) {
                if (result.length == 0){
                    Toast.makeText(getContext(),"Error -- No Videos Trailer for YouTube" , Toast.LENGTH_LONG).show();
                }
                else {
                    //TODO; update VideoId for youtube
                    videoId = result[0];

                    String mVideoPath = "vnd.youtube:" + videoId;
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mVideoPath));

                    PackageManager pckgMngr = getContext().getPackageManager();

                    // Check for youtube app in device. ??
                    List<ResolveInfo> infos = pckgMngr.queryIntentActivities(mIntent, PackageManager.MATCH_DEFAULT_ONLY);

                    // There is no youtube app in device
                     if (infos.size() == 0) {
                         mIntent = new Intent(Intent.ACTION_VIEW,
                                 Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                    }
                    startActivity(mIntent);
                }
            }
        }
    }

    //-----------------------------------------------------------
    //-----------------------------------------------------------
    private void removeFromFavourites(String movieID) {

        ContentValues
            contentValues = new ContentValues();
            contentValues.put(MovieInfoEntry.COL_FAVOURITES, 0);

        String select = MovieInfoEntry.COL_MOVIE_ID + "=?";
        String[] selectArg = new String[]{movieID};

        int rowId = getActivity().getContentResolver().update(
                    MovieInfoEntry.CONTENT_URI,
                    contentValues,
                    select,
                    selectArg
            );
        getContext().getContentResolver().notifyChange(MovieReviewEntry.CONTENT_URI, null);
    }

    private void saveToFavourites(String movieID) {

        ContentValues contentValues = new ContentValues();
                      contentValues.put(MovieInfoEntry.COL_FAVOURITES, 1);

        String select = MovieInfoEntry.COL_MOVIE_ID + "=?";
        String[] selectArg = new String[]{movieID};

        int rowId = getActivity().getContentResolver().update(
                MovieInfoEntry.CONTENT_URI,
                contentValues,
                select,
                selectArg
        );
        getContext().getContentResolver().notifyChange(MovieReviewEntry.CONTENT_URI, null);

    }

    private boolean checkFavourites(String movieID) {

        String selection = MovieInfoEntry.COL_MOVIE_ID + "=?"
                            + " AND "
                            + MovieInfoEntry.COL_FAVOURITES + "=?";
        String[] selectionArg = new String[]{movieID, "1"};

        Boolean favouritesBoolean = false;

        Cursor cursor = getActivity().getContentResolver().query(
                MovieInfoEntry.CONTENT_URI,   // uri
                null,                           // projection  ( get the specified Column(s) )
                selection,                      // selection   ( satisfying the  conditions )
                selectionArg,                   // selectionArg ( conditions of specific values )
                null                            // sort order
        );

        if (cursor != null) {
            if (cursor.moveToFirst() && cursor.getCount() > 0) {
                favouritesBoolean = true;
            }
        }

        cursor.close();

        return favouritesBoolean;
    }

    //-------------------------------------------
    //-- for View.onClickListener, movievideo.setOnClickListener(this);
    //-------------------------------------------
    @Override
    public void onClick(View view) {

        String movieID = mUri.getPathSegments().get(1);

        switch (view.getId()) {

            case R.id.movievideo_tv:

                //...................
                // TODO: call DBAsyncTAsk , call method FetchComplete()
                //
                FetchMoviesDbAsyncTask mTask = new FetchMoviesDbAsyncTask(getContext(), new FetchComplete());
                                       mTask.execute(movieID);

                break;

            case R.id.favourite_btn:

                //movieID = mUri.getPathSegments().get(1);

                isFavouriteEnabled = checkFavourites(movieID);

                if (!isFavouriteEnabled) {
                    moviefavourite.setImageDrawable(fillHeart);
                    fillHeart.start();

                    saveToFavourites(movieID);
                }
                else {
                    moviefavourite.setImageDrawable(emptyHeart);
                    emptyHeart.start();

                    removeFromFavourites(movieID);
                }

                break;

            default:
                break;
        }
    }
    //-------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Return the arguments supplied when the fragment was instantiated, if any.
        // ?????? is it needed ???
        Bundle bundle = this.getArguments();
        if (bundle.containsKey(ITEMID_KEY) == true) {
            mItemId = bundle.getInt(ITEMID_KEY);
        /*if (bundle.containsKey("MyKey1") == true) {
            mItemId = bundle.getInt("MyKey1");*/

            uri = MovieInfoEntry.CONTENT_URI;
            uri = ContentUris.withAppendedId(uri, mItemId);
            mUri = uri;
        }

        // tky add, july10 2017,
        // must add this to solve the change-of-orientation where
        // the 'Details_Activity' gets recreated but the loader in LoaderManager still exist.
        //
        // https://developer.android.com/reference/android/support/v4/app/LoaderManager.html
        // Starts a new or restarts an existing Loader in this manager, registers the callbacks to it,
        // and (if the activity/fragment is currently started) starts loading it.
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, detailsLoaderCallback_0); //  help maintain position ??
        getLoaderManager().restartLoader(MOVIE_REVIEW_LOADER, null, reviewLoaderCallback_1); //  help maintain position ??
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*Intent intent = getActivity().getIntent();
        mUriString = intent.getDataString();
        mUri = intent.getData();*/

//        Bundle mBundle = this.getArguments();
//        if (mBundle != null) {
//            mUri = mBundle.getParcelable(Detail_Fragment1.DETAIL_URI);
//        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details1, container, false);

        ButterKnife.bind(this, rootView);

        movievideo.setOnClickListener(this);

        // ----------------------
        emptyHeart = (AnimatedVectorDrawable) getActivity().getDrawable(R.drawable.avd_heart_empty);
        fillHeart = (AnimatedVectorDrawable) getActivity().getDrawable(R.drawable.avd_heart_fill);
        // ----------------------

        moviefavourite.setOnClickListener(this);

        targetSharedElementTransition = getString(R.string.shared_name) + mItemId;

        ViewCompat.setTransitionName(moviethumbnail, targetSharedElementTransition);
        /// moviethumbnail
        // **************************************
        // --- old way ?? ---
        //toolbar = (Toolbar) rootView.findViewById(R.id.tool_bar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the tool_bar exists in the activity and is not null.
        //-- To to use setSupportActionBar in fragment, use '((AppCompatActivity)getActivity()).'  --
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // --- new way ?? --- using
        // https://material.io/icons/#ic_arrow_back
        // https://developer.android.com/reference/android/support/v7/widget/Toolbar.html
        if (toolbar != null ){

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationContentDescription(getString(R.string.descriptor_backarrow)); // tky add, working

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //----------------------------------------------------------------
                    // Indirect a call to a method via ArticaleDetailActivity/Activity.
                    // supportFinishAfterTransition() -- refers to a method in FragmentActivity.java
                    //----------------------------------------------------------------
                    // https://developer.android.com/reference/android/support/v4/app/Fragment.html
                    // https://developer.android.com/reference/android/support/v4/app/FragmentActivity.html
                    // getActivity() --- Return the FragmentActivity this fragment is currently associated with.
                    // supportFinishAfterTransition() --- Reverses the Activity Scene entry Transition and triggers the calling Activity to reverse its exit Transition
                    getActivity().supportFinishAfterTransition();
                    // onSupportNavigateUp();
                }
            });
            /*toolbar.setTitle("HELLO");*/

        }
        // **************************************
        //++++++++++++++++++++++++++++++++++++++
        // https://developer.android.com/reference/android/support/v4/app/Fragment.html#getLoaderManager()
        // https://developer.android.com/reference/android/support/v4/app/LoaderManager.html
        // getLoaderManager() :- Return the LoaderManager for this fragment, creating it if needed.
        // initLoader :- Ensures a loader is initialized and active
        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, detailsLoaderCallback_0);
        getLoaderManager().initLoader(MOVIE_REVIEW_LOADER, null, reviewLoaderCallback_1);
        //++++++++++++++++++++++++++++++++++++++

        detailsAdapter = new DetailRcyclrVw_Adapter(getContext());

        LinearLayoutManager lytMngr = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(lytMngr);
        recyclerView.setAdapter(detailsAdapter);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //----------------------------------------------------------------
                    // Indirect a call to a method via ArticaleDetailActivity/Activity.
                    // supportFinishAfterTransition() -- refers to a method in FragmentActivity.java
                    //----------------------------------------------------------------
                    getActivity().supportFinishAfterTransition(); // onSupportNavigateUp();
                }
            });*/
    }

    @Override
    public void onStart() {
        super.onStart();    //Log.d(LOG_TAG, "++++ 3 onStart() --");
    }

    @Override   // ++++ 5
    public void onPause() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                          sp.unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }

    @Override
    public void onResume() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                          sp.registerOnSharedPreferenceChangeListener(this);

        super.onResume();
    }

    //-----------------------------------------------------------
    //--- for SharedPreferences.OnSharedPreferenceChangeListener
    //-----------------------------------------------------------
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    //-----------------------------------------------------------
    //-- LoaderManager.LoaderCallbacks<Cursor> ------------------
    //----------- LOADER Stuff ----------------------------------
    //-----------------------------------------------------------
    private LoaderManager.LoaderCallbacks<Cursor> detailsLoaderCallback_0 =
                                new LoaderManager.LoaderCallbacks<Cursor>()
    {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            String[] projection = MyQuery.MovieInfo.PROJECTION;

            return new CursorLoader(getActivity(), mUri, projection, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            if (cursor != null && cursor.moveToFirst()) {

                //movietitle.setText(cursor.getString(MyQuery.MovieInfo.COL_MOVIE_TITLE));
                moviereleasedate.setText(cursor.getString(MyQuery.MovieInfo.COL_RELEASEDATE));

                String string = Utility.getPreferredSortSequence(getContext());
                if (string.equals(getString(R.string.pref_value_movies_sortby_ratings))){
                    movieratings.setText(cursor.getString(MyQuery.MovieInfo.COL_VOTE_AVERAGE));
                }else{
                    movieratings.setText(cursor.getString(MyQuery.MovieInfo.COL_VOTE_COUNT));
                }

                moviesynopsis.setText(cursor.getString(MyQuery.MovieInfo.COL_OVERVIEW));

                // -------------------------------
                // ---- Using Volley, begin ------
                // -------------------------------
                ImageLoader imageLoader = ImageLoaderHelper.getInstance(getContext()).getImageLoader();

                String stringUrl = cursor.getString(MyQuery.MovieInfo.COL_POSTERLINK);
                //String stringUrl = cursor.getString(MyQuery.MovieInfo.COL_BACKDROPLINK);

                Log.d(LOG_TAG, "? ? ? ?  " + stringUrl + " ? ? ? ?");
                // -- thumb-nail-View --
                // .setImageUrl -- define in ImageView
                moviethumbnail.setImageUrl(stringUrl, imageLoader);
                moviethumbnail.setAspectRatio(0.66f); // 1.0f, 1.5f, 0.66f     // --combo 1 --,-- combo2,1.0f --, combo3,1.0f
                //moviethumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER); // --combo 1 --
                moviethumbnail.setScaleType(ImageView.ScaleType.FIT_XY); // --combo 1 --


//--                moviethumbnail.setAspectRatio(1.0f); // 1.5f, 0.66f     // --combo 1 --,-- combo2,1.0f --, combo3,1.0f
//--                moviethumbnail.setScaleType(ImageView.ScaleType.FIT_END);   // combo3
//                  moviethumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER); // --combo 1 --
//                moviethumbnail.setScaleType(ImageView.ScaleType.FIT_START); // -- combo2 --
//                moviethumbnail.setScaleType(ImageView.ScaleType.FIT_XY); // --combo 1 --
//                  moviethumbnail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                moviethumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP); // lousy

//                moviethumbnail.setScaleType(ImageView.ScaleType.MATRIX);

                // -------------------------------
                // ---- Using Volley, end --------
                // -------------------------------
                scheduleStartPostponedTransition(moviethumbnail);

                int favouriteValue = cursor.getInt(MyQuery.MovieInfo.COL_FAVOURITES);

                if (favouriteValue == 1) {
                    isFavouriteEnabled = true;
                    moviefavourite.setImageDrawable(fillHeart);
                    fillHeart.start();

                } else {
                    isFavouriteEnabled = false;
                    moviefavourite.setImageDrawable(emptyHeart);
                    emptyHeart.start();

                }

                cursor.close();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.d(LOG_TAG, "++ onLoaderReset() --");
            loader = null;
        }

        ////////////////////////////////////////////////////////////
        private void scheduleStartPostponedTransition(final View sharedElement) {
            sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        getActivity().startPostponedEnterTransition();
                        return true;
                    }
                });
        }
        ////////////////////////////////////////////////////////////
    };

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private LoaderManager.LoaderCallbacks<Cursor> reviewLoaderCallback_1 =
                new LoaderManager.LoaderCallbacks<Cursor>()
    {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            String[] projection = MyQuery.Reviews.PROJECTION;
            String mvID = mUri.getPathSegments().get(1);
            Uri xUri = ContentUris.withAppendedId(MovieReviewEntry.CONTENT_URI, Long.valueOf(mvID));

            return new CursorLoader(getActivity(), xUri, projection, null, null, null);

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            detailsAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

            detailsAdapter.swapCursor(null);
        }
    };
}