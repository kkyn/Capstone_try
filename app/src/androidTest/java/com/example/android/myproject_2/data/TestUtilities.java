package com.example.android.myproject_2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.PopularEntry;
import com.example.android.myproject_2.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {



    public static final String LOG_TAG = TestUtilities.class.getSimpleName();

    static final String TEST_SORTBY_VALUE = "popularity";

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {

            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);

            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);

            String expectedValue = entry.getValue().toString();

            Log.d("-- " + LOG_TAG,  "column indx: " + idx + " -- " + columnName + "\t\t -- value: " + expectedValue ); // tky add

            assertEquals(
                "Value '"
                    + entry.getValue().toString() // org.
                    //  + String.valueOf(idx)
                    //  + columnName
                    //    + valueCursor.getString(idx)
                    + "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));

        }
    }



    /*
        Students: You can uncomment this helper function once you have finished creating the
        PopularEntry part of the MovieContract.
    */
    static ContentValues createValues_4MovieInfo(long rowId) {

        ContentValues mContentValues = new ContentValues();

        mContentValues.put(MovieInfoEntry.COL_ID, rowId-1);  // rowId

        mContentValues.put(MovieInfoEntry.COL_TITLE, "MyMovie");
        mContentValues.put(MovieInfoEntry.COL_RELEASEDATE, 95); // 55
        mContentValues.put(MovieInfoEntry.COL_OVERVIEW, "MyMovie_Synopsis");

        mContentValues.put(MovieInfoEntry.COL_POSTERLINK, "MyMovie_PosterLink");
        mContentValues.put(MovieInfoEntry.COL_VIDEOLINK, "MyMovie_VideoLink");

        return mContentValues;
    }
    static ContentValues createValues_4MovieInfo_1() {

        ContentValues mContentValues = new ContentValues();

        mContentValues.put(MovieInfoEntry.COL_ID, 1); // tky comment , 1L
        mContentValues.put(MovieInfoEntry.COL_TITLE, "MyMovie");
        mContentValues.put(MovieInfoEntry.COL_RELEASEDATE, 95);  // 55, 88
        mContentValues.put(MovieInfoEntry.COL_OVERVIEW, "MyMovie_Synopsis");

        mContentValues.put(MovieInfoEntry.COL_POSTERLINK,"MyMovie_PosterLink");
        mContentValues.put(MovieInfoEntry.COL_VIDEOLINK,"MyMovie_VideoLink");

//        //    mContentValues.put(MovieInfoEntry.COL_MV_ID, 2L);
//        mContentValues.put(MovieInfoEntry.COL_MV_ID, 1L);
//        mContentValues.put(MovieInfoEntry.COL_TITLE, "MyMovie");
//        mContentValues.put(MovieInfoEntry.COL_RELEASEDATE, "9thMarch");
//        mContentValues.put(MovieInfoEntry.COL_OVERVIEW, "MyMovie_Synopsis");
//        //    mContentValues.put(MovieInfoEntry.COL_OVERVIEW, "xxxx");

//        mContentValues.put(MovieInfoEntry.COL_POSTERLINK,"yyyyy");
//        mContentValues.put(MovieInfoEntry.COL_VIDEOLINK,"zzzzzz");

        return mContentValues;
    }
    /*
     static ContentValues createValues_4MovieInfo_1() {
        ContentValues mContentValues = new ContentValues();

        mContentValues.put(MovieInfoEntry.COL_MV_ID, 1L);
        mContentValues.put(MovieInfoEntry.COL_TITLE, "MyMovie");
        mContentValues.put(MovieInfoEntry.COL_RELEASEDATE, "9thMarch");
        mContentValues.put(MovieInfoEntry.COL_OVERVIEW, "xxxx");
        mContentValues.put(MovieInfoEntry.COL_POSTERLINK,"yyyyy");
        mContentValues.put(MovieInfoEntry.COL_VIDEOLINK,"zzzzzz");

        return mContentValues;
    }
     */

    /*
        Students: Use this to create some default MovieSelect values for your database tests.
     */
    static ContentValues createPopularValues() {

        // Create a new map of values, where column names are the keys
        ContentValues mContentValues = new ContentValues();

        mContentValues.put(PopularEntry.COL_MV_ID, 1); // 33, 2
        mContentValues.put(PopularEntry.COL_TITLE, "MyMovie"); /// tky comment, -- AMovie

        return mContentValues;
    }

    /*
        Students: You can uncomment this function once you have finished creating the
        PopularEntry part of the MovieContract as well as the MovieDbHelperr.
     */
    static long insertPopularValues(Context context) {

        // insert our test records into the database
        MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testContentValues = TestUtilities.createPopularValues();
        /// ContentValues dryTestValues = TestUtilities.createValues_4MovieInfo();

        long popularRowId;
        popularRowId = db.insert(PopularEntry.TABLE_NAME, null, testContentValues);
    //    popularRowId = db.insert(PopularEntry.TABLE_NAME, null, dryTestValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", popularRowId != -1);

        return popularRowId;
    }


    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    //++++++++++++++++++++++++++===========
        /*
        Students: Use this to create some default Movie values for your database tests.
     */
//    static ContentValues createMovieValues() {
//
//        // Create a new map of values, where column names are the keys
//        ContentValues movieContentValues = new ContentValues();
//
//        // movieContentValues.put(MovieContract.PopularEntry.COL_SORTBYSETTING, TEST_SORTBY_VALUE);
//        movieContentValues.put(MovieEntry.COL_MOVIE_ID, 1L);
//        movieContentValues.put(MovieEntry.COL_TITLE, "AMovie");
//        movieContentValues.put(MovieEntry.COL_OVERVIEW,"Bla..Bla");
//        movieContentValues.put(MovieEntry.COL_VOTECOUNT, 240);
//        movieContentValues.put(MovieEntry.COL_RATING, 8.8);
//        movieContentValues.put(MovieEntry.COL_RELEASEDATE, "4March");
//
//        return movieContentValues;
//    }

    //+++++++++++++++++++++++++++++++++++++++++++
    /*
        You can uncomment this function once you have finished creating the
        MovieEntry part of the MovieContract as well as the MovieDbHelperr.
    */
//    static long insertMovieValues(Context context) {
//
//        // insert our test records into the database
//        MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues testContentValues = TestUtilities.createMovieValues();
//        //ContentValues testContentValues = TestUtilities.createPopularValues();
//        /// ContentValues dryTestValues = TestUtilities.createValues_4MovieInfo();
//
//        long movieRowId;
//        movieRowId = db.insert(MovieEntry.TABLE_NAME, null, testContentValues);
//        //    popularRowId = db.insert(PopularEntry.TABLE_NAME, null, dryTestValues);
//
//        // Verify we got a row back.
//        assertTrue("Error: Failure to insert Movie Values", movieRowId != -1);
//
//        return movieRowId;
//    }

    static long insertContentValues_MovieInfo(Context context) {

        // insert our test records into database
        MovieSQLiteOpenHelper mSqlDbOpenHelper = new MovieSQLiteOpenHelper(context);
        SQLiteDatabase mSqlDb = mSqlDbOpenHelper.getWritableDatabase();

        ContentValues mContentValues = TestUtilities.createValues_4MovieInfo_1();

        long mRowId;
        mRowId = mSqlDb.insert(MovieInfoEntry.TABLE_NAME, null, mContentValues);

        mSqlDb.close();

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Movie Info Values", mRowId != -1);

        return mRowId;
    }
}
