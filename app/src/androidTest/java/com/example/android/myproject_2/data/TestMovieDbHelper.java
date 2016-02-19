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
package com.example.android.myproject_2.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;
import com.example.android.myproject_2.data.MovieContract.SortByEntry;

import java.util.HashSet;

public class TestMovieDbHelper extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieDbHelper.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
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
        tableNameHashSet.add(MovieInfoEntry.TABLE_MOVIEINFO);
        tableNameHashSet.add(SortByEntry.TABLE_SORTBY);

        // tky:
        // Deletes a database including its journal file and other auxiliary files
        // that may have been created by the database engine
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);

        // tky:
        // Create and/or open a database that will be used for reading and writing.
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor mCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        //Cursor mCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                mCursor.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(mCursor.getString(0));
        } while( mCursor.moveToNext() );

        // if this fails, it means that your database doesn't contain both the SortBy entry
        // and MovieInfo entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        mCursor = db.rawQuery("PRAGMA table_info(" + MovieInfoEntry.TABLE_MOVIEINFO + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                mCursor.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();

        locationColumnHashSet.add(MovieInfoEntry._ID);
        locationColumnHashSet.add(MovieInfoEntry.COL_POSTERLINK);
        locationColumnHashSet.add(MovieInfoEntry.COL_VIDEOLINK);
        locationColumnHashSet.add(MovieInfoEntry.COL_ID);

        int columnNameIndex = mCursor.getColumnIndex("name");
        do {
            String columnName = mCursor.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(mCursor.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required SortBy
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    /*
        Build code to test that we can insert and query the SortBy database.
        You'll want to look in TestUtilities, the "createSortByValues" function.
        You can also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public void testSortByTable() {
        insertSortBy();
    }



    /*
        Build code to test that we can insert and query the database.
        You'll want to look in TestUtilities where you can use the "createSortByValues" function.
        You can also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testMovieInfoTable() {
        // First insert the sortby value, and then use the sortByRowId to insert
        // the movieInfo. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testSortByTable
        // we can move this code to insertSortBy and then call insertSortBy from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testSortByTable can only return void because it's a test.

        long sortByRowId = insertSortBy();

        // Make sure we have a valid row ID.
        assertFalse("Error: Location Not Inserted Correctly", sortByRowId == -1L);

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (MovieInfo): Create MovieInfo values
        ContentValues movieInfoValues = TestUtilities.createMovieInfoValues(sortByRowId);

        // Third Step (MovieInfo): Insert ContentValues into database and get a row ID back
        long movieInfoRowId = db.insert(MovieInfoEntry.TABLE_MOVIEINFO, null, movieInfoValues);
        assertTrue(movieInfoRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor movieInfoCursor = db.query(
                MovieInfoEntry.TABLE_MOVIEINFO,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to a valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from location query", movieInfoCursor.moveToFirst());

        // Fifth Step: Validate the movieInfo Query
        TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                movieInfoCursor, movieInfoValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from weather query",
                movieInfoCursor.moveToNext());

        // Sixth Step: Close cursor and database
        movieInfoCursor.close();
        dbHelper.close();
    }

    /*
        This is a helper method for the testMovieInfoTable. You can move your
        code from testSortByTable to here so that you can call this code from both
        testMovieInfoTable and testSortByTable. ??
     */
    public long insertSortBy() {
        // Step 1 : Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Step 2 : Create ContentValues of what you want to insert
        // (you can use the createSortByValues if you wish)
        ContentValues testValues = TestUtilities.createSortByValues();

        // Step 3 : Insert ContentValues into database and get a row ID back
        long movieRowId;
        movieRowId = db.insert(SortByEntry.TABLE_SORTBY, null, testValues);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Step 4 : Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                SortByEntry.TABLE_SORTBY, // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order

        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );

        // Step 5 : Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",
                cursor.moveToNext() );

        // Step 6 : Close Cursor and Database
        cursor.close();
        db.close();

        return movieRowId;
    }

    /*
        This is a helper method for the testMovieInfoTable. You can move your
        code from testSortByTable to here so that you can call this code from both
        testMovieInfoTable and testSortByTable.
     */
    //public long insertSortBy() {
    //    return -1L;
    //}
}
