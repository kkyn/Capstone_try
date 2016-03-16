package com.example.android.myproject_2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/* */

import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.PopularEntry;
//import com.example.android.myproject_2.data.MovieContract.RatingEntry;
//import com.example.android.myproject_2.data.MovieContract.MovieEntry;

/* */
/**
 * Created by kkyin on 22/1/2016.
 */
// * Content providers implement functionality based upon URIs passed to them.
// * The defined URIs for this content provider will be used to
//      execute different types of operations against the underlying SQL database.
public class MovieProvider extends ContentProvider {

    private MovieSQLiteOpenHelper mMovie_SQLiteOpenHelper = null;

    // Step-3 ... URIMatcher
    // * Constants used by the URImatcher.
    // * Constants used to tie each URI type internally.
    // * Can easily use them in switch statements.
    static final int POPULAR_ = 100;
    static final int POPULAR_MOVIEID = 101;
    static final int MOVIEINFO_ = 104;

    static final int RATING_DIR = 102;
    static final int RATING_MOVIEID = 103;
    static final int MOVIE_DIR = 105;

    // * http://developer.android.com/reference/android/content/UriMatcher.html
    // The URI Matcher used by this content provider / MovieProvider
    // * Android provides UriMatcher-class to help match incoming URIs to the
    //      content provider integer constants.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // * UriMatcher -- a tool which Android provides to help write a content provider
    // * UriMatcher -- Utility class to aid in matching URIs in content providers.
    // * UriMatcher provides for an expression syntax to match various URIs that
    //      works a bit like regular expressions.
    // * e.g. path/# --> will match a number.
    // * e.g. path/* --> will match any string.
    // Step-3a
    static UriMatcher buildUriMatcher() {

        // e.g. "com.example.android.myproject_2"
        String authority = MovieContract.AUTHORITY;

        // e.g. "com.example.android.myproject_2/Popularity"
        // e.g. "com.example.android.myproject_2/Popularity/*"
        // e.g. "com.example.android.myproject_2/Rating"
        // e.g. "com.example.android.myproject_2/Rating/*"
        // e.g. "com.example.android.myproject_2/MovieInfo"
        // e.g. "com.example.android.myproject_2/Movie"
        UriMatcher aUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        aUriMatcher.addURI(authority, MovieContract.POPULAR, POPULAR_);
        aUriMatcher.addURI(authority, MovieContract.POPULAR + "/*", POPULAR_MOVIEID);

        aUriMatcher.addURI(authority, MovieContract.RATING,             RATING_DIR);
        aUriMatcher.addURI(authority, MovieContract.RATING + "/*",      RATING_MOVIEID);

        aUriMatcher.addURI(authority, MovieContract.MOVIEINFO, MOVIEINFO_);

        aUriMatcher.addURI(authority, MovieContract.MOVIE,      MOVIE_DIR);

        return aUriMatcher;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // The SQLiteQueryBuilder -- a convience class that helps build SQL queries
    //                      to be sent to SQLiteDatabase objects.
    private static final SQLiteQueryBuilder mPopularMovies_SQLiteQueryBuilder;
    //private static final SQLiteQueryBuilder mRatedMovies_SQLiteQueryBuilder;
    //private static final SQLiteQueryBuilder mMovie_SQLiteQueryBuilder;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    static {
        mPopularMovies_SQLiteQueryBuilder = new SQLiteQueryBuilder();

        // .setTables -- Sets the list of tables to query.
        mPopularMovies_SQLiteQueryBuilder.setTables(
            PopularEntry.TABLE_NAME + " INNER JOIN " + MovieInfoEntry.TABLE_NAME
                + " ON "
                + PopularEntry.TABLE_NAME + "." + PopularEntry.COL_MV_ID
                + " = "
                + MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry.COL_ID
        );
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    static {
//        mRatedMovies_SQLiteQueryBuilder = new SQLiteQueryBuilder();
//
//        // .setTables -- Sets the list of tables to query.
//        mRatedMovies_SQLiteQueryBuilder.setTables(
//            RatingEntry.TABLE_NAME + " INNER JOIN " + MovieInfoEntry.TABLE_NAME
//                + " ON "
//                + RatingEntry.TABLE_NAME + "." + RatingEntry.COL_MV_ID
//                + " = "
//                + MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry.COL_MV_ID
//        );
//    }




    // ContentResolver > Content-Provider > "DATABASE"
    // private Cursor getPopularMoviesData (Uri uri, String[] projection, String sortOrder) {
    private Cursor getPopularMoviesData(
        Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return mPopularMovies_SQLiteQueryBuilder.query
                (
                    mMovie_SQLiteOpenHelper.getReadableDatabase()
                    , projection
                    , selection
                    , selectionArgs
                    , null
                    , null
                    , sortOrder
                );
    }

//    private Cursor getMovieInfoData(
//        Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder
//    ) {
//        return
//
//    }

    // Popularity.MovieID = ?
    private static final String sPopular_MovieId_Selection =
        PopularEntry.TABLE_NAME + "." + PopularEntry.COL_MV_ID + " = ?";

    // ContentResolver > Content-Provider > "DATABASE"
    // This method is to be used in Content-Provider's query method
    private Cursor getPopularMovieData_fromMovieId(Uri uri, String[] projection, String sortOrder) {

        // get the last segment in the uri
        String mMovieId = PopularEntry.getMovieId_fromUri(uri);

        // call database and query for the row with such mMovieId
        return mPopularMovies_SQLiteQueryBuilder.query(
            mMovie_SQLiteOpenHelper.getReadableDatabase()
            , projection
            , sPopular_MovieId_Selection
            , new String[]{mMovieId}
            , null
            , null
            , sortOrder
        );
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    static {
//        mMovie_SQLiteQueryBuilder = new SQLiteQueryBuilder();
//
//        mMovie_SQLiteQueryBuilder.setTables(
//            MovieEntry.TABLE_NAME
//        );
//    }
/*
    // movie.votecount = ?
    private static final String sMovie_VoteCount_Selection =
        MovieEntry.TABLE_NAME + "."+ MovieEntry.COL_VOTECOUNT + " = ? ";

    private Cursor getMovieBy_VoteCount
        ( Uri uri, String[] projection, String sortOrder )
    {
// get
        return null;
    }
*/


   // @Nullable
    @Override
    public String getType(Uri uri) {

        // Use the Uri Mather to determine what kind of URI this is.
        final int aMatch = sUriMatcher.match(uri);

        switch (aMatch) {

            case POPULAR_           :   return PopularEntry.POPULARITY_MULTI_ITEM_CURSOR;
            case POPULAR_MOVIEID    :   return PopularEntry.POPULARITY_SINGLE_ITEM_CURSOR;

            case MOVIEINFO_         :   return MovieInfoEntry.DIR_CURSOR_MOVIEINFO;

            //    case RATING_DIR         :   return RatingEntry.RATING_MULTI_ITEM_CURSOR;
            //    case RATING_MOVIEID     :   return RatingEntry.RATING_SINGLE_ITEM_CURSOR;

            //    case MOVIE_DIR:         return MovieEntry.DIR_CURSOR_MOVIE;

            default:
                throw new UnsupportedOperationException("Error: Unknown Uri " + uri);
        }
        //return null;
    }
    // ContentResolver > "CONTENT PROVIDER" > Database
    //@Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        // The switch statement gets a URI, it will determine what kind of request it is,
        // and query the database accordingly.
        switch (sUriMatcher.match(uri)) {

            //  Popularity/*
            case POPULAR_MOVIEID: {
                retCursor = getPopularMovieData_fromMovieId(uri, projection, sortOrder);
                break;
            }
            // Popularity/
             case POPULAR_: {
                retCursor = getPopularMoviesData(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case MOVIEINFO_: {
                // retCursor = null;
                retCursor = mMovie_SQLiteOpenHelper.getReadableDatabase()
                                .query(
                                        MovieInfoEntry.TABLE_NAME,
                                        projection,
                                        selection,
                                        selectionArgs,
                                        null,
                                        null,
                                        sortOrder
                                    );
                break;
            }
            /*
            //  Rating/*
            case RATING_DIR: {
                retCursor = null;
                break;
            }
            // Movie/
            case MOVIE_DIR: {
                retCursor = null;
                break;
            }
            */
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        // to uncomment later -- retCursor cannot be null
        //retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }



    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase mSqlDb = mMovie_SQLiteOpenHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case POPULAR_: {
                rowsUpdated = mSqlDb.update(PopularEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase mSqlDb = mMovie_SQLiteOpenHelper.getWritableDatabase();
        Uri retUri;

        switch (sUriMatcher.match(uri)) {
            //---------------------
            case POPULAR_: {
                long _id = mSqlDb.insert(PopularEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    retUri = PopularEntry.buildUriPopularityWithId(_id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            //---------------------
            case MOVIEINFO_: {
                long _id = mSqlDb.insert(MovieInfoEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    retUri = MovieInfoEntry.buildUri_MovieInfo_Id(_id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;

    }

    // + onCreate runs on the UI thread, so you should avoid executing any long-lasting tasks in this method.
    // + Your content provider is usually created at the start of your app.
    @Override
    public boolean onCreate() {

        // Create/Reference the Database ( movie.db ) associated to this content provider
        //
        mMovie_SQLiteOpenHelper = new MovieSQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase mSqlDb = mMovie_SQLiteOpenHelper.getWritableDatabase();
        int rowsDeleted;

        if (selection == null) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case POPULAR_: {
                rowsDeleted = mSqlDb.delete(PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


}
