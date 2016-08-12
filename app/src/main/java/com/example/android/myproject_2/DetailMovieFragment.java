package com.example.android.myproject_2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myproject_2.data.MovieContract;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailMovieFragment extends Fragment
                     implements LoaderManager.LoaderCallbacks<Cursor>
{

    public DetailMovieFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    private static final String LOG_TAG = DetailMovieFragment.class.getSimpleName();

    private static final int DETAIL_MOVIE_LOADER = 3;
    private String mUriString;
    private Uri mUri;

    static final String DETAIL_URI = "URI";



    private static final String[] PROJECTION_RATING = {
            MovieContract.RatingEntry.TABLE_NAME + "." + MovieContract.RatingEntry._ID,
            MovieContract.RatingEntry.COL_MV_ID,
            MovieContract.MovieInfoEntry.COL_POSTERLINK,
            MovieContract.MovieInfoEntry.COL_OVERVIEW,
            MovieContract.MovieInfoEntry.COL_RELEASEDATE
            // MovieContract.RatingEntry.COL_TITLE,
    };
    private static final String[] PROJECTION_POPULAR = {
            MovieContract.PopularEntry.TABLE_NAME + "." + MovieContract.PopularEntry._ID,
            MovieContract.PopularEntry.COL_MV_ID,
            MovieContract.MovieInfoEntry.COL_POSTERLINK,
            MovieContract.MovieInfoEntry.COL_OVERVIEW,
            MovieContract.MovieInfoEntry.COL_RELEASEDATE
            // MovieContract.PopularEntry.COL_TITLE
    };
    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int PROJECTION_RATING_ID = 0;
    public static final int PROJECTION_RATING_MOVIE_ID = 1;
    public static final int PROJECTION_RATING_POSTERLINK = 2;
    public static final int PROJECTION_RATING_OVERVIEW = 3;
    public static final int PROJECTION_RATING_RELEASEDATE = 4;
    public static final int PROJECTION_RATING_MOVIE_TITLE = 5;

    public static final int PROJECTION_POPULAR_ID = 0;
    public static final int PROJECTION_POPULAR_MOVIE_ID = 1;
    public static final int PROJECTION_POPULAR_POSTERLINK = 2;
    public static final int PROJECTION_POPULAR_OVERVIEW = 3;
    public static final int PROJECTION_POPULAR_RELEASEDATE = 4;
    public static final int PROJECTION_POPULAR_TITLE = 5;

    ///////////////////////////////////////////////
    @Bind(com.example.android.myproject_2.R.id.movie_title_textView)
    TextView movie_title_textView;
    ///////////////////////////////////////////////
/*
    @Bind(com.example.android.myproject_2.R.id.movie_title_textView)
    TextView movie_title_textView;
//    @Bind(com.example.android.myproject_2.R.id.thumbnail_imageView)
//    ImageView thumbnail_imageView;
    @Bind(com.example.android.myproject_2.R.id.movie_release_date_textView)
    TextView movie_release_date_textView;
//    @Bind(com.example.android.myproject_2.R.id.movie_ratings)
//    TextView movie_ratings;
//    @Bind(com.example.android.myproject_2.R.id.movie_synopsis)
//    TextView movie_synopsis;
*/

    private TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_blank, container, false);

        Log.d(LOG_TAG, "++ onCreateView() --");
/*
        Intent intent = getActivity().getIntent();
        mUriString = intent.getDataString();
        mUri = intent.getData();
*/
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            mUri = mBundle.getParcelable(DetailMovieFragment.DETAIL_URI);
        }
        View view = inflater.inflate(R.layout.fragment_detailmovie, container, false);
        //+++++++++++++++
      /* ButterKnife.bind(this, view);
       movie_title_textView.setText(mUri.toString());*/
        //+++++++++++++++
        ButterKnife.bind(this, view);
        //+++++++++++++++
/*

//        mTextView = (TextView) view.findViewById(com.example.android.myproject_2.R.id.movie_title_textView);

            ButterKnife.bind(this, view);
    //    movie_title_textView.setText(mUri.toString());
     //   movie_title_textView.setText(mUriString);
*/

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
//        View rootView = inflater.inflate(com.example.android.myproject_2.R.layout.fragment_detailmovie, container, false);
//
//        ButterKnife.bind(this, rootView);
//
//        movie_title_textView.setText(aMovieInfo.mOriginalTitle);
//
//        Picasso.with(getActivity()).load(aMovieInfo.mThumbnail).into(thumbnail_imageView);
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

        Log.d(LOG_TAG, "++ onActivityCreated() --");
/*

        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);

*/
        getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    /// ??? mMovieFragment.myRestartLoaderCode();
    // tky add, 3rd Aug, 2016 //DetailMovieFragment.myResetLoaderCode_1();
    /**
    void myResetLoaderCode_1(){

        Log.d(LOG_TAG, "++ myResetLoaderCode_1() ----");
        getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);

        // Not Working ?? both lines
        //mvRcyclrVwAdapterVwHldr.swapCursor(null);  / no working
        //getLoaderManager().initLoader(POPULAR_LOADER_ID, null, this);

    }
    * */

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "++ onStart() --");
    /// ???    getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "++ onPause() --");
    }

    //-------------------------------------------
    //-- LoaderManager.LoaderCallbacks<Cursor> --
    //----------- LOADER Stuff ------------------
    //-------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Loader<Cursor> cursorLoader;
        //---------------
        String sortType = Utility.getPreferredSortSequence(getContext());
        if (sortType.equals("popularity.desc")) { return new CursorLoader(getContext(), mUri,PROJECTION_POPULAR, null, null, null);}
        else                                    { return new CursorLoader(getContext(), mUri,PROJECTION_RATING, null, null, null);}

        //---------------
//        String sortType = Utility.getPreferredSortSequence(getContext());
//        if (sortType.equals("popularity.desc")) {cursorLoader = new CursorLoader(getContext(), mUri,PROJECTION_POPULAR, null, null, null);}
//        else                                    {cursorLoader = new CursorLoader(getContext(), mUri,PROJECTION_RATING, null, null, null);}
//        return cursorLoader;
        //---------------
        //return new CursorLoader(getContext(), mUri,PROJECTION_POPULAR, null, null, null);

        //return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null && cursor.moveToFirst()) {

            Log.d(LOG_TAG, "++ onLoadFinished()/ Not NULL -- " + "cursor.getString(1):" + cursor.getString(1) + " --");
            Log.d(LOG_TAG, "++ onLoadFinished() -- loader ID : " + loader.getId() + " --");
        //    Log.d(LOG_TAG, "++ " + cursor.getString(1) + " --");

            //++++++++

            String sortType = Utility.getPreferredSortSequence(getContext());
            if (sortType.equals("popularity.desc")) {
                movie_title_textView.setText(cursor.getString(PROJECTION_POPULAR_RELEASEDATE));
            } else {
                movie_title_textView.setText(cursor.getString(PROJECTION_RATING_RELEASEDATE));
            }
                //++++++++
            //cursor.getString(PROJECTION_POPULAR_TITLE);
        ////    movie_release_date_textView.setText(cursor.getString(PROJECTION_POPULAR_RELEASEDATE));

             //  movie_title_textView.setText(cursor.getString(PROJECTION_POPULAR_TITLE));
           /// mTextView.setText(cursor.getString(PROJECTION_POPULAR_TITLE));
        } else {

            Log.d(LOG_TAG, "++ onLoadFinished() / NULL --");
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(LOG_TAG, "++ onLoaderReset() --");
    }
}
