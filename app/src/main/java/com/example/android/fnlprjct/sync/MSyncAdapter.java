package com.example.android.fnlprjct.sync;

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
import com.example.android.fnlprjct.R;
import com.example.android.fnlprjct.Utility;
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

/*
 * Created by kkyin on 2/7/2016.
 */
public class MSyncAdapter extends AbstractThreadedSyncAdapter {

    private final String LOG_TAG = MSyncAdapter.class.getSimpleName();

    // Interval at which to sync with the server, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    private static final int SYNC_INTERVAL = 60 * 720;
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

    public MSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

//? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ?
//        String sortBy = Utility.getPreferredSortSequence(mContext);
//
//        // (1)
//        Uri uri = Utility.formUri_4_MovieInfo(mContext);
//        String mUrl = uri.toString();
//
//        //---------------------------------
//        // (1) Create a new 'futureRequest'
//        // (2) Create a new request, with 'futureRequest' as one of the arguments.
//        // (3) Get an instance of the requestQ.
//        // (4) Add (2) into (3) -or- Place the new request into the requestQ
//        //---------------------------------
//        // (1)
//        final RequestFuture<JSONObject> futureRequest = RequestFuture.newFuture();
//        // (2)
//          JsonObjectRequest jsnObjctRqst = new JsonObjectRequest(mUrl, null, futureRequest, futureRequest);
//        //JsonObjectRequest jsnObjctRqst = new JsonObjectRequest(Request.Method.GET, mUrl, new JSONObject(), futureRequest, futureRequest);
//        // (3)
//        MyRqstQ myRqstQ = MyRqstQ.getInstance(mContext.getApplicationContext());
//        RequestQueue requestQ = myRqstQ.getRequestQueue();
//        //RequestQueue requestQ =
//        //    MyRqstQ.getInstance(mContext.getApplicationContext()).getRequestQueue();
//        // (4)
//        requestQ.add(jsnObjctRqst);
//
//        Log.d(LOG_TAG, "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
//        try {
//
//            JSONObject response = futureRequest.get(10, TimeUnit.SECONDS);
//            // JSONObject response = futureRequest.get(); // this will block
//            Log.d(LOG_TAG, "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
//
//            int[] mMovieIDs = Utility.getMovieInfoFromJson(response, sortBy, mContext);
//            //long[] mMovieIDs = Utility.get_MovieInfoFromJson(response, sortBy, mContext); // ????
//            Log.d(LOG_TAG, "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
//
//            // ***************** Movie Review **********************
//            Utility.get_MovieReviews(mContext, mMovieIDs);
//
//            // ***************** Movie Video ********************
//            Utility.get_MovieVideos(mContext, mMovieIDs);
//
//        } catch (InterruptedException e) {
//            // exception handling
//            e.printStackTrace();
//            Log.d(LOG_TAG, "++++ InterruptedException ++++");
//            //Toast.makeText(mContext,"++++ InterruptedException ++++", Toast.LENGTH_LONG).show();
//        } catch (ExecutionException e) {
//            // exception handling
//            e.printStackTrace();
//            Log.d(LOG_TAG, "++++ ExecutionException ++++");
//            //Toast.makeText(mContext,"++++ ExecutionException ++++", Toast.LENGTH_LONG).show();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//            Log.d(LOG_TAG, "++++ TimeoutException ++++");
//            //Toast.makeText(mContext,"++++ TimeoutException ++++", Toast.LENGTH_LONG).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ? ?

        //-----------------------------------------
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//        String sortBy = sharedPreferences.getString(mContext.getString(R.string.pref_sortmovies_key), mContext.getString(R.string.pref_sortmovies_default_value));
        //-----------------------------------------
        String sortBy = Utility.getPreferredSortSequence(mContext);
        //-----------------------------------------

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection = null;
        BufferedReader bufferedReader = null;

        // Will contain the raw JSON response as a string.
        String movieInfoInJsonStr;
        Uri uri;
        URL url;

        // (1) build the url,
        // (2) create request and open connection with the url
        // (3) read the input-stream and convert the stream to string
        // (4) create an input-stream-reader
        // (4a) create a buffered-reader
        // (5) convert string-buffer to string

        // (6) convert string from(5) to JSONObject
        // (7) return elements from JSONOject to method caller e.g. getIdfromJson

        // get the api result from moviedb.org

