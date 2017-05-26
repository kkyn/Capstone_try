package com.example.android.myproject_2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myproject_2.data.MovieContract.X_MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.X_MovieReviewEntry;
import com.example.android.myproject_2.data.MovieContract.X_MovieVideosEntry;

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

    private static final String TYPE_TEXT_NOT_NULL = " TEXT NOT NULL";
    private static final String TYPE_INT_NOT_NULL = " INTEGER NOT NULL";
    private static final String TYPE_INT_PRIMARY_KEY    = " INTEGER PRIMARY KEY";
    //private static final String TYPE_INT_UNIQUE_NOT_NULL = " INTEGER UNIQUE NOT NULL";
    private static final String AUTOINCREMENT           = " AUTOINCREMENT";
    private static final String COMMA                   = ", ";

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
        final String SQL_CREATE_TABLE_X_MOVIE_INFO =

                "CREATE TABLE " + X_MovieInfoEntry.TABLE_NAME
                   + " ("
                   + X_MovieInfoEntry._ID               + TYPE_INT_PRIMARY_KEY + AUTOINCREMENT + COMMA
                   + X_MovieInfoEntry.COL_MV_ID         + TYPE_INT_NOT_NULL     + COMMA //" INTEGER UNIQUE NOT NULL, " -or- " INTEGER NOT NULL, "
                   + X_MovieInfoEntry.COL_TITLE         + TYPE_TEXT_NOT_NULL    + COMMA
                   + X_MovieInfoEntry.COL_RELEASEDATE   + TYPE_TEXT_NOT_NULL    + COMMA //" INTEGER NOT NULL, "
                   + X_MovieInfoEntry.COL_POPULARITY    + TYPE_TEXT_NOT_NULL    + COMMA
                   + X_MovieInfoEntry.COL_VOTE_AVERAGE  + TYPE_TEXT_NOT_NULL    + COMMA //" REAL NOT NULL, "
                   + X_MovieInfoEntry.COL_VOTE_COUNT    + TYPE_INT_NOT_NULL     + COMMA //" INTEGER NOT NULL, "
                   + X_MovieInfoEntry.COL_FAVOURITES    + TYPE_INT_NOT_NULL     + COMMA
                   + X_MovieInfoEntry.COL_OVERVIEW      + TYPE_TEXT_NOT_NULL    + COMMA
                   + X_MovieInfoEntry.COL_POSTERLINK    + TYPE_TEXT_NOT_NULL    + COMMA
                   + X_MovieInfoEntry.COL_BACKDROP_PATH + TYPE_TEXT_NOT_NULL
                   + ");";

        final String SQL_CREATE_TABLE_X_MOVIE_REVIEW =
                "CREATE TABLE " + X_MovieReviewEntry.TABLE_NAME
                    + " ("
                    + X_MovieReviewEntry._ID                + TYPE_INT_PRIMARY_KEY + AUTOINCREMENT + COMMA //" INTEGER PRIMARY KEY, "

                    + X_MovieReviewEntry.COL_MV_ID          + TYPE_INT_NOT_NULL + COMMA //" INTEGER UNIQUE NOT NULL, "

                    + X_MovieReviewEntry.COL_REVIEWER 	    + TYPE_TEXT_NOT_NULL + COMMA
                    + X_MovieReviewEntry.COL_REVIEWCONTENT 	+ TYPE_TEXT_NOT_NULL

                    + ");";

        final String SQL_CREATE_TABLE_X_MOVIE_VIDEO =
                "CREATE TABLE " + MovieContract.X_MovieVideosEntry.TABLE_NAME
                    + " ("
                    + X_MovieVideosEntry._ID            + TYPE_INT_PRIMARY_KEY + AUTOINCREMENT + COMMA
                    + X_MovieVideosEntry.COL_MV_ID      + TYPE_INT_NOT_NULL     + COMMA // TYPE_INT_UNIQUE_NOT_NULL
                    + X_MovieVideosEntry.COL_VIDEO_KEY  + TYPE_INT_NOT_NULL
                    + " )";

        // Have the system to execute the "SQL...", to build the database table

        db.execSQL(SQL_CREATE_TABLE_X_MOVIE_REVIEW);
        db.execSQL(SQL_CREATE_TABLE_X_MOVIE_INFO);
        db.execSQL(SQL_CREATE_TABLE_X_MOVIE_VIDEO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
