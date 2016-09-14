package com.example.android.myproject_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.myproject_2.sync.MoviesSyncAdapter;

public class Main_Activity extends AppCompatActivity implements Main_Fragment.CallBackListner {

 //  public class Main_Activity extends FragmentActivity implements Main_Fragment.CallBackListner{
 //  protected MainActivityFragment newFragment = new MainActivityFragment();

    private static final String LOG_TAG = Main_Activity.class.getSimpleName(); // tky add
    private static final String DETAILMOVIE_TAG = "DETAIL_MOVIE";

    public boolean is2Pane = false;

    private int displayMode;
    private String mode;

    ////////////////////////////////////

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "xxxx 2 onPause() ---");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "xxxx 3 onStop() ---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "xxxx 4 onRestart() ---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "xxxx 5 onDestroy() ---");
    }

    ////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();

       displayMode = getResources().getConfiguration().orientation;

        mode = (displayMode==1)? "portrait" :"landscape";
        // displayMode -> 1 ==> portrait, 2 ==> landscape
        Log.d(LOG_TAG, "xxxx 1 onResume() ----- orientation = " + mode); //displayMode
        Toast.makeText(getApplicationContext(),"xxxx 1 onResume() ----- orientation = "+mode+" -", Toast.LENGTH_LONG).show();

        /////////////////////////////////////////////////////////////
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Main_Fragment mainFragment = (Main_Fragment) fragmentManager.findFragmentById(R.id.container_id4_fragment_main);
        if ( mainFragment != null ) {

            Log.d(LOG_TAG, "xxxx 1 onResume() / mainFragment != null / mainFragment.myRestartLoaderCode()  -----");
            Toast.makeText(getApplicationContext(),"-xxxx 1 onResume() / mainFragment != null / mainFragment.myRestartLoaderCode()-", Toast.LENGTH_LONG).show();

            mainFragment.myRestartLoaderCode();
        }
        /////////////////////////////////////////////////////////////
        /*
		// Main_Fragment mainFragment = (Main_Fragment) fragmentManager.findFragmentByTag(null);
        */
    }

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.android.myproject_2.R.layout.activity_main);

        Log.d(LOG_TAG, "xxxx 0 onCreate() -----");

        // layout 'activity_main.xml' opens a fragment via 'android:name' attribute,
        // so no need to use getSupportFragmentManager,..fragmentTransaction,..add,..commit.

        displayMode = getResources().getConfiguration().orientation;

        mode = (displayMode==1)? "portrait" :"landscape";

        // ToDO : add fragmentTransaction
        // The view with 'id' only exist within layout 'activity_main.xml' file,
        // if the device is a tablet.
        if (findViewById(R.id.container_id4_moviedetails) != null) {
            Toast.makeText(getApplicationContext(),"xxxx 0 onCreate() -- "+mode+ " ---", Toast.LENGTH_LONG).show();
            is2Pane = true;
            Log.d(LOG_TAG, "xxxx 0 onCreate() ----- in findViewById(R.id.container_detail_movie) ----");
            if (savedInstanceState == null) {
                Toast.makeText(getApplicationContext(),"xxxx 0 onCreate() -- Into savedInstanceState == null", Toast.LENGTH_LONG).show();
                /*android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_id4_moviedetails, new MovieDetails_Fragment(), DETAILMOVIE_TAG);
                fragmentTransaction.commit();*/
            }
        }
        //----------------------
        else{
            Toast.makeText(getApplicationContext(),"xxxx 0 onCreate-- "+mode+" ---", Toast.LENGTH_SHORT).show();
        }
        //----------------------

        //////////////////////////////
        /*if (findViewById(R.id.container_detail_movie) != null) {
      //  if (findViewById(R.id.linear_layout_detail_movie) != null) {
            Toast.makeText(getApplicationContext(),"--- Detected container_detail_movie ---", Toast.LENGTH_LONG).show();
            if (savedInstanceState == null) {
                MovieDetails_Fragment mDetailMovieFragment = new MovieDetails_Fragment();

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container_detail_movie, mDetailMovieFragment);
                fragmentTransaction.commit();
            }
        }*/
        MoviesSyncAdapter.initializeSyncAdapter(this);
        /////////////////////////////////////////////////////////////
		//  Main_Fragment mMovie_Fragment = (Main_Fragment) getSupportFragmentManager().findFragmentById(R.id.container_in_actvty_mn);
        /////////////////////////////////////////////////////////////
