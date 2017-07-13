package com.example.android.fnlprjct.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.fnlprjct.R;

/*
 * Created by kkyin on 6/1/2016.
 */
public class MovieContract {

    // Step-1
    // "Content authority" --- is the name of the Content Provider
    // Note: Cannot do this way, "@string/content_authority".
    // Have to define this way, a string, e.g. "com.example.android.fnlprjct"
    public static final String AUTHORITY = "com.example.android.fnlprjct";

    // With the "Content authority" --- Create the "base content URI"
    // which apps need have to contact the Content Provider
    public static final Uri URI_CONTENT_AUTHORITY = Uri.parse("content://" + AUTHORITY);

    // Possible valid paths (appended to the "base content URI" for possible URIs)
    // Note the valid paths matches the tables in the database.
    // e.g "content://com.example.android.myproject_2/movieSortBy"
    // e.g "content://com.example.android.myproject_2/movieInfo"

    // tky comment, 26th May 2016
    // not use, to remove later
    /*public static final String POPULAR = "popularity";
    public static final String RATING = "rating";
    public static final String MOVIEINFO = "movieInfo";*/


    public static final String MOVIEINFO = "movieinfo";  // "movie_info"
    public static final String MOVIEREVIEW = "moviereview";
    public static final String MOVIEVIDEO = "movievideo";
    public static final String MOVIEFAVOURITES = "moviefavourites";

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // DEFINITIONS FOR TABLE 1 -- "content://com.example.android.myproject_2/movieinfo"
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    public static final class MovieInfoEntry implements BaseColumns {

        // "content://com.example.android.myproject_2/movieinfo"
        public static final Uri CONTENT_URI =
                URI_CONTENT_AUTHORITY.buildUpon().appendPath(MOVIEINFO).build();

        //--------------------------------------------------------------
        public static final String DIR_CURSOR_MOVIEINFO =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIEINFO;

        public static final String ITEM_CURSOR_MOVIEINFO =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIEINFO;

        //--------------------------------------------------------------
        public static final String TABLE_NAME = "Table_MovieInfo";

        // Column contains foreign-key used in the SortByTable
        public static final String COL_KEY_ID = "KeyID";
        public static final String COL_MOVIE_ID = "MovieID";

        public static final String COL_YEAR = "Year";

        public static final String COL_FAVOURITES = "Favourites";

        public static final String COL_POSTERLINK = "PosterLink";
        public static final String COL_BACKDROPLINK = "BackdropLink";
        public static final String COL_VIDEOLINK = "VideoLink";

        public static final String COL_ORIGINAL_TITLE = "original_title";
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_POPULARITY = "popularity";
        public static final String COL_VOTE_COUNT = "vote_count";
        public static final String COL_VOTE_AVERAGE = "vote_average";


        // Step-2
        //-------------------------------------------------------//

        // * http://developer.android.com/reference/android/net/Uri.Builder.html
        // * Uri.Builder -- Helper class for building or manipulating URI references.
        // * Convenient supporting methods to help build the Content Provider Queries, Uri(s)
        //
        // *  e.g. "content://com.example.android.myproject_2/movieinfo/movieName"
        public static Uri buildUri_MovieInfo(String movieName) {

            return CONTENT_URI.buildUpon().appendPath(movieName).build();
        }

        // * http://developer.android.com/reference/android/content/ContentUris.html
        // * ContentUris : Contains Utility methods useful for working with Uri objects
        //      that use the "content" (content://) scheme.
        // * content://authority/path/id
        //      e.g. "content://com.example.android.myproject_2/movieinfo/movieId"
        // * public static Uri withAppendedId (Uri contentUri, long id)
        //---------------------------------------------------//
        // Supporting methods to build Uris
        //
        public static Uri buildUri_MovieInfoWithId(long movieId) {

            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }

        public static String getMovieId_FromMovieInfoUri(Uri uri) {

            return uri.getPathSegments().get(1);
            //-- or --
            // return uri.getLastPathSegment();
        }
        //------------------------------------------------------------
    }
    //*******************************************************************//
    //*******************************************************************//
    public static final class MovieReviewEntry implements BaseColumns {

        // Step-1
        //-------------------------------
        // Uri reference to TABLE 2 -- "content://com.example.android.myproject_2/moviereview"
        //-------------------------------
        // "content://com.example.android.myproject_2/moviereview"
        public static final Uri CONTENT_URI =
                URI_CONTENT_AUTHORITY.buildUpon().appendPath(MOVIEREVIEW).build();

