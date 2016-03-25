package com.example.android.myproject_2.data;

import android.content.ComponentName;
import android.content.ContentResolver;
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

        Log.d(LOG_TAG, "---- testMovieProviderRegistry() ----"); // tky add

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

        Log.d(LOG_TAG, "---- testGetType() ----"); // tky add

        // input : "content://com.example.android.myproject_2/Popularity"
        // output : "vnd.android.cursor.dir/com.example.android.myproject_2/Popularity"
        String type = mContext.getContentResolver().getType(PopularEntry.CONTENT_URI);

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
    //    type = mContext.getContentResolver().getType(MovieInfoEntry.CONTENT_URI);
    //    assertEquals("Error : ...",
    //        MovieInfoEntry.DIR_CURSOR_MOVIEINFO, type);
    }

    //++++++++++++++++++++++++++++++++++++++++++++
/**/
    public void testBasicQuery_MovieInfo() {

        Log.d(LOG_TAG, "---- testBasicQuery_MovieInfo() ----"); // tky add

        // insert our test records into database
        MovieSQLiteOpenHelper mSqlDbOpenHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase mSqlDb = mSqlDbOpenHelper.getWritableDatabase();

        ContentValues tValues = TestUtilities.createValues_4MovieInfo_1();

        long mRowId;
        mRowId = mSqlDb.insert(MovieInfoEntry.TABLE_NAME, null, tValues);

        mSqlDb.close();

//--        Log.d("-- " + LOG_TAG, "testBasicQuery_MovieInfo()");
//--        Log.d("-- " + LOG_TAG + " after insert###", "mRowId: " + String.valueOf(mRowId)); // tky add
        // Verify we got a row back.
        assertTrue("Error: Failure to insert Movie Values", mRowId != -1);

        Cursor mCursor = mContext.getContentResolver().query(
            MovieInfoEntry.CONTENT_URI,
            null, null, null, null
        );

//--        Log.d("-- " + LOG_TAG, "TestUtilities.validateCursor()---");
        // to uncomment later
        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", mCursor, tValues);
        mCursor.close();
    }
/**/
    //--------------------------------------------------------------
    /*
    public void testBasicMovieQuery() {

        // insert our test records into the database
        MovieSQLiteOpenHelper mMovieSQLiteOpenHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase db = mMovieSQLiteOpenHelper.getWritableDatabase();

        // Add data into movie table
        ContentValues mContentValues = TestUtilities.createPopularValues();
        long movieRowId = TestUtilities.insertPopularValues(mContext);
       // long movieRowId = TestUtilities.insertMovieValues(mContext);

        // Add data into MovieInfo table
        ContentValues nContentValues = TestUtilities.createValues4MovieInfo(movieRowId);

        long movieInfoRowId = db.insert(MovieContract.MovieInfoEntry.TABLE_NAME, null, nContentValues);

        assertTrue("Unable to insert MovieInfoEntry into the Database", movieInfoRowId != -1);

        db.close();
//
        // Test the basic content provider query.
        Cursor mCursor = mContext.getContentResolver().query(
            MovieInfoEntry.CONTENT_URI,
            null, null, null, null
        );
//
        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", mCursor, nContentValues);
    }
*/
    //==================================================================
//++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
// ------- UPDATE -------
//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++

