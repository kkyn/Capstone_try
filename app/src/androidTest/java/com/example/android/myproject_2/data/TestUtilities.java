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

import com.example.android.myproject_2.data.MovieContract.X_MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.X_MovieReviewEntry;
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

        // Returns a set of all of the keys and values in 'expectedValues'
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {

            // Returns the key corresponding to this entry.
            String columnNameFromEntry = entry.getKey();

            // Returns the zero-based index for the given column name, or -1 if the column doesn't exist
            int index = valueCursor.getColumnIndex(columnNameFromEntry);

            String colNameFromCursor = valueCursor.getColumnName(index);

            assertFalse("Column '" + columnNameFromEntry + "' not found. " + error, index == -1);

            // Returns the value corresponding to this entry.
            String expctdValue = entry.getValue().toString();

            Log.d("-- " + LOG_TAG,  "  +++ ColIndx: " + index + " -- " + "ColName: " + columnNameFromEntry + " colNameFromCursor: " + colNameFromCursor + " --- " + expctdValue ); // tky add

            assertEquals(
                "Value '"
                    + entry.getValue().toString() // org.
                    //  + String.valueOf(index)
                    //  + columnNameFromEntry
                    //    + valueCursor.getString(index)
                    + "' did not match the expected value '" +
                    expctdValue + "'. " + error,
                    expctdValue, valueCursor.getString(index));
                //    expctdValue + "'. " + error, expctdValue, valueCursor.getString(index));

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

    //Returns the numbers of rows in the cursor.
    int rowCount = valueCursor.getCount();
    //Return total number of columns.
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
    */
    //----------------------------------------

}

    /**/
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++  BEGIN  ++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    static ContentValues create_ContentValues4_X_MovieReview(long rowId) {

        ContentValues cntValues = new ContentValues();

        // Here I use the input rowId of cursor as the 'COL_KEY_ID', for testing only.
//xxx        cntValues.put(X_MovieReviewEntry.COL_KEY_ID, rowId);
        cntValues.put(X_MovieReviewEntry.COL_MV_ID, 12);
        cntValues.put(X_MovieReviewEntry.COL_REVIEWER, "XMovie_Reviewer");
        cntValues.put(X_MovieReviewEntry.COL_REVIEWCONTENT, "XMovie_ReviewContent");

        return cntValues;
    }
    /*
     */
    static ContentValues create_ContentValues4_X_MovieInfo() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(X_MovieInfoEntry.COL_MV_ID,         12);  // y
        contentValues.put(X_MovieInfoEntry.COL_TITLE,         "XMovie_Title"); // y
        contentValues.put(X_MovieInfoEntry.COL_RELEASEDATE,   "XMovie_ReleaseDate"); // y
        contentValues.put(X_MovieInfoEntry.COL_OVERVIEW,      "XMovie_Overview"); // y

        contentValues.put(X_MovieInfoEntry.COL_VOTE_COUNT,    123); // y
        contentValues.put(X_MovieInfoEntry.COL_VOTE_AVERAGE,  "XMovie_Vote_Average"); // y
        contentValues.put(X_MovieInfoEntry.COL_POPULARITY,    "XMovie_Popularity"); // y

        contentValues.put(X_MovieInfoEntry.COL_FAVOURITES,    0);

        contentValues.put(X_MovieInfoEntry.COL_POSTERLINK,    "XMovie_PosterLink"); // y
        contentValues.put(X_MovieInfoEntry.COL_BACKDROP_PATH, "XMovie_BackDrop_Path");

        return contentValues;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++  END  +++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++
    /*
        Students: You can uncomment this helper function once you have finished creating the
        PopularEntry part of the MovieContract_x.
    */
//    static ContentValues createValues4MovieInfo(long rowId) {
//
//        return tValues;
//    }
//    static ContentValues createMovieInfoValues_1() {
//
//        return mContentValues;
//    }

    /*
        Students: Use this to create some default MovieSelect values for your database tests.
     */
//    static ContentValues createPopularValues() {
//
//        // Create a new map of values, where column names are the keys
//        ContentValues mContentValues = new ContentValues();
//
//        return mContentValues;
//    }

    /*
        Students: You can uncomment this functReturn total number of columnsion once you have finished creating the
        PopularEntry part of the MovieContract_x as well as the MovieDbHelperr.
     */
//    static long insertPopularValues(Context context) {
//
//        // insert our test records into the database
//
//        ContentValues testContentValues = TestUtilities.createPopularValues();
//
//        long popularRowId;
//        popularRowId = db.insert(PopularEntry.TABLE_NAME, null, testContentValues);
//
//        // Verify we got a row back.
//
//        return popularRowId;
//    }


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
//
//        // Verify we got a row back.
//
//        return movieRowId;
//    }

}
