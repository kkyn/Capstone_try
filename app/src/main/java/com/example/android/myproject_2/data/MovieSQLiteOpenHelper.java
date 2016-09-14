package com.example.android.myproject_2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.PopularEntry;
import com.example.android.myproject_2.data.MovieContract.RatingEntry;

/**
 * Created by kkyin on 8/1/2016.
 *
 * Contains code to create and initialize the movie database.
 */
// SQLiteOpenHelper -- A helper class to manage database creation and version management
public class MovieSQLiteOpenHelper extends SQLiteOpenHelper {

    // If you change the schema, you must increment the database version.
    // Must be manually incremented, each time we release an updated APK with a new database schema
    private static final int DATABASE_VERSION = 1;

    // Actual database filename in the file system
    static final String DATABASE_NAME = "movie.db";

    // Constructor for "MovieSQLiteOpenHelper".
    // "DATABASE_NAME", "DATABASE_VERSION" are passed in to initialize the 'database helper'
    public MovieSQLiteOpenHelper(Context context) {

        // call, SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // * Call only 1 time.
    // * Called when the database is created for the first time.
    // * Create database table(s), SQLiteOpenHelper's onCreate will be called, to build the database table(s).
    // * The SQL and MovieContract_x-class are used together,
    // to write the correct SQL statement string to create the correct database table.
    //
    @Override
    public void onCreate(SQLiteDatabase db) {

        //+++++++++++++++++++++++++++++++++++++++++++++
        // Define the formats of the Database tables
        //+++++++++++++++++++++++++++++++++++++++++++++
        final String SQL_CREATE_TABLE_POPULARITY =
		
            "CREATE TABLE " + PopularEntry.TABLE_NAME
                + " ("
           //   + PopularEntry._ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PopularEntry._ID 			+ " INTEGER PRIMARY KEY, "
                + PopularEntry.COL_KEY_ID 	+ " INTEGER NOT NULL, " // constraint
                + PopularEntry.COL_MV_ID 	+ " INTEGER NOT NULL, "
             // + PopularEntry.COL_MV_ID 	+ " INTEGER UNIQUE NOT NULL, "
                + PopularEntry.COL_TITLE 	+ " TEXT NOT NULL, " // constraint
                + PopularEntry.COL_POSTER 	+ " TEXT NOT NULL, "

             // * Set the COL_KEY_ID-column as containing foreignKey(s)/ID(s)
             // to reference movies' ID(s) in MovieInfoEntry.COL_KEY_ID
             //
             // * definition of a FOREIGN KEY constraint...
                + " FOREIGN KEY (" + PopularEntry.COL_KEY_ID + ")"
                + " REFERENCES " + MovieInfoEntry.TABLE_NAME + " (" + MovieInfoEntry._ID + ") "

                + ");";

        final String SQL_CREATE_TABLE_MOVIEINFO =

            "CREATE TABLE " + MovieInfoEntry.TABLE_NAME
                + " ("
                + MovieInfoEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieInfoEntry.COL_MV_ID          + " INTEGER NOT NULL, " // " INTEGER UNIQUE NOT NULL, "
                + MovieInfoEntry.COL_TITLE          + " TEXT NOT NULL, "
                + MovieInfoEntry.COL_RELEASEDATE    + " TEXT NOT NULL, " //" INTEGER NOT NULL, "
                + MovieInfoEntry.COL_VOTE_AVERAGE   + " TEXT NOT NULL, " //" REAL NOT NULL, "
                + MovieInfoEntry.COL_POPULARITY     + " TEXT NOT NULL, "
                + MovieInfoEntry.COL_VOTE_COUNT     + " INTEGER NOT NULL, "//" INTEGER NOT NULL, "
                + MovieInfoEntry.COL_OVERVIEW       + " TEXT NOT NULL, "
                + MovieInfoEntry.COL_POSTERLINK     + " TEXT NOT NULL "
                //+ MovieInfoEntry.COL_VIDEOLINK      + " TEXT NOT NULL "
                + ");";

        /*

        public static final String COL_VOTE_AVERAGE = "vote_average";
        public static final String COL_VOTE_COUNT = "vote_count";
         */
        final String SQL_CREATE_TABLE_RATING =
		
            "CREATE TABLE " + RatingEntry.TABLE_NAME
				+ " ("
                + RatingEntry._ID 			+ " INTEGER PRIMARY KEY, "
                + RatingEntry.COL_KEY_ID 	+ " INTEGER NOT NULL, "
                + RatingEntry.COL_MV_ID 	+ " INTEGER NOT NULL, "
                + RatingEntry.COL_TITLE 	+ " TEXT NOT NULL, "
                + RatingEntry.COL_POSTER 	+ " TEXT NOT NULL, "

                + " FOREIGN KEY (" + RatingEntry.COL_KEY_ID + ")"
                + " REFERENCES " + MovieInfoEntry.TABLE_NAME + " (" + MovieInfoEntry._ID + ") "
                + ");";

///*
//final String SQL_CREATE_TABLE_POPULARITY =
//            "CREATE TABLE " + PopularEntry.TABLE_NAME
//                + " ("
//                //   + PopularEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + PopularEntry._ID + " INTEGER PRIMARY KEY, "
//
//                    + PopularEntry.COL_KEY_ID + " INTEGER NOT NULL, " // constraint
//                    + PopularEntry.COL_MV_ID + " INTEGER NOT NULL, "
//                // + PopularEntry.COL_MV_ID + " INTEGER UNIQUE NOT NULL, "
//                    + PopularEntry.COL_TITLE + " TEXT NOT NULL, " // constraint
//
//                // * Set the COL_KEY_ID-column as containing foreignKey(s)/ID(s)
//                // to reference movies' ID(s) in MovieInfoEntry.COL_KEY_ID
//                //
//                // * definition of a FOREIGN KEY constraint...
//                + " FOREIGN KEY (" + PopularEntry.COL_KEY_ID + ")"
//
//                + " REFERENCES " + MovieInfoEntry.TABLE_NAME + " (" + MovieInfoEntry._ID + ") "
//                /*
//                    + " FOREIGN KEY (" + PopularEntry.COL_KEY_ID + ")"
//
//                        + " REFERENCES " + MovieInfoEntry.TABLE_NAME +
//                                " (" + MovieInfoEntry.COL_KEY_ID + ") "
//                */
//        + ");";
// */

        //+++++++++++++++++++++++++++++++++++
/*
        public static final String TABLE_NAME = "Table_Movie";

        public static final String COL_MOVIE_ID = "movieId";
        public static final String COL_VOTECOUNT = "votes";
        public static final String COL_RATING = "ratings";
        public static final String COL_TITLE = "title";
        public static final String COL_RELEASEDATE = "release_date";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_VIDEOLINK = "video_link";
        public static final String COL_POSTERLINK = "poster_link";
 */

        // Have the system to execute the "SQL...", to build the database table
        db.execSQL(SQL_CREATE_TABLE_POPULARITY);
        db.execSQL(SQL_CREATE_TABLE_MOVIEINFO);
        db.execSQL(SQL_CREATE_TABLE_RATING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
