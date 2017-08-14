package com.example.android.fnlprjct.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;
import com.example.android.fnlprjct.data.MovieContract.MovieReviewEntry;
import com.example.android.fnlprjct.data.MovieContract.MovieVideosEntry;
import com.example.android.fnlprjct.data.MovieContract.MovieFavouritesEntry;


// * Content providers implement functionality based upon URIs passed to them.
// * The defined URIs for this content provider will be used to
//      execute different types of operations against the underlying SQL database.
public class MovieProvider extends ContentProvider {

    public static final String LOG_TAG = MovieProvider.class.getSimpleName(); // tky add

    private MovieSQLiteOpenHelper mvsSqLtOpnHlpr = null;

    static final String CURSOR_LIMIT = "20";

    // Step-3 ... URIMatcher
    // * Constant Ids used by the URImatcher.
    // * Constant Ids used to tie each URI type internally.
    // * Can easily use them in switch statements.
    static final int CNST_MOVIE_INFO_ = 100;
    static final int CNST_MOVIE_INFO_ID_ = 101;

    static final int CNST_MOVIE_REVIEW_ = 102;
    static final int CNST_MOVIE_REVIEW_ID_ = 103;

    static final int CNST_MOVIE_VIDEO_ = 104;
    static final int CNST_MOVIE_VIDEO_ID_ = 105;

    static final int CNST_MOVIE_FAVOURITES_ = 106;
    static final int CNST_MOVIE_FAVOURITES_ID_ = 107;

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

        // e.g. "com.example.android.fnlprjct"
        String authority = MovieContract.AUTHORITY;

        // e.g. "com.example.android.fnlprjct/Popularity"
        // e.g. "com.example.android.fnlprjct/Popularity/*"
        // e.g. "com.example.android.fnlprjct/Rating"
        // e.g. "com.example.android.fnlprjct/Rating/*"
        // e.g. "com.example.android.fnlprjct/MovieInfo"
        // e.g. "com.example.android.fnlprjct/Movie"
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(authority, MovieContract.MOVIEREVIEW,             CNST_MOVIE_REVIEW_);
      //uriMatcher.addURI(authority, MovieContract.MOVIEREVIEW + "/#",      CNST_MOVIE_REVIEW_ID_);
        uriMatcher.addURI(authority, MovieContract.MOVIEREVIEW + "/*",      CNST_MOVIE_REVIEW_ID_);

        uriMatcher.addURI(authority, MovieContract.MOVIEINFO,               CNST_MOVIE_INFO_);
        uriMatcher.addURI(authority, MovieContract.MOVIEINFO + "/*",        CNST_MOVIE_INFO_ID_);

        uriMatcher.addURI(authority, MovieContract.MOVIEVIDEO,              CNST_MOVIE_VIDEO_);
		uriMatcher.addURI(authority, MovieContract.MOVIEVIDEO + "/*",       CNST_MOVIE_VIDEO_ID_);

        uriMatcher.addURI(authority, MovieContract.MOVIEFAVOURITES,         CNST_MOVIE_FAVOURITES_);
        uriMatcher.addURI(authority, MovieContract.MOVIEFAVOURITES + "/*",  CNST_MOVIE_FAVOURITES_ID_);

        return uriMatcher;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // The SQLiteQueryBuilder -- a convience class that helps build SQL queries
    //                      to be sent to SQLiteDatabase objects.
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private static final SQLiteQueryBuilder mvInfo_SQLiteQryBldr;
    static  {
        mvInfo_SQLiteQryBldr = new SQLiteQueryBuilder();
        mvInfo_SQLiteQryBldr.setTables(MovieInfoEntry.TABLE_NAME);
    }

    private static final SQLiteQueryBuilder mvReviewSQLiteQryBldr;
    static {
        mvReviewSQLiteQryBldr = new SQLiteQueryBuilder();
        // .setTables -- Sets the list of tables to query.
        mvReviewSQLiteQryBldr.setTables(MovieReviewEntry.TABLE_NAME);
    }

