package com.example.android.myproject_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myproject_2.data.MovieContract;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetails_Fragment extends Fragment
                             implements
                              LoaderManager.LoaderCallbacks<Cursor>
                            , SharedPreferences.OnSharedPreferenceChangeListener
                            , View.OnClickListener

{

    public MovieDetails_Fragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    private static final String LOG_TAG = MovieDetails_Fragment.class.getSimpleName();

    private static final int DETAIL_MOVIE_LOADER = 3;
    private String mUriString;
    private Uri mUri;
    private String[] mProjection;
    static final String DETAIL_URI = "URI";
    private String videoId;



    private static final String[] PROJECTION_RATING = //new String[]
            {
            MovieContract.RatingEntry.TABLE_NAME + "." + MovieContract.RatingEntry._ID,
            MovieContract.RatingEntry.COL_MV_ID,
            MovieContract.MovieInfoEntry.COL_POSTERLINK,
            MovieContract.MovieInfoEntry.COL_OVERVIEW,
            MovieContract.MovieInfoEntry.COL_RELEASEDATE,
            MovieContract.MovieInfoEntry.TABLE_NAME + "." + MovieContract.MovieInfoEntry.COL_TITLE,
            MovieContract.MovieInfoEntry.COL_VOTE_AVERAGE
    };
    private static final String[] PROJECTION_POPULAR = //new String[]
            {
            MovieContract.PopularEntry.TABLE_NAME + "." + MovieContract.PopularEntry._ID,
            MovieContract.PopularEntry.COL_MV_ID,
            MovieContract.MovieInfoEntry.COL_POSTERLINK,
            MovieContract.MovieInfoEntry.COL_OVERVIEW,
            MovieContract.MovieInfoEntry.COL_RELEASEDATE,
            MovieContract.MovieInfoEntry.TABLE_NAME + "." + MovieContract.MovieInfoEntry.COL_TITLE,
            MovieContract.MovieInfoEntry.TABLE_NAME + "." + MovieContract.MovieInfoEntry.COL_VOTE_AVERAGE
    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int PROJECTION_RATING_ID = 0;
    public static final int INDX_MOVIE_ID = 1;
//    public static final int PROJECTION_RATING_POSTER = 2;
    public static final int INDX_POSTERLINK = 2;
    public static final int INDX_OVERVIEW = 3;
    public static final int INDX_RELEASEDATE = 4;
    public static final int INDX_MOVIE_TITLE = 5;
    public static final int INDX_MOVIE_RATING = 6;

    /*
    public static final int PROJECTION_POPULAR_ID = 0;
    public static final int PROJECTION_POPULAR_MOVIE_ID = 1;
//    public static final int PROJECTION_POPULAR_POSTER = 2;
    public static final int PROJECTION_POPULAR_POSTERLINK = 2;
    public static final int PROJECTION_POPULAR_OVERVIEW = 3;
    public static final int PROJECTION_POPULAR_RELEASEDATE = 4;
    public static final int PROJECTION_POPULAR_MOVIE_TITLE = 5;
    public static final int PROJECTION_POPULAR_MOVIE_RATING = 6;*/
    /*
    public static final int PROJECTION_RATING_ID = 0;
    public static final int PROJECTION_RATING_MOVIE_ID = 1;
    //    public static final int PROJECTION_RATING_POSTER = 2;
    public static final int PROJECTION_RATING_POSTERLINK = 2;
    public static final int PROJECTION_RATING_OVERVIEW = 3;
    public static final int PROJECTION_RATING_RELEASEDATE = 4;
    public static final int PROJECTION_RATING_MOVIE_TITLE = 5;
    public static final int PROJECTION_RATING_MOVIE_RATING = 6;
*/
    ///////////////////////////////////////////////
    ///////////////////////////////////////////////
    @Bind(com.example.android.myproject_2.R.id.movie_title_textView)
    TextView movie_title_textView;
    @Bind(com.example.android.myproject_2.R.id.thumbnail_imageView)
    ImageView movie_thumbnail_imageview;
    @Bind(com.example.android.myproject_2.R.id.movie_release_date_textView)
    TextView movie_release_date_textView;
    @Bind(com.example.android.myproject_2.R.id.movie_ratings)
    TextView movie_ratings;
    @Bind(com.example.android.myproject_2.R.id.movie_synopsis)
    TextView movie_synopsis;
    @Bind(com.example.android.myproject_2.R.id.video_button)
    Button movie_video_button;
    @Bind(com.example.android.myproject_2.R.id.review_button)
    Button movie_review_button;

    private TextView mTextView;

    //-----------------------------------------------------------
    //-- for FetchMoviesDbAsyncTask_1 ... AsyncTask<...> Stuff ----
    //-----------------------------------------------------------

    public class FetchComplete implements FetchMoviesDbAsyncTask_2.OnAsyncTaskCompletedListener {
        @Override
        public void onAsyncTaskCompleted(String[] result) {
            if (result != null) {
                //TODO; update VideoId for youtube
                //Toast.makeText(getContext(),"Great out of AsynTask :- " + result[0],Toast.LENGTH_LONG).show();
                videoId = result[0];
            }
            else {
                //
                //
                // Toast.makeText(getContext(),"Shit out of AsynTask :- ",Toast.LENGTH_LONG).show();
            }
        }
    }

    /*public class FetchComplete implements FetchMoviesDbAsyncTask_1.OnAsyncTaskCompletedListener {
        @Override
        public void onAsyncTaskCompleted(MoviesSelectedInfo[] result) {
            if (result != null) {
                //TODO; update VideoId for youtube
            }
        }
    }*/
    //-----------------------------------------------------------
    //-----------------------------------------------------------

    //-------------------------------------------
    //-- for View.onClickListener, movie_video_button.setOnClickListener(this);
    //-------------------------------------------
    @Override
    public void onClick(View view) {

        //movie_video_button.setText("Just Clicked");
        //Log.d(LOG_TAG, "++ OnClicked, VideoID = " + videoId );
        //Toast.makeText(getContext(),"VideoID : " + videoId, Toast.LENGTH_LONG).show();
        /*int viewId;*/
        switch (view.getId()) {
            case R.id.video_button :
            case R.id.thumbnail_imageView :
                String mVideoPath = "vnd.youtube:" + videoId;
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mVideoPath));
                startActivity(mIntent);
                break;
            default:
                break;
        }
        /*String mVideoPath = "vnd.youtube:" + videoId;
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mVideoPath));
        startActivity(mIntent);*/
    }
    //-------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_blank, container, false);

       // Log.d(LOG_TAG, "++++ 1 onCreateView() --");
