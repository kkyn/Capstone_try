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
import android.util.Log;

import com.example.android.myproject_2.data.MovieContract.PopularEntry;

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

    private static final long TEST_MOVIE_ID = 14L;

    public static final String LOG_TAG = TestMovieContract.class.getSimpleName();


    //============================================================================//
    ///////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.myproject_2/popularity/movieName"
    ///////////////////////////////////////////
    public void testBuildUriPopularity() {

        Uri mUri = PopularEntry.buildUriPopularity(TEST_MOVIE_NAME);

        assertNotNull("Error: Null Uri returned. "
                + "You must fill-in buildUriPopularity in MovieContract_x."
                , mUri);

        assertEquals("Error: 'TEST_MOVIE_NAME' not properly appended to the end of the Uri"
                , TEST_MOVIE_NAME
                , mUri.getLastPathSegment());

        // " content://com.example.android.myproject_2/movieSortBy//popularity "
        // " content://com.example.android.myproject_2/SortByPopularity//XMovie "
        assertEquals("Error: 'Popularity' Uri doesn't match our expected result"
                , mUri.toString()
                , "content://com.example.android.myproject_2/popularity/%2FXMovie" );
    }
    ///////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.myproject_2/popularity/id"
    ///////////////////////////////////////////
    public void testBuildUriPopularityWithId() {
        Uri mUri = PopularEntry.buildUri_PopularityWithId(TEST_MOVIE_ID);

        assertNotNull("Error: Null Uri returned. "
                + "You must fill in movie ID in MovieContract_x."
                , mUri);

        assertEquals("Error: Long expected",
                TEST_MOVIE_ID,
                Long.parseLong(mUri.getLastPathSegment(), 10));
    }
    //============================================================================//
//    public void testBuildUriRating() {
//        Uri mUri = MovieContract_x.RatingEntry.buildUri_Rating(TEST_MOVIE_NAME);
//
//        assertNotNull("Error: Uri for Rating expected"
//                + "You must fill-in buildUri_Rating in MovieContract_x."
//                , mUri );
//
//        assertEquals("Error: 'TEST_MOVIE_NAME' not properly appended to the end of the Uri."
//            , TEST_MOVIE_NAME
//            , mUri.getLastPathSegment());
//
//        assertEquals("Error: 'Rating' Uri doesn't match our expected result"
//                , mUri.toString()
//                , "content://com.example.android.myproject_2/rating/%2FXMovie");
//    }
//    public void testBuildUriRatingAndId() {
//        Uri mUri = MovieContract_x.RatingEntry.buildUri_RatingWithId(TEST_MOVIE_ID);
//
//        assertNotNull("Error: Uri for Rating expected"
//            + "You must fill-in buildUri_Rating in MovieContract_x."
//            , mUri);
//
//        assertEquals("Error: Long expected"
//                , TEST_MOVIE_ID
//                , Long.parseLong(mUri.getLastPathSegment(), 10));
//    }

    //============================================================================//
    // /////////////////////////////////////////
    // Test methods for Uri,
    // "content://com.example.android.myproject_2/movieInfo"
    ////////////////////////////////////////////
//    public void testBuildUri_MovieInfo_Id() {
//
//        Uri mUri = MovieContract_x.MovieInfoEntry.buildUri_MovieInfo_Id(TEST_MOVIE_ID);
//
//        assertNotNull("Error: Null Uri returned. "
//                + "you must fill in buildUri_MovieInfo_Id "
//                + "MovieContract_x."
//                , mUri);
//
//        assertEquals("Error: Long expected"
//                , TEST_MOVIE_ID
//                , Long.parseLong(mUri.getLastPathSegment(), 10));
//    }

//    public void testBuildUri_Movie_Id() {
//        Uri mUri = MovieContract_x.MovieEntry.buildUri_Movie_Id(TEST_MOVIE_ID);
//
//        assertNotNull("Error:  ", mUri);
//
//        assertEquals("Error:...", TEST_MOVIE_ID, Long.parseLong(mUri.getLastPathSegment(), 10));
//    }
//
//    public void testBuildUri_Movie_Name() {
//        Uri mUri = MovieContract_x.MovieEntry.buildUri_Movie_Name(TEST_MOVIE_NAME);
//
//        assertNotNull("Error:: Null Uri returned. "
//            + "You must fill-in buildUri_Movie_Name in MovieContract_x."
//            , mUri);
//
//        assertEquals("Error: 'TEST_MOVIE_NAME' not properly appended to the end of the Uri"
//            , TEST_MOVIE_NAME
//            , mUri.getLastPathSegment());
//
//        // " content://com.example.android.myproject_2/movieSortBy//popularity "
//        // " content://com.example.android.myproject_2/SortByPopularity//XMovie "
//        assertEquals("Error: 'Popularity' Uri doesn't match our expected result"
//            , mUri.toString()
//            , "content://com.example.android.myproject_2/movie/%2FXMovie");
//    }
    public void testGetMovieId_fromUri() {

        Uri mUri =  Uri.parse("content://com.example.android.myproject_2/popularity/1234");
        String mMovieId = PopularEntry.getMovieId_fromUri(mUri);

        assertEquals("Error: 'extracted Movie_Id from Uri don't match with fixed 1234",
            "1234", mMovieId);
        Log.d("-- " + LOG_TAG, "Uri : " + mUri.toString() ); // tky add
        Log.d("-- "+ LOG_TAG, "movieId : " + mMovieId);     // tky add

    }
}