        //-----------------------------------------------------
        // Definitions of Cursor returned from Content Provider.
        // Cursors returned have unique types based upon their content and
        // base path used for the query.
        // Android uses a form similar to the internet media type or
        // mime type to describe the type returned by the URI.
        // Cursors that can return more than 1 item are prefixed by the CURSOR_DIR_BASE_TYPE string.
        // While cursors that return only 1 single item are prefixed by the CURSOR_ITEM_BASE_TYPE string.
        //-----------------------------------------------------
        // Dir_Cursor return >1 items, from the content provider
        // Item_Cursor return 1 item, from the content provider
        //--------------------------------------------------------//
        // * Dir_Cursor string set cursor return >1 item, from the content provider
        // (prefix, ContentResolver.CURSOR_DIR_BASE_TYPE, implies
        // cursor return >1 item from the URI/Content Provider.)
        // * Android uses a form, mime type, to describe the type returned by the URI.
        public static final String DIR_CURSOR_MOVIEREVIEW =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIEREVIEW;

        // * Item_Cursor string set cursor return 1 item, from the content provider.
        // (prefix, ContentResolver.CURSOR_ITEM_BASE_TYPE, implies
        // cursor return 1 item from the URI/Content Provider.)
        // * Android uses a form, mime type, to describe the type returned by the URI.
        public static  final String ITEM_CURSOR_MOVIEREVIEW =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIEREVIEW;

        //----------------------------------------
        // TABLE 2's name and column constants
        //----------------------------------------
        public static final String TABLE_NAME = "Table_MovieReview";

        public static final String COL_KEY_ID = "KeyID";
        public static final String COL_MOVIE_ID = "MovieID";
        public static final String COL_TITLE = "Title";
        public static final String COL_REVIEWER = "Reviewer";
        public static final String COL_REVIEWCONTENT = "ReviewerContent";


        // Step-2
        //-------------------------------------------------------//

        // * http://developer.android.com/reference/android/net/Uri.Builder.html
        // * Uri.Builder -- Helper class for building or manipulating URI references.
        // * Convenient supporting methods to help build the Content Provider Queries, Uri(s)
        //
        // *  e.g. "content://com.example.android.myproject_2/moviereview/movieName"
        public static Uri buildUri_4MovieReviewWithName(String movieName) {

            return CONTENT_URI.buildUpon().appendPath(movieName).build();
        }

        // * http://developer.android.com/reference/android/content/ContentUris.html
        // * ContentUris : Contains Utility methods useful for working with Uri objects
        //      that use the "content" (content://) scheme.
        // * content://authority/path/id
        //      e.g. "content://com.example.android.myproject_2/moviereview/movieId"
        // * public static Uri withAppendedId (Uri contentUri, long id)
        public static Uri buildUri_MovieReviewWithId(long movieId) {

            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }

        public static String getMovieId_FromMovieReviewUri(Uri uri) {
            return uri.getPathSegments().get(1);
            // return uri.getLastPathSegment();
        }
        //------------------------------------------------------------
    }
    //*******************************************************************//
    //*******************************************************************//
    public static final class MovieVideosEntry implements BaseColumns {

        public static Uri CONTENT_URI = URI_CONTENT_AUTHORITY.buildUpon().appendPath(MOVIEVIDEO).build();
        //--------------------------------------------------------------
        public static final String DIR_CURSOR_MOVIEVIDEO =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIEVIDEO;

        public static final String ITEM_CURSOR_MOVIEVIDEO =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIEVIDEO;
        //--------------------------------------------------------------

        //----------------------------------------
        // TABLE 2's name and column constants
        //----------------------------------------
        public static final String TABLE_NAME = "Table_MovieVideo";
     //   public static final String COL_KEY_ID = "KeyID";
        public static final String COL_MOVIE_ID = "MovieID";
        public static final String COL_VIDEO_KEY = "VideoKey";

        public static String getMovieId_FromMovieVideoUri(Uri uri) {
            return uri.getPathSegments().get(1);
            // return uri.getLastPathSegment();
        }

        public static Uri buildUri_MovieVideoWithId(long movieId) {
            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }

    }
    //*******************************************************************//
    //*******************************************************************//
    public static final class MovieFavouritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = URI_CONTENT_AUTHORITY.buildUpon().appendPath(MOVIEFAVOURITES).build();

        public static final String FAVOURITES_MULTI_ITEM_CURSOR =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIEFAVOURITES;

        public static final String FAVOURITES_SINGLE_ITEM_CURSOR =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIEFAVOURITES;

        public static final String TABLE_NAME = "Table_Favourites";
        public static final String COL_MOV_ID = "MovieID";
        public static final String COL_FAVOURITES = "Favourites";
    }
}
