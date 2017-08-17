package com.example.android.fnlprjct;

import com.example.android.fnlprjct.data.MovieContract.MovieReviewEntry;
import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;


public class MyQuery {

    public interface Reviews {

        String[] PROJECTION = {
            MovieReviewEntry.TABLE_NAME + "." + MovieReviewEntry._ID,
            MovieReviewEntry.TABLE_NAME + "." + MovieReviewEntry.COL_MOVIE_ID,
            MovieReviewEntry.COL_REVIEWER,
            MovieReviewEntry.COL_REVIEWCONTENT
        };
        // These constant values are related to the index positions in
        // string array 'PROJECTION'
        int COL_MV_ID = 1;
        int COL_REVIEWER = 2;
        int COL_REVIEWCONTENT = 3;
    }

    //-------------------------------------------------
    public interface Favourites {

        String[] PROJECTION =
            {
                MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
                , MovieInfoEntry.COL_MOVIE_ID
                , MovieInfoEntry.COL_VOTE_AVERAGE + " as sort_column"
                , MovieInfoEntry.COL_POSTERLINK
                , MovieInfoEntry.COL_ORIGINAL_TITLE
            };
    }

    //-------------------------------------------------
    // state the columns of data I want
    public interface VoteAverage {

        String[] PROJECTION =
            {
                MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
                , MovieInfoEntry.COL_MOVIE_ID
                , MovieInfoEntry.COL_VOTE_AVERAGE + " as sort_column"
                , MovieInfoEntry.COL_POSTERLINK
                , MovieInfoEntry.COL_ORIGINAL_TITLE
                , MovieInfoEntry.COL_YEAR
            };
    }

    //-------------------------------------------------
    public interface Popularity {

        String[] PROJECTION =
            {
                MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
                , MovieInfoEntry.COL_MOVIE_ID
                , MovieInfoEntry.COL_VOTE_COUNT + " as sort_column"
                , MovieInfoEntry.COL_POSTERLINK
                , MovieInfoEntry.COL_ORIGINAL_TITLE
                , MovieInfoEntry.COL_YEAR
            };
        int COL_MOVIE_ID = 1;
        int COL_VOTE_COUNT = 2;
      //int COL_POSTERLINK = 3;
        int COL_ORIGINAL_TITLE = 4;
      //int COL_YEAR = 5;
    }

    //-------------------------------------------------
    public interface MovieInfo {

        String[] PROJECTION =
            {
                MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID,
                MovieInfoEntry.COL_MOVIE_ID,
                MovieInfoEntry.COL_FAVOURITES,
                MovieInfoEntry.COL_POSTERLINK,
                MovieInfoEntry.COL_OVERVIEW,
                MovieInfoEntry.COL_RELEASE_DATE,
                MovieInfoEntry.COL_ORIGINAL_TITLE,
                MovieInfoEntry.COL_VOTE_AVERAGE,
                MovieInfoEntry.COL_VOTE_COUNT,
                MovieInfoEntry.COL_YEAR
            };

        // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
        // must change.
        //public static final int PROJECTION_RATING_ID = 0;
        int COL_MOVIE_ID = 1;
        int COL_FAVOURITES = 2;
        int COL_POSTERLINK = 3;
        int COL_OVERVIEW = 4;
        int COL_RELEASEDATE = 5;
        int COL_MOVIE_TITLE = 6;
        int COL_VOTE_AVERAGE = 7;
        int COL_VOTE_COUNT = 8;
        int COL_YEAR = 9;
    }
}
