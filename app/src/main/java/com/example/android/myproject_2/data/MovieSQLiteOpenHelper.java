package com.example.android.myproject_2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.PopularEntry;
//import com.example.android.myproject_2.data.MovieContract.RatingEntry;

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
    // * The SQL and MovieContract-class are used together,
    // to write the correct SQL statement string to create the correct database table.
    //
    @Override
    public void onCreate(SQLiteDatabase db) {

        //+++++++++++++++++++++++++++++++++++++++++++++
        // Define the formats of the Database tables
        //+++++++++++++++++++++++++++++++++++++++++++++
        final String SQL_CREATE_TABLE_POPULARITY =
                "CREATE TABLE " + PopularEntry.TABLE_NAME +
                        " ("
                        + PopularEntry._ID + " INTEGER PRIMARY KEY, "
                      //  + PopularEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                        + PopularEntry.COL_MV_ID + " INTEGER NOT NULL, " // constraint
                        + PopularEntry.COL_TITLE + " TEXT NOT NULL, " // constraint
                        // + PopularEntry.COL_SORTBYSETTING + " TEXT NOT NULL, " // constraint
                        // + PopularEntry.COL_KEY + " INTEGER NOT NULL, " // constraint

                        // * Set the COL_MV_ID-column as containing foreignKey(s)/ID(s)
                        // to reference movies' ID(s) in MovieInfoEntry.COL_MV_ID
                        //
                        // * definition of a FOREIGN KEY constraint...

                        + " FOREIGN KEY (" + PopularEntry.COL_MV_ID + ")"
                  //  + " FOREIGN KEY (" + PopularEntry._ID + ")"
                            + " REFERENCES " + MovieInfoEntry.TABLE_NAME +
                                        " (" + MovieInfoEntry.COL_ID + ") "
                        + ");";

//        final String SQL_CREATE_TABLE_RATING =
//                "CREATE TABLE " + RatingEntry.TABLE_NAME +
//                        " ("
//                        + RatingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                        + RatingEntry.COL_MV_ID + " INTEGER NOT NULL, "
//                        + RatingEntry.COL_TITLE + " TEXT NOT NULL, "
//
//                        + " FOREIGN KEY (" + RatingEntry.COL_MV_ID + ")"
//                            + " REFERENCES " + MovieInfoEntry.TABLE_NAME +
//                                        " (" + MovieInfoEntry.COL_MV_ID + ") "
//                        + ");";

        final String SQL_CREATE_TABLE_MOVIEINFO =

            "CREATE TABLE " + MovieInfoEntry.TABLE_NAME
                + " ("
                + MovieInfoEntry._ID
                //    + " INTEGER PRIMARY KEY, "
                  + " INTEGER PRIMARY KEY AUTOINCREMENT, "

           //         + MovieInfoEntry._ID + " INTEGER PRIMARY KEY, "
                    + MovieInfoEntry.COL_ID + " INTEGER NOT NULL, "
                    + MovieInfoEntry.COL_TITLE + " TEXT NOT NULL, "

                    + MovieInfoEntry.COL_RELEASEDATE + " INTEGER NOT NULL, "
                    + MovieInfoEntry.COL_OVERVIEW  + " TEXT NOT NULL, "

                    + MovieInfoEntry.COL_POSTERLINK + " TEXT NOT NULL, "
                    + MovieInfoEntry.COL_VIDEOLINK + " TEXT NOT NULL "
                + ");";


        //+++++++++++++++++++++++++++++++++++
//        final String SQL_CREATE_TABLE_MOVIE =
//            "CREATE TABLE " + MovieEntry.TABLE_NAME
//                + " ("
//                    + MovieEntry._ID + " INTEGER PRIMARY KEY, "
//                    + MovieEntry.COL_MOVIE_ID + " INTEGER UNIQUE NOT NULL, "
//                    + MovieEntry.COL_TITLE + " TEXT NOT NULL, "
//                    + MovieEntry.COL_VOTECOUNT + " INTEGER NOT NULL, "
//                    + MovieEntry.COL_RATING + " REAL NOT NULL, "
//                    + MovieEntry.COL_RELEASEDATE + " TEXT NOT NULL, "
//                    + MovieEntry.COL_OVERVIEW + " TEXT NOT NULL "
//                +    ");";
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
//        db.execSQL(SQL_CREATE_TABLE_RATING);
        db.execSQL(SQL_CREATE_TABLE_MOVIEINFO);
     //   db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
