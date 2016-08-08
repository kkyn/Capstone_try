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

        Log.d("-- " + LOG_TAG, "  +++ validateCursor()--- "); // tky add

        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {

        Log.d("-- " + LOG_TAG, "  +++ validateCurrentRecord()--- "); // tky add

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {

            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);

            String cColName = valueCursor.getColumnName(idx);

            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);

            String expectedValue = entry.getValue().toString();

            Log.d("-- " + LOG_TAG,  "  +++ ColIndx: " + idx + " -- " + "ColName: " + columnName + " cColName: " + cColName + " --- " + expectedValue ); // tky add

            assertEquals(
                "Value '"
                    + entry.getValue().toString() // org.
                    //  + String.valueOf(idx)
                    //  + columnName
                    //    + valueCursor.getString(idx)
                    + "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
                //    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));

        }
    }

/* */
static void validateContentValue(//String error, Cursor valueCursor,
ContentValues expectedValues) {

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {

            String columnName = entry.getKey();
          //  int idx = valueCursor.getColumnIndex(columnName);

          //  String cColName = valueCursor.getColumnName(idx);

          //  assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);

            String expectedValue = entry.getValue().toString();
            Log.d("-- " + LOG_TAG,  "  +++ key: " + columnName + "  " + "value: " + expectedValue ); // tky add

          //  Log.d("-- " + LOG_TAG,  "ColmnIndx: " + idx + "  " + "ColmnName: " + columnName + "  cColName: " + cColName + " value: " + expectedValue ); // tky add

          //  assertEquals(
          //      "Value '"
          //          + entry.getValue().toString() // org.
          //          //  + String.valueOf(idx)
          //          //  + columnName
          //          //    + valueCursor.getString(idx)
          //          + "' did not match the expected value '" +
          //          expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
          //      //    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));

        }
    }
