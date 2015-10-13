package com.android.test.popularmoviestwo.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.test.popularmoviestwo.async.AsyncGetMoviePosters;
import com.android.test.popularmoviestwo.async.PojoMovies;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.Utils;
import com.android.test.popularmoviestwo.activities.ActivityDetail;
import com.android.test.popularmoviestwo.adapters.AdapterMovies;

public class FragmentMain extends Fragment implements AsyncGetMoviePosters.IAsyncMovies {

	GridView mGridview;
	AdapterMovies mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		mGridview = (GridView) view.findViewById(R.id.fragment_main_gridview);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getMovies();

		mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(mAdapter != null && mAdapter.getCount() > 0) {
					Intent intent = new Intent(getActivity(), ActivityDetail.class);
					intent.putExtra(ActivityDetail.TAG_MOVIE_OBJECT, mAdapter.getItem(position));

					ActivityOptions options = null;
					// create the transition animation - the images in the layouts of both activities are defined with android:transitionName="MyTransition"
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.movieTransitionName));
					}
					// start the new activity
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && options != null) {
						getActivity().startActivity(intent, options.toBundle());
					}else{
						startActivity(intent);
					}
				}
			}
		});
	}

	/**
	 * Get movies from web. This method is public as it may be refreshed from activity.
	 */
	public void getMovies(){
		AsyncGetMoviePosters async = new AsyncGetMoviePosters(this);
		async.execute(getActivity());
	}

	@Override
	public void onMoviesReceived(PojoMovies movies) {
		if(movies != null) {
			mAdapter = new AdapterMovies(getActivity(), R.layout.item_movie, movies.results);
		}
		mGridview.setEmptyView(getView().findViewById(R.id.fragment_main_no_results));
		mGridview.setAdapter(mAdapter);
	}
}