/*
        Intent intent = getActivity().getIntent();
        mUriString = intent.getDataString();
        mUri = intent.getData();
*/
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            mUri = mBundle.getParcelable(MovieDetails_Fragment.DETAIL_URI);
        }

        View view = inflater.inflate(R.layout.fragment_moviedetails, container, false);

        //+++++++++++++++
        ButterKnife.bind(this, view);
        //+++++++++++++++

        movie_video_button.setOnClickListener(this);
        movie_thumbnail_imageview.setOnClickListener(this);
        movie_review_button.setOnClickListener(this);

        //++++++++++++++++++++++++++++++++++++++
        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
        //++++++++++++++++++++++++++++++++++++++

        Log.d(LOG_TAG, "++++ 1 onCreateView() ---- after initLoader call");

        return view;
//++++++++++++++++++++++++++++++++++
//
//        MoviesSelectedInfo aMovieInfo = null;
//
//        /*****
//         Intent intent = getActivity().getIntent();
//         Bundle bundle = intent.getExtras();
//         // -- or --
//         // Bundle bundle = getActivity().getIntent().getExtras();
//
//         if (bundle != null) {
//         aMovieInfo = bundle.getParcelable("try");
//         }*****/
//        // -- or --
//        Intent intent = getActivity().getIntent();
//        aMovieInfo = intent.getParcelableExtra("aMovieKey");
//
//        View rootView = inflater.inflate(com.example.android.myproject_2.R.layout.fragment_moviedetails, container, false);
//
//        ButterKnife.bind(this, rootView);
//
//        movie_title_textView.setText(aMovieInfo.mOriginalTitle);
//
//        Picasso.with(getActivity()).load(aMovieInfo.mThumbnail).into(movie_thumbnail_imageview);
//
//        movie_release_date_textView.setText("Release Date: " + aMovieInfo.mReleaseDate);
//        movie_ratings.setText("Ratings : " + aMovieInfo.mVoteAverage);
//        movie_synopsis.setText("SYNOPSIS : " + aMovieInfo.mOverview);
//
//        return rootView;
// ++++++++++++++++++++++++++++++++++++++++
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Log.d(LOG_TAG, "++++ 2 onActivityCreated() --");
//        //++++++++++++++++++++++++++++++++++++++
//        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
//        //++++++++++++++++++++++++++++++++++++++

