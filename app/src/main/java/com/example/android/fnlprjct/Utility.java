package com.example.android.fnlprjct;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.fnlprjct.data.MovieContract;
import com.example.android.fnlprjct.data.MovieContract.MovieReviewEntry;
import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;
import com.example.android.fnlprjct.data.MovieContract.MovieVideosEntry;

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

/*
 * Created by kkyin on 1/7/2016.
 */
public class Utility {
    public static String LOG_TAG = Utility.class.getSimpleName();

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/"; //"http://api.themoviedb.org/3/"
    private static final String MOVIE_ = "movie";
    private static final String VIDEOS_ = "videos";
    private static final String REVIEWS_ = "reviews";
    private static final String PARAM_API_KEY = "api_key";

    static Uri uri;
//    static Uri buildUri;
//    static URL url;

    public static String getPreferredSortSequence(Context context) {
    //public static String getPreferredSortSequence(Context context) {

        // get the file, SharedPreferences
        // Gets a SharedPreferences instance that points to the default file
        // that is used by the preference framework in the given context.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Retrieve a String value from the preferences.
        // getString(String key, String defValue)

        String mstring = context.getString(R.string.pref_movies_sort_key);
        String mstringDefault = context.getString(R.string.pref_movies_sortby_default_value);
    //    String mstring = String.valueOf((R.string.pref_sortmovies_key));

        String string = sharedPreferences.getString(mstring, mstringDefault);

        Log.d(LOG_TAG, "1111 getPreferredSortSequence -- actualSortSeq : " + string );

        return string;
    }

    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // /**************************************************/
    /******************* Movie Reviews *******************/
    /*****************************************************/
    public static void get_MovieReviews(Context context, long[] mMovieIDs) throws  IOException, JSONException{ //MalformedURLException,

        //Uri buildUri;
        URL url;
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection; // = null;
        BufferedReader bufferedReader; // = null;

        // Will contain the raw JSON response as a string.
//        String movieInfoInJsonStr;// = null;
        Uri buildUri; // = null;

        InputStream inputStream;
        //StringBuilder stringBuffer;
        StringBuffer stringBuffer;

    //    Vector<ContentValues> vectorOfCV = new Vector<ContentValues>();
        Vector<ContentValues> vectorOfCV = new Vector<>();

        for (long movieId : mMovieIDs) {

            // (1) build the Url
            buildUri =
                    Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
                            .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI

                            .appendPath(MOVIE_  /* movie/ */)
                            .appendPath(Long.toString(movieId)  /* ID/ */)
                            .appendEncodedPath(REVIEWS_  /* reviews? */)
                            .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                            .build();

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

            String line;
            //   StringBuffer
            stringBuffer = new StringBuffer();
            //StringBuilder
            //stringBuffer = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                line = line + "\n";
                stringBuffer.append(line);
            //  stringBuffer.append(line + "\n");
            }

            // (5) convert string-buffer to string
            String movieReviews_In_JsonStr = stringBuffer.toString();
            //movieReviewsInJsonStr

            //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
            final String RESULTS = "results";
            final String AUTHOR = "author";
            final String CONTENT = "content";

            JSONObject movieReviews_In_JSONObject = new JSONObject(movieReviews_In_JsonStr);
            JSONArray results_In_JSONArray = movieReviews_In_JSONObject.getJSONArray(RESULTS);

            for (int index = 0; index < results_In_JSONArray.length(); index++) {

                JSONObject result_In_JSONObject = results_In_JSONArray.getJSONObject(index);

                String author = result_In_JSONObject.getString(AUTHOR);
                String content = result_In_JSONObject.getString(CONTENT);

                ContentValues contentValues = new ContentValues();

                contentValues.put(MovieReviewEntry.COL_MV_ID, movieId);
                contentValues.put(MovieReviewEntry.COL_REVIEWER, author);
                contentValues.put(MovieReviewEntry.COL_REVIEWCONTENT, content);
                //    contentValues.put(MovieReviewEntry.COL_KEY_ID, movieId);

                vectorOfCV.add(contentValues);
            }
            //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
        }

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        if (vectorOfCV.size() > 0) {

            ContentValues[] arrayOfCV = new ContentValues[vectorOfCV.size()];

            vectorOfCV.toArray(arrayOfCV);

            //-------------------
            //
            // context.getContentResolver().bulkInsert(MovieReviewEntry.CONTENT_URI, arrayOfCV);

            //-------------------
            Uri uri;
            String[] projection = null;
            String   selection ;
            String[] selectionArg;
            String   sortOrder = null;
            Cursor   cursor;

            for (ContentValues cv : arrayOfCV) {

                //uri = MovieReviewEntry.buildUri_MovieReviewWithId(cv.getAsLong(MovieReviewEntry.COL_MV_ID));
                uri = MovieReviewEntry.CONTENT_URI;

                projection = new String[]{MovieContract.MovieReviewEntry._ID};

                selection = MovieReviewEntry.COL_MV_ID + "=? AND " +
                            MovieReviewEntry.COL_REVIEWER + "=?";

                selectionArg = new String[]{String.valueOf(cv.getAsLong(MovieReviewEntry.COL_MV_ID)),
                                            cv.getAsString(MovieReviewEntry.COL_REVIEWER)
                                            };

                cursor = context.getContentResolver().query(uri, projection, selection, selectionArg, sortOrder);

                if (cursor != null) {
                    if (!cursor.moveToFirst()) {
                        context.getContentResolver().insert(MovieReviewEntry.CONTENT_URI, cv);
                    }
                }

                cursor.close();
            }
            //--------------------
        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    }
    /*****************************************************/
    /******************* Movie Video *********************/
    /*****************************************************/
    public static void get_MovieVideos(Context context, long[] mMovieIDs) throws  IOException, JSONException{ //MalformedURLException,

        //Uri buildUri;
        URL url;
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection; // = null;
        BufferedReader bufferedReader; // = null;

        // Will contain the raw JSON response as a string.
//        String movieInfoInJsonStr;// = null;
        Uri buildUri; // = null;

        InputStream inputStream;
        //StringBuilder stringBuffer;
        StringBuffer stringBuffer;

     //   Vector<ContentValues> vectorOfCV = new Vector<ContentValues>();
        Vector<ContentValues> vectorOfCV = new Vector<>();

            for (long movieId : mMovieIDs) {

                // (1) build the Url
                buildUri =
                        Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
                            .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI

                            .appendPath(MOVIE_  /* movie/ */)
                            .appendPath(Long.toString(movieId)  /* ID/ */)
                            .appendEncodedPath(VIDEOS_  /* videos? */)
                            .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                            .build();

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

                String line;
                //   StringBuffer
                stringBuffer = new StringBuffer();
                //StringBuilder
                //stringBuffer = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    line = line + "\n";
                    stringBuffer.append(line);
                    //stringBuffer.append(line + "\n");
                }

                // (5) convert string-buffer to string
                String movieVideosIn_JsonStr = stringBuffer.toString();

                //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
                final String RESULTS = "results";
                final String KEY = "key";
                String aVideoKey;

                JSONObject movieVideosIn_JsonObject = new JSONObject(movieVideosIn_JsonStr);
                JSONArray results_In_JSONArray = movieVideosIn_JsonObject.getJSONArray(RESULTS);
                //==================
                //String[] moviesVideoKey = new String[results_In_JSONArray.length()];
                for (int i = 0; i < results_In_JSONArray.length(); i++) {

                    if (i == 0) {
                        JSONObject aResult_In_JSONObject = results_In_JSONArray.getJSONObject(i);

                        aVideoKey = aResult_In_JSONObject.getString(KEY);

                        ContentValues contentValues = new ContentValues();

                        contentValues.put(MovieVideosEntry.COL_VIDEO_KEY, aVideoKey);
                        contentValues.put(MovieVideosEntry.COL_MV_ID, movieId);

                        /*context.getContentResolver()
                                .insert(MovieContract.MovieVideosEntry.CONTENT_URI, contentValues);*/
                        vectorOfCV.add(contentValues);
                    }
                }
                //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
            }