//    /*
//        This test uses the provider to insert and then update the data. Uncomment this test to
//        see if your update location is functioning correctly.
//     */
///**/
public void testUpdatePopularProvider() {
    Log.d(LOG_TAG, "---- testUpdatePopularProvider() ----"); // tky add
    //------- tky add
    deleteAllRecordsFromProvider();
   // deleteAllRecordsFromDB();
    //testDeleteRecords();
    //deleteAllRecords();
///  // insert our test records into the database
///    MovieSQLiteOpenHelper mMovieSQLiteOpenHelper = new MovieSQLiteOpenHelper(mContext);
///    SQLiteDatabase db = mMovieSQLiteOpenHelper.getWritableDatabase();

    //------------------

    // Create a new map of tValues, where column names are the keys
    ContentValues tValues = TestUtilities.createPopularValues();

    ContentResolver tResolver = mContext.getContentResolver();
/**/
    // Register a content observer for our insert.
    // This time, directly with the content resolver
    TestUtilities.TestContentObserver tObserver = TestUtilities.getTestContentObserver();
    tResolver.registerContentObserver(PopularEntry.CONTENT_URI, true, tObserver);
/**/
    Uri popularUri = tResolver.insert(PopularEntry.CONTENT_URI, tValues);

    assertTrue(popularUri != null);  // tky add
/**/
    tObserver.waitForNotificationOrFail();
    tResolver.unregisterContentObserver(tObserver);
/**/
    long popularRowId = ContentUris.parseId(popularUri);

    Log.d(LOG_TAG, "popularUri: " + popularUri.toString()); // tky add
    Log.d(LOG_TAG, "popularRowId: " + popularRowId); // tky add
    Log.d(LOG_TAG, "mpopularUri: " + popularUri.toString()); // tky add
    Log.d(LOG_TAG, "mpopularID: " + PopularEntry.getMovieId_fromUri(popularUri));// tky add

    // Verify we got a row back.
    assertTrue(popularRowId != -1);
    //Log.d(LOG_TAG, "New popularRowId: " + popularRowId); // tky add

///*
//    public static final String TABLE_NAME = "Table_Popularity";
//    public static final String COL_KEY_ID = "KeyId";
//    public static final String COL_MV_ID = "MovieIDs";
//    public static final String COL_TITLE = "Title";
//    //------------
//    mContentValues.put(PopularEntry.COL_KEY_ID, 1); // 33, 2
//    mContentValues.put(PopularEntry.COL_MV_ID, 3);
//    mContentValues.put(PopularEntry.COL_TITLE, "MyMovie"); /// tky comment, -- AMovie
// */

    TestUtilities.validateContentValue(tValues);

    // + add (key,value) set, tky add
    //ContentValues tUpdtdValues = new ContentValues(tValues);
    ContentValues tUpdtdValues = new ContentValues(tValues);
    tUpdtdValues.put(PopularEntry._ID, popularRowId);
    tUpdtdValues.put(PopularEntry.COL_TITLE, "Santa's Village");

    TestUtilities.validateContentValue(tUpdtdValues);

    // Create a cursor with observer to make sure that the content provider is notifying
    // the observers as expected
    Cursor tCursor = tResolver.query(PopularEntry.CONTENT_URI, null, null, null, null);

////??    TestUtilities.validateCursorValue(tCursor);

//------------------------------------------------
    TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
    // Register an observer that is called when changes happen to the content backing this cursor.
    tCursor.registerContentObserver(tco);

//    tCursor.unregisterContentObserver(tco);
//    tCursor.close();

/* tky inserted, copied from above ... */
 // Register a content observer for our popular delete.
//         TestUtilities.TestContentObserver popularObserver = TestUtilities.getTestContentObserver();
 //        mContext.getContentResolver().registerContentObserver(PopularEntry.CONTENT_URI, true, popularObserver);
 //

 // Did our content observer get called?
 // If this fails, your insert PopularEntry data
 // isn't calling getContext().getContentResolver().notifyChange(uri, null);
// testContentObserver.waitForNotificationOrFail();
//
// mContext.getContentResolver().unregisterContentObserver(testContentObserver);
/**/

    // tky add
//        TestUtilities.validateCursor("testUpdateProvider, Error validating PopularEntry insert  ",
//     tCursor, tValues);
//            tCursor, tUpdtdValues);

//
    /**/
    int count = tResolver.update(
        PopularEntry.CONTENT_URI,
        tUpdtdValues,

        //--- selection
        PopularEntry._ID + "= ?",
        //PopularEntry.TABLE_NAME + "." + PopularEntry._ID + " = ?",
        //PopularEntry.TABLE_NAME + "." + PopularEntry.COL_KEY_ID + " = ?",

        //--- selectionArg,  Values for the "where" clause //
        new String[]{Long.toString(popularRowId)}
        //new String[]{Integer.toString(1)}
        //new String[]{Long.toString(2)}
        //new String[]{Long.toString(3)}
    );
    /**/


//----    TestUtilities.validateCursorValue(tCursor); ///// ???
//
//    ///   Log.d(LOG_TAG, "rowId: " + popularRowId + " -- " + "count: " + count);
//    //   Log.d(LOG_TAG, "rowId: " + Long.toString(1L) + " -- " + "count: " + count);
    assertEquals(count, 1);
//
//    // Test to make sure our observer is called.  If not, we throw an assertion.
//    //
//    // Students: If your code is failing here, it means that your content provider
//    // isn't calling getContext().getContentResolver().notifyChange(uri, null);
    tco.waitForNotificationOrFail();

///---    TestUtilities.validateCursorValue(tCursor); ///// ???
    tCursor.unregisterContentObserver(tco);

    tCursor.close();

    //---------------------------------
////--    tCursor = tResolver.query(PopularEntry.CONTENT_URI, null, null, null, null);
///    TestUtilities.validateCursorValue(tCursor);
////--    tCursor.close();
    //+++++++++++++++++++++++++++++++++++++++++++++++
    // A cursor is your primary interface to the query results.
    Cursor cursor = tResolver.query(
        PopularEntry.CONTENT_URI,
        null,   // projection
        PopularEntry._ID + " = ? ",    // selection
        //PopularEntry._ID + " = " + popularRowId,    // selection

        //null,   // Values for the "where" clause // selectionArg
        new String[]{Long.toString(popularRowId)},   // Values for the "where" clause // selectionArg

        null    // sort order
    );

    TestUtilities.validateCursorValue(cursor);

    TestUtilities.validateCursor("testUpdatePopularProvider.  Error validating popular entry update.",
        cursor, tUpdtdValues);


    cursor.close();
//        tCursor.close();
}
    //==================================================================
