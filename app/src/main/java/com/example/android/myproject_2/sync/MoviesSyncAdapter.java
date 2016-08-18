package com.example.android.myproject_2.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.myproject_2.BuildConfig;
import com.example.android.myproject_2.MoviesSelectedInfo;
import com.example.android.myproject_2.R;
import com.example.android.myproject_2.Utility;
import com.example.android.myproject_2.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by kkyin on 2/7/2016.
 */
public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    private final String LOG_TAG = MoviesSyncAdapter.class.getSimpleName();

    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    //+++++++++++++++++++++++++++++++++
    //+++ Add 'ACCOUNT' required by the 'Framework'
    //+++++++++++++++++++++++++++++++++
    // 1) The authority for the sync adapter's content provider
    // 2) An account type, in the form of a domain name
    // 3) The account name
    // 4) Instance name
    //+++++++++++++++++++++++++++++++++

    private static Context mContext;

    public MoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        //-----------------------------------------
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//        String sortBy = sharedPreferences.getString(mContext.getString(R.string.pref_sortmovies_key), mContext.getString(R.string.pref_sortmovies_default_value));
        //-----------------------------------------
        String sortBy = Utility.getPreferredSortSequence(mContext);
        //-----------------------------------------

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection = null;
        BufferedReader bufferedRdr = null;

        // Will contain the raw JSON response as a string.
        String movieInfoInJsonStr;// = null;
        Uri buildUri; // = null;

        // (1) build the url,
        // (2) create request and open connection with the url
        // (3) read the input-stream and convert the stream to string
        // (4) create an input-stream-reader
        // (4a) create a buffered-reader
        // (5) convert string-buffer to string

        // (6) convert string from(5) to JSONObject
        // (7) return elements from JSONOject to method caller e.g. getIdfromJson

        // get the api result from moviedb.org

        try {

            //----------------------------------------- added 8th August 2 am
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//            String sortBy = sharedPreferences.getString(mContext.getString(R.string.pref_sortmovies_key), mContext.getString(R.string.pref_sortmovies_default_value));
            //-----------------------------------------

            final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";
            final String DISCOVER_MODE = "discover/movie";
            final String SORT_PARAM = "sort_by";
            final String KEY_PARAM = "api_key";

            // (1) build the Url
            buildUri = Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
                    .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI
                    .appendEncodedPath(DISCOVER_MODE)
                    .appendQueryParameter(KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(SORT_PARAM, sortBy)
                    //.appendQueryParameter(SORT_PARAM, params[0]) //.appendQueryParameter(SORT_PARAM, DESC)

                    //          .appendQueryParameter("with_genres", "18")
                    .appendQueryParameter("certification_country", "US")
                    .appendQueryParameter("primary_release_year", "2015")
                    .appendQueryParameter("vote_count.gte", "50")
                    .build();

            URL url = new URL(buildUri.toString());

            // (2) create request and open connection with the url
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            // (3) read the input-stream and convert the stream to string
            InputStream inputStream = httpUrlConnection.getInputStream();
            //StringBuffer stringBuffer = new StringBuffer();

//            if (inputStream == null) { // commented 12July
//                return null;
//            }

            // (4) create an input-stream-reader
            // (4a) create a buffered-reader
            // (4b) create a string-buffer
            bufferedRdr = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            //   StringBuffer stringBuffer = new StringBuffer();
            StringBuilder stringBuffer = new StringBuilder();

            while ((line = bufferedRdr.readLine()) != null) {
                String st = line + "\n";
                stringBuffer.append(st);
                //   stringBuffer.append(line + "\n");
            }
//            if (stringBuffer.length() == 0) { // commented 12July
//                return null;
//            }

            // (5) convert string-buffer to string
            movieInfoInJsonStr = stringBuffer.toString();

            Log.d(LOG_TAG, "--------" + "sortBy ::: " + sortBy + "-----------");
            // tky, to add and replace code below
            getMovieInfoFromJson(movieInfoInJsonStr, sortBy);
            Log.d(LOG_TAG, "  <--- OUTSIDE getMovieInfoFromJson(); ---");

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);

        } catch (JSONException e) { // tky, JSONException linked to getMovieInfoFromJson()
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();

        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            if (bufferedRdr != null) {
                try {
                    bufferedRdr.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        //Toast.makeText(mContext, "--- getMovieInfoFromJson(movieInfoInJsonStr, sortBy); ---", Toast.LENGTH_SHORT).show();
//        return null; // commented 12July
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++
    private void getMovieInfoFromJson(String moviesJsonStr, String sortBy) throws JSONException {

        long rowId; // = 0L;
        //long rowId = 0L;
        //String rowId = "";
        final String RESULTS = "results";
        final String ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String BACKDROP_PATH = "backdrop_path"; // movie poster image thumbnail
        final String OVERVIEW = "overview";     // plot synopsis
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average"; // user rating
        final String VOTE_COUNT = "vote_count";
        final String POPULARITY = "popularity";

        final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";
        // W92 = "w92/"; W154 = "w154/";
        // W185 = "w185/"; W342 = "w342/";
        // W342 = "w342/"; W500 = "w500/";
        // W780 = "w780/"; ORIGINAL = "original/";
        //    final String W780 = "w780/";
        final String W500 = "w500/";

        Log.d(LOG_TAG, "  ---> INSIDE  getMovieInfoFromJson(); ---");

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultArray = moviesJson.getJSONArray(RESULTS);

        //++++++++++++++++++
        Vector<ContentValues> mVofCV = new Vector<ContentValues>(resultArray.length());
        //++++++++++++++++++
        for (int i = 0; i < resultArray.length(); i++) {

            JSONObject movieInfoJson = resultArray.getJSONObject(i);

            /// tky, ? where to add method: addMovieInfo(movieInfoJson, params[0]);
            ///
            long   mvId          = movieInfoJson.getLong(ID);
            String mvOrgTitle    = movieInfoJson.getString(ORIGINAL_TITLE);
            String mvOverview    = movieInfoJson.getString(OVERVIEW); // plot synopsis
            String mvVoteAverage = movieInfoJson.getString(VOTE_AVERAGE);  // user rating
            long   mvVoteCount   = movieInfoJson.getLong(VOTE_COUNT);  // user, sum number of votes
            String mvPopularity  = movieInfoJson.getString(POPULARITY);
            String mvReleaseDate = movieInfoJson.getString(RELEASE_DATE);

            String mvPosterPath   = TMDB_BASE_URL + W500 + movieInfoJson.getString(POSTER_PATH);
            String mvBackDropPath = TMDB_BASE_URL + "w780" +
                    "/" + movieInfoJson.getString(BACKDROP_PATH); // movie poster image thumbnail
       //     String mvBackDropPath = TMDB_BASE_URL + W500 + movieInfoJson.getString(BACKDROP_PATH); // movie poster image thumbnail

            //++++++++++++++++++++++++++++++++++++++++++++++
            ContentValues mCvMvInfo = new ContentValues();
            mCvMvInfo.put(MovieContract.MovieInfoEntry.COL_MV_ID,         mvId);
            mCvMvInfo.put(MovieContract.MovieInfoEntry.COL_TITLE,         mvOrgTitle);
            mCvMvInfo.put(MovieContract.MovieInfoEntry.COL_RELEASEDATE,   mvReleaseDate);
            mCvMvInfo.put(MovieContract.MovieInfoEntry.COL_OVERVIEW,      mvOverview);
            mCvMvInfo.put(MovieContract.MovieInfoEntry.COL_POSTERLINK,    mvBackDropPath);
      //      mCvMvInfo.put(MovieContract.MovieInfoEntry.COL_POSTERLINK,    mvPosterPath);
            mCvMvInfo.put(MovieContract.MovieInfoEntry.COL_VOTE_AVERAGE,  mvVoteAverage);
            mCvMvInfo.put(MovieContract.MovieInfoEntry.COL_POPULARITY,    mvPopularity);
            mCvMvInfo.put(MovieContract.MovieInfoEntry.COL_VOTE_COUNT,    mvVoteCount);

            rowId = addMovieInfo(mCvMvInfo,  sortBy);

            ContentValues mCv = new ContentValues();
            if(sortBy.equals("popularity.desc")) {
                mCv.put(MovieContract.PopularEntry.COL_MV_ID, mvId);
                mCv.put(MovieContract.PopularEntry.COL_TITLE, mvOrgTitle);
                mCv.put(MovieContract.PopularEntry.COL_POSTER, mvPosterPath);
                mCv.put(MovieContract.PopularEntry.COL_KEY_ID, rowId); // ??

                addPopularTable(mCv, i + 1, rowId);
            }
            if(sortBy.equals("vote_average.desc")){
                mCv.put(MovieContract.RatingEntry.COL_MV_ID, mvId);
                mCv.put(MovieContract.RatingEntry.COL_TITLE, mvOrgTitle);
                mCv.put(MovieContract.RatingEntry.COL_POSTER, mvPosterPath);
                mCv.put(MovieContract.RatingEntry.COL_KEY_ID, rowId); // ??

                addRatingTable(mCv, i + 1, rowId);
            }
            //++++++++++++++++++++++++++++++++++++++++++++++
        }

    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void addPopularTable(ContentValues contentvalues, int arrayIndx, long rowId) {
        long popularTableRow_Id;

        long movieId = contentvalues.getAsLong(MovieContract.PopularEntry.COL_MV_ID);

        //Log.d(LOG_TAG, "  ---> 355 addPopular ---> contentResolver-query" + "----" + String.valueOf(movieInfo.mId));
        //    Cursor mCursor = mContext.getContentResolver()
        Cursor mCursor = getContext().getContentResolver()
                .query(
                        MovieContract.PopularEntry.CONTENT_URI,         // uri
                        new String[]{MovieContract.PopularEntry._ID}, //, PopularEntry.COL_KEY_ID},
                                                                        // projection
                        MovieContract.PopularEntry.COL_MV_ID + "=?",    // selection
                        new String[]{String.valueOf(movieId)},          // selectionArg
                        null                                            // sort-order
                );

        //Log.d(LOG_TAG, "  <--- 365 addPopular <--- contentResolver-query");

        // there is such a movie_Id in table
        if (mCursor.moveToFirst()==true) {
            //---- get the '_id' value
            // from the cursor, get the column index scalar value with the associated column name
            int mColIndx = mCursor.getColumnIndex(MovieContract.MovieInfoEntry._ID);
            // with the column index value, get the value residing in it.

            popularTableRow_Id = mCursor.getLong(mColIndx);

            // the movie_Id is in the correct table location/sequence
            if (arrayIndx == (int)popularTableRow_Id) {
                // do nothing
            }
            else {

                String selection = MovieContract.PopularEntry.TABLE_NAME + "." + MovieContract.PopularEntry._ID + "=?";

                //Log.d(LOG_TAG, "  ---> 387 addPopular ---> contentResolver-update");

                //    int rowsUpdated = mContext.getContentResolver()
                int rowsUpdated = getContext().getContentResolver()
                        .update(MovieContract.PopularEntry.CONTENT_URI,
                                contentvalues,
                                //cv,
                                selection,
                                new String[]{String.valueOf(arrayIndx)}
                        );

                //Log.d(LOG_TAG, "  <--- 395 addPopular <--- contentResolver-update");
            }
        }
        // there is no such movie_Id in table, so we need to insert the info.
        else {

            //Log.d(LOG_TAG, "  ---> 406 addPopular ---> contentResolver-insert");

            //Uri rowsUpdated = mContext.getContentResolver()
            Uri rowsUpdated = getContext().getContentResolver()
                    .insert(MovieContract.PopularEntry.CONTENT_URI,
                            contentvalues
                            //cv
                    );

            //Log.d(LOG_TAG, "  <--- 412 addPopular <--- contentResolver-insert");
        }

        mCursor.close();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private long addMovieInfo(ContentValues contentValues, String sortBy) {

        long valueOf_Id;

        //Log.d(LOG_TAG, "  ---> 417 addMovieInfo ---> contentResolver-query");
        //    Cursor mCursor = mContext.getContentResolver()
        Cursor mCursor = getContext().getContentResolver()
                .query(
                        MovieContract.MovieInfoEntry.CONTENT_URI,         // uri
                        new String[]{MovieContract.MovieInfoEntry._ID},   // projection
                        MovieContract.MovieInfoEntry.COL_MV_ID + "=?",    // selection
                        new String[]{String.valueOf(contentValues.getAsLong(MovieContract.MovieInfoEntry.COL_MV_ID))},  // selectionArg
                   //     new String[]{String.valueOf(movieInfo.mId)},  // selectionArg
                        null                                            // sort order
                );

        //Log.d(LOG_TAG, "  <--- 427 addMovieInfo <--- contentResolver-query");

        // Check presence or row/data
        if (mCursor.moveToFirst()==true) {

            // get the '_id' value
            int mColIndx = mCursor.getColumnIndex(MovieContract.MovieInfoEntry._ID);
            valueOf_Id = mCursor.getLong(mColIndx);
            //Log.d(LOG_TAG, "  <--- 435 addMovieInfo, YES IS A ROW EXIST.... ");

        } else {
            /*
                + MovieInfoEntry.COL_VOTE_AVERAGE   + " REAL NOT NULL, " //" REAL NOT NULL, "
                + MovieInfoEntry.COL_VOTE_COUNT     + " INTEGER NOT NULL, "//" INTEGER NOT NULL, "
             */
            //Log.d(LOG_TAG, "  ---> 461 addMovieInfo ---> contentResolver-insert");
            //  Uri insertUri = mContext.getContentResolver().insert(MovieContract.MovieInfoEntry.CONTENT_URI, cv);
            Uri insertUri = getContext().getContentResolver().insert(MovieContract.MovieInfoEntry.CONTENT_URI, contentValues);
            //Log.d(LOG_TAG, "  <--- 463 addMovieInfo <--- contentResolver-insert");

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            valueOf_Id = ContentUris.parseId(insertUri);
            //valueOf_Id = insertUri.getLastPathSegment();
        }

        mCursor.close();

        return valueOf_Id;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void addRatingTable(ContentValues contentValues, int arrayIndx, long rowId) {
        long ratingTableRow_Id;

        long movieId = contentValues.getAsLong(MovieContract.RatingEntry.COL_MV_ID);
        Cursor mCursor = getContext().getContentResolver().query(
                MovieContract.RatingEntry.CONTENT_URI,              // uri
                new String[]{MovieContract.RatingEntry._ID
                            //,MovieContract.RatingEntry.COL_KEY_ID
                            },                                      // projection
                MovieContract.RatingEntry.COL_MV_ID + "=?",         // selection
                new String[]{String.valueOf(movieId)},              // selectionArg
                null                                                // sort-order
        );
        ////*
        // there is such a movie_Id in table
        if (mCursor.moveToFirst()) {
            // get the '_id' value
            int mColIndx = mCursor.getColumnIndex(MovieContract.MovieInfoEntry._ID);
            ratingTableRow_Id = mCursor.getLong(mColIndx);

            // the movie_Id is in the correct table location/sequence
            if (arrayIndx == (int )ratingTableRow_Id) {
                // do nothing
            }
            else {
                Log.d(LOG_TAG, "aaaa addRatingTable  add Content Values");

                String selection = MovieContract.RatingEntry.TABLE_NAME + "." + MovieContract.RatingEntry._ID + "=?";

                int rowsUpdated = getContext().getContentResolver()
                        .update(MovieContract.RatingEntry.CONTENT_URI,
                                contentValues,
                                //cv,
                                selection,
                                new String[]{String.valueOf(arrayIndx)}
                        );
            }
        }
        // there is no such movie_Id in table, so we need to insert the info.
        else {
            Log.d(LOG_TAG, "aaaa addRatingTable ---  add Content Values");

            Uri rowsUpdated = getContext().getContentResolver()
                    .insert(MovieContract.RatingEntry.CONTENT_URI,
                            contentValues
                            //cv
                    );
        }

        mCursor.close();
        /// */
    }


    //+++++++++++++++++++++++++++++++++
    //+++ Add 'ACCOUNT' required by the 'Framework'
    //+++++++++++++++++++++++++++++++++
    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {

        // Get an instance of the Android account manager
        AccountManager
                accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name),
                                         context.getString(R.string.sync_account_type));

        Boolean noPasswordAndAccount = (accountManager.getPassword(newAccount) == null);
        // If the password doesn't exist, the account doesn't exist
        if ( noPasswordAndAccount ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            Boolean alreadyExist = !accountManager.addAccountExplicitly(newAccount, "", null);
            if (alreadyExist) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MoviesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }
    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }
    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

}
