package com.example.android.myproject_2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kkyin on 3/11/2015.
 */
public class FetchMoviesDb_AsyncTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMoviesDb_AsyncTask.class.getSimpleName();

    ///////////////////////////////////////////////////
    private Context mContext;

    public FetchMoviesDb_AsyncTask(Context context) {

        this.mContext = context;
    }

    ///////////////////////////////////////////
    @Override
    protected Void doInBackground(String... params) {
    //  protected MoviesSelectedInfo[] doInBackground(String... params) { // tky, to remove
        if (params.length == 0) {
            return null;
        }
        String sortBy = params[0];

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

            if (inputStream == null) {
                return null;
            }

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
            if (stringBuffer.length() == 0) {
                return null;
            }

            // (5) convert string-buffer to string
            movieInfoInJsonStr = stringBuffer.toString();

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
        return null;
    }
    private MoviesSelectedInfo[] getMovieInfoFromJson(String moviesJsonStr, String sortBy) throws JSONException {
        return null;
    }
//    private MoviesSelectedInfo[] getMovieInfoFromJson(String moviesJsonStr, String sortBy) throws JSONException {
//    //private MoviesSelectedInfo[] getMovieInfoFromJson(String moviesJsonStr) throws JSONException {
//
//        long rowId; // = 0L;
//        //long rowId = 0L;
//        //String rowId = "";
//        final String RESULTS = "results";
//        final String ID = "id";
//        final String ORIGINAL_TITLE = "original_title";
//        final String POSTER_PATH = "poster_path";
//        final String BACKDROP_PATH = "backdrop_path"; // movie poster image thumbnail
//        final String OVERVIEW = "overview";     // plot synopsis
//        final String RELEASE_DATE = "release_date";
//        final String VOTE_AVERAGE = "vote_average"; // user rating
//        final String VOTE_COUNT = "vote_count";
//        final String POPULARITY = "popularity";
//
//        final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";
//        // W92 = "w92/"; W154 = "w154/";
//        // W185 = "w185/"; W342 = "w342/";
//        // W342 = "w342/"; W500 = "w500/";
//        // W780 = "w780/"; ORIGINAL = "original/";
//        //    final String W780 = "w780/";
//        final String W500 = "w500/";
//
//        Log.d(LOG_TAG, "  ---> INSIDE  getMovieInfoFromJson(); ---");
//
//        JSONObject moviesJson = new JSONObject(moviesJsonStr);
//        JSONArray resultArray = moviesJson.getJSONArray(RESULTS);
//
//        MoviesSelectedInfo[] myMovSlctdInfo = new MoviesSelectedInfo[resultArray.length()];
//
//        for (int i = 0; i < resultArray.length(); i++) {
//
//            JSONObject movieInfoJson = resultArray.getJSONObject(i);
//
//            /// tky, ? where to add method: addMovieInfo(movieInfoJson, params[0]);
//            ///
//            long   mvId          = movieInfoJson.getLong(ID);
//            String mvOrgTitle    = movieInfoJson.getString(ORIGINAL_TITLE);
//            String mvOverview    = movieInfoJson.getString(OVERVIEW); // plot synopsis
//            String mvVoteAverage = movieInfoJson.getString(VOTE_AVERAGE);  // user rating
//            long   mvVoteCount   = movieInfoJson.getLong(VOTE_COUNT);  // user, sum number of votes
//            String mvPopularity  = movieInfoJson.getString(POPULARITY);
//            String mvReleaseDate = movieInfoJson.getString(RELEASE_DATE);
//
//            String mvPosterPath   = TMDB_BASE_URL + W500 + movieInfoJson.getString(POSTER_PATH);
//            String mvBackDropPath = TMDB_BASE_URL + W500 + movieInfoJson.getString(BACKDROP_PATH); // movie poster image thumbnail
//
//            myMovSlctdInfo[i] = new MoviesSelectedInfo(
//                mvId, mvOrgTitle,
//                mvOverview, mvVoteAverage, mvPopularity,
//                mvReleaseDate,  mvVoteCount,
//                mvPosterPath, mvBackDropPath);
//
//                /*Log.v(LOG_TAG, "Movie: " + myMovSlctdInfo[i].mOriginalTitle + " " + myMovSlctdInfo[i].mVoteAverage ); //+ " " + myMovSlctdInfo[i].mPoster);*/
//        }
//            /*for (MoviesSelectedInfo s : myMovSlctdInfo) {
//                Log.v(LOG_TAG, "Movie: " + s.mOriginalTitle + " " + s.mVoteAverage + " " + s.mPoster);
//            }*/
//
//            /*Log.v(LOG_TAG, "Movie: " + urladdress);*/
//
//        //====================
//
//        for (int i = 0; i < resultArray.length(); i++) {
//
//            //Log.d(LOG_TAG, "  ---> 273 addMovieInfo-TABLE >1>1>1>1>1>1>1>1>>");
//
//            rowId = addMovieInfo(myMovSlctdInfo[i], sortBy);
//            // Log.d(LOG_TAG, "  xxxx addMovieInfo/after add Content Values");
//            //Log.d(LOG_TAG, "  <--- 277 addMovieInfo-TABLE <1<1<1<1<1<1<1<1<<  rowId = " + rowId);
//
//            if (sortBy.equals("popularity.desc") ) {
//                Log.d(LOG_TAG, "  === " + sortBy + "...  rowId = " + rowId);
//                ///Log.d(LOG_TAG, "  ---> 282 addPopular-TABLE   >2>2>2>2>2>2>2>2>>");
//
//                addPopularTable(myMovSlctdInfo[i], i + 1, rowId);
//
//                //Log.d(LOG_TAG, "  <--- 286 addPopular-TABLE   <2<2<2<2<2<2<2<2<<  rowId = " + rowId);
//            } else {
//                Log.d(LOG_TAG, "  === " + sortBy + "...  rowId = " + rowId);
//               // Toast.makeText(mContext, "  === " + sortBy + "... rowId =" + rowId, Toast.LENGTH_LONG).show();
//                addRatingTable(myMovSlctdInfo[i], i + 1, rowId);
//            }
//        }
//        return myMovSlctdInfo;
//    }
//
//    private void addPopularTable(MoviesSelectedInfo movieInfo, int arrayIndx, long rowId) {
//        long popularTableRow_Id;
//
//        //Log.d(LOG_TAG, "  ---> 355 addPopular ---> contentResolver-query" + "----" + String.valueOf(movieInfo.mId));
//        Cursor mCursor = mContext.getContentResolver()
//                        .query(
//                            PopularEntry.CONTENT_URI,
//                            new String[]{PopularEntry._ID}, //, PopularEntry.COL_KEY_ID},   // projection
//                            PopularEntry.COL_MV_ID + "=?",          // selection
//                            new String[]{String.valueOf(movieInfo.mId)}, // selectionArg
//                            null
//                        );
//
//        //Log.d(LOG_TAG, "  <--- 365 addPopular <--- contentResolver-query");
//
//        // there is such a movie_Id in table
//        if (mCursor.moveToFirst()==true) {
//            // get the '_id' value
//            int mColIndx = mCursor.getColumnIndex(MovieContract.MovieInfoEntry._ID);
//            popularTableRow_Id = mCursor.getLong(mColIndx);
//
//            // the movie_Id is in the correct table location/sequence
//            if (arrayIndx == (int)popularTableRow_Id) {
//                // do nothing
//            }
//            else {
//                ContentValues cv = new ContentValues();
//                cv.put(PopularEntry.COL_MV_ID, movieInfo.mId);
//                cv.put(PopularEntry.COL_TITLE, movieInfo.mOriginalTitle);
//                cv.put(PopularEntry.COL_POSTER, movieInfo.mPoster);
//                cv.put(PopularEntry.COL_KEY_ID, rowId);
//
//                String selection = PopularEntry.TABLE_NAME + "." + PopularEntry._ID + "=?";
//
//                //Log.d(LOG_TAG, "  ---> 387 addPopular ---> contentResolver-update");
//
//                int rowsUpdated = mContext.getContentResolver()
//                                    .update(PopularEntry.CONTENT_URI,
//                                            cv,
//                                            selection,
//                                            new String[]{String.valueOf(arrayIndx)}
//                                    );
//
//                //Log.d(LOG_TAG, "  <--- 395 addPopular <--- contentResolver-update");
//            }
//        }
//        // there is no such movie_Id in table, so we need to insert the info.
//        else {
//            ContentValues cv = new ContentValues();
//            cv.put(PopularEntry.COL_MV_ID, movieInfo.mId);
//            cv.put(PopularEntry.COL_TITLE, movieInfo.mOriginalTitle);
//            cv.put(PopularEntry.COL_POSTER, movieInfo.mPoster);
//            cv.put(PopularEntry.COL_KEY_ID, rowId);
//
//            //Log.d(LOG_TAG, "  ---> 406 addPopular ---> contentResolver-insert");
//
//            Uri rowsUpdated = mContext.getContentResolver()
//                                .insert(PopularEntry.CONTENT_URI,
//                                        cv
//                                );
//
//            //Log.d(LOG_TAG, "  <--- 412 addPopular <--- contentResolver-insert");
//        }
//
//        mCursor.close();
//    }
//
//    //private void addMovieInfo(MoviesSelectedInfo movieInfo, String sortBy) {
//    private long addMovieInfo(MoviesSelectedInfo movieInfo, String sortBy) {
//
//        long valueOf_Id;
//
//        //Log.d(LOG_TAG, "  ---> 417 addMovieInfo ---> contentResolver-query");
//        Cursor mCursor = mContext.getContentResolver()
//                            .query(
//                                MovieInfoEntry.CONTENT_URI,         // uri
//                                new String[]{MovieInfoEntry._ID},   // projection
//                                MovieInfoEntry.COL_MV_ID + "=?",    // selection
//                                new String[]{String.valueOf(movieInfo.mId)},  // selectionArg
//                                null                                            // sort order
//                            );
//
//        //Log.d(LOG_TAG, "  <--- 427 addMovieInfo <--- contentResolver-query");
//
//        // Check presence or row/data
//        if (mCursor.moveToFirst()==true) {
//
//            // get the '_id' value
//            int mColIndx = mCursor.getColumnIndex(MovieContract.MovieInfoEntry._ID);
//            valueOf_Id = mCursor.getLong(mColIndx);
//            //Log.d(LOG_TAG, "  <--- 435 addMovieInfo, YES IS A ROW EXIST.... ");
//
//        } else {
//            ContentValues cv = new ContentValues();
//            cv.put(MovieInfoEntry.COL_MV_ID,          movieInfo.mId);
//            cv.put(MovieInfoEntry.COL_TITLE,          movieInfo.mOriginalTitle);
//            cv.put(MovieInfoEntry.COL_RELEASEDATE,    movieInfo.mReleaseDate);
//            cv.put(MovieInfoEntry.COL_OVERVIEW, movieInfo.mOverview);
//            cv.put(MovieInfoEntry.COL_POSTERLINK,     movieInfo.mPoster);
//            cv.put(MovieInfoEntry.COL_VOTE_AVERAGE,   movieInfo.mVoteAverage);
//            cv.put(MovieInfoEntry.COL_POPULARITY,     movieInfo.mPopularity);
//            cv.put(MovieInfoEntry.COL_VOTE_COUNT,     movieInfo.mVoteCount);
//            /*
//                + MovieInfoEntry.COL_VOTE_AVERAGE   + " REAL NOT NULL, " //" REAL NOT NULL, "
//                + MovieInfoEntry.COL_VOTE_COUNT     + " INTEGER NOT NULL, "//" INTEGER NOT NULL, "
//             */
//            //Log.d(LOG_TAG, "  ---> 461 addMovieInfo ---> contentResolver-insert");
//            Uri insertUri = mContext.getContentResolver().insert(MovieInfoEntry.CONTENT_URI, cv);
//            //Log.d(LOG_TAG, "  <--- 463 addMovieInfo <--- contentResolver-insert");
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
//
//    private void addRatingTable(MoviesSelectedInfo movieInfo, int arrayIndx, long rowId) {
//        long ratingTableRow_Id;
//
//        Cursor mCursor = mContext.getContentResolver().query(
//            RatingEntry.CONTENT_URI,
//            new String[]{RatingEntry._ID, RatingEntry.COL_KEY_ID},
//            RatingEntry.COL_MV_ID + "=?",
//            new String[]{String.valueOf(movieInfo.mId)}, // selectionArg
//            null
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
//                Log.d(LOG_TAG, "  xxxx addRatingTable  add Content Values");
//
//                ContentValues cv = new ContentValues();
//                cv.put(RatingEntry.COL_MV_ID, movieInfo.mId);
//                cv.put(RatingEntry.COL_TITLE, movieInfo.mOriginalTitle);
//                cv.put(RatingEntry.COL_POSTER, movieInfo.mPoster);
//                cv.put(RatingEntry.COL_KEY_ID, rowId);
//
//                String selection = RatingEntry.TABLE_NAME + "." + RatingEntry._ID + "=?";
//
//                int rowsUpdated = mContext.getContentResolver()
//                    .update(RatingEntry.CONTENT_URI,
//                        cv,
//                        selection,
//                        new String[]{String.valueOf(arrayIndx)}
//                    );
//            }
//        }
//        // there is no such movie_Id in table, so we need to insert the info.
//        else {
//            Log.d(LOG_TAG, "  xxxx addRatingTable ---  add Content Values");
//
//            ContentValues cv = new ContentValues();
//            cv.put(RatingEntry.COL_MV_ID, movieInfo.mId);
//            cv.put(RatingEntry.COL_TITLE, movieInfo.mOriginalTitle);
//            cv.put(RatingEntry.COL_POSTER, movieInfo.mPoster);
//            cv.put(RatingEntry.COL_KEY_ID, rowId);
//
//            Uri rowsUpdated = mContext.getContentResolver()
//                .insert(RatingEntry.CONTENT_URI,
//                    cv
//                );
//        }
//
//        mCursor.close();
//        /// */
//    }
}
        //    }
        //mInfoValues.put(MovieInfoEntry.COL_VIDEOLINK, movieInfo.);

/////////////////--- Pre implementation of new version ----Begin-//////////////
/////////////////--- Pre implementation of new version ----End---//////////////