            if (vectorOfCV.size() > 0) {
                ContentValues[] arrayOfCV = new ContentValues[vectorOfCV.size()];
                vectorOfCV.toArray(arrayOfCV);

                //---------------------------------
                //context.getContentResolver().bulkInsert(MovieVideosEntry.CONTENT_URI, arrayOfCV);
                //-------------------
                Uri uri;
                String[] projection = null;
                String   selection ;
                String[] selectionArg;
                String   sortOrder = null;
                Cursor   cursor;

                for (ContentValues cv : arrayOfCV) {

                    //uri = MovieVideosEntry.buildUri_MovieReviewWithId(cv.getAsLong(MovieVideosEntry.COL_MV_ID)); // ???
                    uri = MovieVideosEntry.CONTENT_URI;

                    projection = new String[]{MovieVideosEntry._ID};

                    selection = MovieVideosEntry.COL_MV_ID + "=?";

                    selectionArg = new String[]{String.valueOf(cv.getAsLong(MovieVideosEntry.COL_MV_ID))};

                    cursor = context.getContentResolver().query(uri, projection, selection, selectionArg, sortOrder);

                    if (cursor != null) {
                        if (!cursor.moveToFirst()) {
                            context.getContentResolver().insert(MovieVideosEntry.CONTENT_URI, cv);
                        }
                    }

                    cursor.close();
                }
                //--------------------

            }

    }

    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    //=================================================
    //private
    public static Uri formUri_MovieInfo(Context context) {

        final String DISCOVER_ = "discover";
        final String PARAM_SORT_BY = "sort_by";
        final String PARAM_COUNTRY = "certification_country";
        final String PARAM_RELEASE_DATE = "primary_release_year";
        final String PARAM_VOTECOUNT_GRTR = "vote_count.gte";

        final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";
        final String MOVIE_ = "movie";
        final String PARAM_API_KEY = "api_key";
        /* */

        String sortBy = Utility.getPreferredSortSequence(context);

        // (1) build the Url
        return
                Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
                .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI

                .appendPath(DISCOVER_ /* discover/ */)
                .appendEncodedPath(MOVIE_ /* movie? */ )

                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(PARAM_SORT_BY, sortBy) //.appendQueryParameter(PARAM_SORTBY, DESC)

                //.appendQueryParameter("with_genres", "18")
                .appendQueryParameter(PARAM_COUNTRY, "US")
                .appendQueryParameter(PARAM_RELEASE_DATE, "2016")
                .appendQueryParameter(PARAM_VOTECOUNT_GRTR, "50")
                .build();

    }

    // =================================================
    // tky add, called at MoviesSyncAdapter
    public static long[] get_MovieInfoFromJson(String moviesJsonStr, String sortBy, Context context) throws JSONException {

        // long rowId;
        final String RESULTS        = "results";
        final String ID             = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH    = "poster_path";
        final String BACKDROP_PATH  = "backdrop_path";  // movie poster image thumbnail
        final String OVERVIEW       = "overview";       // plot -- synopsis
        final String RELEASE_DATE   = "release_date";
        final String VOTE_AVERAGE   = "vote_average";   // user rating
        final String VOTE_COUNT     = "vote_count";
        final String POPULARITY     = "popularity";

        final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";

        // W92 = "w92/"; W154 = "w154/";
        // W185 = "w185/"; W342 = "w342/";
        // W342 = "w342/"; W500 = "w500/";
        // W780 = "w780/"; ORIGINAL = "original/";

        final String W780 = "w780/";
        final String W500 = "w500/";

        Log.d(LOG_TAG, "  ---> INSIDE  getMovieInfoFromJson(); ---");

        JSONObject movies_JSONObject = new JSONObject(moviesJsonStr);
        JSONArray results_JSONArray = movies_JSONObject.getJSONArray(RESULTS);

        long[] movie_IDs = new long[results_JSONArray.length()];

        //++++++++++++++++++
//        Vector<ContentValues> mVofCV = new Vector<ContentValues>(results_JSONArray.length());
        //++++++++++++++++++
        for (int i = 0; i < results_JSONArray.length(); i++) {

            JSONObject movieInfo_JSONObject = results_JSONArray.getJSONObject(i);

            long   mvId          = movieInfo_JSONObject.getLong(ID);
            String mvOrgTitle    = movieInfo_JSONObject.getString(ORIGINAL_TITLE);
            String mvOverview    = movieInfo_JSONObject.getString(OVERVIEW); // plot synopsis
            String mvVoteAverage = movieInfo_JSONObject.getString(VOTE_AVERAGE);  // user rating
            long   mvVoteCount   = movieInfo_JSONObject.getLong(VOTE_COUNT);  // user, sum number of votes
            String mvPopularity  = movieInfo_JSONObject.getString(POPULARITY);
            String mvReleaseDate = movieInfo_JSONObject.getString(RELEASE_DATE);

            String mvPosterPath   = TMDB_BASE_URL + W500 + movieInfo_JSONObject.getString(POSTER_PATH);
            String mvBackDropPath = TMDB_BASE_URL + W780 + movieInfo_JSONObject.getString(BACKDROP_PATH); // movie poster image thumbnail
          //String mvBackDropPath = TMDB_BASE_URL + W500 + movieInfo_JSONObject.getString(BACKDROP_PATH); // movie poster image thumbnail

            movie_IDs[i] = mvId;

            ContentValues movieInfo = new ContentValues();

            movieInfo.put(MovieInfoEntry.COL_MV_ID, mvId);
            movieInfo.put(MovieInfoEntry.COL_TITLE, mvOrgTitle);
            movieInfo.put(MovieInfoEntry.COL_RELEASEDATE, mvReleaseDate);
            movieInfo.put(MovieInfoEntry.COL_POPULARITY, mvPopularity);
            movieInfo.put(MovieInfoEntry.COL_VOTE_AVERAGE, mvVoteAverage);
            movieInfo.put(MovieInfoEntry.COL_VOTE_COUNT, mvVoteCount);
            movieInfo.put(MovieInfoEntry.COL_FAVOURITES, 0);
            movieInfo.put(MovieInfoEntry.COL_OVERVIEW, mvOverview);
            movieInfo.put(MovieInfoEntry.COL_POSTERLINK, mvPosterPath);
            movieInfo.put(MovieInfoEntry.COL_BACKDROP_PATH, mvBackDropPath);

            //Log.d(LOG_TAG, "---------- get_MovieInfoFromJson --------- ");

            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            add_MovieInfo(movieInfo, context);

            //Log.d(LOG_TAG, "---------- mValueId : " );
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        }
        return movie_IDs;
    }

    private static
    long add_MovieInfo(ContentValues movieInfo, Context context){

        long mValueOf_ID = 0;

        Uri uri                 = MovieInfoEntry.CONTENT_URI;
        String[] projection     = new String[]{MovieInfoEntry._ID};
        String selection        = MovieInfoEntry.COL_MV_ID + "=?";
        String[] selectionArg   = new String[]{String.valueOf(movieInfo.getAsLong(MovieInfoEntry.COL_MV_ID))};

        ContentResolver contentResolver = context.getContentResolver();

        Cursor mCursor = contentResolver.query(uri, projection, selection, selectionArg, null /*sortOrder*/ );

        if(mCursor!=null) {
            // Check presence or row/data
            if (mCursor.moveToFirst()) {
                // get the '_id' value
                int mColumnIndex = mCursor.getColumnIndex(MovieInfoEntry._ID);
                mValueOf_ID = mCursor.getLong(mColumnIndex);
            } else {
                Uri mUri = contentResolver.insert(MovieInfoEntry.CONTENT_URI, movieInfo);

                mValueOf_ID = ContentUris.parseId(mUri);
            }
            mCursor.close();

            Log.d(LOG_TAG, "---------- mValueId : " + mValueOf_ID);
        }
       return mValueOf_ID;
    }
}