//      getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    /// ??? mMovieFragment.myRestartLoaderCode();
    // tky add, 3rd Aug, 2016 //MovieDetails_Fragment.myResetLoaderCode_1();
    /**
    void myResetLoaderCode_1(){

        Log.d(LOG_TAG, "++ myResetLoaderCode_1() ----");
        getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);

    }
    * */
    //------------------------
    void myRestartLoaderCode(){

        Log.d(LOG_TAG, "-- myRestartLoaderCode() / getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this) ----");

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    }
    //-----------------------
    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "++++ 3 onStart() --");
    /// ???    getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);
    }

    @Override
    public void onPause() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //    sp.unregisterOnSharedPreferenceChangeListener(listener);
        sp.unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
        Log.d(LOG_TAG, "++++ 5 onPause() --");
    }

    @Override
    public void onResume() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //    sp.registerOnSharedPreferenceChangeListener(listener);
        sp.registerOnSharedPreferenceChangeListener(this);

        super.onResume();

        //&&&&&&&&&&&&&&&&&&&&&&&&&

        int displayMode = getResources().getConfiguration().orientation;

        String mMode;
        // displayMode -> 1 ==> portrait, 2 ==> landscape
        if (displayMode == 1) {
            mMode = "portrait";
        } else mMode = "landscape";
        Log.d(LOG_TAG, "++++ 4 onResume() ----- orientation = " + mMode  );
        //&&&&&&&&&&&&&&&&&&&&&&&&&

    //    Log.d(LOG_TAG, "++ 4 onResume() -- restartLoader(DETAIL_MOVIE_LOADER, ...)");
    //    getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);  // tky add, 14 August 1.44 am ??

        // TODO: call DBAsyncTAsk , call method FetchComplete()
        //
