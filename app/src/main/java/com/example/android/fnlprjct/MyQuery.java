package com.example.android.fnlprjct;

import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;
/**
 * Created by kkyin on 8/6/2017.
 */

public class MyQuery {

    public interface Favourites {

        String[] PROJECTION =
            {
                MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
                , MovieInfoEntry.COL_MV_ID
                , MovieInfoEntry.COL_VOTE_AVERAGE + " as sort_column"
//            ,MovieInfoEntry.COL_FAVOURITES
                , MovieInfoEntry.COL_POSTERLINK
                , MovieInfoEntry.COL_BACKDROP_PATH
            };
    }
    //-------------------------------------------------

    // state the columns of data I want
    public interface VoteAverage {

        String[] PROJECTION =
            {
                MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
                , MovieInfoEntry.COL_MV_ID
                , MovieInfoEntry.COL_VOTE_AVERAGE + " as sort_column"
                , MovieInfoEntry.COL_POSTERLINK
                , MovieInfoEntry.COL_BACKDROP_PATH
            };
    }
    //-------------------------------------------------
    public interface Popularity {

        String[] PROJECTION =
            {
                MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
                , MovieInfoEntry.COL_MV_ID
                , MovieInfoEntry.COL_POPULARITY + " as sort_column"
                , MovieInfoEntry.COL_POSTERLINK
                //, MovieInfoEntry.COL_BACKDROP_PATH
            };
    }
    //-------------------------------------------------
    public interface MovieInfo {

        String[] PROJECTION =
            {
                MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID,
                MovieInfoEntry.COL_MV_ID,
                MovieInfoEntry.COL_FAVOURITES,
                MovieInfoEntry.COL_BACKDROP_PATH,
                MovieInfoEntry.COL_OVERVIEW,
                MovieInfoEntry.COL_RELEASEDATE,
                MovieInfoEntry.COL_TITLE,
                MovieInfoEntry.COL_VOTE_AVERAGE
            };

        // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
        // must change.
        //public static final int PROJECTION_RATING_ID = 0;
         int COL_MOVIE_ID = 1;
         int COL_FAVOURITES = 2;
        int COL_BACKDROP_PATH = 3;
        int COL_OVERVIEW = 4;
        int COL_RELEASEDATE = 5;
        int COL_MOVIE_TITLE = 6;
        int COL_MOVIE_RATING = 7;

        /*String[] PROJECTION = {
            ItemsContract.Items._ID,
            ItemsContract.Items.TITLE,
            ItemsContract.Items.PUBLISHED_DATE,
            ItemsContract.Items.AUTHOR,
            ItemsContract.Items.THUMB_URL,
            ItemsContract.Items.PHOTO_URL,
            ItemsContract.Items.ASPECT_RATIO,
            ItemsContract.Items.BODY,
        };

        int _ID = 0;
        int TITLE = 1;
        int PUBLISHED_DATE = 2;
        int AUTHOR = 3;
        int THUMB_URL = 4;
        int PHOTO_URL = 5;
        int ASPECT_RATIO = 6;
        int BODY = 7;*/
    }
}