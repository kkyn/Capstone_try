package com.example.android.fnlprjct;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.fnlprjct.adapter.PagerViewAdapter;
import com.example.android.fnlprjct.data.MovieContract;
import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kkyin on 7/6/2017.
 */

public class Detail_FragmentPv extends Fragment
                    implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String LOG_TAG = Detail_FragmentPv.class.getSimpleName();
    private static final int LOADER_ID = 4;

    private Cursor cursor;
//--    private ViewPager pager;
    private PagerViewAdapter pagerAdapter;

    int currentPage;
    int movieId;
    Uri mUri;

    // Firebase-Analytics, ----- (step1) Declare a variable
    private FirebaseAnalytics mFirebaseAnalytics;

//--    @BindView(R.id.viewpager) ViewPager pagerView;
    @BindView(R.id.viewpager) ViewPager pager;

    public Detail_FragmentPv(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String sortMoviesBy = Utility.getPreferredSortSequence(getContext());
        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onCreate  ) ) ) ) ) ) ");
        Log.d(LOG_TAG, " ( ( ( ( ( ( (  Before getLoaderManager().initLoader  ) ) ) ) ) ) ");

        this.getLoaderManager().initLoader(LOADER_ID, null, this);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  After getLoaderManager().initLoader  ) ) ) ) ) ) ");

        FragmentManager fMngr = getFragmentManager(); // when called in Fragment

        pagerAdapter = new PagerViewAdapter(fMngr, sortMoviesBy);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onCreate  ) ) ) ) ) ) ");

        // Firebase-Analytics ---- (step2) Obtain a FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);


        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            mUri = mBundle.getParcelable(Detail_Fragment1.DETAIL_URI);
        //    Toast.makeText(getContext(), "uri : " + mUri.toString(), Toast.LENGTH_LONG).show();
            movieId = Integer.parseInt(MovieContract.MovieInfoEntry.getMovieId_FromMovieInfoUri(mUri));
        }

        View rootView = inflater.inflate(R.layout.page_view, container, false);

        ButterKnife.bind(this, rootView);
//--        pager = (ViewPager) rootView.findViewById(R.id.viewpager);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onCreateView  ) ) ) ) ) ) ");

        //pager.setOnPageChangeListener(); // deprecated ???

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            // Called when swipe for a new page.
            @Override
            public void onPageSelected(int position) {
                Log.d(LOG_TAG, " ( ( ( ( ( position : " + position + " ) ) ) ) ) )");
                //Toast.makeText(getActivity()," ( ( ( ( ( position : " + position + " ) ) ) ) ) )", Toast.LENGTH_SHORT).show();

                currentPage = position;

                //pagerAdapter.notifyDataSetChanged();

                //???????????????????????????????
                // Firebase-Analytics ---- (step3) generate FirebaseAnalytics.logEvent
                // Begin, movie-selection event
                cursor.moveToPosition(currentPage);

                // [START image_view_event]
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, cursor.getString(MyQuery.Popularity.COL_MOVIE_ID));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, cursor.getString(MyQuery.Popularity.COL_ORIGINAL_TITLE));
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "selection_vp");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                // [END image_view_event]

                // End, movie-selection event
                //???????????????????????????????
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //pager.setCurrentItem(currentPage);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onViewCreated  ) ) ) ) ) ) ");
    }

    // ????
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("outState", 8);
    }

    int myOutState;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //---------------------------------------
        if (savedInstanceState != null) {

      //--      Toast.makeText(getContext(), "+ + + + + savedInstanceState NOT NULL ", Toast.LENGTH_SHORT).show();
            // Restore last state for checked position.
            //mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
        else {
      //--      Toast.makeText(getContext(), "+ + + + + savedInstanceState     NULL ", Toast.LENGTH_SHORT).show();

        }

        /*if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getData() != null) {
                mStartId = ItemsContract.Items.getItemId(getIntent().getData());

            }
        }*/
        //--------------------------------------

        pager.setAdapter(pagerAdapter);

        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onActivityCreated  ) ) ) ) ) ) ");
    }

/* ??? ???
 @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
 */
    //--- Callback methods for "LoaderManager.LoaderCallbacks<Cursor>" ---
    //--------------------------------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortMoviesBy = Utility.getPreferredSortSequence(getContext());
        String searchYear = Utility.getPreferredYear(getContext());

        Uri uri = MovieInfoEntry.CONTENT_URI;

        String[] projection;
        String selection;
        String[] selectionArg;
        String sortOrder;

        if (sortMoviesBy.equals(getString(R.string.pref_value_movies_sortby_default))) {

            projection = MyQuery.Popularity.PROJECTION;
            selection = MovieInfoEntry.COL_YEAR + "=?";                                       //
            selectionArg = new String[]{searchYear};
            sortOrder = MovieInfoEntry.COL_VOTE_COUNT + " DESC"; // ???? just change July12 2017
            //sortOrder = MovieInfoEntry.COL_POPULARITY + " DESC";

        } else if (sortMoviesBy.equals(getString(R.string.pref_value_movies_sortby_ratings))) {

            projection = MyQuery.VoteAverage.PROJECTION;
            selection = MovieInfoEntry.COL_YEAR + "=?";
            selectionArg = new String[]{searchYear};
            sortOrder = MovieInfoEntry.COL_VOTE_AVERAGE + " DESC";

        } else {  // sortMoviesBy.equals(getString(R.string.pref_movies_sortby_favourites))

            projection = MyQuery.Favourites.PROJECTION;
            selection = MovieInfoEntry.COL_FAVOURITES + "=?";
            selectionArg = new String[]{"1"};
            sortOrder = MovieInfoEntry.COL_VOTE_AVERAGE + " DESC";
        }

        CursorLoader cursorLoader =
            new CursorLoader(   getActivity(),           // context
                                uri,                     // uri
                                projection,              // projection
                                selection,               // selection
                                selectionArg,            // selectionArg
                                sortOrder                // sortOrder
                            );
        Log.d(LOG_TAG, " ( ( ( ( ( ( (  onCreateLoader  ) ) ) ) ) ) ");

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

//        pagerAdapter.swapCursor(data);

        cursor = data;
        pagerAdapter.swapCursor(cursor);

        if (movieId > 0) {
            cursor.moveToFirst();
            // TODO: optimize
            while (!cursor.isAfterLast()) {
                if (cursor.getInt(MyQuery.MovieInfo.COL_MOVIE_ID) == movieId) {
                    final int position = cursor.getPosition();
                    pager.setCurrentItem(position, false);
                    break;
                }
                cursor.moveToNext();
            }
            movieId = 0;
        }
        //pagerAdapter.notifyDataSetChanged();
        //Log.d(LOG_TAG, " ( ( ( ( ( ( (  onLoadFinished  ) ) ) ) ) ) "  + cursor.getCount());

        //pagerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        pagerAdapter.swapCursor(null);

    }
    //---------- End -----------------------------------------------------
}
