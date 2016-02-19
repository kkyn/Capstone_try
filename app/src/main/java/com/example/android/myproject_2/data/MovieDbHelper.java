package com.example.android.myproject_2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myproject_2.data.MovieContract.SortByEntry;
import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;

/**
 * Created by kkyin on 8/1/2016.
 *
 * Contains code to create and initialize the movie database.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the schema, you must increment the database version.
    // Must be manually incremented, each time we release an updated APK with a new database schema
    private static final int DATABASE_VERSION = 1;

    // Actual database filename in the file system
    static final String DATABASE_NAME = "movie.db";

    // Constructor for "MovieDbHelper".
    // "DATABASE_NAME", "DATABASE_VERSION" are passed in to initialize the 'database helper'
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // * Call only 1 time.
    // * Create database table(s), SQLiteOpenHelper's onCreate will be called, to build the database table(s).
    // * The SQL and MovieContract-class are used together,
    // to write the correct SQL statement string to create the correct database table.
    //
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Define the format of the Database table
        //
        final String SQL_CREATE_SORTBY_TABLE =
                "CREATE TABLE " + SortByEntry.TABLE_SORTBY +
                        " ("
                        + SortByEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                        + SortByEntry.COL_SORTBYSETTING + " TEXT NOT NULL, " // constraint
                        + SortByEntry.COL_MOVIEID + " INTEGER NOT NULL, " // constraint

                        // * Set the COL_MOVIEID-column as containing foreignKey(s)/ID(s)
                        // to reference movies' ID(s) in MovieInfoEntry.COL_ID
                        //
                        // * definition of a FOREIGN KEY constraint...
                        + " FOREIGN KEY (" + SortByEntry.COL_MOVIEID + ")"
                            + " REFERENCES " + MovieInfoEntry.TABLE_MOVIEINFO + " (" + MovieInfoEntry.COL_ID + ") "
                        + ");";

        // Define the format of the Database table
        final String SQL_CREATE_MOVIEINFO_TABLE =

                "CREATE TABLE " + MovieInfoEntry.TABLE_MOVIEINFO
                        + " ("
                        + MovieInfoEntry._ID + " INTEGER PRIMARY KEY, "

                        + MovieInfoEntry.COL_ID + " INTEGER UNIQUE NOT NULL, "
                        + MovieInfoEntry.COL_TITLE + " TEXT NOT NULL, "

                        + MovieInfoEntry.COL_RELEASEDATE + " INTEGER NOT NULL, "
                        + MovieInfoEntry.COL_OVERVIEW  + " TEXT NOT NULL, "

                        + MovieInfoEntry.COL_POSTERLINK + " TEXT NOT NULL, "
                        + MovieInfoEntry.COL_VIDEOLINK + " TEXT NOT NULL "
                        + ");";


        // Have the system to execute the "SQL...", to build the database table
        db.execSQL(SQL_CREATE_SORTBY_TABLE);
        db.execSQL(SQL_CREATE_MOVIEINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