/* */
/* */
static void validateCursorValue(//String error,
                                 Cursor valueCursor
                                 //,ContentValues expectedValues
                                 ) {

    Log.d("-- " + LOG_TAG, "  +++ validateCursorValue() --- "); // tky add
  //  Cursor mCursor = valueCursor;
    int rowCount = valueCursor.getCount();
    int colCount = valueCursor.getColumnCount();

    Log.d("-- "+LOG_TAG, "  +++ rows:- " + rowCount +  " cols:- " + colCount);
    //Log.d("-- "+LOG_TAG, "  +++ rows:: " + Integer.toString(rowCount) +  " cols:: " + Integer.toString(colCount));

    int count = 0;
    String mValue = "";
    String nValue = valueCursor.moveToFirst() == true ? "yes it is first" : "no -- not first--";
    Log.d("-- "+LOG_TAG, "  +++ *** :" + nValue);
    do {
        mValue = valueCursor.isFirst() == true ? "YES it is first" : "NO -- not first";
        Log.d("-- "+LOG_TAG, "  +++ *** :" + mValue);
        for (count = 0; count < colCount; count++) {
            Log.d("-- "+LOG_TAG, "  +++ " + valueCursor.getColumnName(count)+ " : " + valueCursor.getString(count));
        }
        valueCursor.moveToNext();
    }
    while (!valueCursor.isAfterLast());
    //+++++++++++++++++++++++++++++++++++++
/*
    Cursor mCursor = valueCursor;
    int rowCount = mCursor.getCount();
    int colCount = mCursor.getColumnCount();

    Log.d(LOG_TAG, "rows:: " + Integer.toString(rowCount) +  " cols:: " + Integer.toString(colCount));

    int count = 0;
    String mValue = "";
    mCursor.moveToFirst();
    do {
        mValue = mCursor.isFirst() == true ? "YES it is first" : "NO -- not first";
        Log.d(LOG_TAG, "*** :" + mValue);
        for (count = 0; count < colCount; count++) {
            Log.d(LOG_TAG, mCursor.getColumnName(count)+ " : " + mCursor.getString(count));
        }
        mCursor.moveToNext();
    }
    while (!mCursor.isAfterLast());
    */
    //----------------------------------------
//    mValue = mCursor.isFirst()==true ? "YES" : "NO";
//    Log.d(LOG_TAG, "*** :" + mValue);
//    for (count = 0; count < colCount; count++) {
//        Log.d(LOG_TAG, "=== : " + mCursor.getString(count));
//    }
//
//    mCursor.moveToNext();
//    mValue = mCursor.isFirst()==true ? "YES" : "NO";
//    Log.d(LOG_TAG, "*** :" + mValue);
//    for (count = 0; count < colCount; count++) {
//        Log.d(LOG_TAG, "+++ : " + mCursor.getString(count));
//    }
}
/*
    Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

    for (Map.Entry<String, Object> entry : valueSet) {

        String columnName = entry.getKey();
          int idx = valueCursor.getColumnIndex(columnName);

          String cColName = valueCursor.getColumnName(idx);

          assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);

        String expectedValue = entry.getValue().toString();
        Log.d("-- " + LOG_TAG,  "key: " + columnName + "  " + "value: " + expectedValue ); // tky add

        //  Log.d("-- " + LOG_TAG,  "ColmnIndx: " + idx + "  " + "ColmnName: " + columnName + "  cColName: " + cColName + " value: " + expectedValue ); // tky add

        //  assertEquals(
        //      "Value '"
        //          + entry.getValue().toString() // org.
        //          //  + String.valueOf(idx)
        //          //  + columnName
        //          //    + valueCursor.getString(idx)
        //          + "' did not match the expected value '" +
        //          expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        //      //    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));

    }
}
    /* */
    /*
        Students: You can uncomment this helper function once you have finished creating the
        PopularEntry part of the MovieContract_x.
    */
    static ContentValues createValues4MovieInfo(long rowId) {

        ContentValues tValues = new ContentValues();

        //tValues.put(MovieInfoEntry.COL_MV_ID, 8);  // rowId
         tValues.put(MovieInfoEntry.COL_MV_ID, rowId);  // rowId

        tValues.put(MovieInfoEntry.COL_TITLE, "MyMovie");
        tValues.put(MovieInfoEntry.COL_RELEASEDATE, 95); // 55
        tValues.put(MovieInfoEntry.COL_OVERVIEW, "MyMovie_Synopsis");

        tValues.put(MovieInfoEntry.COL_POSTERLINK, "MyMovie_PosterLink");
        tValues.put(MovieInfoEntry.COL_VIDEOLINK, "MyMovie_VideoLink");

        return tValues;
    }
    static ContentValues createMovieInfoValues_1() {

        Log.d(LOG_TAG, "  ++++ Into createMovieInfoValues_1() ---"); // tky add
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(MovieInfoEntry.COL_MV_ID, 99); // tky comment , 1L, 3
     // mContentValues.put(MovieInfoEntry.COL_MV_ID, 8); // tky comment , 1L
        mContentValues.put(MovieInfoEntry.COL_TITLE, "MyMovie");
        mContentValues.put(MovieInfoEntry.COL_RELEASEDATE, 95);  // 55, 88
        mContentValues.put(MovieInfoEntry.COL_OVERVIEW, "MyMovie_Synopsis");

        mContentValues.put(MovieInfoEntry.COL_POSTERLINK,"MyMovie_PosterLink");
        mContentValues.put(MovieInfoEntry.COL_VIDEOLINK,"MyMovie_VideoLink");

        return mContentValues;
    }

    /*
        Students: Use this to create some default MovieSelect values for your database tests.
     */
    static ContentValues createPopularValues() {

        // Create a new map of values, where column names are the keys
        ContentValues mContentValues = new ContentValues();

        mContentValues.put(PopularEntry.COL_KEY_ID, 1); // 33, 2
        mContentValues.put(PopularEntry.COL_MV_ID, 3);
        mContentValues.put(PopularEntry.COL_TITLE, "MyMovie"); /// tky comment, -- AMovie

        return mContentValues;
    }

    /*
        Students: You can uncomment this function once you have finished creating the
        PopularEntry part of the MovieContract_x as well as the MovieDbHelperr.
     */
    static long insertPopularValues(Context context) {

        // insert our test records into the database
        MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testContentValues = TestUtilities.createPopularValues();
        /// ContentValues dryTestValues = TestUtilities.createValues4MovieInfo();

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
            new PollingCheck(5000) { // 5000, 6000
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
//        // movieContentValues.put(MovieContract_x.PopularEntry.COL_SORTBYSETTING, TEST_SORTBY_VALUE);
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
        MovieEntry part of the MovieContract_x as well as the MovieDbHelperr.
    */
//    static long insertMovieValues(Context context) {
//
//        // insert our test records into the database
//        MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues testContentValues = TestUtilities.createMovieValues();
//        //ContentValues testContentValues = TestUtilities.createPopularValues();
//        /// ContentValues dryTestValues = TestUtilities.createValues4MovieInfo();
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

        ContentValues mContentValues = TestUtilities.createMovieInfoValues_1();

        long mRowId;
        mRowId = mSqlDb.insert(MovieInfoEntry.TABLE_NAME, null, mContentValues);

        mSqlDb.close();

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Movie Info Values", mRowId != -1);

        return mRowId;
    }
}
