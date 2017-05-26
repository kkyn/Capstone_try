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

import com.example.android.myproject_2.data.MovieContract.X_MovieInfoEntry;

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
//    public void testGetType() {
//
//        Log.d(LOG_TAG, "---- testGetType() ----"); // tky add
//
//        // input : "content://com.example.android.myproject_2/Popularity"
//        // output : "vnd.android.cursor.dir/com.example.android.myproject_2/Popularity"
//        String type = mContext.getContentResolver().getType(PopularEntry.CONTENT_URI);
//
//        assertEquals("Error: expected out is not vnd.android.cursor.dir/com.example.android.myproject_2/Popularity",
//            PopularEntry.POPULARITY_MULTI_ITEM_CURSOR, type
//            );
//
//        // input : "content://com.example.android.myproject_2/Rating"
//        // output : "vnd.android.cursor.dir/com.example.android.myproject_2/Popularity"
//        // from Content Resolver --> call getType in a Content-Provider
////        type = mContext.getContentResolver().getType(RatingEntry.CONTENT_URI);
////        assertEquals("Error : expected out is not vnd.android.cursor.dir/com.example.android.myproject_2/Rating",
////            RatingEntry.RATING_MULTI_ITEM_CURSOR, type);
//
//
//        // input : "content://com.example.android.myproject_2/movieInfo"
//        // output : "vnd.android.cursor.dir/com.example.android.myproject_2/movieInfo"
//        // from Content Resolver --> call getType in a Content-Provider
//    //    type = mContext.getContentResolver().getType(MovieInfoEntry.CONTENT_URI);
//    //    assertEquals("Error : ...",
//    //        MovieInfoEntry.DIR_CURSOR_MOVIEINFO, type);
//    }

    //++++++++++++++++++++++++++++++++++++++++++++
  /*
    public void testBasicQuery_MovieInfo() {

        // insert our test records into database

        mSqlDb.close();

        // Verify we got a row back.

        // to uncomment later
        // Make sure we get the correct cursor out of the database

    }
  */
    //--------------------------------------------------------------
    /*
    public void testBasicMovieQuery() {

        // insert our test records into the database

        // Add data into movie table

        // Add data into MovieInfo table

        db.close();

        // Test the basic content provider query.

        // Make sure we get the correct cursor out of the database
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

    /*   public void testUpdatePopularProvider() {
     */
    //==================================================================
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //
    // ------- INSERT, READ -------
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /*    public void testInsertReadProvider() {
    }*/

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
/**/
    /*
       Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
       you have implemented delete functionality there.
    */
//    public void deleteAllRecords() {
//        deleteAllRecordsFromProvider();
//    }
    /*
       This helper function deletes all records from both database tables using the database
       functions only.  This is designed to be used to reset the state of the database until the
       delete functionality is available in the ContentProvider.
     */
//    public void deleteAllRecordsFromDB() {
//    }
    /*
      This helper function deletes all records from both database tables using the ContentProvider.
      It also queries the ContentProvider to make sure that the database has been successfully
      deleted, so it cannot be used until the Query and Delete functions have been written
      in the ContentProvider.

      Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
      the delete functionality in the ContentProvider.
    */
//    public void deleteAllRecordsFromProvider() {
//
//        Log.d(LOG_TAG, "  === Into deleteAllRecordsFromProvider()");
//
//        mContext.getContentResolver().delete(
//            MovieInfoEntry.CONTENT_URI,
//            null, null
//        );
//        mContext.getContentResolver().delete(
//            PopularEntry.CONTENT_URI,
//            null, null
//        );
//
//        Log.d(LOG_TAG, "  === Before MovieInfoEntry: -->X.query -->" + MovieInfoEntry.CONTENT_URI); // tky add
//        Cursor cursor = mContext.getContentResolver().query(
//            MovieInfoEntry.CONTENT_URI,
//            null, null, null, null
//        );
//        assertEquals("Error: Records not deleted from Weather table during delete", 0, cursor.getCount());
//        cursor.close();
//
//        Log.d(LOG_TAG, "  === Before PopularEntry: -->Y.query -->" + PopularEntry.CONTENT_URI);//     tky add
//        cursor = mContext.getContentResolver().query(
//            PopularEntry.CONTENT_URI,
//            null, null, null, null
//        );
//        assertEquals("Error: Records not deleted from Location table during delete", 0, cursor.getCount());
//        cursor.close();
//    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    static private final int BULK_INSERT_NUM_OF_RECORDS = 5;
    static ContentValues[] createBulkInsertMovieInfoValues() {

        ContentValues[] mValuesArray = new ContentValues[BULK_INSERT_NUM_OF_RECORDS];
        int j = 1;
        for (int i = 0; i < BULK_INSERT_NUM_OF_RECORDS; i++, j++) {

            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            //--------------------------------------------------------------
            //-------------------------------------------------------//

            ContentValues mMovieInfoValues = new ContentValues();

            mMovieInfoValues.put(X_MovieInfoEntry.COL_MV_ID, j);
            mMovieInfoValues.put(X_MovieInfoEntry.COL_TITLE, "MyMovie_" + j);
            mMovieInfoValues.put(X_MovieInfoEntry.COL_POSTERLINK, "PosterLink_" + j);
            mMovieInfoValues.put(X_MovieInfoEntry.COL_OVERVIEW, "OverView_"+ j);
            mMovieInfoValues.put(X_MovieInfoEntry.COL_RELEASEDATE, "ReleaseDate_" + j);

            mMovieInfoValues.put(X_MovieInfoEntry.COL_VOTE_AVERAGE, "VoteAverage_" + j);
            mMovieInfoValues.put(X_MovieInfoEntry.COL_POPULARITY, "Popularity_" + j);
            mMovieInfoValues.put(X_MovieInfoEntry.COL_VOTE_COUNT, "VoteCount_" + j);
            mMovieInfoValues.put(X_MovieInfoEntry.COL_FAVOURITES, 0);

            mMovieInfoValues.put(X_MovieInfoEntry.COL_BACKDROP_PATH, "BackDropPath_" + j);
         //   mMovieInfoValues.put(X_MovieInfoEntry.COL_VIDEOLINK, "VideoLink_" + j);

            mValuesArray[i] = mMovieInfoValues;
            //=============================================
            //=============================================
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
        /**/
        MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(X_MovieInfoEntry.TABLE_NAME, null, null);
        db.close();

        ContentValues[] mBulkInsertValues = createBulkInsertMovieInfoValues();

        ContentResolver mResolver = mContext.getContentResolver();
        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver mObserver = TestUtilities.getTestContentObserver();
        mResolver.registerContentObserver(X_MovieInfoEntry.CONTENT_URI, true, mObserver);

        int insertCount = mResolver.bulkInsert(X_MovieInfoEntry.CONTENT_URI, mBulkInsertValues);

        mObserver.waitForNotificationOrFail();
        mResolver.unregisterContentObserver(mObserver);

        Log.d(LOG_TAG, "-- number of inserts : " + insertCount); // tky add

        assertEquals(insertCount, BULK_INSERT_NUM_OF_RECORDS);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mResolver.query(
                X_MovieInfoEntry.CONTENT_URI,
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

    }

}