    private static final SQLiteQueryBuilder mvVideoSQLiteQryBldr;
    static {
        mvVideoSQLiteQryBldr = new SQLiteQueryBuilder();
        mvVideoSQLiteQryBldr.setTables(MovieVideosEntry.TABLE_NAME);
    }

    private static final SQLiteQueryBuilder mvFavouritesSQLiteQryBldr;
    static {
        mvFavouritesSQLiteQryBldr = new SQLiteQueryBuilder();
        mvFavouritesSQLiteQryBldr.setTables(MovieFavouritesEntry.TABLE_NAME);
    }



    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzz BEGIN zzzzzzzzzzzzzzzzzzzzzzzzzzzzz
    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
    // ContentResolver > Content-Provider > "DATABASE"
    // private Cursor getMovieReviewData (Uri uri, String[] projection, String sortOrder) {
    private Cursor getMovieReviewData(
            Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        /*Log.d(LOG_TAG, "---- INTO getMovieReviewData() ---- call SQLiteQueryBuilder.query()");*/ // tky add


        return mvReviewSQLiteQryBldr.query(
                mvsSqLtOpnHlpr.getReadableDatabase()
                , projection
                , selection
                , selectionArgs
                , null
                , null
                , sortOrder
        );
    }

    //------------------------------------//
    //---------- Movie Info --------------//
    //------------------------------------//
    private static final String selectionMovieInfoWithMovieId =
            MovieContract.MovieInfoEntry.TABLE_NAME + "." + MovieContract.MovieInfoEntry.COL_MOVIE_ID + " = ?";

    private Cursor getMovieInfoDataFromMovieId(Uri uri, String[] projection, String sortOrder) {

        String movieId = MovieInfoEntry.getMovieIdFromMovieInfoUri(uri);

        SQLiteDatabase  readSQLiteDB = mvsSqLtOpnHlpr.getReadableDatabase();
        String          selection = selectionMovieInfoWithMovieId;
        String[]        selectionArg = new String[]{movieId};

        return mvInfo_SQLiteQryBldr.query(
                            readSQLiteDB, projection, selection, selectionArg,
                            null, null, sortOrder);

    }
    //------------------------------------//
    //---------- Movie Review ------------//
    //------------------------------------//
    private static final String selectionMovieReviewWithMovieId =
            MovieReviewEntry.TABLE_NAME + "." + MovieContract.MovieReviewEntry.COL_MOVIE_ID + " = ?";

    // ContentResolver > Content-Provider > "DATABASE"
    // This method is to be used in Content-Provider's query method
    //
    // e.g. uri --> "content://com.example.android.fnlprjct/moviereview/123"
    //
    private Cursor getMovieReviewDataFromMovieId(Uri uri, String[] projection, String sortOrder) {

        // get the last segment in the uri
        String mMovieId = MovieReviewEntry.getMovieIdForFromMovieReviewUri(uri);

        SQLiteDatabase readSQLiteDB = mvsSqLtOpnHlpr.getReadableDatabase();

        // call database and query for the row with such mMovieId
        return mvReviewSQLiteQryBldr.query(
                readSQLiteDB
                , projection
                , selectionMovieReviewWithMovieId
                , new String[]{mMovieId}
                , null
                , null
                , sortOrder
        );
    }

    //------------------------------------//
    //---------- Movie Video -------------//
    //------------------------------------//
    // Table_MovieVideo.MovieID = ?
    //
    private static final String selectMovieVideoWithMovieId =
            MovieVideosEntry.TABLE_NAME + "." + MovieContract.MovieVideosEntry.COL_MOVIE_ID + " = ?";
    //
    // e.g. uri --> "content://com.example.android.fnlprjct/movievideo/123"
    //
    private Cursor getMovieVideoKeyFromMovieId(Uri uri, String[] projection, String sortOrder) {

        // get the last segment in the uri
        String movieId = MovieContract.MovieVideosEntry.getMovieIdFromMovieVideoUri(uri);

        SQLiteDatabase readSQLiteDataBase = mvsSqLtOpnHlpr.getReadableDatabase();

        String selection = selectMovieVideoWithMovieId;

        String[] selectionArg = new String[]{movieId};

        // call database and query for the row with such movieId
        return mvVideoSQLiteQryBldr.query(
                      readSQLiteDataBase
                      , projection              // get some columns
                      , selection               // selection
                      , selectionArg            // selectionArg
                      , null
                      , null
                      , sortOrder
              );
    }
    //------------------------------------//
    //---------- Movie Favourites -------------//
    //------------------------------------//
    // Table_MovieFavourites.MovieID = ?
    //

