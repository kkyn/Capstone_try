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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;


public class Utility {
    public static String LOG_TAG = Utility.class.getSimpleName();

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/"; //"http://api.themoviedb.org/3/"
    private static final String DISCOVER_ = "discover";
    private static final String MOVIE_ = "movie";
    private static final String VIDEOS_ = "videos";
    private static final String REVIEWS_ = "reviews";

    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String PARAM_COUNTRY = "certification_country";
    private static final String PARAM_RELEASE_DATE = "primary_release_year";
    private static final String PARAM_VOTECOUNT_GRTR = "vote_count.gte";


    public static int getThisYearValue() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static String getPreferredSortSequence(Context context) {
        //public static String getPreferredSortSequence(Context context) {

        // get the file, SharedPreferences
        // Gets a SharedPreferences instance that points to the default file
        // that is used by the preference framework in the given context.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Retrieve a String value from the preferences.
        // getString(String key, String defValue)

        String mKey = context.getString(R.string.pref_key_movies_sortby);
        String mDefaultSequence = context.getString(R.string.pref_value_movies_sortby_default);

        String string = sharedPreferences.getString(mKey, mDefaultSequence);

        /*Log.d(LOG_TAG, "1111 getPreferredSortSequence -- actualSortSeq : " + string);*/

        return string;
    }

    public static String getPreferredYear(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String year = sharedPreferences.getString(context.getString(R.string.pref_key_year), context.getResources().getString(R.string.default_year));

        return year;
    }

