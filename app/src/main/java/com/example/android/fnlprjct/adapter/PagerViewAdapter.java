package com.example.android.fnlprjct.adapter;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.example.android.fnlprjct.Detail_Fragment1;
import com.example.android.fnlprjct.MyQuery;

/**
 * Created by kkyin on 6/6/2017.
 */

public class PagerViewAdapter extends FragmentStatePagerAdapter {

    private static final String LOG_TAG = PagerViewAdapter.class.getSimpleName();

    private Cursor cursor;
   // private Detail_Fragment fgmnt1;
    /*private*/
   String sortMovieBy;

    public PagerViewAdapter(FragmentManager fMngr, String sortMovieBy) {
        super(fMngr);
        this.sortMovieBy = sortMovieBy;
    }
    /*public PagerViewAdapter(FragmentManager fMngr, Cursor cursor) {
        super(fMngr);
        this.cursor = cursor;
    }*/

    // Called to inform the adapter of which item is currently considered to be the "primary",
    // that is the one show to the user as the current page.
    // --- Deprecated --- ???
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        Log.d(LOG_TAG, "  - - - - - - setPrimaryItem /position : " + position + " + + + + + +");

        /*Detail_Fragment*///fgmnt1 = (Detail_Fragment) object;

    }

    //-------------------------------------------------------
    // (1) In this adapter, with the new 'position'.
    // (2) Instantiate and Inflate a page fragment w.r.t. the 'position'.
    // (3) The FragmentManager calls this method, 'I' don't call !!
    @Override
    public Fragment getItem(int position) {

        Log.d(LOG_TAG, "  - - - - - - getItem /position : " + position + " + + + + + +");

        cursor.moveToPosition(position);

        int movieId = cursor.getInt(MyQuery.MovieInfo.COL_MOVIE_ID);

        Log.d(LOG_TAG, "  - - - - - - " + position + " + + + + + +");

        // Here we call an newinstance which create a new MDetails-Fragment with a given argument.
        // The 'argument-value' comes from the cursor residing/given in/to this ViewPager-Adapter.
        // To display at the ViewPager UI
        Detail_Fragment1 fgmnt1 = Detail_Fragment1.newInstance(movieId);

        return (Fragment) fgmnt1;
    }

    // (1) In this adapter, get the number of 'items'.
    @Override
    public int getCount() {

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }


    public void swapCursor(Cursor newCursor) {

        cursor = newCursor;

        this.notifyDataSetChanged();
    }

    //-----------------------------------------------
    // tky copied
    // Force a refresh of the page when a different fragment is displayed.
    /*@Override
    public int getItemPosition(Object object) {
        //return super.getItemPosition(object);
        // this method will be called for every fragment in the ViewPager
        if (object instanceof Detail_Fragment1){
            return POSITION_UNCHANGED;  // don't force a reload
        } else {
            // POSITION_NONE means something like: this fragment is no longer valid
            // triggering the ViewPager to re-build the instance of this fragment.
            return POSITION_NONE;
        }
    }*/
    //-----------------------------------------------

}
