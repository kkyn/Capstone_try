package com.example.android.myproject_2.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.PopularEntry;
//import com.example.android.myproject_2.data.MovieContract.RatingEntry;

/**
 * Created by kkyin on 1/3/2016.
 */
public class TestMovieProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieProvider.class.getSimpleName();

    //++++++++++++++++++++++++++++++++++++++++++++
    public void testMovieProviderRegistry() {

        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName =
            new ComponentName(mContext.getPackageName(),
                                MovieProvider.class.getName());

        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals(
                "Error: MovieProvider registered authority: "
                    + providerInfo.authority
                    + " instead of authority: "
                    + MovieContract.AUTHORITY,
                providerInfo.authority,
                MovieContract.AUTHORITY);

        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: MovieProvider not registerd at "
                + mContext.getPackageName(), false);
        }
    }

    //++++++++++++++++++++++++++++++++++++++++++++
    public void testGetType() {

        // input : "content://com.example.android.myproject_2/Popularity"
        // output : "vnd.android.cursor.dir/com.example.android.myproject_2/Popularity"
        String type = mContext.getContentResolver().getType(PopularEntry.URI_CONTENT_AUTHORITY_POPULAR);

        assertEquals("Error: expected out is not vnd.android.cursor.dir/com.example.android.myproject_2/Popularity",
            PopularEntry.POPULARITY_MULTI_ITEM_CURSOR, type
            );

        // input : "content://com.example.android.myproject_2/Rating"
        // output : "vnd.android.cursor.dir/com.example.android.myproject_2/Popularity"
        // from Content Resolver --> call getType in a Content-Provider
//        type = mContext.getContentResolver().getType(RatingEntry.URI_CONTENT_AUTHORITY_RATING);
//        assertEquals("Error : expected out is not vnd.android.cursor.dir/com.example.android.myproject_2/Rating",
//            RatingEntry.RATING_MULTI_ITEM_CURSOR, type);


        // input : "content://com.example.android.myproject_2/movieInfo"
        // output : "vnd.android.cursor.dir/com.example.android.myproject_2/movieInfo"
        // from Content Resolver --> call getType in a Content-Provider
    //    type = mContext.getContentResolver().getType(MovieInfoEntry.URI_CONTENT_AUTHORITY_MOVIEINFO);
    //    assertEquals("Error : ...",
    //        MovieInfoEntry.DIR_CURSOR_MOVIEINFO, type);
    }

    //++++++++++++++++++++++++++++++++++++++++++++
    public void testBasicQuery_MovieInfo() {

        // insert our test records into database
        MovieSQLiteOpenHelper mSqlDbOpenHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase mSqlDb = mSqlDbOpenHelper.getWritableDatabase();

        ContentValues mContentValues = TestUtilities.createValues_4MovieInfo_1();

        long mRowId;
        mRowId = mSqlDb.insert(MovieInfoEntry.TABLE_NAME, null, mContentValues);

        mSqlDb.close();

        Log.d("-- " + LOG_TAG, "testBasicQuery_MovieInfo()");
        Log.d("-- " + LOG_TAG + " after insert###", "mRowId: " + String.valueOf(mRowId)); // tky add
        // Verify we got a row back.
        assertTrue("Error: Failure to insert Movie Values", mRowId != -1);

        Cursor mCursor = mContext.getContentResolver().query(
            MovieInfoEntry.URI_CONTENT_AUTHORITY_MOVIEINFO,
            null,
            null,
            null,
            null
        );

        Log.d("-- " + LOG_TAG, "TestUtilities.validateCursor()---");
        // to uncomment later
        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", mCursor, mContentValues);
    }

    //--------------------------------------------------------------
    /*
    public void testBasicMovieQuery() {

        // insert our test records into the database
        MovieSQLiteOpenHelper mMovieSQLiteOpenHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase db = mMovieSQLiteOpenHelper.getWritableDatabase();

        // Add data into movie table
    //    ContentValues mContentValues = TestUtilities.createPopularValues();
        long movieRowId = TestUtilities.insertMovieValues(mContext);

//        // Add data into MovieInfo table
//        ContentValues nContentValues = TestUtilities.createValues_4MovieInfo(movieRowId);
//
//        long movieInfoRowId = db.insert(MovieContract.MovieInfoEntry.TABLE_NAME, null, nContentValues);
//
//        assertTrue("Unable to insert MovieInfoEntry into the Database", movieInfoRowId != -1);
//
        db.close();
//
        // Test the basic content provider query.
        Cursor mCursor = mContext.getContentResolver().query(
            MovieEntry.URI_CONTENT_AUTHORITY_MOVIE,
            null,
            null,
            null,
            null
        );
//
//        // Make sure we get the correct cursor out of the database
//        TestUtilities.validateCursor("testBasicMovieQuery", mCursor, nContentValues);
    }
*/
    /**/
    public void testInsertReadProvider() {

        ContentValues mPopularValues = TestUtilities.createPopularValues();

        // Register a content observer for our insert.
        // This time, directly with the content resolver
        TestUtilities.TestContentObserver testContentObserver = TestUtilities.getTestContentObserver();

        mContext.getContentResolver()
                .registerContentObserver(PopularEntry.URI_CONTENT_AUTHORITY_POPULAR, true, testContentObserver);

        // Insert test data into PopularEntry.
        // Return the uri after the insert operation.
        Uri popularUri = mContext
                        .getContentResolver()
                        .insert(PopularEntry.URI_CONTENT_AUTHORITY_POPULAR, mPopularValues);
        Log.d("-- " + LOG_TAG , " testInsertReadProvider()");
        Log.d("-- " + LOG_TAG + " after insert-- ", "popularUri: " + popularUri.toString()); // tky add,String.valueOf(popularRowId)

        // Did our content observer get called?
        // If this fails, your insert PopularEntry data
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        testContentObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(testContentObserver);

        // Get the row id from the uri.
        long popularRowId = ContentUris.parseId(popularUri);

        // Verify data's inserted.  IN THEORY.
        // Verify we got a row back.
        assertTrue(popularRowId != -1);

        Log.d("-- " + LOG_TAG + " after insert++ ", "popularRowID: " + String.valueOf(popularRowId)); // tky add

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // Now pull some out to stare at it and
        // verify it made the round trip.

        // A cursor is your primary interface to the query results.
        Cursor testCursor = mContext
                            .getContentResolver()
                            .query(PopularEntry.URI_CONTENT_AUTHORITY_POPULAR,
                                null, // leaving "columns" null just returns all the columns.
                                null, // cols for "where" clause
                                null, // values for "where" clause
                                null  // sort order
                            );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating PopularEntry.",
            testCursor, mPopularValues);

     //   testCursor.close(); // tky add
     //   Log.d("-- " + LOG_TAG , " testCursor closed"); // tky add
        //================================================================

        ContentValues testMovieInfo_Values = TestUtilities.createValues_4MovieInfo(popularRowId);

        // Register a content observer for our insert.
        // This time, directly with the content resolver
        testContentObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver()
                .registerContentObserver(MovieInfoEntry.URI_CONTENT_AUTHORITY_MOVIEINFO, true, testContentObserver);

        Uri movieinfoUri = mContext
                            .getContentResolver()
                            .insert(MovieInfoEntry.URI_CONTENT_AUTHORITY_MOVIEINFO, testMovieInfo_Values);

        //assertTrue(movieinfoUri != null);

        testContentObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(testContentObserver);

        assertTrue(movieinfoUri != null);
        Log.d("-- " + LOG_TAG, "movieinfoUri: " + movieinfoUri.toString()); // tky add
        Log.d("-- " + LOG_TAG, "movieinfoID: " + MovieInfoEntry.getMovieId_fromUri( movieinfoUri));// tky add
        //-----------------------------------------------------------
        Cursor mMovieInfoCursor = mContext
                                    .getContentResolver()
                                    .query(MovieInfoEntry.URI_CONTENT_AUTHORITY_MOVIEINFO,
                                            null, null, null, null);
//
        TestUtilities.validateCursor("testInsertReadProvider, Error validating MovieInfoEntry insert  ",
                        mMovieInfoCursor, testMovieInfo_Values );
//
        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
    }
/**/
}
