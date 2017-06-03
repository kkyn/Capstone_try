package com.example.android.fnlprjct;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.fnlprjct.sync.MSyncAdapter;

public class Main_Activity extends AppCompatActivity implements Main_Fragment.CallBackListener {

    /*private static final String LOG_TAG = Main_Activity.class.getSimpleName(); // tky add
    private static final String DETAILMOVIE_TAG = "DETAIL_MOVIE";*/

    public boolean is2Pane = false;

    private int displayMode;
    private String mode;

    ////////////////////////////////////
    @Override //--- 2
    protected void onPause() {
        super.onPause();
    }
    @Override   //--- 3
    protected void onStop() {
        super.onStop();
    }
    @Override   //--- 4
    protected void onRestart() {
        super.onRestart();
    }
    @Override   //--- 5
    protected void onDestroy() {
        super.onDestroy();
    }
    ////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();

       displayMode = getResources().getConfiguration().orientation;

        mode = (displayMode==1)? "portrait" :"landscape";

        // displayMode -> 1 ==> portrait, 2 ==> landscape

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Main_Fragment mainFragment = (Main_Fragment) fragmentManager.findFragmentById(R.id.container_id4_fragment_main);
        if ( mainFragment != null ) {

            mainFragment.myRestartLoaderCode();
        }
    }

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.android.fnlprjct.R.layout.activity_main);

        // layout 'activity_main.xml' opens a fragment via 'android:name' attribute,
        // so no need to use getSupportFragmentManager,..fragmentTransaction,..add,..commit.

        displayMode = getResources().getConfiguration().orientation;

        mode = (displayMode==1)? "portrait" :"landscape";

        // ToDO : add fragmentTransaction
        // The view with 'id' only exist within layout 'activity_main.xml' file,
        // if the device is a tablet.
        if (findViewById(R.id.pane2_container) != null) {
            is2Pane = true;
        }

        MSyncAdapter.initializeSyncAdapter(this);
    }

    //------------------------------------------------
    //----------- OPTIONS MENU Stuff (Begin)------------------
    //------------------------------------------------
    @Override public boolean
    onCreateOptionsMenu(Menu menu) {
        // Inflate the 'menu';
        // This add items in 'R.menu.menu_main' to the action bar, 'menu' if it is present.
        // @1st param -- Resource ID for an XML layout resource to load (e.g., R.menu.main_activity)
        // @2nd param -- menu, The Menu to inflate into. The items and submenus will be added to this Menu
        this.getMenuInflater().inflate(com.example.android.fnlprjct.R.menu.menu_main, menu);

        return true;
    }

    @Override public boolean
    onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.example.android.fnlprjct.R.id.action_settings) {
            startActivity(new Intent(this, SettingsPreferenceActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------------
    //----------- OPTIONS MENU Stuff (End) -------------------
    //------------------------------------------------

    @Override
    public void onItemSelectedInRecyclerView(Uri uri) {

        displayMode = getResources().getConfiguration().orientation;
        mode = (displayMode==1)? "portrait" :"landscape";

        if (is2Pane == true) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(MDetails_Fragment.DETAIL_URI, uri);

            MDetails_Fragment movieDetails_Fragment = new MDetails_Fragment();
            movieDetails_Fragment.setArguments(bundle);

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.pane2_container, movieDetails_Fragment);
            fragmentTransaction.commit();
        }
        else {

        /* Open a new view*/
            Intent mIntent = new Intent(this, MDetails_Activity.class);
            mIntent.setData(uri);
            startActivity(mIntent);
        }
    }

}
