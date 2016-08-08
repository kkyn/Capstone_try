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
//import com.example.android.myproject_2.data.MovieContract_x.RatingEntry;

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
//        type = mContext.getContentResolver().getType(RatingEntry.CONTENT_URI);
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

        Log.d(LOG_TAG, "  === testBasicQuery_MovieInfo() ----"); // tky add

        //////
        /**/
         MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(mContext);
         SQLiteDatabase db = dbHelper.getWritableDatabase();

         db.delete(MovieInfoEntry.TABLE_NAME, null, null);
         db.close();
         /**/
        //////
        // insert our test records into database
        MovieSQLiteOpenHelper mSqlDbOpenHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase mSqlDb = mSqlDbOpenHelper.getWritableDatabase();

        ContentValues tValues = TestUtilities.createMovieInfoValues_1();

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

        /**/
        mContext.getContentResolver().delete(
            MovieInfoEntry.CONTENT_URI,
            null, null
        );
        /* */
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

        long movieInfoRowId = db.insert(MovieContract_x.MovieInfoEntry.TABLE_NAME, null, nContentValues);

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
    /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */
///**/
    public void testUpdatePopularProvider() {

        Log.d(LOG_TAG, "  === testUpdatePopularProvider() ----"); // tky add
        //------- tky add
        //deleteAllRecordsFromProvider();
        //deleteAllRecordsFromDB();
        //deleteAllRecords();
        //testDeleteRecords();

  ///////
    /**/
        MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MovieInfoEntry.TABLE_NAME, null, null);
        db.delete(PopularEntry.TABLE_NAME, null, null);
        db.close();
    /**/
    ///////
    ///  // insert our test records into the database
    ///    MovieSQLiteOpenHelper mMovieSQLiteOpenHelper = new MovieSQLiteOpenHelper(mContext);
    ///    SQLiteDatabase db = mMovieSQLiteOpenHelper.getWritableDatabase();

        //------------------

        // insert our test records into database
        MovieSQLiteOpenHelper mSqlDbOpenHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase mSqlDb = mSqlDbOpenHelper.getWritableDatabase();

        ContentValues sValues = TestUtilities.createMovieInfoValues_1();

        Log.d(LOG_TAG, "  === BEFORE INSERT MOVIE-INFO ----"); // tky add
        long mRowId;
        mRowId = mSqlDb.insert(MovieInfoEntry.TABLE_NAME, null, sValues);

        mSqlDb.close();

        Log.d(LOG_TAG, "  === AFTER INSERT MOVIE-INFO ----"); // tky add

    //--   Log.d("-- " + LOG_TAG, "testBasicQuery_MovieInfo()");
    //--   Log.d("-- " + LOG_TAG + " after insert###", "mRowId: " + String.valueOf(mRowId)); // tky add
        // Verify we got a row back.
        assertTrue("Error: Failure to insert Movie Values", mRowId != -1);

        ContentResolver tResolver = mContext.getContentResolver();

        Cursor mCursor = tResolver.query(MovieInfoEntry.CONTENT_URI,
            null, null, null, null
        );

        Log.d(LOG_TAG, "  === AFTER QUERY MOVIE-INFO ----"); // tky add
        // to uncomment later
        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", mCursor, sValues);
        mCursor.close();

        // Create a new map of tValues, where column names are the keys
        ContentValues tValues = TestUtilities.createPopularValues();

        // Register a content observer for our insert.
        // This time, directly with the content resolver
        TestUtilities.TestContentObserver tObserver = TestUtilities.getTestContentObserver();
        tResolver.registerContentObserver(PopularEntry.CONTENT_URI, true, tObserver);

        Uri popularUri = tResolver.insert(PopularEntry.CONTENT_URI, tValues);

        assertTrue(popularUri != null);  // tky add

        tObserver.waitForNotificationOrFail();
        tResolver.unregisterContentObserver(tObserver);

        Log.d(LOG_TAG, "  === AFTER INSERT POPULAR ----"); // tky add

        Integer popularRowId = (int) (long) ContentUris.parseId(popularUri);
        //long popularRowId = ContentUris.parseId(popularUri);

       /// Log.d(LOG_TAG, "popularUri: " + popularUri.toString()); // tky add
       /// Log.d(LOG_TAG, "popularRowId: " + popularRowId); // tky add
        Log.d(LOG_TAG, "  === mpopularUri: " + popularUri.toString()); // tky add
        Log.d(LOG_TAG, "  === mpopularID: " + PopularEntry.getMovieId_fromUri(popularUri));// tky add

        // Verify we got a row back.
        assertTrue(popularRowId != -1);

        Log.d(LOG_TAG, "  === BEFORE calling TestUtilities.validateContentValue(tValues);"); // tky add

        TestUtilities.validateContentValue(tValues);

        // + add (key,value) set, tky add
        ContentValues tUpdtdValues = new ContentValues(tValues);
        tUpdtdValues.put(PopularEntry._ID, popularRowId);
        tUpdtdValues.put(PopularEntry.COL_TITLE, "Santa's Village");

        Log.d(LOG_TAG, "  === BEFORE calling TestUtilities.validateContentValue(tUpdtdValues);"); // tky add

        TestUtilities.validateContentValue(tUpdtdValues);

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor tCursor = tResolver.query(PopularEntry.CONTENT_URI, null, null, null, null);

        Log.d(LOG_TAG, "  === AFTER QUERY POPULAR ----"); // tky add
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
            //PopularEntry._ID + "= ?",
            PopularEntry.TABLE_NAME + "." + PopularEntry._ID + " = ?",

            //--- selectionArg,  Values for the "where" clause //
            new String[]{Long.toString(popularRowId)}
        );
        /**/

        Log.d(LOG_TAG, "  === AFTER UPDATE POPULAR ----"); // tky add

    //----    TestUtilities.validateCursorValue(tCursor); ///// ???
    //
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

        Log.d(LOG_TAG, "  === BEFORE QUERY POPULAR xxxx"); // tky add
        Log.d(LOG_TAG, "  === BEFORE QUERY POPULAR xxxx, "+ "popularRowId: " + popularRowId); // tky add
        //---------------------------------
    ////--    tCursor = tResolver.query(PopularEntry.CONTENT_URI, null, null, null, null);
    ///    TestUtilities.validateCursorValue(tCursor);
    ////--    tCursor.close();
        //+++++++++++++++++++++++++++++++++++++++++++++++
        // A cursor is your primary interface to the query results.
        Cursor cursor = tResolver.query(
            PopularEntry.CONTENT_URI,
            null,   // projection

            PopularEntry._ID + " = ? ",    //-- selection
            //PopularEntry.TABLE_NAME + "." + PopularEntry._ID + " = ? ",    //-- selection

            new String[]{Long.toString(popularRowId )},   //-- Values for the "where" clause // selectionArg

            // MovieContract_x.PopularEntry._ID + " = " + popularRowId,    //-- selection ??? last looked at 29MArch, work
            // null,   //-- Values for the "where" clause // selectionArg

            null    //-- sort order
        );

          TestUtilities.validateCursorValue(cursor);

        Log.d(LOG_TAG, "  === AFTER QUERY POPULAR xxxx"); // tky add

        TestUtilities.validateCursor("testUpdatePopularProvider.  Error validating popular entry update.",
            cursor, tUpdtdValues);

        cursor.close();
    }
    //==================================================================
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //
    // ------- INSERT, READ -------
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void testInsertReadProvider() {

        Log.d(LOG_TAG, "---- testInsertReadProvider() ----"); // tky add

    ///////
    /**/
     MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(mContext);
     SQLiteDatabase db = dbHelper.getWritableDatabase();

     db.delete(MovieInfoEntry.TABLE_NAME, null, null);
     db.close();
     /**/
    ///////
        //   ContentValues tValues = TestUtilities.createValues4MovieInfo(4);
        ContentValues tValues = TestUtilities.createMovieInfoValues_1();

        ContentResolver tResolver = mContext.getContentResolver();

        // Register a content observer for our insert.
        // This time, directly with the content resolver
        TestUtilities.TestContentObserver tObserver = TestUtilities.getTestContentObserver();
        tResolver.registerContentObserver(MovieInfoEntry.CONTENT_URI, true, tObserver);

        Uri mMovieInfoUri = tResolver.insert(MovieInfoEntry.CONTENT_URI, tValues);

        assertTrue(mMovieInfoUri != null);

        tObserver.waitForNotificationOrFail();
        tResolver.unregisterContentObserver(tObserver);

        Log.d("-- " + LOG_TAG, "mMovieInfoUri: " + mMovieInfoUri.toString()); // tky add
      //Log.d("-- " + LOG_TAG, "mMovieInfoID: " + MovieInfoEntry.getMovieId_fromUri(mMovieInfoUri));// tky add

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

//    Good!?
    public void testDeleteRecords() {

        Log.d(LOG_TAG, "---- testDeleteRecords() ----"); // tky add

    ///////
    /**/
        MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MovieInfoEntry.TABLE_NAME, null, null);
        db.delete(PopularEntry.TABLE_NAME, null, null);
        db.close();
    /**/
    ///////
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

        Log.d(LOG_TAG, "  === Into deleteAllRecordsFromProvider()");

        mContext.getContentResolver().delete(
            MovieInfoEntry.CONTENT_URI,
            null, null
        );
        mContext.getContentResolver().delete(
            PopularEntry.CONTENT_URI,
            null, null
        );

        Log.d(LOG_TAG, "  === Before MovieInfoEntry: -->X.query -->" + MovieInfoEntry.CONTENT_URI); // tky add
        Cursor cursor = mContext.getContentResolver().query(
            MovieInfoEntry.CONTENT_URI,
            null, null, null, null
        );
        assertEquals("Error: Records not deleted from Weather table during delete", 0, cursor.getCount());
        cursor.close();

        Log.d(LOG_TAG, "  === Before PopularEntry: -->Y.query -->" + PopularEntry.CONTENT_URI);//     tky add
        cursor = mContext.getContentResolver().query(
            PopularEntry.CONTENT_URI,
            null, null, null, null
        );
        assertEquals("Error: Records not deleted from Location table during delete", 0, cursor.getCount());
        cursor.close();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    static private final int BULK_INSERT_NUM_OF_RECORDS = 5;
    static ContentValues[] createBulkInsertMovieInfoValues() {

        ContentValues[] mValuesArray = new ContentValues[BULK_INSERT_NUM_OF_RECORDS];
        int j = 1;
        for (int i = 0; i < BULK_INSERT_NUM_OF_RECORDS; i++, j++) {
            ContentValues mMovieInfoValues = new ContentValues();

            mMovieInfoValues.put(MovieInfoEntry.COL_MV_ID, j);
            mMovieInfoValues.put(MovieInfoEntry.COL_TITLE, "MyMovie_" + j);
            mMovieInfoValues.put(MovieInfoEntry.COL_VIDEOLINK, "VideoLink_" + j);
            mMovieInfoValues.put(MovieInfoEntry.COL_POSTERLINK, "PosterLink_" + j);
            mMovieInfoValues.put(MovieInfoEntry.COL_OVERVIEW, "OverView_"+ j);
            mMovieInfoValues.put(MovieInfoEntry.COL_RELEASEDATE, "ReleaseDate_" + j);
            mValuesArray[i] = mMovieInfoValues;
        }
        return mValuesArray;
    }

    static ContentValues[] createBulkInsertPopularValues() {

        ContentValues[] mValuesArray = new ContentValues[BULK_INSERT_NUM_OF_RECORDS];

        int j = 0;
        for (int i = 1; i <= BULK_INSERT_NUM_OF_RECORDS; i++, j++) {
            ContentValues mPopularValues = new ContentValues();

            mPopularValues.put(PopularEntry.COL_KEY_ID, j);
            mPopularValues.put(PopularEntry.COL_MV_ID, j);
            mPopularValues.put(PopularEntry.COL_TITLE, "MyMovie_" + j); /// tky comment, -- AMovie
            mValuesArray[i] = mPopularValues;
        }
        return mValuesArray;
    }

    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.
    public void testBulkInsert() {

        Log.d(LOG_TAG, "  === Into testBulkInsert()");
        //deleteAllRecords();
        /**
         MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MovieInfoEntry.TABLE_NAME, null, null);
        db.close();
        **/
        /**
         mContext.getContentResolver().delete(
            MovieInfoEntry.CONTENT_URI,
            null, null
        );
        * */

        ContentValues[] mBulkInsertValues = createBulkInsertMovieInfoValues();

        ContentResolver mResolver = mContext.getContentResolver();
        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver mObserver = TestUtilities.getTestContentObserver();
        mResolver.registerContentObserver(MovieInfoEntry.CONTENT_URI, true, mObserver);

        int insertCount = mResolver.bulkInsert(MovieInfoEntry.CONTENT_URI, mBulkInsertValues);

        mObserver.waitForNotificationOrFail();
        mResolver.unregisterContentObserver(mObserver);

        Log.d(LOG_TAG, "-- number of inserts : " + insertCount); // tky add

        assertEquals(insertCount, BULK_INSERT_NUM_OF_RECORDS);

        /**/
        // A cursor is your primary interface to the query results.
        Cursor cursor = mResolver.query(
                MovieInfoEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // WeatherEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
        );

        Log.d(LOG_TAG, "-- number of rows in cursor : " + cursor.getCount());  // tky add

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_NUM_OF_RECORDS);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_NUM_OF_RECORDS; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, mBulkInsertValues[i]);
        }
        cursor.close();
        /* */

    }
    /*
      public static final String COL_MV_ID = "movie_id";
        public static final String COL_TITLE = "title";

        public static final String COL_RELEASEDATE = "release_date";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_VIDEOLINK = "video_link";
        public static final String COL_POSTERLINK = "poster_link";
     */
/***
    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.
    public void testBulkInsert() {
        // first, let's create a location value
        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
        Uri locationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testValues);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating LocationEntry.",
                cursor, testValues);

        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for weather
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = createBulkInsertWeatherValues(locationRowId);

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(WeatherEntry.CONTENT_URI, true, weatherObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        weatherObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(weatherObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                WeatherEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
 ***/

/*************************************************************/
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
}