//++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
// ------- INSERT, READ -------
//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void testInsertReadProvider() {

    Log.d(LOG_TAG, "---- testInsertReadProvider() ----"); // tky add

    //   ContentValues tValues = TestUtilities.createValues4MovieInfo(4);
    ContentValues tValues = TestUtilities.createValues_4MovieInfo_1();

    ContentResolver tResolver = mContext.getContentResolver();

    // Register a content observer for our insert.
    // This time, directly with the content resolver
    TestUtilities.TestContentObserver tObserver = TestUtilities.getTestContentObserver();
    tResolver.registerContentObserver(MovieInfoEntry.CONTENT_URI, true, tObserver);

    Uri mMovieInfoUri = tResolver.insert(MovieInfoEntry.CONTENT_URI, tValues);

    assertTrue(mMovieInfoUri != null);

    tObserver.waitForNotificationOrFail();
    tResolver.unregisterContentObserver(tObserver);

//--    Log.d("-- " + LOG_TAG, "mMovieInfoUri: " + mMovieInfoUri.toString()); // tky add
//--    Log.d("-- " + LOG_TAG, "mMovieInfoID: " + MovieInfoEntry.getMovieId_fromUri(mMovieInfoUri));// tky add

    Cursor tCursor = tResolver.query(MovieInfoEntry.CONTENT_URI, null, null, null, null);
//
    TestUtilities.validateCursor("testInsertReadProvider, Error validating MovieInfoEntry insert  ",
        tCursor, tValues);
