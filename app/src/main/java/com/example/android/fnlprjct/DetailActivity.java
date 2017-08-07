package com.example.android.fnlprjct;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tky add, copied
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        setContentView(com.example.android.fnlprjct.R.layout.activity_details);

        // tky add, -----------------------
        postponeEnterTransition();

        // ?? bundle from MainActivity is passed on to the Detail_Fragment ?? how/why ??, see code below !!
        // (1) Get the 'intent' with the start-of-this-activity.
        // (2) Get the data attached with the 'intent'
        Intent intent = this.getIntent();
        mUri = intent.getData();

        /*Log.d(LOG_TAG, "yyyy DetailActivity / onCreate / mUri : " + mUri.toString());*/
        // Log.d(LOG_TAG, "yyyy onCreate / savedInstanceState == null / DetailMoviewFragment --");
        // Log.d(LOG_TAG, "yyyy onCreate / mUri : " + mUri.toString());

        //-- Begin-- Attach data to a fragment
        // (1) Place 'data' into bundle
        Bundle bundle = new Bundle();
        bundle.putParcelable(DetailFragmentNew.DETAIL_URI, mUri);

        /*Log.d(LOG_TAG, "( ( ( ( ( ( ( " + mUri.toString()+ " ) ) ) ) ) ) ) ");*/

        if (savedInstanceState == null) {
            // (2) Attach the bundle to a fragment
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            DetailFragmentPv pagerViewDetailsFragment = new DetailFragmentPv();

            pagerViewDetailsFragment.setArguments(bundle);
            //-- End  -- Attach data to a fragment

            FragmentManager frgmntMngr = getSupportFragmentManager();
            FragmentTransaction frgmntTrnsctn = frgmntMngr.beginTransaction();

            frgmntTrnsctn.add(R.id.fragmentdetail_container, pagerViewDetailsFragment);
            frgmntTrnsctn.commit();

            /*
            Detail_Fragment dtlFrgmnt = new Detail_Fragment();
            dtlFrgmnt.setArguments(bundle);
            FragmentManager fMngr = getSupportFragmentManager();
            FragmentTransaction frgmntTrnsctn = fMngr.beginTransaction();
            frgmntTrnsctn.add(R.id.pane2_container, dtlFrgmnt);
            frgmntTrnsctn.commit();
            // --or--
            // getSupportFragmentManager().beginTransaction()
            //                             .add(R.id.detail_movie_container, new Detail_Fragment())
            //                             .commit();
            */
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*Toast.makeText(this, "+ + + + + DetailActivity/onSaveInstanceState ", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}
