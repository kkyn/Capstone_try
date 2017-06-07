package com.example.android.fnlprjct;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by kkyin on 6/6/2017.
 */

public class VPAdapter extends FragmentStatePagerAdapter {
    Cursor cursor;

    public VPAdapter(FragmentManager fMngr, Cursor cursor) {
        super(fMngr);
        this.cursor = cursor;
    }

    // Called to inform the adapter of which item is currently considered to be the "primary",
    // that is the one show to the user as the current page.
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        MDetails_Fragment fragment = (MDetails_Fragment) object;
    }

    // (1) In this adpater, with the new 'position'.
    // (2) Instantiate and Inflate a page fragment w.r.t. the 'position'.
    @Override
    public Fragment getItem(int position) {


        return null;
    }

    // (1) In this adapter, get the number of 'items'.
    @Override
    public int getCount() {
        return 0;
    }
}
