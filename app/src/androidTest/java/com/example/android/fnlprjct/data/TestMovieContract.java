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
import com.example.android.fnlprjct.data.MovieContract.MovieReviewEntry;

/*
    This is NOT a complete test for the MovieContract --- just for the functions
    that we expect you to write.
 */
public class TestMovieContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    // e.g. /North Pole ==> %2FNorth%20Pole
    //      /popularity ==> %2Fpopularity
    private static final String TEST_MOVIE_NAME = "/XMovie";

    private static final long TEST_MOVIE_ID = 14L;

    public static final String LOG_TAG = TestMovieContract.class.getSimpleName();

    ///////////////////////////////////////////////////////
    ////////////// MovieInfoEntry /////////////////////
    ///////////////////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.fnlprjct/movieinfo/movieName"
    ///////////////////////////////////////////
    public void testBuildUriMovieInfo() {

        Uri mUri = MovieInfoEntry.buildUriForMovieInfo(TEST_MOVIE_NAME);

        assertNotNull(
                "Error: Null Uri returned. "
                        + "You must fill-in buildUriPopularity in MovieContract_x."
                , mUri);

        assertEquals(
                "Error: 'TEST_MOVIE_NAME' not properly appended to the end of the Uri",
                TEST_MOVIE_NAME,
                mUri.getLastPathSegment());

        // " content://com.example.android.fnlprjct/movieSortBy//popularity "
        // " content://com.example.android.fnlprjct/SortByPopularity//XMovie "
        // " content://com.example.android.fnlprjct/movieinfo/%2FXMovie "
        assertEquals(
                "Error: 'MovieInfo' Uri doesn't match our expected result",
                mUri.toString(),
                "content://com.example.android.fnlprjct/movieinfo/%2FXMovie" );
    }
    public void testBuildUriMovieInfoWithId() {

        Uri mUri = MovieInfoEntry.buildUriForMovieInfoWithId(TEST_MOVIE_ID);

        assertNotNull(
                "Error: Null Uri returned. " +
                "You must fill-in buildUriForMovieInfoWithId() in MovieContract.",
                mUri);

        assertEquals(
                "Error:  Long expected",
                TEST_MOVIE_ID,
                Long.parseLong(mUri.getLastPathSegment(), 10));
    }

    public void testGetMovieIdFromMovieInfoUri() {

        Uri mUri =  Uri.parse("content://com.example.android.fnlprjct/movieinfo/1234");

        String mMovieId = MovieInfoEntry.getMovieIdFromMovieInfoUri(mUri);

        assertEquals(
                "Error: 'extracted Movie_Id from Uri don't match with fixed 1234",
                "1234",
                mMovieId);

    }

    ///////////////////////////////////////////////////////
    ////////////// MovieReviewEntry /////////////////////
    ///////////////////////////////////////////////////////
    public void testBuildUriMovieReviewWithName() {

        Uri mUri = MovieReviewEntry.buildUriForMovieReviewWithName(TEST_MOVIE_NAME);

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

    public void testBuildUriXMovieReviewWithId() {

        Uri mUri = MovieReviewEntry.buildUriForMovieReviewWithId(TEST_MOVIE_ID);

        assertNotNull(
                "Error: Null Uri returned. " +
                        "You must fill-in buildUriForMovieReviewWithId() in MovieContract.",
                mUri);

        assertEquals(
                "Error:  Long expected",
                TEST_MOVIE_ID,
                Long.parseLong(mUri.getLastPathSegment(), 10));
    }

    public void testGetMovieIdFromMovieReviewUri() {

        Uri mUri =  Uri.parse("content://com.example.android.fnlprjct/moviereview/1234");

        String mMovieId = MovieReviewEntry.getMovieIdForFromMovieReviewUri(mUri);

        assertEquals(
                "Error: 'extracted Movie_Id from Uri don't match with fixed 1234",
                "1234",
                mMovieId);
    }
    

    ///////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.fnlprjct/popularity/movieName"
    // "content://com.example.android.fnlprjct/popularity/id"
    // "content://com.example.android.fnlprjct/movieInfo"
    ////////////////////////////////////////////

}
