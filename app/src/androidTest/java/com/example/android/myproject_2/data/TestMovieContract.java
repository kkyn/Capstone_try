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

import com.example.android.myproject_2.data.MovieContract.MovieSelectEntry;

/*
    Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestMovieContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_MOVIE_NAME = "/North Pole";
    private static final long TEST_WEATHER_DATE = 1419033600L;  // December 20th, 2014

    /*
        Students: Uncomment this out to test your weather location function.
     */
    public void testBuildMovieSelect() {
        Uri locationUri = MovieSelectEntry.buildMovieSelect(TEST_MOVIE_NAME);

        assertNotNull("Error: Null Uri returned.  You must fill-in buildMovieSelect in " +
                        "MovieContract.",
                locationUri);

        assertEquals("Error: Weather location not properly appended to the end of the Uri",
                TEST_MOVIE_NAME, locationUri.getLastPathSegment());

        assertEquals("Error: Weather location Uri doesn't match our expected result",
                locationUri.toString(),
                "content://com.example.android.myproject_2/movieSelect/%2FNorth%20Pole");
    }
}
