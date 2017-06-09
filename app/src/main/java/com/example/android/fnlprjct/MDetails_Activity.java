package com.example.android.fnlprjct;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

public class MDetails_Activity extends AppCompatActivity {

    private static final String LOG_TAG = MDetails_Activity.class.getSimpleName();

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.android.fnlprjct.R.layout.activity_details);

        // ?? bundle from Main_Activity is passed on to the MDetails_Fragment ?? how/why ??, see code below !!
        // (1) Get the 'intent' with the start-of-this-activity.
        // (2) Get the data attached with the 'intent'
        Intent intent = this.getIntent();
        mUri = intent.getData();

        Log.d(LOG_TAG, "yyyy MDetails_Activity / onCreate / mUri : " + mUri.toString());

        // Log.d(LOG_TAG, "yyyy onCreate / savedInstanceState == null / DetailMoviewFragment --");
        // Log.d(LOG_TAG, "yyyy onCreate / mUri : " + mUri.toString());

        //-- Begin-- Attach data to a fragment
        // (1) Place 'data' into bundle
        Bundle bundle = new Bundle();
        bundle.putParcelable(MDetails_Fragment.DETAIL_URI, mUri);

        Log.d(LOG_TAG, "( ( ( ( ( ( ( " + mUri.toString()+ " ) ) ) ) ) ) ) ");

        if (savedInstanceState == null) {
            // (2) Attach the bundle to a fragment
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            NewFragment newFragment = new NewFragment();
            newFragment.setArguments(bundle);
            //-- End  -- Attach data to a fragment

            FragmentManager fMngr = getSupportFragmentManager();
            FragmentTransaction fTrnsctn = fMngr.beginTransaction();

            fTrnsctn.add(R.id.pane2_container, newFragment);
        /*
        MDetails_Fragment dtlFrgmnt = new MDetails_Fragment();
        dtlFrgmnt.setArguments(bundle);
        FragmentManager fMngr = getSupportFragmentManager();
        FragmentTransaction fTrnsctn = fMngr.beginTransaction();
        fTrnsctn.add(R.id.pane2_container, dtlFrgmnt);
         */

            fTrnsctn.commit();
            // --or--
            // getSupportFragmentManager().beginTransaction()
            //                             .add(R.id.detail_movie_container, new MDetails_Fragment())
            //                             .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("outState", 8);
        Toast.makeText(this, "+ + + + + Detail_Activity/onSaveInstanceState ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.android.fnlprjct.R.menu.menu_detailmovie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.example.android.fnlprjct.R.id.action_settings) {

            startActivity(new Intent(this, SettingsPreferenceActivity.class));

            return true;

        } else if (id == android.R.id.home) {

            this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//# # # # # # # # # # # # # # # # # # # # # # # # # # #
//# # # # # # # # # # # # # # # # # # # # # # # # # # #
    //-------------------------------------------------//
    ///----- BEGIN, FRAGMENT-STATUS-PAGER-ADAPTER ----///
    //-------------------------------------------------//
    /**
     * A pager-adapter that represents ArticleDetailFragment objects.
     */
    // Creates a class that extends the FragmentStatePagerAdapter abstract class
//    private class MyFrgmntPgrAdptr extends FragmentStatePagerAdapter {
//
//        public MyFrgmntPgrAdptr(FragmentManager fm) {
//            super(fm);
//        }
//
//        // Called to inform the adapter of which item is currently considered to be the "primary",
//        // that is the one show to the user as the current page.
//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            super.setPrimaryItem(container, position, object);
//
//            ArticleDetailFragment fragment = (ArticleDetailFragment) object;
//
//            if (fragment != null) {
//
//            }
//        }
//
//        // Implement the getItem() method to supply instances of ArticleDetailFragment as new pages.
//        @Override
//        public Fragment getItem(int position) {
//
//            mCursor.moveToPosition(position);
//
//            long itemID = mCursor.getLong(ArticleLoader.Query._ID);
//
//            ArticleDetailFragment dtlFragment = ArticleDetailFragment.newInstance(itemID);
//
//            return (Fragment) dtlFragment;
//        }
//
//        // The pager adapter also requires that you implement the getCount() method,
//        // which returns the amount of pages the adapter will create.
//        @Override
//        public int getCount() {
//
//            if(mCursor!=null) {
//                int intCount = mCursor.getCount();
//                return intCount;
//            }
//            else return 0;
//        }
//    }
    //-------------------------------------------------//
    ///----- END, FRAGMENT-STATUS-PAGER-ADAPTER ------///
    //-------------------------------------------------//

//# # # # # # # # # # # # # # # # # # # # # # # # # # #

}
