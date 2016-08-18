package com.example.android.myproject_2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.PopularEntry;
import com.example.android.myproject_2.data.MovieContract.RatingEntry;

/**
 * Created by kkyin on 22/1/2016.
 */
// * Content providers implement functionality based upon URIs passed to them.
// * The defined URIs for this content provider will be used to
//      execute different types of operations against the underlying SQL database.
public class MovieProvider extends ContentProvider {

    public static final String LOG_TAG = MovieProvider.class.getSimpleName(); // tky add
    private MovieSQLiteOpenHelper mvsSqLtOpnHlpr = null;

    // Step-3 ... URIMatcher
    // * Constants used by the URImatcher.
    // * Constants used to tie each URI type internally.
    // * Can easily use them in switch statements.
    static final int POPULAR_ = 100;
    static final int POPULAR_MOVIEID = 101;

    static final int MOVIEINFO_ = 104;

    static final int RATING_ = 102;
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
        //aUriMatcher.addURI(authority, MovieContract.POPULAR + "/#", POPULAR_MOVIEID);
        aUriMatcher.addURI(authority, MovieContract.POPULAR + "/*", POPULAR_MOVIEID);
        //aUriMatcher.addURI(authority, MovieContract_x.POPULAR + "/*", POPULAR_MOVIEID);

        aUriMatcher.addURI(authority, MovieContract.MOVIEINFO, MOVIEINFO_);

        aUriMatcher.addURI(authority, MovieContract.RATING, RATING_);
        //aUriMatcher.addURI(authority, MovieContract.RATING + "/#", RATING_MOVIEID);
        aUriMatcher.addURI(authority, MovieContract.RATING + "/*", RATING_MOVIEID);
        //aUriMatcher.addURI(authority, MovieContract_x.MOVIE,      MOVIE_DIR);

        return aUriMatcher;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // The SQLiteQueryBuilder -- a convience class that helps build SQL queries
    //                      to be sent to SQLiteDatabase objects.
    private static final SQLiteQueryBuilder popularSqLtQryBldr;
    private static final SQLiteQueryBuilder ratedSQLtQryBuldr;
    //private static final SQLiteQueryBuilder mMovie_SQLiteQueryBuilder;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    static {
        popularSqLtQryBldr = new SQLiteQueryBuilder();

