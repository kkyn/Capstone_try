package com.example.android.fnlprjct;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by kkyin on 6/6/2017.
 */

public class VPAdapter extends FragmentStatePagerAdapter {

    private static final String LOG_TAG = VPAdapter.class.getSimpleName();

    Cursor cursor;

    public VPAdapter(FragmentManager fMngr) {
        super(fMngr);
    }
    /*public VPAdapter(FragmentManager fMngr, Cursor cursor) {
        super(fMngr);
        this.cursor = cursor;
    }*/

    // Called to inform the adapter of which item is currently considered to be the "primary",
    // that is the one show to the user as the current page.
    // --- Deprecated ---
    /*@Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        MDetails_Fragment fragment = (MDetails_Fragment) object;
    }*/

    //-------------------------------------------------------
    // (1) In this adpater, with the new 'position'.
    // (2) Instantiate and Inflate a page fragment w.r.t. the 'position'.
    @Override
    public Fragment getItem(int position) {


        cursor.moveToPosition(position);
        int specificValue = cursor.getInt(1); // get the movie-Id
        Log.d(LOG_TAG, " ( ( ( ( ( ( ( " + position + " ) ) ) ) ) ) ");
        // Here we call an newinstance which create a new MDetails-Fragment with a given argument.
        // The 'argument-value' comes from the cursor residing/given in/to this ViewPager-Adapter.
        // To display at the ViewPager UI
        MDetails_Fragment1 fgmnt1 = MDetails_Fragment1.newInstance(specificValue);

        return (Fragment) fgmnt1;
        //return null;
    }

    // (1) In this adapter, get the number of 'items'.
    @Override
    public int getCount() {

        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }


    public void swapCursor(Cursor newCursor) {

        cursor = newCursor;

        this.notifyDataSetChanged();
    }
}