//

    ContentValues mPopularValues = TestUtilities.createPopularValues();

    // Register a content observer for our insert.
    // This time, directly with the content resolver
    tObserver = TestUtilities.getTestContentObserver();

    tResolver.registerContentObserver(PopularEntry.CONTENT_URI, true, tObserver);

    // Insert test data into PopularEntry.
    // Return the uri after the insert operation.
    Uri mPopularUri = tResolver.insert(PopularEntry.CONTENT_URI, mPopularValues);

//--    Log.d("-- " + LOG_TAG , " testInsertReadProvider()");
//--    Log.d("-- " + LOG_TAG + " after insert-- ", "mPopularUri: " + mPopularUri.toString()); // tky add,String.valueOf(popularRowId)

    // Did our content observer get called?
    // If this fails, your insert PopularEntry data
    // isn't calling getContext().getContentResolver().notifyChange(uri, null);
    tObserver.waitForNotificationOrFail();

    tResolver.unregisterContentObserver(tObserver);

    // Get the row id from the uri.
    long popularRowId = ContentUris.parseId(mPopularUri);

    // Verify data's inserted.  IN THEORY.
    // Verify we got a row back.
    assertTrue(popularRowId != -1);

//--    Log.d("-- " + LOG_TAG + " after insert++ ", "popularRowID: " + String.valueOf(popularRowId)); // tky add

    // Now pull some out to stare at it and
    // verify it made the round trip.

    // A cursor is your primary interface to the query results.
    Cursor mCursor = tResolver.query(PopularEntry.CONTENT_URI,
        null, // leaving "columns" null just returns all the columns.
        null, // cols for "where" clause
        null, // values for "where" clause
        null  // sort order
    );

    TestUtilities.validateCursor("testInsertReadProvider. Error validating PopularEntry.",
        mCursor, mPopularValues);
}
    //==================================================================