        // .setTables -- Sets the list of tables to query.
        popularSqLtQryBldr.setTables(/**/
            /**/
            MovieInfoEntry.TABLE_NAME
                + " INNER JOIN " + PopularEntry.TABLE_NAME
                + " ON "
                + MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
                + " = "
                + PopularEntry.TABLE_NAME + "." + PopularEntry.COL_KEY_ID
            /**/
            /*
            PopularEntry.TABLE_NAME
            + " INNER JOIN " + MovieInfoEntry.TABLE_NAME
                + " ON "
                + "(" + PopularEntry.TABLE_NAME + "." + PopularEntry.COL_KEY_ID
                + " = "
                + MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
                + ");"
            */
            /**
             PopularEntry.TABLE_NAME
             **/


        );
    }

    static {
        ratedSQLtQryBuldr = new SQLiteQueryBuilder();

        ratedSQLtQryBuldr.setTables(

            MovieInfoEntry.TABLE_NAME
                + " INNER JOIN " + RatingEntry.TABLE_NAME
                + " ON "
                + MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry._ID
                + " = "
                + RatingEntry.TABLE_NAME + "." + RatingEntry.COL_KEY_ID
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
//                + RatingEntry.TABLE_NAME + "." + RatingEntry.COL_KEY_ID
//                + " = "
//                + MovieInfoEntry.TABLE_NAME + "." + MovieInfoEntry.COL_KEY_ID
//        );
//    }


    // ContentResolver > Content-Provider > "DATABASE"
    // private Cursor getPopularMoviesData (Uri uri, String[] projection, String sortOrder) {
    private Cursor getPopularMoviesData(
        Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Log.d(LOG_TAG, "---- INTO getPopularMoviesData() ---- call SQLiteQueryBuilder.query()"); // tky add

        return popularSqLtQryBldr.query(
            mvsSqLtOpnHlpr.getReadableDatabase()
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
        //PopularEntry.TABLE_NAME + "." + PopularEntry.COL_KEY_ID + " = ?";

    // ContentResolver > Content-Provider > "DATABASE"
    // This method is to be used in Content-Provider's query method
    private Cursor getPopularMovieData_fromMovieId(Uri uri, String[] projection, String sortOrder) {

        // get the last segment in the uri
        String mMovieId = PopularEntry.getMovieId_fromUri(uri);

        SQLiteDatabase readSqLtDB = mvsSqLtOpnHlpr.getReadableDatabase();

        //Log.d(LOG_TAG, "-- MovieProvider / getPopularMovieData_fromMovieId --");

        // call database and query for the row with such mMovieId
        return popularSqLtQryBldr.query(
                                    readSqLtDB
                                    , projection
                                    , sPopular_MovieId_Selection
                                    , new String[]{mMovieId}
                                    , null
                                    , null
                                    , sortOrder
        );
    }

    // Rating.MovieID = ?
    private static final String sRating_MovieId_Selection =
            RatingEntry.TABLE_NAME + "." + RatingEntry.COL_MV_ID + " = ?";
            //RatingEntry.TABLE_NAME + "." + RatingEntry.COL_KEY_ID + " = ?";

    // ContentResolver > Content-Provider > "DATABASE"
    // This method is to be used in Content-Provider's query method
    private Cursor getRatingMovieData_fromMovieId(Uri uri, String[] projection, String sortOrder) {

        // get the last segment in the uri
        String mMovieId = RatingEntry.getMovieId_fromUri(uri);

        SQLiteDatabase readSqLtDB = mvsSqLtOpnHlpr.getReadableDatabase();

        // call database and query for the row with such mMovieId
        return ratedSQLtQryBuldr.query(
                                    readSqLtDB
                                    , projection
                                    , sRating_MovieId_Selection
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

    // + onCreate runs on the UI thread, so you should avoid executing any long-lasting tasks in this method.
    // + Your content provider is usually created at the start of your app.
    @Override
    public boolean onCreate() {

        Log.d(LOG_TAG, "---- In MovieProvider / onCreate()  ----");  // tky add
        // Create/Reference the Database ( movie.db ) associated to this content provider
        //
        mvsSqLtOpnHlpr = new MovieSQLiteOpenHelper(getContext());
        return true;
    }

    // @Nullable
    @Override
    public String getType(Uri uri) {

        // Use the Uri Mather to determine what kind of URI this is.
        final int aMatch = sUriMatcher.match(uri);

        switch (aMatch) {

            case POPULAR_:          return PopularEntry.POPULARITY_MULTI_ITEM_CURSOR;
            case POPULAR_MOVIEID:   return PopularEntry.POPULARITY_SINGLE_ITEM_CURSOR;

            case MOVIEINFO_:        return MovieInfoEntry.DIR_CURSOR_MOVIEINFO;

            case RATING_:           return RatingEntry.RATING_MULTI_ITEM_CURSOR;
            case RATING_MOVIEID:    return RatingEntry.RATING_SINGLE_ITEM_CURSOR;
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
        SQLiteDatabase rdblSqLtDB = mvsSqLtOpnHlpr.getReadableDatabase();

        // Log.d(LOG_TAG, "---- Query() - :: " + uri.toString());  // tky add

        // The switch statement gets a URI, it will determine what kind of request it is,
        // and query the database accordingly.
        switch (sUriMatcher.match(uri)) {

            //  Popularity/*
            case POPULAR_MOVIEID: {
                retCursor = getPopularMovieData_fromMovieId(uri, projection, sortOrder);
                break;
            }
            //  Rating/*
            case RATING_MOVIEID: {
                retCursor = getRatingMovieData_fromMovieId(uri, projection, sortOrder);
                break;
            }
            // Popularity/
            case POPULAR_: {
                //Log.d(LOG_TAG, "==>> 264   Query/Popular, Uri: " + uri.toString());  // tky add

                //   retCursor = getPopularMoviesData(uri, projection, selection, selectionArgs, sortOrder);
                retCursor = rdblSqLtDB.query(
                                                PopularEntry.TABLE_NAME,
                                                projection,
                                                selection,
                                                selectionArgs,
                                                null,
                                                null,
                                                sortOrder
                                            );
                // 26th Aprill, add this , error message about the squaredview
                //Log.d(LOG_TAG, "<<== 277   Query/Popular, selection:-----------------------");
//                Log.d(LOG_TAG, "---- 265 In  Query, selection:" + selection + "...selectionArg:" +selectionArgs[0]
//                    + " ... " + " sortOrder:" + sortOrder);  // tky add

                //Log.d(LOG_TAG, "xxxx Out Query, Uri: " + uri.toString());  // tky add
//                retCursor =  popularSqLtQryBldr.query(
//                    mvsSqLtOpnHlpr.getReadableDatabase()
//                    , projection
//                    , selection
//                    , selectionArgs
//                    , null
//                    , null
//                    , sortOrder
//                );
                break;
            }
            case MOVIEINFO_: {
                //Log.d(LOG_TAG, "==>> 294  Query/MovieInfo, Uri: " + uri.toString());  // tky add
                //Log.d(LOG_TAG, "==>> 295  Query/MovieInfo, selection:" + selection + "...selectionArg:"+selectionArgs[0] + " ... " + " sortOrder:" + sortOrder );
                //Log.d(LOG_TAG, "---- In  Query, MovieInfoUri: " + uri.toString());  // tky add
                // retCursor = null;
                retCursor = rdblSqLtDB.query(
                                            MovieInfoEntry.TABLE_NAME,
                                            projection,
                                            selection,
                                            selectionArgs,
                                            null,
                                            null,
                                            sortOrder
                                        );

                //Log.d(LOG_TAG, "<<== 308   Query/MovieInfo, selection:-----------------------");
                //Log.d(LOG_TAG, "xxxx Out Query, MovieInfoUri: " + uri.toString());  // tky add
                break;
            }

            //  Rating
            case RATING_: {
                /**/
                // Log.d(LOG_TAG, "---- RatingUri: " + uri.toString());  // tky add
                retCursor = rdblSqLtDB.query(
                                            RatingEntry.TABLE_NAME,
                                            projection,
                                            selection,
                                            selectionArgs,
                                            null,
                                            null,
                                            sortOrder
                                        );
                 /**/
                //retCursor = null;
                break;
            }

            /*
            // Movie/
            case MOVIE_DIR: {
                retCursor = null;
                break;
            }
            */
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (retCursor == null) {
           // Log.d(LOG_TAG, "---- retCursor is NULL");
        } else {
           // Log.d(LOG_TAG, "---- retCursor is not NULL");
        }
        // to uncomment later -- retCursor cannot be null
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return retCursor;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase wrtblSqlDb = mvsSqLtOpnHlpr.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case POPULAR_: {
                rowsUpdated = wrtblSqlDb.update(PopularEntry.TABLE_NAME, values, selection, selectionArgs);

                //Log.d(LOG_TAG, "---- update/uri: " + uri.toString() + " ++++ rowsUpdated: " + rowsUpdated); // tky add
                break;
            }
            case MOVIEINFO_: {
                rowsUpdated = wrtblSqlDb.update(MovieInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case RATING_: {
                rowsUpdated = wrtblSqlDb.update(RatingEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //getContext().getContentResolver().notifyChange(uri, null);
        if (rowsUpdated != 0) {
            //    if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
        //return 0;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase wrtblSqLtDb = mvsSqLtOpnHlpr.getWritableDatabase();
        Uri retUri;

        switch (sUriMatcher.match(uri)) {
            //---------------------
            case POPULAR_: {

                //Log.d(LOG_TAG, "----- insert()/POPULAR");
                long _id = wrtblSqLtDb.insert(PopularEntry.TABLE_NAME, null, values);
                //Log.d(LOG_TAG, "----- insert()/POPULAR inserted into PopularEntry _id = " + _id);
                if (_id > 0) {
                    retUri = PopularEntry.buildUri_PopularityWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            //---------------------
            case MOVIEINFO_: {
                //Log.d(LOG_TAG, "----- insert()/MOVIEINFO");
                long _id = wrtblSqLtDb.insert(MovieInfoEntry.TABLE_NAME, null, values);
                //Log.d(LOG_TAG, "----- insert()/MOVIEINFO inserted into MovieInfoEntry _id = " + _id);
                if (_id > 0) {
                    retUri = MovieInfoEntry.buildUri_MovieInfo_Id(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            //---------------------
            case RATING_: {
                long _id = wrtblSqLtDb.insert(RatingEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    retUri = RatingEntry.buildUri_RatingWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            }
            break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;

    }


    // e.g. Uri == "content://authority/table-name/row-ID" , row-Id is needed if a specific row is to be deleted
    // + selection, (a String) == An optional restriction to apply to rows when deleting.
    // + return int == the number of rows deleted
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase wrtblSqLtDb = mvsSqLtOpnHlpr.getWritableDatabase();
        int rowsDeleted;

        if (selection == null) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case POPULAR_: {
                rowsDeleted = wrtblSqLtDb.delete(PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MOVIEINFO_: {
                rowsDeleted = wrtblSqLtDb.delete(MovieInfoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case RATING_: {
                rowsDeleted = wrtblSqLtDb.delete(RatingEntry.TABLE_NAME, selection, selectionArgs);
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

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase wrtblSqLtDb = mvsSqLtOpnHlpr.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int returnCount = 0;

        switch (match) {
            case POPULAR_: {
                wrtblSqLtDb.beginTransaction();
                returnCount = 0;
               // int returnCount = 0;
                try {
                    for (ContentValues mvalue : values) {
                        long _id = wrtblSqLtDb.insert(MovieContract.PopularEntry.TABLE_NAME, null, mvalue);  // ??
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    wrtblSqLtDb.setTransactionSuccessful();
                } finally {
                    wrtblSqLtDb.endTransaction();
                }
             ///   getContext().getContentResolver().notifyChange(uri, null);
             ///   return returnCount;
            }
            break;
            //
            case MOVIEINFO_: {
                wrtblSqLtDb.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues mvalue : values) {
                        long _id = wrtblSqLtDb.insert(MovieContract.MovieInfoEntry.TABLE_NAME, null, mvalue);  // ??
                        if (_id != -1) {
                            returnCount++;

                        }
                    }
                    wrtblSqLtDb.setTransactionSuccessful();
                } finally {
                    wrtblSqLtDb.endTransaction();
                }
            }
            break;
            default:
                //return super.bulkInsert(uri, values);
                returnCount = super.bulkInsert(uri, values);
                break;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}