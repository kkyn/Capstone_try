package com.example.android.myproject_2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myproject_2.data.MovieContract.SortByEntry;
import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;

/**
 * Created by kkyin on 8/1/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Call only one time, to build the database table(s)
    //
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Define the format of the Database table
        final String SQL_CREATE_SORTBY_TABLE =

                "CREATE TABLE " + SortByEntry.TABLE_NAME
                        + " ("
                        + SortByEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                        + SortByEntry.COL_SETTING_SORTBY + " TEXT NOT NULL, "
                        + SortByEntry.COL_MOVIE_ID + " INTEGER NOT NULL, "

                        // COL_MOVIE_ID contain foreignKey(s)/ID(s)
                        // that are movies' ID(s) in COL_ID
                        //
                        + " FOREIGN KEY (" + SortByEntry.COL_MOVIE_ID + ")"
                            + " REFERENCES " + MovieInfoEntry.TABLE_NAME
                                + " (" + MovieInfoEntry.COL_ID + ") "
                        + ");";

        // Define the format of the Database table
        final String SQL_CREATE_MOVIEINFO_TABLE =

                "CREATE TABLE " + MovieInfoEntry.TABLE_NAME
                        + " ("
                        + MovieInfoEntry._ID + " INTEGER PRIMARY KEY, "

                        + MovieInfoEntry.COL_ID + " INTEGER NOT NULL, "
                        + MovieInfoEntry.COL_TITLE + " TEXT NOT NULL, "

                        + MovieInfoEntry.COL_RELEASE_DATE + " INTEGER NOT NULL, "
                        + MovieInfoEntry.COL_OVERVIEW  + " TEXT NOT NULL, "

                        + MovieInfoEntry.COL_POSTER_LINK + " TEXT NOT NULL, "
                        + MovieInfoEntry.COL_VIDEO_LINK + " TEXT NOT NULL "
                        + ");";


        // Build the database table
        db.execSQL(SQL_CREATE_SORTBY_TABLE);
        db.execSQL(SQL_CREATE_MOVIEINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
