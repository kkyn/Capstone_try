package com.example.android.myproject_2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kkyin on 6/1/2016.
 */
public class MovieContract_2_afterchanges {

    // Step-1
    // "Content authority" --- is the name of the Content Provider
    public static final String AUTHORITY = "com.example.android.myproject_2";

    // With the "Content authority" --- Create the "base content URI"
    // which apps need have to contact the Content Provider
    public static final Uri URI_CONTENT_AUTHORITY = Uri.parse("content://" + AUTHORITY);

    // Possible valid paths (appended to the "base content URI" for possible URIs)
    // Note the valid paths matches the tables in the database.
    // e.g "content://com.example.android.myproject_2/movieSortBy"
    // e.g "content://com.example.android.myproject_2/movieInfo"
    public static final String POPULAR = "popularity";
   // public static final String MOVIEINFO = "movieInfo";
   public static final String RATING = "rating";
   //public static final String MOVIE = "movie";


    //+++++++++++++++++++++++++++++++++++++++++++++//
    // DEFINITIONS FOR TABLE 1 -- "content://com.example.android.myproject_2/popularity"
    //+++++++++++++++++++++++++++++++++++++++++++++//
    public static final class PopularEntry implements BaseColumns {

        // Step-1
        //-------------------------------
        // Uri reference to TABLE 1 -- "content://com.example.android.myproject_2/popularity"
        //-------------------------------
        // "content://com.example.android.myproject_2/popularity"
        public static final Uri CONTENT_URI =
                URI_CONTENT_AUTHORITY.buildUpon().appendPath(POPULAR).build();

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
        public static final String POPULARITY_MULTI_ITEM_CURSOR =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + POPULAR;

        // * Item_Cursor string set cursor return 1 item, from the content provider.
        // (prefix, ContentResolver.CURSOR_ITEM_BASE_TYPE, implies
        // cursor return 1 item from the URI/Content Provider.)
        // * Android uses a form, mime type, to describe the type returned by the URI.
        public static  final String POPULARITY_SINGLE_ITEM_CURSOR =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + POPULAR;

        //----------------------------------------
        // TABLE 1's name and column constants
        //----------------------------------------
        public static final String TABLE_NAME = "Table_Popularity";
    //    public static final String COL_KEY_ID = "KeyID";
        public static final String COL_MV_ID = "MovieID";
        public static final String COL_TITLE = "Title";

///////////////////////////////////

        // Column contains foreign-key used in the SortByTable
      //  public static final String COL_MV_ID = "movie_id";
      //  public static final String COL_TITLE = "title";

        public static final String COL_RELEASEDATE = "release_date";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_VIDEOLINK = "video_link";
        public static final String COL_POSTERLINK = "poster_link";
////////////////////////////////
        // Step-2
        //-------------------------------------------------------//

        // * http://developer.android.com/reference/android/net/Uri.Builder.html
        // * Uri.Builder -- Helper class for building or manipulating URI references.
        // * Convenient supporting methods to help build the Content Provider Queries, Uri(s)
        //
        // *  e.g. "content://com.example.android.myproject_2/Popularity/movieName"
        public static Uri buildUriPopularity(String movieName) {

            return CONTENT_URI.buildUpon().appendPath(movieName).build();
           // return CONTENT_URI.buildUpon().appendPath(SortedBy).build();
        }

        // * http://developer.android.com/reference/android/content/ContentUris.html
        // * ContentUris : Contains Utility methods useful for working with Uri objects
        //      that use the "content" (content://) scheme.
        // * content://authority/path/id
        //      e.g. "content://com.example.android.myproject_2/Popularity/movieId"
        // * public static Uri withAppendedId (Uri contentUri, long id)
        public static Uri buildUriPopularityWithId(long movieId) {

            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }
//        public static Uri buildUri_PopularityWithId(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }

        public static String getMovieId_fromUri(Uri uri) {
            return uri.getPathSegments().get(1);
           // return uri.getLastPathSegment();
        }
    }

