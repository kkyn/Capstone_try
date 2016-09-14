package com.example.android.myproject_2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kkyin on 3/11/2015.
 */

public class FetchMoviesDbAsyncTask_1 extends AsyncTask<String, Void, MoviesSelectedInfo[]> {

    private final String LOG_TAG = FetchMoviesDb_AsyncTask.class.getSimpleName();

    ///////////////////////////////////////////////////

    private Context mContext;
    private OnAsyncTaskCompletedListener mListener;

    public FetchMoviesDbAsyncTask_1(Context context, OnAsyncTaskCompletedListener listener) {

        this.mContext = context;
        this.mListener = listener;
    }

    //--------------------------------------
    public interface OnAsyncTaskCompletedListener {

        void onAsyncTaskCompleted(com.example.android.myproject_2.MoviesSelectedInfo[] result);
    }
    //--------------------------------------

    ///////////////////////////////////////////

    //++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    protected MoviesSelectedInfo[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection httpUrlConnection = null;
        BufferedReader bufferedRdr = null;

        // Will contain the raw JSON response as a string.
        String movieInfoInJsonStr = null;
        Uri buildUri = null;

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
            final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String VIDEO_MODE = "video";
            final String KEY_PARAM = "api_key";

            // (1) build the Url
            buildUri = Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
                    .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI
                    .appendPath(params[0])
                    .appendEncodedPath(VIDEO_MODE)
                    .appendQueryParameter(KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(buildUri.toString());

            /*final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";
            final String DISCOVER_MODE = "discover/movie";
            final String SORT_PARAM = "sort_by";
            final String KEY_PARAM = "api_key";

            // (1) build the Url
            buildUri = Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
                    .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI
                    .appendEncodedPath(DISCOVER_MODE)
                    .appendQueryParameter(KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(SORT_PARAM, params[0]) //.appendQueryParameter(SORT_PARAM, DESC)

                            //          .appendQueryParameter("with_genres", "18")
                    .appendQueryParameter("certification_country", "US")
                    .appendQueryParameter("primary_release_year", "2015")
                    .appendQueryParameter("vote_count.gte", "50")
                    .build();

            URL url = new URL(buildUri.toString());
            */

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
            StringBuffer stringBuffer = new StringBuffer();

            while ((line = bufferedRdr.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
            if (stringBuffer.length() == 0) {
                return null;
            }

            // (5) convert string-buffer to string
            movieInfoInJsonStr = stringBuffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
            return null;
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
        try {
            return getMovieInfoFromJson(movieInfoInJsonStr);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(MoviesSelectedInfo[] returnedResult) {
        super.onPostExecute(returnedResult);

        mListener.onAsyncTaskCompleted(returnedResult);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++

    private MoviesSelectedInfo[] getMovieInfoFromJson(String moviesJsonStr) throws JSONException {

        final String RESULTS = "results";
        final String KEY = "key";
        final String SITE = "site";

        String movieKey;
        String movieSite;

        JSONObject moviesJsonObject = new JSONObject(moviesJsonStr);
        JSONArray resultArray = moviesJsonObject.getJSONArray(RESULTS);

        MoviesSelectedInfo[] myMovSlctdInfo = new MoviesSelectedInfo[resultArray.length()];

        for (int i = 0; i < resultArray.length(); i++) {

            JSONObject movie = resultArray.getJSONObject(i);

            movieKey = movie.getString(KEY);
            movieSite = movie.getString(SITE);

            myMovSlctdInfo[i] = new MoviesSelectedInfo(
                    movieKey,
                    movieSite);
        }

        return myMovSlctdInfo;

        /*
        final String RESULTS = "results";
        final String ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String BACKDROP_PATH = "backdrop_path"; // movie poster image thumbnail
        final String OVERVIEW = "overview";     // plot synopsis
        final String VOTE_AVERAGE = "vote_average"; // user rating
        final String RELEASE_DATE = "release_date";
        //   final String VOTE_COUNT = "vote_count";

        final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";
        // W92 = "w92/"; W154 = "w154/";
        // W185 = "w185/"; W342 = "w342/";
        // W342 = "w342/"; W500 = "w500/";
        // W780 = "w780/"; ORIGINAL = "original/";
    //    final String W780 = "w780/";
        final String W500 = "w500/";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultArray = moviesJson.getJSONArray(RESULTS);

        MoviesSelectedInfo[] myMovSlctdInfo = new MoviesSelectedInfo[resultArray.length()];

        for (int i = 0; i < resultArray.length(); i++) {

            JSONObject movie = resultArray.getJSONObject(i);

            long movieId = movie.getLong(ID);
            String movieOrgTitle = movie.getString(ORIGINAL_TITLE);
            String movieOverview = movie.getString(OVERVIEW); // plot synopsis
            double movieVoteAverage = movie.getDouble(VOTE_AVERAGE);  // user rating
            //    long movieVoteCount = movie.getLong(VOTE_COUNT);  // user, sum number of votes
            String movieReleaseDate = movie.getString(RELEASE_DATE);

            String movieBackDropPath = TMDB_BASE_URL + W500 + movie.getString(BACKDROP_PATH); // movie poster image thumbnail
            String moviePosterPath = TMDB_BASE_URL + W500 + movie.getString(POSTER_PATH);

            myMovSlctdInfo[i] = new MoviesSelectedInfo(
                    movieId, movieOrgTitle,
                    movieOverview, movieVoteAverage,
                    movieReleaseDate, // movieVoteCount,
                    moviePosterPath, movieBackDropPath);

                //Log.v(LOG_TAG, "Movie: " + myMovSlctdInfo[i].mOriginalTitle + " " + myMovSlctdInfo[i].mVoteAverage ); //+ " " + myMovSlctdInfo[i].mPoster);
        }

          //  for (MoviesSelectedInfo s : myMovSlctdInfo) {
          //      Log.v(LOG_TAG, "Movie: " + s.mOriginalTitle + " " + s.mVoteAverage + " " + s.mPoster);
          //  }
          //
          //  Log.v(LOG_TAG, "Movie: " + urladdress);

        return myMovSlctdInfo;
        */
    }
}