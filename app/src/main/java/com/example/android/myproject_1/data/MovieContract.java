package com.example.android.myproject_1.data;

import android.provider.BaseColumns;

/**
 * Created by kkyin on 6/1/2016.
 */
public class MovieContract {

    public static final class StaticInfo implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final String COL_MV_ID = "movie_id";
        public static final String COL_MV_LINK = "movie_link";
        public static final String COL_MV_NAME = "name";
        public static final String COL_MV_RELEASEDATE = "releasedate";
    //    public static final String COLUMN_MOVIE_RATINGS = "ratings";
        public static final String COL_MV_VIDEO_LINK = "video_link";
        public static final String COL_MV_POSTER_LINK = "poster_link";
        public static final String COL_MV_SYNOPSIS = "synopsis";
    }

    public static final class DynamicPInfo implements BaseColumns {
        public static final String TABLE_NAME = "popular";

        public static final String COL_MV_KEY = "movie_id";
    }

    public static final class MovieRatingsEntry implements BaseColumns {
        public static final String TABLE_RATING = "rating";

        public static final String COLUMN_RATING_KEY = "rating_key";
    }
}