        Log.d(LOG_TAG, "******************************************");
        try {
            //----------------------------------------- added 8th August 2 am
            //            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            //            String sortBy = sharedPreferences.getString(mContext.getString(R.string.pref_sortmovies_key), mContext.getString(R.string.pref_sortmovies_default_value));
            //-----------------------------------------//

            // ***************** Movie Info **********************
            // ***************************************************
            // (1)
            uri = Utility.formUri_4_MovieInfo(mContext);
            url = new URL(uri.toString());
            // (2)
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();
            // (3)
            InputStream inputStream = httpUrlConnection.getInputStream();
            //StringBuffer stringBuffer = new StringBuffer();

            // (4). (4a), (4b)
            // https://developer.android.com/reference/java/io/InputStreamReader.html
            // https://developer.android.com/reference/java/io/BufferedReader.html
            // An InputStreamReader is a bridge from byte streams to character streams.
            // BufferedReader :- Reads text from a character-input stream, buffering characters so as to
            // provide for the efficient reading of characters, arrays, and lines.
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // https://developer.android.com/reference/java/lang/StringBuilder.html
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line + "/n";
                stringBuilder.append(line);
            }

            // (5)
            movieInfoInJsonStr = stringBuilder.toString();

            Log.d(LOG_TAG, "  <--- INTO getMovieInfoFromJson() ------------------");
            //long[] mMovieIDs = Utility.get_MovieInfoFromJson(movieInfoInJsonStr, sortBy, mContext); // ????

            int[] mMovieIDs = Utility.getMovieInfoFromJson(movieInfoInJsonStr, sortBy, mContext); // ????
            //int[] mMovieIDs = Utility.getMovieInfoFromJson(response, sortBy, mContext);


            // ***************** Movie Review ********************
            Log.d(LOG_TAG, "  <--- INTO get_MovieReviews() ------------------");
            Utility.get_MovieReviews(mContext, mMovieIDs);

            // ***************** Movie Video *********************
            Log.d(LOG_TAG, "  <--- INTO get_MovieVideos() ------------------");
            Utility.get_MovieVideos(mContext, mMovieIDs);

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

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
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
    // https://developer.android.com/reference/android/content/Context.html
    // https://developer.android.com/reference/android/accounts/Account.html
    // https://developer.android.com/reference/android/accounts/AccountManager.html
    private static Account getSyncAccount(Context context) {

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // Get an instance of the Android account manager
        AccountManager accntMngr = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // null if there is no password or if the account doesn't exist
        Boolean noPasswordAndAccount = (accntMngr.getPassword(newAccount) == null);

        // If the password doesn't exist or the account doesn't exist
        if (noPasswordAndAccount) {

            // Add the account and account type, no password or user data
            // If successful, return the Account object, otherwise report an error.
            // Adds an account directly to the AccountManager.
            // True if the account was successfully added,
            // false if the account already exists, the account is null, or another error occurs.
            Boolean alreadyExist = !accntMngr.addAccountExplicitly(newAccount, "", null);

            if (alreadyExist) {
                return null;
            }
            // If you don't set android:syncable="true" in
            // in your <provider> element in the manifest,
            // then call ContentResolver.setIsSyncable(account, AUTHORITY, 1) here.
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    // Called after adding a new-account
    private static void onAccountCreated(Account newAccount, Context context) {

        // Since we've created an account
        // After created an account, now configure-periodic-sync
        configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        // Without calling setSyncAutomatically, our periodic sync will not be enabled.
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        // Finally, let's do a sync to get things started
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

        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    // https://developer.android.com/reference/android/content/SyncRequest.Builder.html
    // https://developer.android.com/reference/android/content/ContentResolver.html
    private static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {

        // calling getSyncAccount here should get an already existing account
        Account account = getSyncAccount(context);

        String authority = context.getString(R.string.content_authority);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // we can enable inexact timers in our periodic sync
            SyncRequest.Builder builder = new SyncRequest.Builder();

            SyncRequest request = builder.syncPeriodic(syncInterval, flexTime)
                                        .setSyncAdapter(account, authority)
                                        .setExtras(new Bundle())
                                        .build();

            // Start an asynchronous sync operation.
            ContentResolver.requestSync(request);

        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);

            //---------------------------------------------
            // tky add, July17 2017
            //Utility.BroadcastMessage();
            //Utility.BroadcastMessage(getAppContext());
            /*Intent intent = new Intent();
                   intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            getAppContext().sendBroadcast(intent);*/
            //getAppContext().sendBroadcast(intent);
            //---------------------------------------------


        }
    }

}
