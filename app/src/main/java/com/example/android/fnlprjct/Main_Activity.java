package com.example.android.fnlprjct;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.fnlprjct.sync.MSyncAdapter;
import com.google.android.gms.analytics.Tracker; // need instance of 'google-service.json'

import static com.example.android.fnlprjct.MyApplication.getAppContext;

public class Main_Activity extends AppCompatActivity implements Main_Fragment.CallBackListener {

    /*private static final String LOG_TAG = Main_Activity.class.getSimpleName(); // tky add
    private static final String DETAILMOVIE_TAG = "DETAIL_MOVIE";*/

    public boolean is2Pane = false;

    /*private int displayMode;
    private String mode;*/

    /*private int mPosition = RecyclerView.NO_POSITION;
    private long itemID = 0;
    private Uri uri;*/

    private Tracker mTracker;

    ////////////////////////////////////
    @Override   //--- 2
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
    @Override   //--- 1
    protected void onStart() {
        super.onStart();
    }

    @Override   //--- 2
    protected void onResume() {
        super.onResume();

        /*displayMode = getResources().getConfiguration().orientation;
        mode = (displayMode == 1) ? "portrait" : "landscape";*/

        // displayMode -> 1 ==> portrait, 2 ==> landscape

        FragmentManager fMngr = getSupportFragmentManager();

        Main_Fragment mainFragment = (Main_Fragment) fMngr.findFragmentById(R.id.mainfragment_container);

        if ( mainFragment != null ) {
            mainFragment.myRestartLoaderCode();
        }
    }


    @Override    //--- 0
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // try, add, 21 June 2017, copied from elsewhere
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

        setContentView(com.example.android.fnlprjct.R.layout.activity_main);

        // layout 'activity_main.xml' opens a fragment via 'android:name' attribute,
        // so no need to use getSupportFragmentManager,..fragmentTransaction,..add,..commit.

        /*displayMode = getResources().getConfiguration().orientation;
        mode = (displayMode==1)? "portrait" :"landscape";*/

        // ToDO : add fragmentTransaction
        // The view with 'id' only exist within layout 'activity_main.xml' file,
        // if the device is a tablet.
        if (findViewById(R.id.fragmentdetail_container) != null) {
            is2Pane = true;
        }

        MSyncAdapter.initializeSyncAdapter(this);

        //--- Begin, Shared-Tracker -----
        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        //--- End,   Shared-Tracker -----
    }

    //------------------------------------------------
    //----------- OPTIONS MENU Stuff (Begin)------------------
    //------------------------------------------------
    @Override public boolean
    onCreateOptionsMenu(Menu menu) {
        // Inflate the 'menu';
        // This add items in 'R.menu.menu_main_activtiy' to the action bar, 'menu' if it is present.
        // @1st param -- Resource ID for an XML layout resource to load (e.g., R.menu.main_activity)
        // @2nd param -- menu, The Menu to inflate into. The items and submenus will be added to this Menu
        this.getMenuInflater().inflate(com.example.android.fnlprjct.R.menu.menu_main_activtiy, menu);

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

    // -------- Implementing Callback methods called from Fragment ----
    // -------- Declared in Fragment associate with this Activity -----
    //-----------------------------------------------------------------

    // ************* newer ***********************
    @Override
    public void onItemSelectedInRecyclerView(Intent intent, Bundle bundle) {

        startActivity(intent, bundle);
    }
    // ************* new ***********************
    /*@Override
    public void onItemSelectedInRecyclerView(MainRcyclrVw_Adapter.MainRcyclrVw_ViewHolder viewHolder, long itemId) {

        ImageView poster_imageview1 = viewHolder.poster_networkimageview;
        //DynamicHeightNetworkImageView poster_imageview1 = viewHolder.poster_networkimageview;

        final Pair<View, String> pair1 = Pair.create((View)poster_imageview1, viewHolder.poster_networkimageview.getTransitionName());
        //final Pair<View, String> pair1 = new Pair<>((View)poster_imageview1, viewHolder.poster_networkimageview.getTransitionName());

        // http://guides.codepath.com/android/shared-element-activity-transition
        ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(Main_Activity.this, pair1);
        //ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(Main_Activity.this, pair1);

        Bundle bundle = option.toBundle();

        uri = MovieContract.MovieInfoEntry.CONTENT_URI;
        uri = ContentUris.withAppendedId(uri, itemId);

        Intent mIntent = new Intent(this, Detail_Activity.class);
        mIntent.setData(uri);

        startActivity(mIntent, bundle);
    }*/
    // ************* old ***********************
    /*@Override
    public void onItemSelectedInRecyclerView(Uri uri) {

        displayMode = getResources().getConfiguration().orientation;

        mode = (displayMode==1)? "portrait" :"landscape";

        if (is2Pane == true) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(Detail_Fragment.DETAIL_URI, uri);

            Detail_Fragment dtlsFrgmnt = new Detail_Fragment();
            dtlsFrgmnt.setArguments(bundle);

            FragmentManager frgmntMngr = getSupportFragmentManager();
            FragmentTransaction frgmntTrnsctn = frgmntMngr.beginTransaction();

            frgmntTrnsctn.replace(R.id.pane2_container, dtlsFrgmnt);

            frgmntTrnsctn.commit();
        }
        else {

        // Open a new view
            Intent mIntent = new Intent(this, Detail_Activity.class);
            mIntent.setData(uri);
            startActivity(mIntent);
        }
    }*/
    // ************* old ***********************

}
