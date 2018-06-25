package com.android.test.popularmoviestwo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.activities.ActivityDetail;
import com.android.test.popularmoviestwo.objects.Movie;

public class FragmentDetail extends Fragment {

    private Movie mMovie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_details, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get movie passed in from activity
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            mMovie = intent.getParcelableExtra(ActivityDetail.TAG_MOVIE_OBJECT);
        }

        ((TextView) getView().findViewById(R.id.detail_synopsis)).setText(mMovie.getOverview());
        ((TextView) getView().findViewById(R.id.detail_reviews)).setText(String.valueOf(mMovie.getVoteAverage()));
        ((TextView) getView().findViewById(R.id.detail_release_date)).setText(mMovie.getReleaseDate());
        ((TextView) getView().findViewById(R.id.detail_length)).setText(String.valueOf(mMovie.getVoteCount()));

    }
}