    /*
    //+++++++++++++++++++++++++++++++++++++++++++++//
    // DEFINITIONS FOR TABLE 3
    //+++++++++++++++++++++++++++++++++++++++++++++//
    public static final class MovieInfoEntry implements BaseColumns {

        // "content://com.example.android.myproject_2/movieInfo"
        public static final Uri CONTENT_URI =
            URI_CONTENT_AUTHORITY.buildUpon().appendPath(MOVIEINFO).build();

        //--------------------------------------------------------------
        public static final String DIR_CURSOR_MOVIEINFO =
            ContentResolver.CURSOR_DIR_BASE_TYPE +"/"+ AUTHORITY +"/"+ MOVIEINFO;

        public static  final String ITEM_CURSOR_MOVIEINFO =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIEINFO;

        //--------------------------------------------------------------
        public static final String TABLE_NAME = "Table_MovieInfo";

        // Column contains foreign-key used in the SortByTable
        public static final String COL_MV_ID = "movie_id";
        public static final String COL_TITLE = "title";

        public static final String COL_RELEASEDATE = "release_date";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_VIDEOLINK = "video_link";
        public static final String COL_POSTERLINK = "poster_link";

        //---------------------------------------------------//
        // Supporting methods to build Uris
        //
        public static Uri buildUri_MovieInfo_Id(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getMovieId_fromUri(Uri uri) {
            return uri.getPathSegments().get(1);
            // return uri.getLastPathSegment();
        }
    }
    */

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++//
    // DEFINITIONS FOR TABLE 2 -- "content://com.example/android.myproject_2/rating"
    //+++++++++++++++++++++++++++++++++++++++++++++//
    public static final class RatingEntry implements BaseColumns {

        //-------------------------------
        // Uri reference to TABLE 2 -- "content://com.example/android.myproject_2/rating"
        //-------------------------------
        // "content://com.example/android.myproject_2/rating"
        public static final Uri URI_CONTENT_AUTHORITY_RATING =
                URI_CONTENT_AUTHORITY.buildUpon().appendPath(RATING).build();

        //--------------------------------------------------------------
        public static final String RATING_MULTI_ITEM_CURSOR =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + RATING;

        public static final String RATING_SINGLE_ITEM_CURSOR =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + RATING;

        //--------------------------------------------------------------
        public static final String TABLE_NAME = "Table_Rating";

        //public static final String COL_KEY_ID = "KeyID";
        public static final String COL_TITLE = "Title";
        public static final String COL_MV_ID = "MovieID";

///////////////////////////////////
    // Column contains foreign-key used in the SortByTable
    //  public static final String COL_MV_ID = "movie_id";
    //  public static final String COL_TITLE = "title";

    public static final String COL_RELEASEDATE = "release_date";
    public static final String COL_OVERVIEW = "overview";
    public static final String COL_VIDEOLINK = "video_link";
    public static final String COL_POSTERLINK = "poster_link";
    ////////////////////////////////
        // Helper functions to build Uris
        public static Uri buildUri_Rating(String MovieName) {

            return URI_CONTENT_AUTHORITY_RATING.buildUpon().appendPath(MovieName).build();
        }
        public static Uri buildUri_Rating_Id(long id) {

            return ContentUris.withAppendedId(URI_CONTENT_AUTHORITY_RATING, id);
        }
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    public static final class MovieEntry implements BaseColumns {
//        //-------------------------------
//        // Uri reference to TABLE 4
//        //-------------------------------
//        // "content://com.example.android.myproject_2/movie"
//        public static final Uri URI_CONTENT_AUTHORITY_MOVIE =
//            URI_CONTENT_AUTHORITY.buildUpon().appendPath(MOVIE).build();
//
//        public static final String DIR_CURSOR_MOVIE =
//            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIE;
//
//        public static final String ITEM_CURSOR_MOVIE =
//            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIE;
//
//        public static final String TABLE_NAME = "Table_Movie";
//
//        public static final String COL_MV_ID = "MovieID";
//        public static final String COL_TITLE = "title";
//        public static final String COL_VOTECOUNT = "votes";
//        public static final String COL_RATING = "ratings";
//        public static final String COL_RELEASEDATE = "release_date";
//        public static final String COL_OVERVIEW = "overview";
//     //   public static final String COL_VIDEOLINK = "video_link";
//     //   public static final String COL_POSTERLINK = "poster_link";
//
//        // Helper methods to build Uri(s)
//        //
//        public static Uri buildUri_Movie_Id(long movieId) {
//
//            return ContentUris.withAppendedId(URI_CONTENT_AUTHORITY_MOVIE, movieId);
//        }
//
//        public static Uri buildUri_Movie_Name(String movieName) {
//
//            return URI_CONTENT_AUTHORITY_MOVIE.buildUpon().appendPath(movieName).build();
//        }
//    }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    public static String getMovieId_fromUri(Uri uri) {
//        return uri.getPathSegments().get(1);
//       // return uri.getLastPathSegment();
//    }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}
