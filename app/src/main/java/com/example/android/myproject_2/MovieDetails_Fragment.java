package com.example.android.myproject_2;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myproject_2.data.MovieContract.X_MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.X_MovieReviewEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetails_Fragment extends Fragment
                             implements
                             // LoaderManager.LoaderCallbacks<Cursor>,
                              SharedPreferences.OnSharedPreferenceChangeListener
                            , View.OnClickListener
{

    public MovieDetails_Fragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    private static final String LOG_TAG = MovieDetails_Fragment.class.getSimpleName();

    private static final int DETAIL_MOVIE_LOADER = 3;
    private static final int MOVIE_REVIEW_LOADER = 4;

    private Uri mUri;

    static final String DETAIL_URI = "URI";
    static final String MOVIE_ID = "MovieID";
    private String videoId;

    private Details_Adapter details_adapter;

    public boolean isFavouriteEnabled = false;

    ///////////////////////////////////////////////
    private static final String[] PROJECTION_X_MOVIEINFO = new String[]{
            X_MovieInfoEntry.TABLE_NAME + "." + X_MovieInfoEntry._ID,
            X_MovieInfoEntry.COL_MV_ID,
            X_MovieInfoEntry.COL_FAVOURITES,
            X_MovieInfoEntry.COL_BACKDROP_PATH,
            X_MovieInfoEntry.COL_OVERVIEW,
            X_MovieInfoEntry.COL_RELEASEDATE,
            X_MovieInfoEntry.COL_TITLE,
            X_MovieInfoEntry.COL_VOTE_AVERAGE
    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    //public static final int PROJECTION_RATING_ID = 0;
    public static final int INDX_MOVIE_ID = 1;
    public static final int INDX_FAVOURITES = 2;
    public static final int INDX_BACKDROP_PATH = 3;
    public static final int INDX_OVERVIEW = 4;
    public static final int INDX_RELEASEDATE = 5;
    public static final int INDX_MOVIE_TITLE = 6;
    public static final int INDX_MOVIE_RATING = 7;

    ///////////////////////////////////////////////
    private static final String[] PROJECTION_X_MOVIEREVIEW = new String[]{
            X_MovieReviewEntry.TABLE_NAME + "." + X_MovieReviewEntry._ID,
            X_MovieReviewEntry.TABLE_NAME + "." + X_MovieReviewEntry.COL_MV_ID,
            X_MovieReviewEntry.COL_REVIEWER,
            X_MovieReviewEntry.COL_REVIEWCONTENT
    };
    //public static final int PROJECTION_RATING_ID = 0;
    //public static final int INDX_1_MOVIE_ID = 1;
    public static final int INDX_1_REVIEWER = 2;
    public static final int INDX_1_REVIEWCONTENT = 3;

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
    @Bind(R.id.favourite_button)
    ImageButton movie_favourite_button;

    private TextView mTextView;

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

                    PackageManager manager = getContext().getPackageManager();

                    // Check for youtube app in device. ??
                    List<ResolveInfo> infos = manager.queryIntentActivities(mIntent, PackageManager.MATCH_DEFAULT_ONLY);

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
        ContentValues contentValues = new ContentValues();
        contentValues.put(X_MovieInfoEntry.COL_FAVOURITES, 0);

        String select = X_MovieInfoEntry.COL_MV_ID + "=?";
        String[] selectArg = new String[]{movieID};

        int rowId = getActivity().getContentResolver().update(
                    X_MovieInfoEntry.CONTENT_URI,
                    contentValues,
                    select,
                    selectArg
            );
        getContext().getContentResolver().notifyChange(X_MovieReviewEntry.CONTENT_URI, null);
    }

    private void saveToFavourites(String movieID) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(X_MovieInfoEntry.COL_FAVOURITES, 1);

        String select = X_MovieInfoEntry.COL_MV_ID + "=?";
        String[] selectArg = new String[]{movieID};

        int rowId = getActivity().getContentResolver().update(
                X_MovieInfoEntry.CONTENT_URI,
                contentValues,
                select,
                selectArg
        );
        getContext().getContentResolver().notifyChange(X_MovieReviewEntry.CONTENT_URI, null);

    }

    private boolean checkFavourites(String movieID) {

        String selection = X_MovieInfoEntry.COL_MV_ID + "=?" + " AND " + X_MovieInfoEntry.COL_FAVOURITES + "=?";
        String[] selectionArg = new String[]{movieID, "1"};

        Boolean favouritesBoolean = false;

        Cursor cursor = getActivity().getContentResolver().query(
                X_MovieInfoEntry.CONTENT_URI,   // uri
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
    //-- for View.onClickListener, movie_video_button.setOnClickListener(this);
    //-------------------------------------------
    @Override
    public void onClick(View view) {

        String movieID = mUri.getPathSegments().get(1);

        switch (view.getId()) {

            case R.id.video_button :

            case R.id.thumbnail_imageView :

                //...................
                // TODO: call DBAsyncTAsk , call method FetchComplete()
                //
                FetchMoviesDbAsyncTask mTask = new FetchMoviesDbAsyncTask(getContext(), new FetchComplete());
                mTask.execute(movieID);

                /*String mVideoPath = "vnd.youtube:" + videoId;
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mVideoPath));
                startActivity(mIntent);*/

                break;

            case R.id.favourite_button :

                //movieID = mUri.getPathSegments().get(1);

                isFavouriteEnabled = checkFavourites(movieID);

                if (!isFavouriteEnabled) {
                    movie_favourite_button.setImageResource(android.R.drawable.btn_star_big_on);
                    saveToFavourites(movieID);
                }
                else {
                    movie_favourite_button.setImageResource(android.R.drawable.btn_star_big_off);
                    removeFromFavourites(movieID);
                }

                break;

            default:
                break;
        }
    }
    //-------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*Intent intent = getActivity().getIntent();
        mUriString = intent.getDataString();
        mUri = intent.getData();*/

        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            mUri = mBundle.getParcelable(MovieDetails_Fragment.DETAIL_URI);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_moviedetails, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_moviedetails);

        ButterKnife.bind(this, rootView);

        movie_video_button.setOnClickListener(this);
        movie_thumbnail_imageview.setOnClickListener(this);
        movie_favourite_button.setOnClickListener(this);

        //++++++++++++++++++++++++++++++++++++++
        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, myTry_0);
        getLoaderManager().initLoader(MOVIE_REVIEW_LOADER, null, myTry_1);
        //++++++++++++++++++++++++++++++++++++++

        details_adapter = new Details_Adapter(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(details_adapter);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();    //Log.d(LOG_TAG, "++++ 3 onStart() --");
    }

    @Override
    public void onPause() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sp.unregisterOnSharedPreferenceChangeListener(this);
        //sp.unregisterOnSharedPreferenceChangeListener(listener);

        super.onPause();    //Log.d(LOG_TAG, "++++ 5 onPause() --");
    }

    @Override
    public void onResume() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sp.registerOnSharedPreferenceChangeListener(this);
        //sp.registerOnSharedPreferenceChangeListener(listener);

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
    //    private LoaderManager.LoaderCallbacks<Cursor> myTry_0 = new MyTryLoader(context, mUri);
    //    private LoaderManager.LoaderCallbacks<Cursor> myTry_0 = new MyTryLoader(this.getActivity(), mUri);
    //    private LoaderManager.LoaderCallbacks<Cursor> myTry_0 = new MyTryLoader(getActivity(), mUri);
    //    private LoaderManager.LoaderCallbacks<Cursor> myTry_0 = new MyTryLoader(getContext(), mUri);
    private LoaderManager.LoaderCallbacks<Cursor> myTry_0 = new LoaderManager.LoaderCallbacks<Cursor>()
    {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            String[] mProjection = PROJECTION_X_MOVIEINFO;

            return new CursorLoader(getActivity(), mUri, mProjection, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            if (cursor != null && cursor.moveToFirst()) {

                movie_title_textView.setText(cursor.getString(INDX_MOVIE_TITLE));
                movie_release_date_textView.setText(cursor.getString(INDX_RELEASEDATE));
                movie_ratings.setText(cursor.getString(INDX_MOVIE_RATING));
                movie_synopsis.setText(cursor.getString(INDX_OVERVIEW));

                Picasso.with(getContext()).load(cursor.getString(INDX_BACKDROP_PATH))
                        .placeholder(R.drawable.sample_1)
                        .error(R.drawable.sample_0)
                        .into(movie_thumbnail_imageview);

                int favouriteValue = cursor.getInt(INDX_FAVOURITES);
                if (favouriteValue == 1) {
                    isFavouriteEnabled = true;
                    movie_favourite_button.setImageResource(android.R.drawable.btn_star_big_on);
                } else {
                    isFavouriteEnabled = false;
                    movie_favourite_button.setImageResource(android.R.drawable.btn_star_big_off);
                }
                //...................
                // TODO: call DBAsyncTAsk , call method FetchComplete()
                //
                /*FetchMoviesDbAsyncTask mTask = new FetchMoviesDbAsyncTask(getContext(), new FetchComplete());
                mTask.execute(cursor.getString(INDX_MOVIE_ID));*/
                //...................

            }
        }

        @Override
        public void onLoaderReset(Loader loader) {
            Log.d(LOG_TAG, "++ onLoaderReset() --");
        }

    };

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private LoaderManager.LoaderCallbacks<Cursor> myTry_1 = new LoaderManager.LoaderCallbacks<Cursor>()
    {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            String[] mProjection = PROJECTION_X_MOVIEREVIEW;
            String mvID = mUri.getPathSegments().get(1);
            Uri xUri = ContentUris.withAppendedId(X_MovieReviewEntry.CONTENT_URI, Long.valueOf(mvID));

            return new CursorLoader(getActivity(), xUri, mProjection, null, null, null);

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            details_adapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

            details_adapter.swapCursor(null);
        }
    };
}