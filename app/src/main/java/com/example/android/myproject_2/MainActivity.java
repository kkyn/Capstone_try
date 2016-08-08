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

public class MainActivity extends AppCompatActivity implements Movie_Fragment.CallBack{

 //   public class MainActivity extends FragmentActivity implements Movie_Fragment.CallBack{
 //  protected MainActivityFragment newFragment = new MainActivityFragment();

    private static final String LOG_TAG = MainActivity.class.getSimpleName(); // tky add

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "xxxx onResume() -----");
    //  Toast.makeText(getApplicationContext(),"-- In MainActivity/onResume() 0 --", Toast.LENGTH_SHORT).show();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Movie_Fragment mMovieFragment = (Movie_Fragment) fragmentManager.findFragmentById(R.id.container);
    //    Movie_Fragment mMovieFragment = (Movie_Fragment) fragmentManager.findFragmentByTag(null);

        if ( mMovieFragment != null ) {

            Log.d(LOG_TAG, "xxxx onResume() / mMovieFragment != null / mMovieFragment.myRestartLoaderCode()  -----");
         //   Toast.makeText(getApplicationContext(),"-- In MainActivity/onResume() 1 --", Toast.LENGTH_SHORT).show();
            mMovieFragment.myRestartLoaderCode();
        }
    }

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.android.myproject_2.R.layout.activity_main);

        Log.d(LOG_TAG, "xxxx onCreate() -----");

        if (savedInstanceState == null){
		//	Toast.makeText(getApplicationContext(),"-- In MainActivity/onCreate --", Toast.LENGTH_SHORT).show();
            //Log.d(LOG_TAG, "--- -1, IN MainActivity/onCREATE/pre  open moviesFragment --------");

            Movie_Fragment moviesFragment = new Movie_Fragment();

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(com.example.android.myproject_2.R.id.container,moviesFragment);
            fragmentTransaction.commit();
            // --or--
            // getSupportFragmentManager().beginTransaction().add(R.id.container,new MainActivityFragment()).commit();

            Log.d(LOG_TAG, "xxxx onCreate() / savedInstanceState == null / MoviesSyncAdapter.initializeSyncAdapter(this) -----");
            //--------------------
            MoviesSyncAdapter.initializeSyncAdapter(this);
			// remove code --- A ---
            //---------------------

           //moviesFragment.onLocationChanged( );
        }
        //Log.d(LOG_TAG, "--- -1, IN MainActivity/onCREATE/post open moviesFragment --------");
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
        Toast.makeText(getApplicationContext(),".. MainActivity() / onItemSelectedInRecyclerView() ..", Toast.LENGTH_LONG).show();
        //Toast.makeText(this,".. MainActivity()/onItemSelectedInRecyclerView() ..", Toast.LENGTH_SHORT).show(); // with 'this' would not work ???

        Log.d(LOG_TAG, "xxxx onItemSelectedInRecyclerView -> StartActivity -> DetailMovieActivity --");
        Log.d(LOG_TAG, "xxxx uri: " + uri.toString());
        /*
        * Open a new view
        */
        Intent mIntent = new Intent(this, DetailMovieActivity.class);
        mIntent.setData(uri);
        startActivity(mIntent);
    }
    //-----------xxxx MENU Stuff (End)----------------
    //------------------------------------------------

}
/*
// remove code --- A ---
//String sortMoviesBy = Utility.getPreferredSortSequence(this);
//FetchMoviesDbAsyncTask task = new FetchMoviesDbAsyncTask(this);
//task.execute(sortMoviesBy);
*/
