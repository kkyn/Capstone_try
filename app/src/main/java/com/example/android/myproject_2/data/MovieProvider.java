package com.example.android.myproject_2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by kkyin on 22/1/2016.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider / MovieProvider
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Constants used by the URImatcher
    static final int CONSTANT_MOVIE_SELECT = 100;
    static final int CONSTANT_MOVIE_WITH_INFO = 101;
    static final int CONSTANT_MOVIE_INFO = 102;


    // 'UriMatcher' --- a tool which Android provides to help write a content provider
    static UriMatcher buildUriMatcher() {

        UriMatcher aUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        sUriMatcher.addURI(authority, MovieContract.POPULARITY, CONSTANT_MOVIE_SELECT);

        sUriMatcher.addURI(authority, MovieContract.MOVIEINFO, CONSTANT_MOVIE_INFO);

        return aUriMatcher;
    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }
}
