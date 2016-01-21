package com.example.android.myproject_2;


import android.content.Intent;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailMovieFragment extends Fragment {

    public DetailMovieFragment() {
        // Required empty public constructor
    }

    ///////////////////////////////////////////////
    @Bind(com.example.android.myproject_2.R.id.movie_title_textView)
    TextView movie_title_textView;

    @Bind(com.example.android.myproject_2.R.id.thumbnail_imageView)
    ImageView thumbnail_imageView;

    @Bind(com.example.android.myproject_2.R.id.movie_release_date_textView)
    TextView movie_release_date_textView;

    @Bind(com.example.android.myproject_2.R.id.movie_ratings)
    TextView movie_ratings;

    @Bind(com.example.android.myproject_2.R.id.movie_synopsis)
    TextView movie_synopsis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_blank, container, false);


        MoviesSelectedInfo aMovieInfo = null;

        /*****
         Intent intent = getActivity().getIntent();
         Bundle bundle = intent.getExtras();
         // -- or --
         // Bundle bundle = getActivity().getIntent().getExtras();

         if (bundle != null) {
         aMovieInfo = bundle.getParcelable("try");
         }
         *****/
        // -- or --
        Intent intent = getActivity().getIntent();
        aMovieInfo = intent.getParcelableExtra("aMovieKey");

        View rootView = inflater.inflate(com.example.android.myproject_2.R.layout.fragment_detailmovie, container, false);

        ButterKnife.bind(this, rootView);

        movie_title_textView.setText(aMovieInfo.mOriginalTitle);

        Picasso.with(getActivity()).load(aMovieInfo.mThumbnail).into(thumbnail_imageView);

        movie_release_date_textView.setText("Release Date: " + aMovieInfo.mReleaseDate);
        movie_ratings.setText("Ratings : " + aMovieInfo.mVoteAverage);
        movie_synopsis.setText("SYNOPSIS : " + aMovieInfo.mOverview);

        return rootView;

    }



}