    // /**************************************************/
    /******************* Movie Reviews *******************/
    /*****************************************************/
    public static void get_MovieReviews(Context context, int[] movieIDArray) throws IOException, JSONException { //MalformedURLException,
        // public static void get_MovieReviews(Context context, long[] movieIDArray) throws  IOException, JSONException{ //MalformedURLException,

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection; // = null;
        BufferedReader bufferedReader; // = null;

        Uri mUri;
        URL url;
        InputStream inputStream;
        StringBuffer stringBuffer;

        Vector<ContentValues> vectorCV = new Vector<ContentValues>();

        if (movieIDArray.length > 0) {

            Log.d(LOG_TAG, "z z z z z z z z z z  " + movieIDArray.length + " y y y y y y y y y y y y");
            for (int movieId : movieIDArray) {

                Log.d(LOG_TAG, "z z z z z z z z z z  " + movieId + " y y y y y y y y y y y y");
                // (1) build the Url
                mUri = formUri_4_MovieReview(movieId);

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

                stringBuffer = new StringBuffer();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    line = line + "\n";
                    stringBuffer.append(line);
                }

                // (5) convert string-buffer to string
                String reviewsJsonStr = stringBuffer.toString();

                final String RESULTS = "results";
                final String AUTHOR = "author";
                final String CONTENT = "content";

                JSONObject reviewsJSONObject = new JSONObject(reviewsJsonStr);

                JSONArray resultsJSONArray = reviewsJSONObject.getJSONArray(RESULTS);

                for (int index = 0; index < resultsJSONArray.length(); index++) {

                    JSONObject resultJSONObject = resultsJSONArray.getJSONObject(index);

                    String author = resultJSONObject.getString(AUTHOR);
                    String content = resultJSONObject.getString(CONTENT);

                    ContentValues cv = new ContentValues();

                    cv.put(MovieReviewEntry.COL_MOVIE_ID, movieId);
                    cv.put(MovieReviewEntry.COL_REVIEWER, author);

                    cv.put(MovieReviewEntry.COL_REVIEWCONTENT, content);
                    // cv.put(MovieReviewEntry.COL_KEY_ID, movieId);

                    Log.d(LOG_TAG, "  <--- Add cv ------------------ " + index);
                    vectorCV.add(cv);
                }
            }

            ContentValues[] arrayCV = new ContentValues[vectorCV.size()];
            vectorCV.toArray(arrayCV);
            // -- or --
            //ContentValues[] arrayContentValues = Vctr.toArray(new ContentValues[Vctr.size()]);

            ContentResolver contentResolver = context.getContentResolver();

            Log.d(LOG_TAG, "x x x x x x x x x x x x x x x " + MovieReviewEntry.CONTENT_URI.toString());

            int size = contentResolver.bulkInsert(MovieReviewEntry.CONTENT_URI, arrayCV);
        }
    }

    /*****************************************************/
    /******************* Movie Video *********************/
    /*****************************************************/
    public static void get_MovieVideos(Context context, int[] movieIDArray) throws IOException, JSONException { //MalformedURLException,

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection; // = null;
        BufferedReader bufferedReader; // = null;

        Uri uri;
        URL url;

        InputStream inputStream;
        StringBuffer stringBuffer;

        Vector<ContentValues> vectorCV = new Vector<>();

        if (movieIDArray.length > 0) {
            for (int movieId : movieIDArray) {

                // (1) build the Url
                uri = formUri_4_MovieVideo(movieId);

                url = new URL(uri.toString());

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
                        cv.put(MovieVideosEntry.COL_MOVIE_ID, movieId);

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

            Log.d(LOG_TAG, "y y y y y y y y y y y y y y y " + MovieVideosEntry.CONTENT_URI.toString());
            int size = contentResolver.bulkInsert(MovieVideosEntry.CONTENT_URI, arrayCV);
        }
    }

    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    //=================================================
    /***************************************************************/
    /******************* Form Uri Movie Video **********************/
    /***************************************************************/
    private static Uri formUri_4_MovieVideo(int movieId) {

        Uri uri =
            Uri.parse(MOVIE_DB_BASE_URL)        // creates a Uri which parses the given encoded URI string
                .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI

                .appendPath(MOVIE_)                     // appendPath postfix a '/', e.g. movie/
                .appendPath(Integer.toString(movieId))  // appendPath postfix a '/', e.g. ID/
                .appendEncodedPath(VIDEOS_)             // appendEncodedPath postfix a '?', e.g.  reviews?
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)// appendQueryParameter infix a '=', e.g. api_key=xxxxxx
                .build();
        return uri;
    }

    /***************************************************************/
    /******************* Form Uri Movie Reivews ********************/
    /***************************************************************/
    private static Uri formUri_4_MovieReview(int movieId) {

        Uri uri = Uri.parse(MOVIE_DB_BASE_URL);        // Creates a Uri from parsing the given encoded URI string
        Uri.Builder uriBuilder = uri.buildUpon();  // Obtain a builder (Uri.Builder) representing an existing URI

        uriBuilder
            .appendPath(MOVIE_)                   // appendPath postfix a '/', e.g. movie/
            .appendPath(Integer.toString(movieId))    // appendPath postfix a '/', e.g. ID/
            .appendEncodedPath(REVIEWS_)          // appendEncodedPath postfix a '?', e.g.  reviews?
            .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY);  // appendQueryParameter infix a '=', e.g. api_key=xxxxxx

        uri = uriBuilder.build();

        return uri;
    }

    /***************************************************************/
    /******************* Form Uri Movie Info ***********************/
    /***************************************************************/
    public static Uri formUri_4_MovieInfo(Context context) {

        String year = Utility.getPreferredYear(context);
        String sortBy = Utility.getPreferredSortSequence(context);

        // e.g. http://api.themoviedb.org/3/
        // (1) build the Url ---Begin--------
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        // e.g. http://api.themoviedb.org/3/discover/movie?api_key=xxxxxx
        //          &sort_by=xxx&certification_country=US&primary_release_year=2017&vote_count.gte=50
        uriBuilder
            .appendPath(DISCOVER_)       // postfix a '/', e.g. discover/
            .appendEncodedPath(MOVIE_)  // postfix a '?', e.g. movie?
            .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY) // e.g. api_key=xxxxxx
            .appendQueryParameter(PARAM_SORT_BY, sortBy)            // sort_by=xxx
            .appendQueryParameter(PARAM_COUNTRY, context.getString(R.string.certification_country))              // certification_country=US

            .appendQueryParameter(PARAM_RELEASE_DATE, year)             // primary_release_year=2017
            .appendQueryParameter(PARAM_VOTECOUNT_GRTR, context.getString(R.string.vote_count_gte));      // vote_count.gte=50

        uri = uriBuilder.build();

        //-----End--------

        return uri;
    }


    /*********************************************************/
    /******************* Get Movie Info  *********************/
    /*********************************************************/
    // tky add, called at MSyncAdapter
    //-- public static int[] getMovieInfoFromJson(JSONObject moviesJsonObj, String sortBy, Context context) throws JSONException {
    //public static Vector<ContentValues> getMovieInfoFromJson(JSONObject moviesJsonObj, String sortBy, Context context) throws JSONException {
    //public static long[] get_MovieInfoFromJson(String moviesJsonStr, String sortBy, Context context) throws JSONException {

    public static int[] getMovieInfoFromJson(String moviesJsonStr, String sortBy, Context context) throws JSONException {

        // long rowId;
        final String RESULTS = "results";
        final String ID = "id";

        final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";
        final String POSTER_PATH = "poster_path";
        final String BACKDROP_PATH = "backdrop_path";  // movie poster image thumbnail

        // W92 = "w92/"; W154 = "w154/";
        // W185 = "w185/"; W342 = "w342/";
        // W342 = "w342/"; W500 = "w500/";
        // W780 = "w780/"; ORIGINAL = "original/";

        final String W342 = "w342/";
        final String W780 = "w780/";
        final String W500 = "w500/";
        final String ORIGINAL = "original/";

        Log.d(LOG_TAG, "  ---> INSIDE  getMovieInfoFromJson(); ---");

        String searchYearBy = Utility.getPreferredYear(context);
        //-------------------
        //-- JSONArray resultsJSONArray = moviesJsonObj.getJSONArray(RESULTS);

        JSONObject movies_JSONObject = new JSONObject(moviesJsonStr);
        JSONArray resultsJSONArray = movies_JSONObject.getJSONArray(RESULTS);
        //-------------------

        List<String> mvIdStringList = new ArrayList<>();

        Vector<ContentValues> Vctr = new Vector<ContentValues>();

        int jsonArrayLength = resultsJSONArray.length();

        for (int i = 0; i < jsonArrayLength; i++) {

            JSONObject aJSONObject = resultsJSONArray.getJSONObject(i);

            long mvId = aJSONObject.getLong(ID);

            String originalTitle = aJSONObject.getString(MovieInfoEntry.COL_ORIGINAL_TITLE);
            String overview = aJSONObject.getString(MovieInfoEntry.COL_OVERVIEW); // plot synopsis
            String voteAverage = aJSONObject.getString(MovieInfoEntry.COL_VOTE_AVERAGE);  // user rating
            long voteCount = aJSONObject.getLong(MovieInfoEntry.COL_VOTE_COUNT);  // user, sum number of votes
            String popularity = aJSONObject.getString(MovieInfoEntry.COL_POPULARITY);
            String releaseDate = aJSONObject.getString(MovieInfoEntry.COL_RELEASE_DATE);

            String posterLink = TMDB_BASE_URL + /*W780*/ ORIGINAL /*W500*/ /*W342*/ + aJSONObject.getString(POSTER_PATH);
            String backdropLink = TMDB_BASE_URL + W780 + aJSONObject.getString(BACKDROP_PATH); // movie poster image thumbnail
            //String backdropLink = TMDB_BASE_URL + W500 + aJSONObject.getString(BACKDROP_PATH); // movie poster image thumbnail

            //movie_IDs[i] = (int)mvId;

            ContentValues cv = new ContentValues();

            cv.put(MovieInfoEntry.COL_MOVIE_ID, mvId);
            cv.put(MovieInfoEntry.COL_ORIGINAL_TITLE, originalTitle);
            cv.put(MovieInfoEntry.COL_RELEASE_DATE, releaseDate);

            cv.put(MovieInfoEntry.COL_YEAR, searchYearBy);
            cv.put(MovieInfoEntry.COL_POPULARITY, popularity);
            cv.put(MovieInfoEntry.COL_VOTE_AVERAGE, voteAverage);
            cv.put(MovieInfoEntry.COL_VOTE_COUNT, voteCount);
            cv.put(MovieInfoEntry.COL_FAVOURITES, 0);
            cv.put(MovieInfoEntry.COL_OVERVIEW, overview);
            cv.put(MovieInfoEntry.COL_POSTERLINK, posterLink);
            cv.put(MovieInfoEntry.COL_BACKDROPLINK, backdropLink);

            //Log.d(LOG_TAG, "---------- get_MovieInfoFromJson --------- ");

            if (!is_movieInDataBase(context, cv)) {

                Vctr.add(cv);

                mvIdStringList.add(Integer.toString((int) mvId));

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

        for (int i = 0; i < mvIdStringArray.length; i++) {

            mvIdIntArray[i] = Integer.parseInt(mvIdStringArray[i]);
        }
        //----------------------------------------------

        return mvIdIntArray;
    }

    private static boolean is_movieInDataBase(/*Uri uri,*/ Context context, ContentValues cv) {

        //Uri  mUri  = uri; // e.g. MovieInfoEntry.CONTENT_URI;
        Uri uri = MovieInfoEntry.CONTENT_URI;
        String[] projection = new String[]{MovieInfoEntry._ID};
        String selection = MovieInfoEntry.COL_MOVIE_ID + "=?";
        String[] selectionArg = new String[]{String.valueOf(cv.getAsLong(MovieInfoEntry.COL_MOVIE_ID))};

        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArg, null /*sortOrder*/);

        return (cursor != null && cursor.moveToFirst());

        /*if(cursor!=null && cursor.moveToFirst()){
            return true;
        } else return false;*/
    }
}
