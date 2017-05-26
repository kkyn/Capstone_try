package com.example.android.myproject_2.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
//import android.content.ContentUris;
//import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
//import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

//import com.example.android.myproject_2.BuildConfig;
import com.example.android.myproject_2.R;
import com.example.android.myproject_2.Utility;
//import com.example.android.myproject_2.data.MovieContract;

//import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.example.android.myproject_2.data.MovieContract.X_MovieReviewEntry;
//import com.example.android.myproject_2.data.MovieContract.X_MovieVideosEntry;

/*
 * Created by kkyin on 2/7/2016.
 */
public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    private final String LOG_TAG = MoviesSyncAdapter.class.getSimpleName();

    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    private static final int SYNC_INTERVAL = 60 * 180;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

//    public static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";
//    public static final String MOVIE_ = "movie";
//    public static final String VIDEOS_ = "videos";
//    public static final String REVIEWS_ = "reviews";
//    public static final String PARAM_API_KEY = "api_key";

    //+++++++++++++++++++++++++++++++++
    //+++ Add 'ACCOUNT' required by the 'Framework'
    //+++++++++++++++++++++++++++++++++
    // 1) The authority for the sync adapter's content provider
    // 2) An account type, in the form of a domain name
    // 3) The account name
    // 4) Instance name
    //+++++++++++++++++++++++++++++++++

    private Context mContext;

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

        //+++++++++++++++++

        Uri uri; //formUri_MovieInfo

        URL url;
        //+++++++++++++++++

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection = null;
        BufferedReader bufferedReader = null;

        // Will contain the raw JSON response as a string.
        String movieInfoInJsonStr;// = null;
//        Uri buildUri; // = null;

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
            //-----------------------------------------//

            //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
            //zzzzzzzzzzzzzzzzzzz BEGIN zzzzzzzzzzzzzzzzzzzzzzzzzzzzz
            //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

            /********************************************************/
            /******************* Movie Info ***********************/
            /********************************************************/
            // (1)
            uri = Utility.formUri_X_MovieInfo(mContext);
         // uri = formUri_X_MovieInfo();
            url = new URL(uri.toString());

            // (2)
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            // (3)
            InputStream
                    inputStream = httpUrlConnection.getInputStream();
            //StringBuffer stringBuffer = new StringBuffer();

            // (4). (4a), (4b)
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder
            stringBuffer = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line + "/n";
                stringBuffer.append(line);
                //stringBuffer.append(line + "\n");
            }

            // (5)
            movieInfoInJsonStr = stringBuffer.toString();

