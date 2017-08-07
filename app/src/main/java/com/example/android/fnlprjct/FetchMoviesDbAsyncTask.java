package com.example.android.fnlprjct;

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


public class FetchMoviesDbAsyncTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchMoviesDbAsyncTask.class.getSimpleName();

    ///////////////////////////////////////////////////
    private Context mContext;
    private OnAsyncTaskCompletedListener mListener;

    public FetchMoviesDbAsyncTask(Context context, OnAsyncTaskCompletedListener listener) {

        this.mContext = context;
        this.mListener = listener;
    }

    //--------------------------------------
    public interface OnAsyncTaskCompletedListener {

        void onAsyncTaskCompleted(String[] result);
    }
    //--------------------------------------
    ///////////////////////////////////////////

    //++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    protected String[] doInBackground(String... params) {
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
            final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/";
            final String MOVIE_ = "movie";
            final String VIDEO_MODE = "videos";
            final String KEY_PARAM = "api_key";

            // (1) build the Url
            buildUri = Uri.parse(MOVIE_DB_BASE_URL) // creates a Uri which parses the given encoded URI string
                    .buildUpon()                    // to obtain a builder (Uri.Builder) representing an existing URI

                    .appendPath(MOVIE_    /*  movie/  */)
                    .appendPath(params[0] /*  ###/    */)

                    .appendEncodedPath(VIDEO_MODE /*  videos?  */)

                    .appendQueryParameter(KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY /*  &api_key=####  */)
                    .build();

            /*Log.d(LOG_TAG, "----xxxxxxx" + buildUri.toString());*/

            URL url = new URL(buildUri.toString());

            // (2) create request and open connection with the url
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            // (3) read the input-stream and convert the stream to string
            InputStream inputStream = httpUrlConnection.getInputStream();
            //StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null) {
                //Log.d(LOG_TAG, "----xxxxxxx ---- null");
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

            //Log.d(LOG_TAG, "----xxxxxxx" + stringBuffer.toString());

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
    protected void onPostExecute(String[] returnedResult) {
        super.onPostExecute(returnedResult);

        mListener.onAsyncTaskCompleted(returnedResult);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++

    private String[] getMovieInfoFromJson(String moviesJsonStr) throws JSONException {

        final String RESULTS = "results";
        final String KEY = "key";
     // final String SITE = "site";

        String movieKey;
        String movieSite;

        JSONObject moviesJsonObject = new JSONObject(moviesJsonStr);
        JSONArray resultArray = moviesJsonObject.getJSONArray(RESULTS);

        String[] myMovSlctdInfo = new String[resultArray.length()];

        for (int i = 0; i < resultArray.length(); i++) {

            JSONObject movie = resultArray.getJSONObject(i);

            movieKey = movie.getString(KEY);

            myMovSlctdInfo[i] = movieKey;
            /*
            movieKey = movie.getString(KEY);
            movieSite = movie.getString(SITE);

            myMovSlctdInfo[i] = new String(
                    movieKey,
                    movieSite);*/
        }

        return myMovSlctdInfo;

    }
}