    // + onCreate runs on the UI thread, so you should avoid executing any long-lasting tasks in this method.
    // + Your content provider is usually created at the start of your app.
    @Override
    public boolean onCreate() {

        Log.d(LOG_TAG, "---- In MovieProvider / onCreate()  ----");

        // Create/Reference the Database ( movie.db ) associated to this content provider
        mvsSqLtOpnHlpr = new MovieSQLiteOpenHelper(getContext());
        return true;
    }

    // @Nullable
    @Override
    public String getType(Uri uri) {

        // Use the Uri Mather to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {

			case CNST_MOVIE_REVIEW_:        return MovieReviewEntry.DIR_CURSOR_MOVIEREVIEW;
            case CNST_MOVIE_REVIEW_ID_:     return MovieReviewEntry.ITEM_CURSOR_MOVIEREVIEW;

            case CNST_MOVIE_INFO_:        	return MovieInfoEntry.DIR_CURSOR_MOVIEINFO;
            case CNST_MOVIE_INFO_ID_:       return MovieInfoEntry.ITEM_CURSOR_MOVIEINFO;

            case CNST_MOVIE_VIDEO_:         return MovieVideosEntry.DIR_CURSOR_MOVIEVIDEO;
            case CNST_MOVIE_VIDEO_ID_:      return MovieVideosEntry.ITEM_CURSOR_MOVIEVIDEO;

            case CNST_MOVIE_FAVOURITES_:    return MovieFavouritesEntry.FAVOURITES_MULTI_ITEM_CURSOR;
            case CNST_MOVIE_FAVOURITES_ID_: return MovieFavouritesEntry.FAVOURITES_SINGLE_ITEM_CURSOR;

            default:
                throw new UnsupportedOperationException("Error: Unknown Uri " + uri);
        }
    }

    //
    // ContentResolver > "CONTENT PROVIDER" > Database
    // reference, https://developer.android.com/reference/android/content/ContentProvider.html
    // reference, https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
    //@Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        SQLiteDatabase rdblSQLiteDB = mvsSqLtOpnHlpr.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        // The switch statement gets a URI, it will determine what kind of request it is,
        // and query the database accordingly.
        switch (match) {

            //---  movieinfo/*
            case CNST_MOVIE_INFO_ID_: {
                 retCursor = getMovieInfoDataFromMovieId(uri, projection, sortOrder);
                break;
            }
		    //---  moviereview/*
            case CNST_MOVIE_REVIEW_ID_: {
                retCursor = getMovieReviewDataFromMovieId(uri, projection, sortOrder);
                break;
            }
            //--- movievideo/*
            case CNST_MOVIE_VIDEO_ID_: {
                retCursor = getMovieVideoKeyFromMovieId(uri, projection, sortOrder);
                break;
            }
            //--- moviefavourites/*
            case CNST_MOVIE_FAVOURITES_ID_:{
                retCursor = null;  /// tky  need to code
            }
            break;

            //---  moviereview/
            case CNST_MOVIE_REVIEW_: {

              //retCursor = getMovieReviewData(uri, projection, selection, selectionArgs, sortOrder);
                retCursor = rdblSQLiteDB.query(
                                    MovieReviewEntry.TABLE_NAME, projection,
                                    selection, selectionArgs,
                                    null, null, sortOrder
                            );
                break;
            }
            // reference, Cursor query (String table,String[] columns,String selection,String[] selectionArgs,
            //                          String groupBy, String having, String orderBy, String limit)
            //---  movieinfo/
            case CNST_MOVIE_INFO_: {
                retCursor = rdblSQLiteDB.query(
                                    MovieInfoEntry.TABLE_NAME, projection,
                                    selection, selectionArgs,
                                    null, null,
                                    sortOrder, CURSOR_LIMIT
                            );
                break;
            }
            //---  movievideo/
            case CNST_MOVIE_VIDEO_: {
                retCursor = rdblSQLiteDB.query(
                                    MovieVideosEntry.TABLE_NAME, projection,
                                    selection, selectionArgs,
                                    null, null, sortOrder
                            );
                break;
            }
            //--- moviefavourites/
            case CNST_MOVIE_FAVOURITES_:{

                retCursor = rdblSQLiteDB.query(
                        MovieFavouritesEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return retCursor;
    }

    //
    // ContentResolver > "CONTENT PROVIDER" > Database
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase wrtblSQLiteDB = mvsSqLtOpnHlpr.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {

			case CNST_MOVIE_REVIEW_: {
                rowsUpdated = wrtblSQLiteDB.update(MovieReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case CNST_MOVIE_INFO_: {
                rowsUpdated = wrtblSQLiteDB.update(MovieInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase wrtbl_SQLiteDB = mvsSqLtOpnHlpr.getWritableDatabase();
        Uri retUri;

        switch (sUriMatcher.match(uri)) {

            case CNST_MOVIE_VIDEO_: {
                long _id = wrtbl_SQLiteDB.insert(MovieVideosEntry.TABLE_NAME, null, values);

                if (_id > 0) {
                    retUri = MovieVideosEntry.buildUri_MovieVideoWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CNST_MOVIE_REVIEW_: {

                long _id = wrtbl_SQLiteDB.insert(MovieReviewEntry.TABLE_NAME, null, values);

                if (_id > 0) {
                    retUri = MovieReviewEntry.buildUriForMovieReviewWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            case CNST_MOVIE_INFO_: {

                long _id = wrtbl_SQLiteDB.insert(MovieInfoEntry.TABLE_NAME, null, values);

                if (_id > 0) {
                    retUri = MovieInfoEntry.buildUriForMovieInfoWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            /*case CNST_MOVIE_FAVOURITES_: {

            }*/

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

        final SQLiteDatabase wrtblSQLiteDB = mvsSqLtOpnHlpr.getWritableDatabase();
        int rowsDeleted;

        if (selection == null) selection = "1";

        switch (sUriMatcher.match(uri)) {

			case CNST_MOVIE_REVIEW_: {
                rowsDeleted = wrtblSQLiteDB.delete(MovieReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case CNST_MOVIE_INFO_: {
                rowsDeleted = wrtblSQLiteDB.delete(MovieInfoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case CNST_MOVIE_VIDEO_: {
                rowsDeleted = wrtblSQLiteDB.delete(MovieVideosEntry.TABLE_NAME, selection, selectionArgs);
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

            case CNST_MOVIE_VIDEO_: {
                wrtblSqLtDb.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues cv : values) {
                        long id = wrtblSqLtDb.insert(MovieVideosEntry.TABLE_NAME, null, cv);
                        if (id != 1) {
                            returnCount++;
                        }
                    }
                    wrtblSqLtDb.setTransactionSuccessful();

                } finally {
                    wrtblSqLtDb.endTransaction();
                }
                break;
            }

			case CNST_MOVIE_REVIEW_: {
                wrtblSqLtDb.beginTransaction();
                returnCount = 0;

                try {
                    for (ContentValues cv : values) {
                        long _id = wrtblSqLtDb.insert(MovieReviewEntry.TABLE_NAME, null, cv);  // ??
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

            case CNST_MOVIE_INFO_: {
                wrtblSqLtDb.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues cv : values) {
                        long id = wrtblSqLtDb.insert(MovieInfoEntry.TABLE_NAME, null, cv);  // ??
                        if (id != -1) {
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
                returnCount = super.bulkInsert(uri, values);
                break;

        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnCount;
    }
}