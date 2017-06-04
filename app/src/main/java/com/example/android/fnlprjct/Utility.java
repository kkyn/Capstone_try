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
import java.util.ArrayList;
import java.util.List;
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
    public static void get_MovieReviews(Context context, int[] movieIDArray) throws  IOException, JSONException{ //MalformedURLException,
 // public static void get_MovieReviews(Context context, long[] movieIDArray) throws  IOException, JSONException{ //MalformedURLException,

        Uri mUri;
        URL url;

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection; // = null;
        BufferedReader bufferedReader; // = null;

        InputStream inputStream;
        //StringBuilder stringBuffer;
        StringBuffer stringBuffer;

        Vector<ContentValues> vectorCV = new Vector<ContentValues>();

        if (movieIDArray.length > 0) {


            Log.d(LOG_TAG, "z z z z z z z z z z  " + movieIDArray.length + " y y y y y y y y y y y y");
            for (int movieId : movieIDArray) {

                // (1) build the Url ---- Begin ----
                mUri = Uri.parse(MOVIE_DB_BASE_URL);        // Creates a Uri from parsing the given encoded URI string
                Uri.Builder uriBuilder = mUri.buildUpon();  // Obtain a builder (Uri.Builder) representing an existing URI

                uriBuilder
                    .appendPath(MOVIE_)                   // appendPath postfix a '/', e.g. movie/
                    .appendPath(Integer.toString(movieId))    // appendPath postfix a '/', e.g. ID/
                    //    .appendPath(Long.toString(movieId))    // appendPath postfix a '/', e.g. ID/
                    .appendEncodedPath(REVIEWS_)          // appendEncodedPath postfix a '?', e.g.  reviews?
                    .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY);  // appendQueryParameter infix a '=', e.g. api_key=xxxxxx

                mUri = uriBuilder.build();
                //build the Url ---- End ----

                url = new URL(mUri.toString());

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
                }

                // (5) convert string-buffer to string
                String movieReviewsJsonStr = stringBuffer.toString();
                //movieReviewsInJsonStr

                //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
                final String RESULTS = "results";
                final String AUTHOR = "author";
                final String CONTENT = "content";

                JSONObject movieReviewsJSONObject = new JSONObject(movieReviewsJsonStr);
                JSONArray resultsJSONArray = movieReviewsJSONObject.getJSONArray(RESULTS);

                for (int index = 0; index < resultsJSONArray.length(); index++) {

                    JSONObject resultJSONObject = resultsJSONArray.getJSONObject(index);

                    String author = resultJSONObject.getString(AUTHOR);
                    String content = resultJSONObject.getString(CONTENT);

                    ContentValues cv = new ContentValues();

                    cv.put(MovieReviewEntry.COL_MV_ID, movieId);
                    cv.put(MovieReviewEntry.COL_REVIEWER, author);

                    cv.put(MovieReviewEntry.COL_REVIEWCONTENT, content);
                    // cv.put(MovieReviewEntry.COL_KEY_ID, movieId);

                    vectorCV.add(cv);
                }
                //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
            }

            ContentValues[] array_CV = new ContentValues[vectorCV.size()];
            vectorCV.toArray(array_CV);
            // -- or --
            //ContentValues[] arrayContentValues = Vctr.toArray(new ContentValues[Vctr.size()]);

            ContentResolver contentResolver = context.getContentResolver();

            int size = contentResolver.bulkInsert(MovieReviewEntry.CONTENT_URI, array_CV);
        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        if (vectorCV.size() > 0) {
//
//            ContentValues[] arrayCV = new ContentValues[vectorCV.size()];
//
//            vectorCV.toArray(arrayCV);
//
//            //-------------------
//            //
//            // context.getContentResolver().bulkInsert(MovieReviewEntry.CONTENT_URI, arrayCV);
//
//            //-------------------
//            Uri uri;
//            String[] projection = null;
//            String   selection ;
//            String[] selectionArg;
//            String   sortOrder = null;
//            Cursor   cursor;
//
//            for (ContentValues cv : arrayCV) {
//
//                //uri = MovieReviewEntry.buildUri_MovieReviewWithId(cv.getAsLong(MovieReviewEntry.COL_MV_ID));
//                uri = MovieReviewEntry.CONTENT_URI;
//
//                projection = new String[]{MovieContract.MovieReviewEntry._ID};
//
//                selection = MovieReviewEntry.COL_MV_ID + "=? AND " +
//                            MovieReviewEntry.COL_REVIEWER + "=?";
//
//                selectionArg = new String[]{String.valueOf(cv.getAsLong(MovieReviewEntry.COL_MV_ID)),
//                                            cv.getAsString(MovieReviewEntry.COL_REVIEWER)
//                                            };
//
//                cursor = context.getContentResolver().query(uri, projection, selection, selectionArg, sortOrder);
//
//                if (cursor != null) {
//                    if (!cursor.moveToFirst()) {
//                        context.getContentResolver().insert(MovieReviewEntry.CONTENT_URI, cv);
//                    }
//                }
//
//                cursor.close();
//            }
//            //--------------------
//        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    }
    /*****************************************************/
    /******************* Movie Video *********************/
    /*****************************************************/
    public static void get_MovieVideos(Context context, int[] movieIDArray) throws IOException, JSONException { //MalformedURLException,
        //   public static void get_MovieVideos(Context context, long[] mMovieIDs) throws  IOException, JSONException{ //MalformedURLException,

        //Uri mUri;
        URL url;
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection; // = null;
        BufferedReader bufferedReader; // = null;

        // Will contain the raw JSON response as a string.
//        String movieInfoInJsonStr;// = null;
        Uri mUri; // = null;

        InputStream inputStream;
        //StringBuilder stringBuffer;
        StringBuffer stringBuffer;

        //   Vector<ContentValues> vectorOfCV = new Vector<ContentValues>();
        Vector<ContentValues> vectorCV = new Vector<>();

        if (movieIDArray.length > 0) {
            for (int movieId : movieIDArray) {

                // (1) build the Url
                mUri =
                    Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
                        .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI

                        .appendPath(MOVIE_  /* movie/ */)
                        .appendPath(Integer.toString(movieId)  /* ID/ */)
                        //      .appendPath(Long.toString(movieId)  /* ID/ */)
                        .appendEncodedPath(VIDEOS_  /* videos? */)
                        .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                url = new URL(mUri.toString());

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
                }

                // (5) convert string-buffer to string
                String movieVideosJsonStr = stringBuffer.toString();

                //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
                final String RESULTS = "results";
                final String KEY = "key";
                String videoKey;

                JSONObject movieVideosJsonObject = new JSONObject(movieVideosJsonStr);
                JSONArray resultsJsonArray = movieVideosJsonObject.getJSONArray(RESULTS);
                //==================
                //String[] moviesVideoKey = new String[results_In_JSONArray.length()];
                for (int i = 0; i < resultsJsonArray.length(); i++) {

                    // Take the first Key value.
                    if (i == 0) {
                        JSONObject resultJsonObject = resultsJsonArray.getJSONObject(i);

                        videoKey = resultJsonObject.getString(KEY);

                        ContentValues cv = new ContentValues();

                        cv.put(MovieVideosEntry.COL_VIDEO_KEY, videoKey);
                        cv.put(MovieVideosEntry.COL_MV_ID, movieId);

                        /*context.getContentResolver()
                                .insert(MovieContract.MovieVideosEntry.CONTENT_URI, contentValues);*/
                        vectorCV.add(cv);
                    }
                }
                //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
            }
            ContentValues[] arrayCV = new ContentValues[vectorCV.size()];
            vectorCV.toArray(arrayCV);
            // -- or --
            //ContentValues[] arrayContentValues = Vctr.toArray(new ContentValues[Vctr.size()]);

            ContentResolver contentResolver = context.getContentResolver();

            int size = contentResolver.bulkInsert(MovieVideosEntry.CONTENT_URI, arrayCV);
        }

        // x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x
//        if (vectorCV.size() > 0) {
//            ContentValues[] arrayOfCV = new ContentValues[vectorCV.size()];
//            vectorCV.toArray(arrayOfCV);
//
//            //---------------------------------
//            //context.getContentResolver().bulkInsert(MovieVideosEntry.CONTENT_URI, arrayOfCV);
//            //-------------------
//            Uri uri;
//            String[] projection = null;
//            String selection;
//            String[] selectionArg;
//            String sortOrder = null;
//            Cursor cursor;
//
//            for (ContentValues cv : arrayOfCV) {
//
//                //uri = MovieVideosEntry.buildUri_MovieReviewWithId(cv.getAsLong(MovieVideosEntry.COL_MV_ID)); // ???
//                uri = MovieVideosEntry.CONTENT_URI;
//
//                projection = new String[]{MovieVideosEntry._ID};
//
//                selection = MovieVideosEntry.COL_MV_ID + "=?";
//
//                selectionArg = new String[]{String.valueOf(cv.getAsLong(MovieVideosEntry.COL_MV_ID))};
//
//                cursor = context.getContentResolver().query(uri, projection, selection, selectionArg, sortOrder);
//
//                if (cursor != null) {
//                    if (!cursor.moveToFirst()) {
//                        context.getContentResolver().insert(MovieVideosEntry.CONTENT_URI, cv);
//                    }
//                }
//
//                cursor.close();
//            }
//            //--------------------
//
//        }

    }

    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    //=================================================
    //private
    public static Uri formUri_4_MovieInfo(Context context) {

        final String DISCOVER_ = "discover";
        final String MOVIE_ = "movie";
        final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";

        final String PARAM_API_KEY = "api_key";
        final String PARAM_SORT_BY = "sort_by";
        final String PARAM_COUNTRY = "certification_country";
        final String PARAM_RELEASE_DATE = "primary_release_year";
        final String PARAM_VOTECOUNT_GRTR = "vote_count.gte";

        final String REF_YEAR = "2017";

        String sortBy = Utility.getPreferredSortSequence(context);

        // (1) build the Url ---Begin--------
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        uriBuilder
            .appendPath(DISCOVER_)       // postfix a '/', e.g. discover/
            .appendEncodedPath(MOVIE_)  // postfix a '?', e.g. movie?
            .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY) // e.g. api_key=xxxxxx
            .appendQueryParameter(PARAM_SORT_BY, sortBy)            // sort_by=xxx
            .appendQueryParameter(PARAM_COUNTRY, "US")              // certification_country=US
            .appendQueryParameter(PARAM_RELEASE_DATE, REF_YEAR)     // primary_release_year=2017
            .appendQueryParameter(PARAM_VOTECOUNT_GRTR, "50");      // vote_count.gte=50

        uri = uriBuilder.build();
        //-----End--------

        return uri;
    }

    /*****************************************************/
    /******************* Movie INFO **********************/
    /*****************************************************/
    // =================================================
    // tky add, called at MSyncAdapter

    public static long[] get_MovieInfoFromJson(JSONObject moviesJsonObj, String sortBy, Context context) throws JSONException {
    //public static long[] get_MovieInfoFromJson(String moviesJsonStr, String sortBy, Context context) throws JSONException {

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

        final String TMDB_BASE_URL  = "http://image.tmdb.org/t/p/";

        // W92 = "w92/"; W154 = "w154/";
        // W185 = "w185/"; W342 = "w342/";
        // W342 = "w342/"; W500 = "w500/";
        // W780 = "w780/"; ORIGINAL = "original/";

        final String W342 = "w342/";
        final String W780 = "w780/";
        final String W500 = "w500/";
        final String ORIGINAL = "original/";

        Log.d(LOG_TAG, "  ---> INSIDE  getMovieInfoFromJson(); ---");

        //-------------------
        JSONArray resultsJSONArray = moviesJsonObj.getJSONArray(RESULTS);

        //JSONObject movies_JSONObject = new JSONObject(moviesJsonStr);
        //JSONArray resultsJSONArray = movies_JSONObject.getJSONArray(RESULTS);
        //-------------------

        long[] movie_IDs = new long[resultsJSONArray.length()];

        //++++++++++++++++++
//        Vector<ContentValues> mVofCV = new Vector<ContentValues>(resultsJSONArray.length());
        Vector<ContentValues> Vctr = new Vector<ContentValues>();
        //++++++++++++++++++
        for (int i = 0; i < resultsJSONArray.length(); i++) {

            JSONObject aJSONObject = resultsJSONArray.getJSONObject(i);

            long   mvId          = aJSONObject.getLong(ID);
            String mvOrgTitle    = aJSONObject.getString(ORIGINAL_TITLE);
            String mvOverview    = aJSONObject.getString(OVERVIEW); // plot synopsis
            String mvVoteAverage = aJSONObject.getString(VOTE_AVERAGE);  // user rating
            long   mvVoteCount   = aJSONObject.getLong(VOTE_COUNT);  // user, sum number of votes
            String mvPopularity  = aJSONObject.getString(POPULARITY);
            String mvReleaseDate = aJSONObject.getString(RELEASE_DATE);

            String mvPosterPath   = TMDB_BASE_URL + /*W780*/ ORIGINAL /*W500*/ /*W342*/ + aJSONObject.getString(POSTER_PATH);
            String mvBackDropPath = TMDB_BASE_URL + W780 + aJSONObject.getString(BACKDROP_PATH); // movie poster image thumbnail
          //String mvBackDropPath = TMDB_BASE_URL + W500 + aJSONObject.getString(BACKDROP_PATH); // movie poster image thumbnail

            movie_IDs[i] = mvId;

            ContentValues cv = new ContentValues();

            cv.put(MovieInfoEntry.COL_MV_ID, mvId);    // Adds a value to the set.
            cv.put(MovieInfoEntry.COL_TITLE, mvOrgTitle);
            cv.put(MovieInfoEntry.COL_RELEASEDATE, mvReleaseDate);
            cv.put(MovieInfoEntry.COL_POPULARITY, mvPopularity);
            cv.put(MovieInfoEntry.COL_VOTE_AVERAGE, mvVoteAverage);
            cv.put(MovieInfoEntry.COL_VOTE_COUNT, mvVoteCount);
            cv.put(MovieInfoEntry.COL_FAVOURITES, 0);
            cv.put(MovieInfoEntry.COL_OVERVIEW, mvOverview);
            cv.put(MovieInfoEntry.COL_POSTERLINK, mvPosterPath);
            cv.put(MovieInfoEntry.COL_BACKDROP_PATH, mvBackDropPath);

            //Log.d(LOG_TAG, "---------- get_MovieInfoFromJson --------- ");

            if(!is_movieInDataBase(context, cv)){
                Vctr.add(cv);
                Log.d(LOG_TAG, "+++++++++++++++  ADD INTO VECTOR  +++++++++++++++");
            }
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            add_MovieInfo(cv, context);

            //Log.d(LOG_TAG, "---------- mValueId : " );
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        }
        return movie_IDs;
    }

    private static long add_MovieInfo(ContentValues contentvalues, Context context){

        long idNum = 0;

        Uri         uri            = MovieInfoEntry.CONTENT_URI;
        String[]    projection     = new String[]{MovieInfoEntry._ID};
        String      selection      = MovieInfoEntry.COL_MV_ID + "=?";
        String[]    selectionArg   = new String[]{String.valueOf(contentvalues.getAsLong(MovieInfoEntry.COL_MV_ID))};

        ContentResolver contentResolver = context.getContentResolver();

        Cursor mCursor = contentResolver.query(uri, projection, selection, selectionArg, null /*sortOrder*/ );

        if(mCursor!=null) {
            // Check presence or row/data
            if (mCursor.moveToFirst()) {
                // get the '_id' value
                int clmnIndx = mCursor.getColumnIndex(MovieInfoEntry._ID);
                idNum = mCursor.getLong(clmnIndx);
            } else {
                Uri mUri = contentResolver.insert(MovieInfoEntry.CONTENT_URI, contentvalues);

                idNum = ContentUris.parseId(mUri);
            }
            mCursor.close();

            Log.d(LOG_TAG, "---------- mValueId : " + idNum);
        }
       return idNum;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++
    public static int[] get_x_MovieInfoFromJson(JSONObject moviesJsonObj, String sortBy, Context context) throws JSONException {
    //public static Vector<ContentValues> get_x_MovieInfoFromJson(JSONObject moviesJsonObj, String sortBy, Context context) throws JSONException {
    // public static long[] get_MovieInfoFromJson(String moviesJsonStr, String sortBy, Context context) throws JSONException {

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

        final String TMDB_BASE_URL  = "http://image.tmdb.org/t/p/";

        // W92 = "w92/"; W154 = "w154/";
        // W185 = "w185/"; W342 = "w342/";
        // W342 = "w342/"; W500 = "w500/";
        // W780 = "w780/"; ORIGINAL = "original/";

        final String W342 = "w342/";
        final String W780 = "w780/";
        final String W500 = "w500/";
        final String ORIGINAL = "original/";

        Log.d(LOG_TAG, "  ---> INSIDE  getMovieInfoFromJson(); ---");

        //-------------------
        JSONArray resultsJSONArray = moviesJsonObj.getJSONArray(RESULTS);

        //JSONObject movies_JSONObject = new JSONObject(moviesJsonStr);
        //JSONArray resultsJSONArray = movies_JSONObject.getJSONArray(RESULTS);
        //-------------------

        List<String>  mvIdStringList = new ArrayList<>();

        Vector<ContentValues> Vctr = new Vector<ContentValues>();

        int jsonArrayLength = resultsJSONArray.length();

        for (int i = 0; i < jsonArrayLength; i++) {

            JSONObject aJSONObject = resultsJSONArray.getJSONObject(i);

            long   mvId          = aJSONObject.getLong(ID);
            String mvOrgTitle    = aJSONObject.getString(ORIGINAL_TITLE);
            String mvOverview    = aJSONObject.getString(OVERVIEW); // plot synopsis
            String mvVoteAverage = aJSONObject.getString(VOTE_AVERAGE);  // user rating
            long   mvVoteCount   = aJSONObject.getLong(VOTE_COUNT);  // user, sum number of votes
            String mvPopularity  = aJSONObject.getString(POPULARITY);
            String mvReleaseDate = aJSONObject.getString(RELEASE_DATE);

            String mvPosterPath   = TMDB_BASE_URL + /*W780*/ ORIGINAL /*W500*/ /*W342*/ + aJSONObject.getString(POSTER_PATH);
            String mvBackDropPath = TMDB_BASE_URL + W780 + aJSONObject.getString(BACKDROP_PATH); // movie poster image thumbnail
            //String mvBackDropPath = TMDB_BASE_URL + W500 + aJSONObject.getString(BACKDROP_PATH); // movie poster image thumbnail

            //movie_IDs[i] = (int)mvId;

            ContentValues cv = new ContentValues();

            cv.put(MovieInfoEntry.COL_MV_ID, mvId);
            cv.put(MovieInfoEntry.COL_TITLE, mvOrgTitle);
            cv.put(MovieInfoEntry.COL_RELEASEDATE, mvReleaseDate);
            cv.put(MovieInfoEntry.COL_POPULARITY, mvPopularity);
            cv.put(MovieInfoEntry.COL_VOTE_AVERAGE, mvVoteAverage);
            cv.put(MovieInfoEntry.COL_VOTE_COUNT, mvVoteCount);
            cv.put(MovieInfoEntry.COL_FAVOURITES, 0);
            cv.put(MovieInfoEntry.COL_OVERVIEW, mvOverview);
            cv.put(MovieInfoEntry.COL_POSTERLINK, mvPosterPath);
            cv.put(MovieInfoEntry.COL_BACKDROP_PATH, mvBackDropPath);

            //Log.d(LOG_TAG, "---------- get_MovieInfoFromJson --------- ");

            if (!is_movieInDataBase(context, cv)) {

                Vctr.add(cv);

                mvIdStringList.add(Integer.toString((int)mvId));

                Log.d(LOG_TAG, "+++++++++++++++  ADD INTO VECTOR  +++++++++++++++" + mvId);
            }
        }
//        Vector<String> v = new Vector<String>();  // sample code
//        String [] s = v.toArray(new String[v.size()]);

        ContentValues[] arrayContentValues = Vctr.toArray(new ContentValues[Vctr.size()]);

        ContentResolver contentResolver = context.getContentResolver();

        int size = contentResolver.bulkInsert(MovieInfoEntry.CONTENT_URI, arrayContentValues);

        Log.d(LOG_TAG, "z z z z z z z z z z SIZE :- " + size + " z z z z z z z z z z z");

        //-----------------------------------------------
        String[] mvIdStringArray = new String[mvIdStringList.size()];

        int[] mvIdIntArray = new int[mvIdStringList.size()];

        mvIdStringList.toArray(mvIdStringArray);

        for(int i=0; i < mvIdStringArray.length-1; i++ ){

            mvIdIntArray[i]= Integer.parseInt(mvIdStringArray[i]);
        }
        //----------------------------------------------

        return mvIdIntArray;
        //return movie_IDs;
    }

    private static boolean is_movieInDataBase(/*Uri uri,*/ Context context, ContentValues cv){

        //Uri         mUri            = uri; // e.g. MovieInfoEntry.CONTENT_URI;
        Uri         uri            = MovieInfoEntry.CONTENT_URI;
        String[]    projection     = new String[]{MovieInfoEntry._ID};
        String      selection      = MovieInfoEntry.COL_MV_ID + "=?";
        String[]    selectionArg   = new String[]{String.valueOf(cv.getAsLong(MovieInfoEntry.COL_MV_ID))};

        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArg, null /*sortOrder*/ );

        return (cursor!=null && cursor.moveToFirst());

        /*if(cursor!=null && cursor.moveToFirst()){
            return true;
        } else return false;*/
    }
}
