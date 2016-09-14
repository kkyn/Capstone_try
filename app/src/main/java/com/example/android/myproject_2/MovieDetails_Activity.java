package com.example.android.myproject_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MovieDetails_Activity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetails_Activity.class.getSimpleName();

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.android.myproject_2.R.layout.activity_moviedetails);

        //++++++++++++++++++ from sunshine as reference
    /*    if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.weather_detail_container, fragment)
                    .commit();
        }
        */
        //++++++++++++++++++ from sunshine as reference

        // ?? bundle from Main_Activity is passed on to the MovieDetails_Fragment ?? how/why ??, see code below !!
    //    if (savedInstanceState == null) {

            Intent intent = this.getIntent();
            mUri = intent.getData();

            Log.d(LOG_TAG, "yyyy onCreate / savedInstanceState == null / DetailMoviewFragment --");
            Log.d(LOG_TAG, "yyyy onCreate / mUri : " + mUri.toString());

            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieDetails_Fragment.DETAIL_URI,mUri );

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            MovieDetails_Fragment mMovieDetailsFragment = new MovieDetails_Fragment();
            mMovieDetailsFragment.setArguments(bundle);

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_id4_moviedetails, mMovieDetailsFragment);
            fragmentTransaction.commit();
            // --or--
//             getSupportFragmentManager().beginTransaction()
//                                .add(R.id.detail_movie_container, new MovieDetails_Fragment())
//                                .commit();

        //    Log.d(LOG_TAG, "yyyy onCreate / savedInstanceState == null / DetailMoviewFragment --");
//        }
        // tky add, 6th August 2.28 am
//        else {
//            Log.d(LOG_TAG, "-------------------------------- ERROR ");
//        }

        // tky add 3rd August 2015
       // MovieDetails_Fragment.myResetLoaderCode_1();


      ///   MovieDetails_Fragment.myRestartLoaderCode();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.android.myproject_2.R.menu.menu_detailmovie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.example.android.myproject_2.R.id.action_settings) {
            startActivity(new Intent(this, SettingsPreferenceActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
