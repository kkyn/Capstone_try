package com.example.android.myproject_2.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kkyin on 6/1/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.myproject_2";
    public static final Uri BASE_CONTENT_TYPE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String SELECT_DIR = "movieSelect";
    public static final String INFO_DIR = "movieInfo";

    //---------------------------------------------//
    public static final class MovieSelectEntry implements BaseColumns {

        public static final Uri MOVIE_SELECT_CONTENT_TYPE_URI =
                BASE_CONTENT_TYPE_URI.buildUpon().appendPath(SELECT_DIR).build();

        //--------------------------------------------------------//
        public static final String TABLE_NAME = "movieSelect_Table";

        public static final String COL_MV_NAME = "movie_name";
        public static final String COL_MV_ID = "movie_id";

        //-----------------------------------------------------//
        // Supporting methods to build Uris
        //
        public static Uri buildMovieSelectUri(long id) {
            return ContentUris.withAppendedId(MOVIE_SELECT_CONTENT_TYPE_URI, id);
        }

        public static Uri buildMovieSelect (String movieSelectSetting) {
            return MOVIE_SELECT_CONTENT_TYPE_URI.buildUpon()
                    .appendPath(movieSelectSetting).build();
        }
    }

    //--------------------------------------------//
    public static final class MovieInfoEntry implements BaseColumns {

        public static final Uri MOVIE_INFO_CONTENT_TYPE_URI =
                BASE_CONTENT_TYPE_URI.buildUpon().appendPath(INFO_DIR).build();

        //-------------------------------------------------------//
        public static final String TABLE_NAME = "movieInfo_Table";

        public static final String COL_MV_KEY = "movie_key";

        public static final String COL_MV_LINK = "movie_link";

        public static final String COL_MV_RELEASE_DATE = "release_date";
        public static final String COL_MV_VIDEO_LINK = "video_link";
        public static final String COL_MV_POSTER_LINK = "poster_link";
        public static final String COL_MV_SYNOPSIS = "synopsis";

        //---------------------------------------------------//
        // Supporting methods to build Uris
        //
        public static Uri buildMovieInfoUri(long id) {
            return ContentUris.withAppendedId(MOVIE_INFO_CONTENT_TYPE_URI, id);
        }

    }


}