//        FetchMoviesDbAsyncTask_2 mTask = new FetchMoviesDbAsyncTask_2(getContext(), new FetchComplete());
//        mTask.execute();
    }

    //-----------------------------------------------------------
    //--- for SharedPreferences.OnSharedPreferenceChangeListener
    //-----------------------------------------------------------
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LOG_TAG, "-- onSharedPreferenceChanged --");
        //  Toast.makeText(getContext(),"-- Main_Fragment/onActivityCreated/onSharedPreferenceChanged()/key --" + key, Toast.LENGTH_SHORT).show();

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        if ( key.equals(getString(R.string.pref_sortmovies_key)) ) {

            String mString = Utility.getPreferredSortSequence(getContext());

            Log.d(LOG_TAG, "-- onSharedPreferenceChanged ..... key: " + key + " ..... SortSeq: " + mString);

            getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);

        //    Log.d(LOG_TAG, "-- onSharedPreferenceChanged ..... MoviesSyncAdapter.syncImmediately(this) --");

           // MoviesSyncAdapter.syncImmediately(getContext());

        }
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    }

    //-------------------------------------------
    //-- LoaderManager.LoaderCallbacks<Cursor> --
    //----------- LOADER Stuff ------------------
    //-------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Loader<Cursor> cursorLoader;

        //Uri mUri;
        //String[] mProjection;
        //---------------
    //    Log.d(LOG_TAG, "++++ A onCreateLoader() -- before call 'getPreferredSortSequence'");
        String sortType = Utility.getPreferredSortSequence(getContext());

        if (sortType.equals("popularity.desc")) { mProjection = PROJECTION_POPULAR; }
        else                                    { mProjection = PROJECTION_RATING; }

    //    Log.d(LOG_TAG, "++++ A onCreateLoader() -- before call 'return'");

        //return new CursorLoader(getActivity(), mUri, mProjection, null, null, null);
        cursorLoader = new CursorLoader(getActivity(), mUri, mProjection, null, null, null);

        Log.d(LOG_TAG, "++++ A onCreateLoader() -- before call 'return'");
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        Log.d(LOG_TAG, "++++ B onLoadFinished(), loader ID : " + loader.getId() + " ---------");

        if (cursor != null && cursor.moveToFirst()) {

            Log.d(LOG_TAG, "++++ B onLoadFinished()/ Not NULL -- " + "cursor.getString(1):" + cursor.getString(1) + " --");
        //    Log.d(LOG_TAG, "++ B onLoadFinished() -- loader ID : " + loader.getId() + " --");
        //    Log.d(LOG_TAG, "++ " + cursor.getString(1) + " --");

            movie_title_textView.setText(cursor.getString(INDX_MOVIE_TITLE));
            movie_release_date_textView.setText(cursor.getString(INDX_RELEASEDATE));
            movie_ratings.setText(cursor.getString(INDX_MOVIE_RATING));
            movie_synopsis.setText(cursor.getString(INDX_OVERVIEW));

            Picasso.with(getContext()).load(cursor.getString(INDX_POSTERLINK))
                    .placeholder(R.drawable.sample_1)
                    .error(R.drawable.sample_0)
                    .into(movie_thumbnail_imageview);

            //...................
            // TODO: call DBAsyncTAsk , call method FetchComplete()
            //
            FetchMoviesDbAsyncTask_2 mTask = new FetchMoviesDbAsyncTask_2(getContext(), new FetchComplete());
            mTask.execute(cursor.getString(INDX_MOVIE_ID));
            //...................

            //++++++++
            /*
            String sortType = Utility.getPreferredSortSequence(getContext());

            String title ;
            if (sortType.equals("popularity.desc")) {
                title = cursor.getString(INDX_MOVIE_TITLE);
                movie_title_textView.setText(title);

                movie_release_date_textView.setText(cursor.getString(INDX_RELEASEDATE));
                movie_ratings.setText(cursor.getString(INDX_MOVIE_RATING));
                movie_synopsis.setText(cursor.getString(INDX_OVERVIEW));

                Picasso.with(getContext()).load(cursor.getString(INDX_POSTERLINK))
                        .placeholder(R.drawable.sample_1)
                        .error(R.drawable.sample_0)
                        .into(movie_thumbnail_imageview);


//                Picasso.with(mContext)
//                    .load(mCursor.getString(Main_Fragment.COLUMN_POSTERLINK))
//                    //                .centerCrop()
//                    .placeholder(com.example.android.myproject_2.R.drawable.sample_1)
//                    .error(com.example.android.myproject_2.R.drawable.sample_0)
//                    .into(holder.imageView);

            } else {
                title = cursor.getString(INDX_MOVIE_TITLE);
                movie_title_textView.setText(title);

                movie_release_date_textView.setText(cursor.getString(INDX_RELEASEDATE));
                movie_ratings.setText(cursor.getString(INDX_MOVIE_RATING));
                movie_synopsis.setText(cursor.getString(INDX_OVERVIEW));

                Picasso.with(getContext())
                        .load(cursor.getString(INDX_POSTERLINK))
                        .placeholder(R.drawable.sample_1)
                        .error(R.drawable.sample_0)
                        .into(movie_thumbnail_imageview);

            }*/
            //++++++++
        } else {

            Log.d(LOG_TAG, "++++ onLoadFinished() / NULL --");
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(LOG_TAG, "++ onLoaderReset() --");
    }

  /*  @Override
    public void onClick(View view) {
        movie_video_button.setText("Just Clicked");
        Log.d(LOG_TAG, "++ OnClicked");
    }*/
}
