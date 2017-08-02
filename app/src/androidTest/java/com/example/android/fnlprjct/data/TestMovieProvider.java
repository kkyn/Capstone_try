package com.example.android.fnlprjct.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;

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

            mMovieInfoValues.put(MovieContract.MovieInfoEntry.COL_MOVIE_ID, j);
            mMovieInfoValues.put(MovieInfoEntry.COL_ORIGINAL_TITLE, "MyMovie_" + j);
            mMovieInfoValues.put(MovieContract.MovieInfoEntry.COL_POSTERLINK, "PosterLink_" + j);
            mMovieInfoValues.put(MovieContract.MovieInfoEntry.COL_OVERVIEW, "OverView_"+ j);
            mMovieInfoValues.put(MovieContract.MovieInfoEntry.COL_RELEASE_DATE, "ReleaseDate_" + j);

            mMovieInfoValues.put(MovieContract.MovieInfoEntry.COL_VOTE_AVERAGE, "VoteAverage_" + j);
            mMovieInfoValues.put(MovieContract.MovieInfoEntry.COL_POPULARITY, "Popularity_" + j);
            mMovieInfoValues.put(MovieInfoEntry.COL_VOTE_COUNT, "VoteCount_" + j);
            mMovieInfoValues.put(MovieInfoEntry.COL_FAVOURITES, 0);

            mMovieInfoValues.put(MovieInfoEntry.COL_BACKDROPLINK, "BackDropPath_" + j);
         //   mMovieInfoValues.put(MovieInfoEntry.COL_VIDEOLINK, "VideoLink_" + j);

            // tky added to solve fail test, july17 2017 ---- it passes with ehe addition!!
            mMovieInfoValues.put(MovieInfoEntry.COL_POSTERLINK, "PosterLink_" +j);
            mMovieInfoValues.put(MovieInfoEntry.COL_YEAR, "Year_" +j);

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

        db.delete(MovieContract.MovieInfoEntry.TABLE_NAME, null, null);
        db.close();

        ContentValues[] mBulkInsertValues = createBulkInsertMovieInfoValues();

        ContentResolver mResolver = mContext.getContentResolver();
        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver mObserver = TestUtilities.getTestContentObserver();
        mResolver.registerContentObserver(MovieContract.MovieInfoEntry.CONTENT_URI, true, mObserver);

        int insertCount = mResolver.bulkInsert(MovieInfoEntry.CONTENT_URI, mBulkInsertValues);

        mObserver.waitForNotificationOrFail();
        mResolver.unregisterContentObserver(mObserver);

        Log.d(LOG_TAG, "-- number of inserts : " + insertCount); // tky add

        assertEquals(insertCount, BULK_INSERT_NUM_OF_RECORDS);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mResolver.query(
                MovieContract.MovieInfoEntry.CONTENT_URI,
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
