package com.example.android.myproject_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A placeholder fragment (MoviesFragment) containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private MoviesArrayAdapter moviesArrayAdapter;
    public ArrayList<MoviesSelectedInfo> arrayListOfMovieInfo;
    private GridView mGridView;
    private FetchMoviesDbAsyncTask myAsyncTask;

    private final String VOTE_AVERAGE_DESC = "vote_average.desc"; // "vote_count.desc"; "vote_average.desc";
    private final String POPULARITY_DESC = "popularity.desc";

    public MoviesFragment() {
    }

    ///////////////////////////////
    public class FetchComplete implements FetchMoviesDbAsyncTask.OnAsyncTaskCompletedListener {

        @Override
        public void onAsyncTaskCompleted(MoviesSelectedInfo[] returnedResult) {
            if (returnedResult != null) {
                moviesArrayAdapter.clear();
                moviesArrayAdapter.addAll(returnedResult);
                moviesArrayAdapter.notifyDataSetChanged();
            }
        }
    }

    ///////////////////////////////
    private void moviesDbList_Update() {
        // get the file, SharedPreferences
        // Gets a SharedPreferences instance that points to the default file
        // that is used by the preference framework in the given context.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Retrieve a String value from the preferences.
        // getString(String key, String defValue)
        String sortMoviesBy = prefs.getString(getString(R.string.pref_sortmovies_by_key), getString(R.string.pref_sortmovies_by_popularity));

        //    Toast.makeText(getActivity(),sortMoviesBy, Toast.LENGTH_LONG).show();

        FetchMoviesDbAsyncTask task = new FetchMoviesDbAsyncTask(getActivity(), new FetchComplete());
        task.execute(sortMoviesBy);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        /*
        if (id == R.id.highest_rated) {

            FetchMoviesAsyncTask myAsyncTask = new FetchMoviesAsyncTask();
            myAsyncTask.execute(VOTE_AVERAGE_DESC);

            //   Toast.makeText(getActivity(), "---a aaaa a---", Toast.LENGTH_LONG).show();
            return true;
        } else
        */
        if (id == R.id.most_popular) {

            FetchMoviesDbAsyncTask task = new FetchMoviesDbAsyncTask(getActivity(), new FetchComplete());
            task.execute(POPULARITY_DESC);

            //   Toast.makeText(getActivity(), "---b bbbb b---", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onStart() {
        super.onStart();
        moviesDbList_Update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        arrayListOfMovieInfo = new ArrayList<MoviesSelectedInfo>();
        moviesArrayAdapter = new MoviesArrayAdapter(getActivity(), arrayListOfMovieInfo);

        mGridView = (GridView) rootView.findViewById(R.id.grid_view);
        mGridView.setAdapter(moviesArrayAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("aMovieKey", arrayListOfMovieInfo.get(position));

                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });

        return rootView;
    }

}
