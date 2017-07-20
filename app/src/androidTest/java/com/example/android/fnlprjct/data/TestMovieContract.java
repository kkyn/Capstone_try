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

import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;

/*
    Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestMovieContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    // e.g. /North Pole ==> %2FNorth%20Pole
    //      /popularity ==> %2Fpopularity
    private static final String TEST_MOVIE_NAME = "/XMovie"; //
   // private static final String TEST_MOVIE_NAME = "/popularity"; //
    //private static final String TEST_MOVIE_INFO = "/movieInfo";

    //private static final long TEST_MOVIE_ID = 14;
    private static final long TEST_MOVIE_ID = 14L;

    public static final String LOG_TAG = TestMovieContract.class.getSimpleName();


    //============================================================================//
    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

    ///////////////////////////////////////////////////////
    ////////////// MovieInfoEntry /////////////////////
    ///////////////////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.myproject_2/movieinfo/movieName"
    ///////////////////////////////////////////
    public void test_buildUri_MovieInfo() {

        Uri mUri = MovieInfoEntry.buildUri_MovieInfo(TEST_MOVIE_NAME);

        assertNotNull(
                "Error: Null Uri returned. "
                        + "You must fill-in buildUriPopularity in MovieContract_x."
                , mUri);

        assertEquals(
                "Error: 'TEST_MOVIE_NAME' not properly appended to the end of the Uri",
                TEST_MOVIE_NAME,
                mUri.getLastPathSegment());

        // " content://com.example.android.myproject_2/movieSortBy//popularity "
        // " content://com.example.android.myproject_2/SortByPopularity//XMovie "
        // " content://com.example.android.myproject_2/movieinfo/%2FXMovie "
        assertEquals(
                "Error: 'X_MovieInfo' Uri doesn't match our expected result",
                mUri.toString(),
                "content://com.example.android.fnlprjct/movieinfo/%2FXMovie" );
    }
    public void test_buildUri_MovieInfoWithId() {

        Uri mUri = MovieInfoEntry.buildUri_MovieInfoWithId(TEST_MOVIE_ID);

        assertNotNull(
                "Error: Null Uri returned. " +
                "You must fill-in buildUri_MovieInfoWithId() in MovieContract.",
                mUri);

        assertEquals(
                "Error:  Long expected",
                TEST_MOVIE_ID,
                Long.parseLong(mUri.getLastPathSegment(), 10));
    }

    public void test_getMovieId_FromMovieInfoUri() {

        Uri mUri =  Uri.parse("content://com.example.android.myproject_2/movieinfo/1234");

        String mMovieId = MovieContract.MovieInfoEntry.getMovieId_FromMovieInfoUri(mUri);

        assertEquals(
                "Error: 'extracted Movie_Id from Uri don't match with fixed 1234",
                "1234",
                mMovieId);

        //Log.d("-- " + LOG_TAG, "Uri : " + mUri.toString() ); // tky add
        //Log.d("-- "+ LOG_TAG, "movieId : " + mMovieId);     // tky add

    }

    ///////////////////////////////////////////////////////
    ////////////// MovieReviewEntry /////////////////////
    ///////////////////////////////////////////////////////
    public void test_buildUri_MovieReviewWithName() {

        Uri mUri = MovieContract.MovieReviewEntry.buildUri_4MovieReviewWithName(TEST_MOVIE_NAME);

        assertNotNull("Error:  Null Uri returned."
                + "You must fill-in buildUri_X_MovieReviewWithName() in MovieContract.\"",
                mUri);

        assertEquals("Error: 'TEST_MOVIE_NAME' not properly appended to the end of the Uri",
                TEST_MOVIE_NAME,
                mUri.getLastPathSegment());

        assertEquals("Error: 'X_MovieReview' Uri doesn't match our expected result",
                mUri.toString(),
                "content://com.example.android.fnlprjct/moviereview/%2FXMovie");
    }
    public void test_buildUri_X_MovieReviewWithId() {

        Uri mUri = MovieContract.MovieReviewEntry.buildUri_MovieReviewWithId(TEST_MOVIE_ID);

        assertNotNull(
                "Error: Null Uri returned. " +
                        "You must fill-in buildUri_MovieReviewWithId() in MovieContract.",
                mUri);

        assertEquals(
                "Error:  Long expected",
                TEST_MOVIE_ID,
                Long.parseLong(mUri.getLastPathSegment(), 10));
    }

    public void test_getMovieId_FromMovieReviewUri() {

        Uri mUri =  Uri.parse("content://com.example.android.myproject_2/moviereview/1234");

        String mMovieId = MovieContract.MovieReviewEntry.getMovieId_FromMovieReviewUri(mUri);

        assertEquals(
                "Error: 'extracted Movie_Id from Uri don't match with fixed 1234",
                "1234",
                mMovieId);
    }
    //zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

    ///////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.myproject_2/popularity/movieName"
    ///////////////////////////////////////////

    ///////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.myproject_2/popularity/id"
    ///////////////////////////////////////////

    //============================================================================//

    //============================================================================//
    // /////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.myproject_2/movieInfo"
    ////////////////////////////////////////////
//    public void testBuildUri_MovieInfo_Id() {

}
