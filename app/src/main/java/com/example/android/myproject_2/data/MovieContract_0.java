package com.example.android.myproject_2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kkyin on 6/1/2016.
 */
public class MovieContract_0
{

    // "Content authority" --- is the name of the Content Provider
    public static final String CONTENT_AUTHORITY = "com.example.android.myproject_2";

    // With the "Content authority" --- Create the "base content URI"
    // which apps need have to contact the Content Provider
    public static final Uri URI_BASE_CONTENT_AUTHORITY = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible valid paths (appended to the "base content URI" for possible URIs)
    // Note the valid paths matches the tables in the database.
    // e.g "content://com.example.android.myproject_2/movieSortBy"
    // e.g "content://com.example.android.myproject_2/movieInfo"
    public static final String MOVIESORTBY = "movieSortBy";
    public static final String MOVIEINFO = "movieInfo";
    public static final String MOVIERATING = "movieRating";


    //+++++++++++++++++++++++++++++++++++++++++++++//
    // DEFINITIONS FOR TABLE 1
    //+++++++++++++++++++++++++++++++++++++++++++++//
    public static final class SortByEntry implements BaseColumns {

        //-------------------------------
        // Uri reference to TABLE 1
        //-------------------------------
        // "content://com.example.android.myproject_2/movieSortBy"
        public static final Uri URI_BCA_SORTBY =
                URI_BASE_CONTENT_AUTHORITY.buildUpon().appendPath(MOVIESORTBY).build();

        //-----------------------------------------------------
        // Returned Cursor definitions from Content Provider
        //-----------------------------------------------------
        //--------------------------------------------------------//
        // Dir_Cursor return >1 items, from the content provider
        // Item_Cursor return 1 item, from the content provider
        //--------------------------------------------------------//
        // * Dir_Cursor string set cursor return >1 item, from the content provider
        // (prefix, ContentResolver.CURSOR_DIR_BASE_TYPE, implies
        // cursor return >1 item from the URI/Content Provider.)
        // * Android uses a form, mime type, to describe the type returned by the URI.
        public static final String DIR_CURSOR_SORTBY =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIESORTBY;

        // * Item_Cursor string set cursor return 1 item, from the content provider.
        // (prefix, ContentResolver.CURSOR_ITEM_BASE_TYPE, implies
        // cursor return 1 item from the URI/Content Provider.)
        // * Android uses a form, mime type, to describe the type returned by the URI.
        public static  final String ITEM_CURSOR_SORTBY =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIESORTBY;

        //----------------------------------------
        // TABLE 1's name and column constants
        //----------------------------------------
        public static final String TABLE_SORTBY = "Table_SortBy";

        public static final String COL_SORTBYSETTING = "sortBySetting";
        public static final String COL_MOVIEID = "movieId";

        //-------------------------------------------------------//
        // Convenient supporting methods to help build the Content Provider Queries, Uri(s)
        //

        public static Uri buildUriMovieSortBy(String SortedBy) {
            return URI_BCA_SORTBY.buildUpon().appendPath(SortedBy).build();
        }

        public static Uri buildUriMovieSortByWithId(long id) {
            return ContentUris.withAppendedId(URI_BCA_SORTBY, id);
        }

    }

    //+++++++++++++++++++++++++++++++++++++++++++++//
    // DEFINITIONS FOR TABLE 3
    //+++++++++++++++++++++++++++++++++++++++++++++//
    public static final class RatingEntry implements BaseColumns {

        //-------------------------------
        // Uri reference to TABLE 3
        //-------------------------------
        // "content://com.example/android.myproject_2/Rating"
        public static final Uri URI_BCA_RATING =
                URI_BASE_CONTENT_AUTHORITY.buildUpon().appendPath(MOVIERATING).build();

        //-----------------------------------------------------
        // Returned Cursor definitions from Content Provider
        //-----------------------------------------------------
        public static final String DIR_CURSOR_RATING =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIERATING;
        public static final String ITEM_CURSOR_RATING =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIERATING;

        public static final String TABLE_RATING = "Table_Rating";
        public static final String COL_ID = "MovieID";
        public static final String COL_NAME = "MovieName";
        public static final String COL_KEY = "MovieRef";

        // Helper functions to build Uris
        public static Uri buildUri4MovieRating(String Rating) {
            return URI_BCA_RATING.buildUpon().appendPath(Rating).build();
        }
        public static Uri buildUri4MovieRatingAndID(long id) {
            return ContentUris.withAppendedId(URI_BCA_RATING, id);
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++//
    // DEFINITIONS FOR TABLE 2
    //+++++++++++++++++++++++++++++++++++++++++++++//
    public static final class MovieInfoEntry implements BaseColumns {

        //-------------------------------
        // Uri reference to TABLE 2
        //-------------------------------
        // "content://com.example.android.myproject_2/movieInfo"
        public static final Uri URI_BCA_MOVIEINFO =
                URI_BASE_CONTENT_AUTHORITY.buildUpon().appendPath(MOVIEINFO).build();

        //-----------------------------------------------------
        // Returned Cursor definitions from Content Provider
        //-----------------------------------------------------
        // * Dir_Cursor string set cursor return >1 item, from the content provider
        // (prefix, ContentResolver.CURSOR_DIR_BASE_TYPE, implies
        // cursor return >1 item from the URI/Content Provider.)
        // * Android uses a form, mime type, to describe the type returned by the URI.
        public static final String DIR_CURSOR_MOVIEINFO =
                ContentResolver.CURSOR_DIR_BASE_TYPE +"/"+ CONTENT_AUTHORITY +"/"+ MOVIEINFO;

        // * Item_Cursor string set cursor return 1 item, from the content provider.
        // (prefix, ContentResolver.CURSOR_ITEM_BASE_TYPE, implies
        // cursor return 1 item from the URI/Content Provider.)
        // * Android uses a form, mime type, to describe the type returned by the URI.
        public static  final String ITEM_CURSOR_MOVIEINFO =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIEINFO;

        //----------------------------------------
        // TABLE 2's name and column constants
        //----------------------------------------
        public static final String TABLE_MOVIEINFO = "Table_MovieInfo";

        // Column contains foreign-key used in the SortByTable
        public static final String COL_ID = "id";
        public static final String COL_TITLE = "title";

        public static final String COL_RELEASEDATE = "release_date";
        public static final String COL_OVERVIEW = "overview";

        public static final String COL_VIDEOLINK = "video_link";
        public static final String COL_POSTERLINK = "poster_link";

        //---------------------------------------------------//
        // Supporting methods to build Uris
        //
        public static Uri buildUriMovieInfoWithId(long id) {
            return ContentUris.withAppendedId(URI_BCA_MOVIEINFO, id);
        }

    }


}