//        if (savedInstanceState == null) {
//            Main_Fragment movie_fragment = new Main_Fragment();
//
//            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.add(R.id.container_in_actvty_mn, movie_fragment);
//            fragmentTransaction.commit();

//            MoviesSyncAdapter.initializeSyncAdapter(this);
//        }
        /////////////////////////////////////////////////////////////
        /*if (savedInstanceState == null){

            Main_Fragment movie_fragment = new Main_Fragment();

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_in_actvty_mn,movie_fragment);
        //    fragmentTransaction.add(com.example.android.myproject_2.R.id.container,movie_fragment);
            fragmentTransaction.commit();
            // --or--
            // getSupportFragmentManager().beginTransaction().add(R.id.container,new MainActivityFragment()).commit();

            Log.d(LOG_TAG, "xxxx onCreate() / savedInstanceState == null / MoviesSyncAdapter.initializeSyncAdapter(this) -----");
            //--------------------
            MoviesSyncAdapter.initializeSyncAdapter(this);
			// remove code --- A ---
            //---------------------

           //movie_fragment.onLocationChanged( );
        }*/
        //Log.d(LOG_TAG, "--- -1, IN Main_Activity/onCREATE/post open movie_fragment --------");
    }

    //------------------------------------------------
    //-------xxxx MENU Stuff (Begin)------------------
    //------------------------------------------------
    @Override public boolean
    onCreateOptionsMenu(Menu menu) {
        // Inflate the 'menu';
        // This add items in 'R.menu.menu_main' to the action bar, 'menu' if it is present.
        // @1st param -- Resource ID for an XML layout resource to load (e.g., R.menu.main_activity)
        // @2nd param -- menu, The Menu to inflate into. The items and submenus will be added to this Menu
        this.getMenuInflater().inflate(com.example.android.myproject_2.R.menu.menu_main, menu);

        return true;
    }

    @Override public boolean
    onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.example.android.myproject_2.R.id.action_settings) {
            startActivity(new Intent(this, SettingsPreferenceActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelectedInRecyclerView(Uri uri) {

        displayMode = getResources().getConfiguration().orientation;
        mode = (displayMode==1)? "portrait" :"landscape";

        Log.d(LOG_TAG, "xxxx onItemSelectedInRecyclerView -> StartActivity -> MovieDetails_Activity --");
        Log.d(LOG_TAG, "xxxx uri: " + uri.toString());

        if (is2Pane == true) {

            //Toast.makeText(getApplicationContext(), "xxxx In is2Pane option ----", Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieDetails_Fragment.DETAIL_URI, uri);

            MovieDetails_Fragment movieDetails_Fragment = new MovieDetails_Fragment();
            movieDetails_Fragment.setArguments(bundle);

            Log.d(LOG_TAG, "xxxx is2Pane ---- before attach movieDetail_Fragment");

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_id4_moviedetails, movieDetails_Fragment);
            fragmentTransaction.commit();

            Log.d(LOG_TAG, "xxxx is2Pane ---- after attach movieDetail_Fragment");

            Toast.makeText(getApplicationContext(),"-onItemSelectedInRecyclerView-- "+mode+" ---", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"-onItemSelectedInRecyclerView-- "+mode+" ---", Toast.LENGTH_SHORT).show();

        /* Open a new view*/
            Intent mIntent = new Intent(this, MovieDetails_Activity.class);
            mIntent.setData(uri);
            startActivity(mIntent);
        }
    }
    //------------------------------------------------
    //-----------xxxx MENU Stuff (End)----------------
    //------------------------------------------------

}
/*
// remove code --- A ---
//String sortMoviesBy = Utility.getPreferredSortSequence(this);
//FetchMoviesDb_AsyncTask task = new FetchMoviesDb_AsyncTask(this);
//task.execute(sortMoviesBy);
*/
