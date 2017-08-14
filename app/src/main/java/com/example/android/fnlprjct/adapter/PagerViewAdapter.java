package com.example.android.fnlprjct.adapter;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.android.fnlprjct.DetailFragmentNew;
import com.example.android.fnlprjct.MyQuery;


public class PagerViewAdapter extends FragmentStatePagerAdapter {

    private static final String LOG_TAG = PagerViewAdapter.class.getSimpleName();

    private Cursor cursor;
    String sortMovieBy;

    public PagerViewAdapter(FragmentManager fMngr, String sortMovieBy) {
        super(fMngr);
        this.sortMovieBy = sortMovieBy;
    }

    // Called to inform the adapter of which item is currently considered to be the "primary",
    // that is the one show to the user as the current page.
    // --- Deprecated ---
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

    }

    //-------------------------------------------------------
    // (1) In this adapter, with the new 'position'.
    // (2) Instantiate and Inflate a page fragment w.r.t. the 'position'.
    // (3) The FragmentManager calls this method, 'I' don't call !!
    @Override
    public Fragment getItem(int position) {

        cursor.moveToPosition(position);

        int movieId = cursor.getInt(MyQuery.MovieInfo.COL_MOVIE_ID);

        // Here we call an newinstance which create a new MDetails-Fragment with a given argument.
        // The 'argument-value' comes from the cursor residing/given in/to this ViewPager-Adapter.
        // To display at the ViewPager UI
        DetailFragmentNew fgmnt = DetailFragmentNew.newInstance(movieId);

        return (Fragment) fgmnt;
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

}
