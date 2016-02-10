package com.example.android.myproject_2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kkyin on 6/1/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.myproject_2";
    public static final Uri BASE_CONTENT_TYPE = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String SORTBY = "movieSortBy";
    public static final String INFO = "movieInfo";

    //---------------------------------------------//
    // TABLE 1, DEFINITIONS
    //--------------------------------------------//
    public static final class SortByEntry implements BaseColumns {

        // "content://com.example.android.myproject_2/movieSortBy"
        public static final Uri URI_4_SORTBY = BASE_CONTENT_TYPE.buildUpon().appendPath(SORTBY).build();

        //--------------------------------------------------------//
        // Dir_Cursor return >1 items, from the content provider
        // Item_Cursor return 1 item, from the content provider
        //--------------------------------------------------------//
        public static final String DIR_CURSOR = ContentResolver.CURSOR_DIR_BASE_TYPE +
                                                            "/" + CONTENT_AUTHORITY +
                                                            "/" + SORTBY;
        public static  final String ITEM_CURSOR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                                                            "/" + CONTENT_AUTHORITY +
                                                            "/" + SORTBY;

        //--------------------------------------------------------//
        public static final String TABLE_NAME = "Table_SortBy";

        public static final String COL_SETTING_SORTBY = "sortBySetting";
        public static final String COL_MOVIE_ID = "MovieId";

        //-------------------------------------------------------//
        // Supporting methods to build Uri(s)
        //
        public static Uri buildUriMovieSortByWithId(long id) {
            return ContentUris.withAppendedId(URI_4_SORTBY, id);
        }

        public static Uri buildUriMovieSortBy(String SortedBy) {
            return URI_4_SORTBY.buildUpon().appendPath(SortedBy).build();
        }
    }

    //--------------------------------------------//
    // TABLE 2, DEFINITIONS
    //--------------------------------------------//
    public static final class MovieInfoEntry implements BaseColumns {

        // "content://com.example.android.myproject_2/movieInfo"
        //
        public static final Uri URI_4_MOVIEINFO = BASE_CONTENT_TYPE.buildUpon()
                                                                    .appendPath(INFO).build();

        //--------------------------------------------------------//
        // Dir_Cursor return >1 item, from the content provider
        // Item_Cursor return 1 item, from the content provider
        public static final String DIR_CURSOR = ContentResolver.CURSOR_DIR_BASE_TYPE +
                                                            "/" + CONTENT_AUTHORITY +
                                                            "/" + INFO;
        public static  final String ITEM_CURSOR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                                                            "/" + CONTENT_AUTHORITY +
                                                            "/" + INFO;

        //-------------------------------------------------------//
        public static final String TABLE_NAME = "Table_MovieInfo";

        // Column contains foreign-key used in the SortByTable
        public static final String COL_ID = "id";
        public static final String COL_TITLE = "title";

        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_OVERVIEW = "overview";

        public static final String COL_VIDEO_LINK = "video_link";
        public static final String COL_POSTER_LINK = "poster_link";

        //---------------------------------------------------//
        // Supporting methods to build Uris
        //
        public static Uri buildUriMovieInfoWithId(long id) {
            return ContentUris.withAppendedId(URI_4_MOVIEINFO, id);
        }

    }


}
