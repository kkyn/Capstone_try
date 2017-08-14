/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.fnlprjct.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;


import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;
import com.example.android.fnlprjct.data.MovieContract.MovieReviewEntry;

import java.util.HashSet;

public class TestMovieDbHelper extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieDbHelper.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {

        mContext.deleteDatabase(MovieSQLiteOpenHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.
        This makes sure that we always have a clean test.
     */
    public void setUp() {

        deleteTheDatabase();
    }

    /*
        Create this test method once you've written the code to create the MovieInfo table.
        Note that this only tests that the MovieInfo table has the correct columns.
        This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();

        tableNameHashSet.add(MovieInfoEntry.TABLE_NAME);      // new
        tableNameHashSet.add(MovieReviewEntry.TABLE_NAME);    // new

        // tky:
        // Deletes a database including its journal file and other auxiliary files
        // that may have been created by the database engine
        deleteTheDatabase();
        //mContext.deleteDatabase(MovieSQLiteOpenHelper.DATABASE_NAME);

        // tky:
        // * (step 1) Create a helper object to assist in create, open, and/or manage a database.
        // * (step 2) The database is not actually created or opened until
        //            one of getWritableDatabase() or getReadableDatabase() is called.
        SQLiteDatabase db = new MovieSQLiteOpenHelper(this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor mCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        //Cursor mCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                mCursor.moveToFirst());

        // verify that the tables have been created
        do {
            Log.d("-- " + LOG_TAG, "TABLE NAME :---- " + mCursor.getString(0));
            tableNameHashSet.remove(mCursor.getString(0));
        } while( mCursor.moveToNext() );

        // if this fails, it means that your database doesn't contain both the SortBy entry
        // and MovieInfo entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> xMovieInfoColumnHashSet = new HashSet<String>();

        xMovieInfoColumnHashSet.add(MovieInfoEntry._ID);
        xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_MOVIE_ID);
        xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_ORIGINAL_TITLE);
        xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_RELEASE_DATE);
        xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_VOTE_AVERAGE);
        xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_POPULARITY);
        xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_VOTE_COUNT);
        xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_OVERVIEW);
        xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_POSTERLINK);
        xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_BACKDROPLINK); // new
     // xMovieInfoColumnHashSet.add(MovieInfoEntry.COL_VIDEOLINK);     // new

        // now, do our tables contain the correct columns?
        mCursor = db.rawQuery("PRAGMA table_info(" + MovieInfoEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                mCursor.moveToFirst());

        int xColumnNameIndex = mCursor.getColumnIndex("name");
        do {
            String xColumnName = mCursor.getString(xColumnNameIndex);

            xMovieInfoColumnHashSet.remove(xColumnName);

        } while(mCursor.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required SortBy
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                xMovieInfoColumnHashSet.isEmpty());
        //db.close();

        //-------------------------------------------------------//

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> yMovieReviewColumnHashSet = new HashSet<String>();

        yMovieReviewColumnHashSet.add(MovieReviewEntry._ID);
        yMovieReviewColumnHashSet.add(MovieReviewEntry.COL_MOVIE_ID);
        yMovieReviewColumnHashSet.add(MovieReviewEntry.COL_REVIEWER);
        yMovieReviewColumnHashSet.add(MovieReviewEntry.COL_REVIEWCONTENT);

        // now, do our tables contain the correct columns?
        mCursor = db.rawQuery("PRAGMA table_info(" + MovieReviewEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                mCursor.moveToFirst());

        int yColumnNameIndex = mCursor.getColumnIndex("name");
        do {
            String yColumnName = mCursor.getString(yColumnNameIndex);

            yMovieReviewColumnHashSet.remove(yColumnName);

        } while(mCursor.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required SortBy
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                yMovieReviewColumnHashSet.isEmpty());

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // now, do our tables contain the correct columns?
        mCursor = db.rawQuery("PRAGMA table_info(" + MovieInfoEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                mCursor.moveToFirst());


        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> zMovieInfoColumnHashSet = new HashSet<String>();

        zMovieInfoColumnHashSet.add(MovieInfoEntry._ID);
        zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_POSTERLINK);
        zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_MOVIE_ID);
        
        zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_ORIGINAL_TITLE);
        zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_RELEASE_DATE);
        zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_OVERVIEW);
        zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_VOTE_AVERAGE);
        zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_POPULARITY);
        zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_VOTE_COUNT);

        zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_BACKDROPLINK); // new
    //    zMovieInfoColumnHashSet.add(MovieInfoEntry.COL_VIDEOLINK);     // new
        //--------------------------------------------------------------

        //---------------------------------------------------//
        int zColumnNameIndex = mCursor.getColumnIndex("name");
        do {
            String zColumnName = mCursor.getString(zColumnNameIndex);

            zMovieInfoColumnHashSet.remove(zColumnName);

        } while(mCursor.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required SortBy
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                zMovieInfoColumnHashSet.isEmpty());

        db.close();
    }


    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    //xxxxxxxxxxxxxxxxxxxxx BEGIN xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    /*
        Build code to test that we can insert and query the SortBy database.
        You'll want to look in TestUtilities, the "createPopularValues" function.
        You can also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public void testMovieInfoTable() {

        Log.d("-- " + LOG_TAG, " ---testPopularTable()--- "); // tky add

        long rowId;
        rowId = insertMovieInfoTable();

        Log.d("-- " + LOG_TAG, " rowId : " + rowId); // tky add
    }


    public long insertMovieInfoTable() {

        SQLiteDatabase db = new MovieSQLiteOpenHelper(this.mContext).getWritableDatabase();

        ContentValues contentValues = TestUtilities.createTestContentValuesForMovieInfo();

        long xMovieInfoRowId = db.insert(MovieInfoEntry.TABLE_NAME, null, contentValues);

        // Verify we got a row back.
        assertTrue(xMovieInfoRowId != -1);

        // Step 4 : Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                            MovieInfoEntry.TABLE_NAME, // Table to Query
                            null, // all columns
                            null, // Columns for the "where" clause
                            null, // Values for the "where" clause
                            null, // columns to group by
                            null, // columns to filter by row groups
                            null // sort order
                    );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from X_MovieInfo query", cursor.moveToFirst() );

        // Step 5 : Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: X_MovieInfo Query Validation Failed",
                                                cursor, contentValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",
                cursor.moveToNext() );

        TestUtilities.validateCursorValue( cursor );

        // Step 6 : Close Cursor and Database
        cursor.close();
        db.close();

        return xMovieInfoRowId;
    }

    //
    //    Build code to test that we can insert and query the database.
    //    You'll want to look in TestUtilities where you can use the "createPopularValues" function.
    //    You can also make use of the validateCurrentRecord function from within TestUtilities.
    //
    public void testMovieReviewTable() {

        Log.d("-- " + LOG_TAG, " ---testMovieInfoTable()--- "); // tky add

        // First insert the sortby value, and then use the xMovieInfoRowId to insert
        // the movieInfo. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testPopularTable
        // we can move this code to insertPopularTable and then call insertPopularTable from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testPopularTable can only return void because it's a test.

        // Instead of rewriting all of the code we've already written in testPopularTable
        // we can move this code to insertPopularTable and then call insertPopularTable from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testPopularTable can only return void because it's a test.

        long xMovieInfoRowId = insertMovieInfoTable();

        // Make sure we have a valid row ID.
        assertFalse("Error: Location Not Inserted Correctly", xMovieInfoRowId == -1L);

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieSQLiteOpenHelper dbHelper = new MovieSQLiteOpenHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (MovieInfo): Create MovieInfo values
        ContentValues contentValues = TestUtilities.createTestContentValuesForMovieReview(xMovieInfoRowId);

        // Third Step (MovieInfo): Insert ContentValues into database and get a row ID back
        long movieReview_RowId = db.insert(MovieReviewEntry.TABLE_NAME, null, contentValues);

        assertTrue(movieReview_RowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                            MovieReviewEntry.TABLE_NAME,  // Table to Query
                            null, // leaving "columns" null just returns all the columns.
                            null, // cols for "where" clause
                            null, // values for "where" clause
                            null, // columns to group by
                            null, // columns to filter by row groups
                            null  // sort order
                    );


        // Move the cursor to a valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        // Fifth Step: Validate the movieInfo Query
        TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                cursor, contentValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from weather query",
                cursor.moveToNext());

        TestUtilities.validateCursorValue(cursor);

        // Sixth Step: Close cursor and database
        cursor.close();
        dbHelper.close();
    }

    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    //xxxxxxxxxxxxxxxxxxxxxx  END  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

}
