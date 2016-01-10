package com.example.android.myproject_1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieContract.StaticInfo.TABLE_NAME + " (" +
                        MovieContract.StaticInfo._ID + " INTEGER PRIMARY KEY, " +

                        MovieContract.StaticInfo.COL_MV_ID + " INTEGER NOT NULL, " +
                        MovieContract.StaticInfo.COL_MV_LINK + " TEXT UNIQUE NOT NULL, " +
                        MovieContract.StaticInfo.COL_MV_NAME + " TEXT NOT NULL, " +
                        MovieContract.StaticInfo.COL_MV_RELEASEDATE + " INTEGER NOT NULL, " +
                        MovieContract.StaticInfo.COL_MV_POSTER_LINK + " TEXT NOT NULL, " +
                        MovieContract.StaticInfo.COL_MV_VIDEO_LINK + " TEXT NOT NULL, " +
                        MovieContract.StaticInfo.COL_MV_SYNOPSIS + " TEXT NOT NULL " +
                        ") ;";

        final String SQL_CREATE_POPULAR_TABLE =
                "CREATE TABLE " + MovieContract.DynamicPInfo.TABLE_NAME + " (" +
                        MovieContract.DynamicPInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                        MovieContract.DynamicPInfo.COL_MV_KEY + " INTEGER NOT NULL, " +
                        " FOREIGN KEY (" + MovieContract.DynamicPInfo.COL_MV_KEY + ") REFERENCES " +
                        MovieContract.StaticInfo.TABLE_NAME + " (" + MovieContract.StaticInfo.COL_MV_ID +
                        ")" +
                        " );";


        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_POPULAR_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
