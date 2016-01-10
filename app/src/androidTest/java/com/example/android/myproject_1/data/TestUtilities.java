package com.example.android.myproject_1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.android.myproject_1.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

//import com.example.android.sunshine.app.utils.PollingCheck;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {
    static final String TEST_MOVIE_ID = "99705";
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

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
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default weather values for your database tests.
     */
    static ContentValues createDynamicPInfoValues(long movieRowId) {
        ContentValues DynamicPInfoValues = new ContentValues();

        DynamicPInfoValues.put(MovieContract.DynamicPInfo.COL_MV_KEY, movieRowId);

        return DynamicPInfoValues;
    }
//    static ContentValues createDynamicPInfoValues() {
//        ContentValues DynamicPInfoValues = new ContentValues();
//
//        DynamicPInfoValues.put(MovieContract.DynamicPInfo.COL_MV_KEY, "xxx");
//
//        return DynamicPInfoValues;
//    }

//    static ContentValues createWeatherValues(long locationRowId) {
//        ContentValues weatherValues = new ContentValues();
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_LOC_KEY, locationRowId);
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_DATE, TEST_DATE);
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_DEGREES, 1.1);
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_HUMIDITY, 1.2);
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_PRESSURE, 1.3);
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_MAX_TEMP, 75);
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_MIN_TEMP, 65);
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_WIND_SPEED, 5.5);
//        weatherValues.put(weatherValues.WeatherEntry.COLUMN_WEATHER_ID, 321);
//
//        return weatherValues;
//    }

    /*
        Students: You can uncomment this helper function once you have finished creating the
        LocationEntry part of the WeatherContract.
     */
    static ContentValues createMovieStaticValues() {

        ContentValues movieStaticValues = new ContentValues();

        movieStaticValues.put(MovieContract.StaticInfo.COL_MV_ID, TEST_MOVIE_ID);

        movieStaticValues.put(MovieContract.StaticInfo.COL_MV_LINK, "movie_http");
        movieStaticValues.put(MovieContract.StaticInfo.COL_MV_NAME, "MyMovie");
        movieStaticValues.put(MovieContract.StaticInfo.COL_MV_POSTER_LINK, "MyMovie_PosterLink");
        movieStaticValues.put(MovieContract.StaticInfo.COL_MV_VIDEO_LINK, "MyMovie_VideoLink");
        movieStaticValues.put(MovieContract.StaticInfo.COL_MV_RELEASEDATE, "ReleaseDate_Today");
        movieStaticValues.put(MovieContract.StaticInfo.COL_MV_SYNOPSIS, "MyMovie_Synopsis");

        return movieStaticValues;
    }
//    static ContentValues createNorthPoleLocationValues() {
//        // Create a new map of values, where column names are the keys
//        ContentValues testValues = new ContentValues();
//        testValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, TEST_MOVIE_ID);
//        testValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, "North Pole");
//        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, 64.7488);
//        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, -147.353);
//
//        return testValues;
//    }

    /*
        Students: You can uncomment this function once you have finished creating the
        LocationEntry part of the WeatherContract as well as the WeatherDbHelper.
     */
    static long insertStaticValues (Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues dryTestValues = TestUtilities.createMovieStaticValues();

        long StaticInfoRowId;
        StaticInfoRowId = db.insert(MovieContract.StaticInfo.TABLE_NAME, null, dryTestValues);
                // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", StaticInfoRowId != -1);

        return StaticInfoRowId;
    }


//    static long insertNorthPoleLocationValues(Context context) {
//        // insert our test records into the database
//        WeatherDbHelper dbHelper = new WeatherDbHelper(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
//
//        long locationRowId;
//        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);
//
//        // Verify we got a row back.
//        assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);
//
//        return locationRowId;
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
}
