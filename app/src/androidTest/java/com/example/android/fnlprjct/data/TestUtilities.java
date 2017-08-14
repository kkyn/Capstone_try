package com.example.android.fnlprjct.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;
import com.example.android.fnlprjct.data.MovieContract.MovieReviewEntry;
import com.example.android.fnlprjct.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your MovieContract class to exactly match the one
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

            assertEquals(
                "Value '"
                    + entry.getValue().toString() // org.
                    + "' did not match the expected value '" +
                    expctdValue + "'. " + error,
                    expctdValue, valueCursor.getString(index));
        }
    }

    static void validateContentValue(ContentValues expectedValues) {

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {

            String columnName = entry.getKey();

            String expectedValue = entry.getValue().toString();
            Log.d("-- " + LOG_TAG, "  +++ key: " + columnName + "  " + "value: " + expectedValue); // tky add

        }
    }

    static void validateCursorValue(Cursor valueCursor) {

        Log.d("-- " + LOG_TAG, "  +++ validateCursorValue() --- "); // tky add
        //  Cursor mCursor = valueCursor;

        //Returns the numbers of rows in the cursor.
        int rowCount = valueCursor.getCount();

        //Return total number of columns.
        int colCount = valueCursor.getColumnCount();

        Log.d("-- " + LOG_TAG, "  +++ rows:- " + rowCount + " cols:- " + colCount);

        int count = 0;
        String mValue = "";
        String nValue = valueCursor.moveToFirst() == true ? "yes it is first" : "no -- not first--";
        Log.d("-- " + LOG_TAG, "  +++ *** :" + nValue);

        do {
            mValue = valueCursor.isFirst() == true ? "YES it is first" : "NO -- not first";
            Log.d("-- " + LOG_TAG, "  +++ *** :" + mValue);
            for (count = 0; count < colCount; count++) {
                Log.d("-- " + LOG_TAG, "  +++ " + valueCursor.getColumnName(count) + " : " + valueCursor.getString(count));
            }
            valueCursor.moveToNext();
        }
        while (!valueCursor.isAfterLast());

        //----------------------------------------

    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++  BEGIN  ++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    static ContentValues createTestContentValuesForMovieReview(long rowId) {

        ContentValues cntValues = new ContentValues();

        // Here I use the input rowId of cursor as the 'COL_MOVIE_ID', for testing only.
        cntValues.put(MovieReviewEntry.COL_MOVIE_ID,        12);
        cntValues.put(MovieReviewEntry.COL_REVIEWER,        "XMovie_Reviewer");
        cntValues.put(MovieReviewEntry.COL_REVIEWCONTENT,   "XMovie_ReviewContent");

        return cntValues;
    }

    static ContentValues createTestContentValuesForMovieInfo() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieInfoEntry.COL_MOVIE_ID,       12);
        contentValues.put(MovieInfoEntry.COL_ORIGINAL_TITLE, "XMovie_Title");
        contentValues.put(MovieInfoEntry.COL_RELEASE_DATE,   "XMovie_ReleaseDate");
        contentValues.put(MovieInfoEntry.COL_YEAR,           "2017");
        contentValues.put(MovieInfoEntry.COL_OVERVIEW,       "XMovie_Overview");

        contentValues.put(MovieInfoEntry.COL_VOTE_COUNT,     123);
        contentValues.put(MovieInfoEntry.COL_VOTE_AVERAGE,   "XMovie_Vote_Average");
        contentValues.put(MovieInfoEntry.COL_POPULARITY,     "XMovie_Popularity");

        contentValues.put(MovieInfoEntry.COL_FAVOURITES,     0);

        contentValues.put(MovieInfoEntry.COL_POSTERLINK,     "XMovie_PosterLink");
        contentValues.put(MovieInfoEntry.COL_BACKDROPLINK,   "XMovie_BackDrop_Path");

        return contentValues;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++  END  +++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++

    /*
        The functions we provide inside of TestProvider use this utility class to test
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

}
