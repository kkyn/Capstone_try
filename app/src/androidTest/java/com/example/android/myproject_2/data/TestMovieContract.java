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

import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.android.myproject_2.data.MovieContract.SortByEntry;
import com.example.android.myproject_2.data.MovieContract.MovieInfoEntry;

/*
    Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestMovieContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    // e.g. /North Pole ==> %2FNorth%20Pole
    //      /popularity ==> %2Fpopularity
    private static final String TEST_MOVIE_SORTED_BY = "/popularity"; //

    //private static final String TEST_MOVIE_INFO = "/movieInfo";

    private static final long TEST_MOVIE_ID = 14L;


    ///////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.myproject_2/movieSortBy"
    ///////////////////////////////////////////
    public void testBuildUriMovieSortByWithId() {
        Uri sortByUri = SortByEntry.buildUriMovieSortByWithId(TEST_MOVIE_ID);

        assertNotNull("Error: Null Uri returned. "
                        + "You must fill in movie ID in MovieContract."
                        , sortByUri);

        assertEquals("Error: Long expected",
                        TEST_MOVIE_ID,
                        Long.parseLong(sortByUri.getLastPathSegment(),10));
    }
    /*     */
    public void testBuildUriMovieSortBy() {
        Uri sortByUri = SortByEntry.buildUriMovieSortBy(TEST_MOVIE_SORTED_BY);

        assertNotNull("Error: Null Uri returned. "
                        + "You must fill-in buildUriMovieSortBy in "
                        + "MovieContract."
                        , sortByUri);

        assertEquals("Error: 'Sort by' not properly appended to the end of the Uri"
                        , TEST_MOVIE_SORTED_BY
                        , sortByUri.getLastPathSegment());

        //
        // " content://com.example.android.myproject_2/movieSortBy//popularity "
        //
        assertEquals("Error: 'Sort by' Uri doesn't match our expected result",
                    sortByUri.toString(),
                    "content://com.example.android.myproject_2/movieSortBy/%2Fpopularity");
    }

    ///////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.myproject_2/movieInfo"
    ////////////////////////////////////////////
    public void testBuildUriMovieInfoWithId() {
        Uri movieInfoUri = MovieInfoEntry.buildUriMovieInfoWithId(TEST_MOVIE_ID);

        assertNotNull("Error: Null Uri returned. "
                + "you must fill in buildUriMovieInfoWithId "
                + "MovieContract."
                , movieInfoUri);

        assertEquals("Error: Long expected"
                , TEST_MOVIE_ID
                , Long.parseLong(movieInfoUri.getLastPathSegment(), 10));
    }
}
