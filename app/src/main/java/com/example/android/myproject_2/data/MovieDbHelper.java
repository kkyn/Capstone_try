package com.example.android.myproject_2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myproject_2.data.MovieContract.MovieSelectEntry;
import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;

/**
 * Created by kkyin on 8/1/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    //    private static final int version = 1;
    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//    public MovieDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }

    // Call one time, build the database table(s)
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Define the format of the Database table
        final String SQL_CREATE_GROUP_SETTINGS_TABLE =

                "CREATE TABLE " + MovieSelectEntry.TABLE_NAME +
                " ("
                        + MovieSelectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                        + MovieSelectEntry.COL_MV_NAME + " TEXT NOT NULL, "

                        + MovieSelectEntry.COL_MV_ID + " INTEGER NOT NULL, "

                        + " FOREIGN KEY (" + MovieSelectEntry.COL_MV_ID + ")"
                        + " REFERENCES " + MovieInfoEntry.TABLE_NAME +
                            " (" + MovieInfoEntry.COL_MV_KEY + ") "
                        +
                ");";

        final String SQL_CREATE_MOVIE_INFO_TABLE =

                "CREATE TABLE " + MovieInfoEntry.TABLE_NAME +
                        " ("
                        + MovieInfoEntry._ID + " INTEGER PRIMARY KEY, "

                        + MovieInfoEntry.COL_MV_KEY + " INTEGER NOT NULL, "
                        + MovieInfoEntry.COL_MV_LINK + " TEXT UNIQUE NOT NULL, "
                        + MovieInfoEntry.COL_MV_RELEASE_DATE + " INTEGER NOT NULL, "
                        + MovieInfoEntry.COL_MV_POSTER_LINK + " TEXT NOT NULL, "
                        + MovieInfoEntry.COL_MV_VIDEO_LINK + " TEXT NOT NULL, "
                        + MovieInfoEntry.COL_MV_SYNOPSIS + " TEXT NOT NULL " +
                        ");";

//        // Define the format of the Database table
//        final String SQL_CREATE_SORTBY_TABLE =
//                "CREATE TABLE " + MovieContract.MovieSelectEntry.TABLE_NAME +
//                " (" + MovieContract.MovieSelectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                        + MovieContract.MovieSelectEntry.COL_MV_GROUP + " INTEGER NOT NULL, "
//                        + " FOREIGN KEY (" + MovieContract.MovieSelectEntry.COL_MV_GROUP +
//                            ") REFERENCES " + MovieContract.MovieInfoEntry.TABLE_NAME +
//                            " ("
//                        + MovieContract.MovieInfoEntry.COL_MV_GROUP
//                        + ") " +
//                ");";
//
//        final String SQL_CREATE_MOVIE_TABLE =
//                "CREATE TABLE " + MovieContract.MovieInfoEntry.TABLE_NAME +
//                        " ("
//                        + MovieContract.MovieInfoEntry._ID + " INTEGER PRIMARY KEY, "
//                        + MovieContract.MovieInfoEntry.COL_MV_GROUP + " INTEGER NOT NULL, "
//                        + MovieContract.MovieInfoEntry.COL_MV_LINK + " TEXT UNIQUE NOT NULL, "
//                        + MovieContract.MovieInfoEntry.COL_MV_RATINGS +  " INTEGER NOT NULL, "
//                        + MovieContract.MovieInfoEntry.COL_MV_POPULARITY + " INTEGER NOT NULL, "
//                //        + MovieContract.MovieInfoEntry.COL_MV_NAME + " TEXT NOT NULL, "
//                        + MovieContract.MovieInfoEntry.COL_MV_RELEASE_DATE + " INTEGER NOT NULL, "
//                        + MovieContract.MovieInfoEntry.COL_MV_POSTER_LINK + " TEXT NOT NULL, "
//                        + MovieContract.MovieInfoEntry.COL_MV_VIDEO_LINK + " TEXT NOT NULL, "
//                        + MovieContract.MovieInfoEntry.COL_MV_SYNOPSIS + " TEXT NOT NULL " +
//                        ");";



        // Build the database table
        db.execSQL(SQL_CREATE_MOVIE_INFO_TABLE);
        db.execSQL(SQL_CREATE_GROUP_SETTINGS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