/*
    public void testInsertReadProvider() {

        ContentValues mPopularValues = TestUtilities.createPopularValues();

        // Register a content observer for our insert.
        // This time, directly with the content resolver
        TestUtilities.TestContentObserver testContentObserver = TestUtilities.getTestContentObserver();

        ContentResolver mContentResolver = mContext.getContentResolver();
        //mContext.getContentResolver()
        mContentResolver.registerContentObserver(PopularEntry.CONTENT_URI, true, testContentObserver);

        // Insert test data into PopularEntry.
        // Return the uri after the insert operation.
        Uri mPopularUri = mContentResolver
                        .insert(PopularEntry.CONTENT_URI, mPopularValues);

        Log.d("-- " + LOG_TAG , " testInsertReadProvider()");
        Log.d("-- " + LOG_TAG + " after insert-- ", "mPopularUri: " + mPopularUri.toString()); // tky add,String.valueOf(popularRowId)

        // Did our content observer get called?
        // If this fails, your insert PopularEntry data
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        testContentObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(testContentObserver);

        // Get the row id from the uri.
        long popularRowId = ContentUris.parseId(mPopularUri);

        // Verify data's inserted.  IN THEORY.
        // Verify we got a row back.
        assertTrue(popularRowId != -1);

        Log.d("-- " + LOG_TAG + " after insert++ ", "popularRowID: " + String.valueOf(popularRowId)); // tky add

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // Now pull some out to stare at it and
        // verify it made the round trip.

        // A cursor is your primary interface to the query results.
        Cursor mCursor = mContentResolver
                            .query(PopularEntry.CONTENT_URI,
                                null, // leaving "columns" null just returns all the columns.
                                null, // cols for "where" clause
                                null, // values for "where" clause
                                null  // sort order
                            );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating PopularEntry.",
            mCursor, mPopularValues);

     //   mCursor.close(); // tky add
     //   Log.d("-- " + LOG_TAG , " mCursor closed"); // tky add
        //================================================================

     //   ContentValues mMovieInfoContentValues = TestUtilities.createValues4MovieInfo(4);
        ContentValues mMovieInfoContentValues = TestUtilities.createValues4MovieInfo(popularRowId);

        // Register a content observer for our insert.
        // This time, directly with the content resolver
        testContentObserver = TestUtilities.getTestContentObserver();

        mContentResolver.registerContentObserver(MovieInfoEntry.CONTENT_URI, true, testContentObserver);

        Uri mMovieInfoUri = mContentResolver
                            .insert(MovieInfoEntry.CONTENT_URI, mMovieInfoContentValues);

        //assertTrue(mMovieInfoUri != null);

        testContentObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(testContentObserver);

        assertTrue(mMovieInfoUri != null);
        Log.d("-- " + LOG_TAG, "mMovieInfoUri: " + mMovieInfoUri.toString()); // tky add
        Log.d("-- " + LOG_TAG, "mMovieInfoID: " + MovieInfoEntry.getMovieId_fromUri(mMovieInfoUri));// tky add
        Log.d("-- " + LOG_TAG, "mMovieInfoID: " + ContentUris.parseId(mPopularUri));
        //-----------------------------------------------------------
        Cursor mMovieInfoCursor = mContentResolver
                                    .query(MovieInfoEntry.CONTENT_URI,
                                        null, null, null, null);
//
        TestUtilities.validateCursor("testInsertReadProvider, Error validating MovieInfoEntry insert  ",
                        mMovieInfoCursor, mMovieInfoContentValues );
//
        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
    }
*/

    //+++++++++++++++++++++++++++++++++++++++++++++++++++
    //
    // ------- DELETE -------
    //
    //+++++++++++++++++++++++++++++++++++++++++++++++++++
    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteRecords() {

        Log.d(LOG_TAG, "---- testDeleteRecords() ----"); // tky add

        testInsertReadProvider();

        ContentResolver mCntntRslvr = mContext.getContentResolver();

        // Register a content observer for our popular delete.
        TestUtilities.TestContentObserver popularObserver = TestUtilities.getTestContentObserver();
        mCntntRslvr.registerContentObserver(PopularEntry.CONTENT_URI, true, popularObserver);

        // Register a content observer for our movieInfo delete.
        TestUtilities.TestContentObserver movieinfoObserver = TestUtilities.getTestContentObserver();
        mCntntRslvr.registerContentObserver(MovieInfoEntry.CONTENT_URI, true, movieinfoObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        popularObserver.waitForNotificationOrFail();
        movieinfoObserver.waitForNotificationOrFail();

        mCntntRslvr.unregisterContentObserver(popularObserver);
        mCntntRslvr.unregisterContentObserver(movieinfoObserver);

        Log.d(LOG_TAG, "---- testDeleteRecords() ---- END"); // tky add
    }
    /*
       Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
       you have implemented delete functionality there.
    */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
        //deleteAllRecordsFromDB();
    }
    /*
       This helper function deletes all records from both database tables using the database
       functions only.  This is designed to be used to reset the state of the database until the
       delete functionality is available in the ContentProvider.
     */
    public void deleteAllRecordsFromDB() {
        //WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(PopularEntry.TABLE_NAME, null, null);
        db.delete(MovieInfoEntry.TABLE_NAME, null, null);
        db.close();
    }
    /*
      This helper function deletes all records from both database tables using the ContentProvider.
      It also queries the ContentProvider to make sure that the database has been successfully
      deleted, so it cannot be used until the Query and Delete functions have been written
      in the ContentProvider.

      Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
      the delete functionality in the ContentProvider.
    */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
            MovieInfoEntry.CONTENT_URI,
            null, null
        );
        mContext.getContentResolver().delete(
            PopularEntry.CONTENT_URI,
            null, null
        );

        Cursor cursor = mContext.getContentResolver().query(
            MovieInfoEntry.CONTENT_URI,
            null, null, null, null
        );
        assertEquals("Error: Records not deleted from Weather table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
            PopularEntry.CONTENT_URI,
            null, null, null, null
        );
        assertEquals("Error: Records not deleted from Location table during delete", 0, cursor.getCount());
        cursor.close();
    }


/**/
}