//xxyy            Log.d(LOG_TAG, "--------" + "sortBy ::: " + sortBy + "-----------");

            long[] mMovieIDs  = Utility.get_X_MovieInfoFromJson(movieInfoInJsonStr,  sortBy, mContext); // ????

            Log.d(LOG_TAG, "  <--- OUTSIDE getMovieInfoFromJson(); ---");


            /********************************************************/
            /******************* Movie Review ***********************/
            /********************************************************/

            Utility.get_MovieReviews(mContext, mMovieIDs);

            /*for (long movieId : mMovieIDs) {

                Log.d(LOG_TAG, "  <--- IN-SIDE getMovieInfoFromJson(); ---");

                // (1) build the Url
                buildUri = formUri_X_MovieReview(movieId);

                url = new URL(buildUri.toString());

                // (2) create request and open connection with the url
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setRequestMethod("GET");
                httpUrlConnection.connect();

                // (3) read the input-stream and convert the stream to string
                inputStream = httpUrlConnection.getInputStream();
                //StringBuffer stringBuffer = new StringBuffer();

                // (4) create an input-stream-reader
                // (4a) create a buffered-reader
                // (4b) create a string-buffer
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                stringBuffer = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                // (5) convert string-buffer to string
                String movieReviewsInJsonStr = stringBuffer.toString();

                getMovieReviews_FromJSON(movieReviewsInJsonStr, movieId);

            }*/
            /*****************************************************/
            /******************* Movie Video ***********************/
            /*****************************************************/
            Utility.get_MovieVideos(mContext, mMovieIDs);

            /*for (long movieId : mMovieIDs) {

                Log.d(LOG_TAG, "  <--- IN-SIDE getMovieInfoFromJson(); ---");

                // (1) build the Url
                buildUri = formUri_X_MovieVideo(movieId);

                url = new URL(buildUri.toString());

                // (2) create request and open connection with the url
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setRequestMethod("GET");
                httpUrlConnection.connect();

                // (3) read the input-stream and convert the stream to string
                inputStream = httpUrlConnection.getInputStream();
                //StringBuffer stringBuffer = new StringBuffer();

                // (4) create an input-stream-reader
                // (4a) create a buffered-reader
                // (4b) create a string-buffer
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                //String line;
                //   StringBuffer stringBuffer = new StringBuffer();
                //StringBuilder
                stringBuffer = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                // (5) convert string-buffer to string
                String mMovieVideoDataInJsonStr = stringBuffer.toString();

                getMovieVideoKeys_FromJSON(mMovieVideoDataInJsonStr, movieId);

            }*/
            //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
            //zzzzzzzzzzzzzzzzzzz  END  zzzzzzzzzzzzzzzzzzzzzzzzzzzzz
            //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);

        } catch (JSONException e) { // tky, JSONException linked to getMovieInfoFromJson()
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();

        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
    //zzzzzzzzzzzzzzzz Begin zzzzzzzzzzzzzzzzzzzzzzzzzzzz
    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
//    //private String[] getMovieVideoKeys_FromJSON(String movieVideoDataInJsonStr, long movieId) throws JSONException {
//    private void getMovieVideoKeys_FromJSON(String movieVideoDataInJsonStr, long movieId) throws JSONException {
//
//        final String RESULTS = "results";
//        final String KEY = "key";
//        String aMovieKey;
//
//        JSONObject movieVideoDataJsonObject = new JSONObject(movieVideoDataInJsonStr);
//        JSONArray resultJSONArray = movieVideoDataJsonObject.getJSONArray(RESULTS);
//        //==================
//        //String[] moviesVideoKey = new String[resultJSONArray.length()];
//        for (int i = 0; i < resultJSONArray.length(); i++) {
//
//            if (i == 0) {
//                JSONObject aMovieJSONObject = resultJSONArray.getJSONObject(i);
//
//                aMovieKey = aMovieJSONObject.getString(KEY);
//
//                ContentValues contentValues = new ContentValues();
//
//                contentValues.put(X_MovieVideosEntry.COL_VIDEO_KEY, aMovieKey);
//                contentValues.put(X_MovieVideosEntry.COL_MV_ID, movieId);
//
//                getContext().getContentResolver()
//                    .insert(X_MovieVideosEntry.CONTENT_URI, contentValues);
//            }
//        }
//
//    }

//    private void X_Add_AMovieVideo(ContentValues contentValues) {
//
//        //Uri rowsUpdated =
//                getContext().getContentResolver()
//                .insert(X_MovieVideosEntry.CONTENT_URI, contentValues);
//    }

//    private void getMovieReviews_FromJSON(String movieReviewsInJsonStr, long movieId) throws JSONException {
//
//        final String RESULTS = "results";
//        final String AUTHOR = "author";
//        final String CONTENT = "content";
//
//        JSONObject movieReviews_JSONObject = new JSONObject(movieReviewsInJsonStr);
//        JSONArray movieReviewsResults_JSONArray = movieReviews_JSONObject.getJSONArray(RESULTS);
//
//        for (int index = 0; index < movieReviewsResults_JSONArray.length(); index++) {
//
//            JSONObject aReview_JSONObject = movieReviewsResults_JSONArray.getJSONObject(index);
//
//            String movieReviewer = aReview_JSONObject.getString(AUTHOR);
//            String movieReviewContent = aReview_JSONObject.getString(CONTENT);
//
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(X_MovieReviewEntry.COL_MV_ID, movieId);
//        //    contentValues.put(X_MovieReviewEntry.COL_KEY_ID, movieId);
//            contentValues.put(X_MovieReviewEntry.COL_REVIEWER, movieReviewer);
//            contentValues.put(X_MovieReviewEntry.COL_REVIEWCONTENT, movieReviewContent);
//
//            //+++++++++++++++++++----------------
//
//            //+++++++++++++++++++----------------
//            X_Add_AMovieReview( contentValues );
//        }
//    }
//    private void X_Add_AMovieReview(ContentValues contentValues) {
//
//        //Uri rowsUpdated = mContext.getContentResolver()
//        Uri rowsUpdated = getContext().getContentResolver()
//                .insert(X_MovieReviewEntry.CONTENT_URI,
//                        contentValues
//                        //cv
//                );
//    }


    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
    //zzzzzzzzzzzzzzzz  End  zzzzzzzzzzzzzzzzzzzzzzzzzzzz
    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    private void addPopularTable(ContentValues contentvalues, int arrayIndx, long rowId) {
//        long popularTableRow_Id;
//
//        long movieId = contentvalues.getAsLong(MovieContract.PopularEntry.COL_MV_ID);
//
//        //    Cursor mCursor = mContext.getContentResolver()
//        Cursor mCursor = getContext().getContentResolver()
//                .query(
//                        MovieContract.PopularEntry.CONTENT_URI,         // uri
//                        new String[]{MovieContract.PopularEntry._ID}, //, PopularEntry.COL_KEY_ID},
//                                                                        // projection
//                        MovieContract.PopularEntry.COL_MV_ID + "=?",    // selection
//                        new String[]{String.valueOf(movieId)},          // selectionArg
//                        null                                            // sort-order
//                );
//
//        // there is such a movie_Id in table
//        if (mCursor.moveToFirst()==true) {
//            //---- get the '_id' value
//            // from the cursor, get the column index scalar value with the associated column name
//            int mColIndx = mCursor.getColumnIndex(MovieContract.MovieInfoEntry._ID);
//            // with the column index value, get the value residing in it.
//
//            popularTableRow_Id = mCursor.getLong(mColIndx);
//
//            // the movie_Id is in the correct table location/sequence
//            if (arrayIndx == (int)popularTableRow_Id) {
//                // do nothing
//            }
//            else {
//
//                String selection = MovieContract.PopularEntry.TABLE_NAME + "." + MovieContract.PopularEntry._ID + "=?";
//
//                //    int rowsUpdated = mContext.getContentResolver()
//                int rowsUpdated = getContext().getContentResolver()
//                        .update(MovieContract.PopularEntry.CONTENT_URI,
//                                contentvalues,
//                                //cv,
//                                selection,
//                                new String[]{String.valueOf(arrayIndx)}
//                        );
//
//            }
//        }
//        // there is no such movie_Id in table, so we need to insert the info.
//        else {
//
//            //Uri rowsUpdated = mContext.getContentResolver()
//            Uri rowsUpdated = getContext().getContentResolver()
//                    .insert(MovieContract.PopularEntry.CONTENT_URI,
//                            contentvalues
//                            //cv
//                    );
//        }
//
//        mCursor.close();
//    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    private long addMovieInfo(ContentValues contentValues, String sortBy) {
//
//        long valueOf_Id;
//
//        //    Cursor mCursor = mContext.getContentResolver()
//        Cursor mCursor = getContext().getContentResolver()
//                .query(
//                        MovieContract.MovieInfoEntry.CONTENT_URI,         // uri
//                        new String[]{MovieContract.MovieInfoEntry._ID},   // projection
//                        MovieContract.MovieInfoEntry.COL_MV_ID + "=?",    // selection
//                        new String[]{String.valueOf(contentValues.getAsLong(MovieContract.MovieInfoEntry.COL_MV_ID))},  // selectionArg
//                   //     new String[]{String.valueOf(movieInfo.mId)},  // selectionArg
//                        null                                            // sort order
//                );
//
//
//        // Check presence or row/data
//        if (mCursor.moveToFirst()==true) {
//
//            // get the '_id' value
//            int mColIndx = mCursor.getColumnIndex(MovieContract.MovieInfoEntry._ID);
//            valueOf_Id = mCursor.getLong(mColIndx);
//
//        } else {
//            /*
//                + MovieInfoEntry.COL_VOTE_AVERAGE   + " REAL NOT NULL, " //" REAL NOT NULL, "
//                + MovieInfoEntry.COL_VOTE_COUNT     + " INTEGER NOT NULL, "//" INTEGER NOT NULL, "
//             */
//            Uri insertUri = getContext().getContentResolver().insert(MovieContract.MovieInfoEntry.CONTENT_URI, contentValues);
//
//            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
//            valueOf_Id = ContentUris.parseId(insertUri);
//            //valueOf_Id = insertUri.getLastPathSegment();
//        }
//
//        mCursor.close();
//
//        return valueOf_Id;
//    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    private void addRatingTable(ContentValues contentValues, int arrayIndx, long rowId) {
//        long ratingTableRow_Id;
//
//        long movieId = contentValues.getAsLong(MovieContract.RatingEntry.COL_MV_ID);
//        Cursor mCursor = getContext().getContentResolver().query(
//                MovieContract.RatingEntry.CONTENT_URI,              // uri
//                new String[]{MovieContract.RatingEntry._ID
//                            //,MovieContract.RatingEntry.COL_KEY_ID
//                            },                                      // projection
//                MovieContract.RatingEntry.COL_MV_ID + "=?",         // selection
//                new String[]{String.valueOf(movieId)},              // selectionArg
//                null                                                // sort-order
//        );
//        ////*
//        // there is such a movie_Id in table
//        if (mCursor.moveToFirst()) {
//            // get the '_id' value
//            int mColIndx = mCursor.getColumnIndex(MovieContract.MovieInfoEntry._ID);
//            ratingTableRow_Id = mCursor.getLong(mColIndx);
//
//            // the movie_Id is in the correct table location/sequence
//            if (arrayIndx == (int )ratingTableRow_Id) {
//                // do nothing
//            }
//            else {
//                Log.d(LOG_TAG, "aaaa addRatingTable  add Content Values");
//
//                String selection = MovieContract.RatingEntry.TABLE_NAME + "." + MovieContract.RatingEntry._ID + "=?";
//
//                int rowsUpdated = getContext().getContentResolver()
//                        .update(MovieContract.RatingEntry.CONTENT_URI,
//                                contentValues,
//                                //cv,
//                                selection,
//                                new String[]{String.valueOf(arrayIndx)}
//                        );
//            }
//        }
//        // there is no such movie_Id in table, so we need to insert the info.
//        else {
//            Log.d(LOG_TAG, "aaaa addRatingTable ---  add Content Values");
//
//            Uri rowsUpdated = getContext().getContentResolver()
//                    .insert(MovieContract.RatingEntry.CONTENT_URI,
//                            contentValues
//                            //cv
//                    );
//        }
//
//        mCursor.close();
//        /// */
//    }
    ////////////////////////////////////////////////////////////
    /////////////////////// BEGIN //////////////////////////////
    ////////////////////////////////////////////////////////////

//
//    private Uri formUri_X_MovieVideo(long movieId) {
//
//        // (1) build the Url
//        Uri  buildUri = Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
//                .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI
//
//                .appendPath(MOVIE_  /* movie/ */ )
//                .appendPath(Long.toString(movieId)  /* ID/ */ )
//                .appendEncodedPath(VIDEOS_  /* videos? */ )
//                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
//                .build();
//
//        return buildUri;
//    }
//
//    private Uri  formUri_X_MovieReview(long movieId) {
//
//        // (1) build the Url
//        Uri buildUri = Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
//                .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI
//
//                .appendPath(MOVIE_ /* movie/ */)
//                .appendPath(Long.toString(movieId) /* ID/ */)
//                .appendEncodedPath(REVIEWS_ /* reviews? */)
//                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
//                .build();
//
//        return buildUri;
//    }
    ////////////////////////////////////////////////////////////
    ///////////////////////  END  //////////////////////////////
    ////////////////////////////////////////////////////////////

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
//    public static Account getSyncAccount(Context context) {
        private static Account getSyncAccount(Context context) {

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
    //public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
    private static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
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
