package com.android.test.popularmoviestwo.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.activities.ActivityDetail;
import com.android.test.popularmoviestwo.activities.ActivityMain;
import com.android.test.popularmoviestwo.adapters.AdapterMovies;
import com.android.test.popularmoviestwo.async.AsyncGetMoviePosters;
import com.android.test.popularmoviestwo.objects.PojoMovies;
import com.android.test.popularmoviestwo.objects.Result;
import com.android.test.popularmoviestwo.database.MoviesContract;
import com.android.test.popularmoviestwo.database.TableHelperFavourites;

import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends Fragment implements AsyncGetMoviePosters.IAsyncMovies, LoaderManager.LoaderCallbacks<Cursor>{

	GridView mGridview;
	AdapterMovies mAdapter;
	SharedPreferences mPrefs;

	private static final int FAVOURITES = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		mGridview = (GridView) view.findViewById(R.id.fragment_main_gridview);
		mGridview.setEmptyView(view.findViewById(R.id.fragment_main_no_results));
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		//Set empty adapter, items added later
		mAdapter = new AdapterMovies(getActivity(), R.layout.item_movie, new ArrayList<Result>());
		mGridview.setAdapter(mAdapter);

		showTiles();

		mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(mAdapter != null && mAdapter.getCount() > 0) {
					Intent intent = new Intent(getActivity(), ActivityDetail.class);
					intent.putExtra(ActivityDetail.TAG_MOVIE_OBJECT, mAdapter.getItem(position));
					intent.putExtra(ActivityDetail.TAG_IS_ON_FAVOURITES, isShowFavourites());

					ActivityOptions options = null;
					// create the transition animation - the images in the layouts of both activities are defined with android:transitionName="MyTransition"
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.movieTransitionName));
					}
					// start the new activity
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && options != null) {
						getActivity().startActivityForResult(intent, ActivityMain.REQUEST_CODE_DETAIL, options.toBundle());
					}else{
						getActivity().startActivityForResult(intent, ActivityMain.REQUEST_CODE_DETAIL);
					}
				}
			}
		});
	}

	/**
	 * Called from activity as well, in response to settings or detail page closing and refresh required.
	 */
	public void showTiles(){
		if(isShowFavourites()){
			getFavourites();
		}else{
			getMovies();
		}
	}

	/**
	 * Get movies from web.
	 */
	private void getMovies(){
		AsyncGetMoviePosters async = new AsyncGetMoviePosters(this);
		async.execute(getActivity());
	}

	/**
	 * Get favourites from local db
	 */
	private void getFavourites(){
		getLoaderManager().restartLoader(FAVOURITES, null, this);
	}

	@Override
	public void onMoviesReceived(PojoMovies movies) {
		if(movies != null) {
			Log.d(FragmentMain.class.getSimpleName(), "movies:" + movies.results.size());
			mAdapter.updateItems(movies.results);
		}
	}

	private boolean isShowFavourites(){
		return mPrefs.getBoolean(getActivity().getString(R.string.pref_favs_key), false);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		CursorLoader retVal = null;

		switch (id) {
			case FAVOURITES: {
				retVal = new CursorLoader(getActivity(), MoviesContract.URI_FAVOURITES_GET_ALL, TableHelperFavourites.AVAILABLE_COLUMNS, null, null, null);
				break;
			}
		}

		return retVal;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(FragmentMain.class.getSimpleName(), "onLoadFinished");
		List<Result> items = new ArrayList<>();
		Result fav;
		if (getView() != null && isShowFavourites()) {
			Log.d(FragmentMain.class.getSimpleName(), "1");
			int id = loader.getId();
			switch (id) {
				case FAVOURITES: {
					Log.d(FragmentMain.class.getSimpleName(), "2");
					if(data != null && data.moveToFirst()) {
						Log.d(FragmentMain.class.getSimpleName(), "3");
						while (!data.isAfterLast()) {
							fav = new Result(
									data.getInt(data.getColumnIndex(TableHelperFavourites.COL_MOVIE_ID)),
									data.getString(data.getColumnIndex(TableHelperFavourites.COL_MOVIE_TITLE)),
									data.getString(data.getColumnIndex(TableHelperFavourites.COL_MOVIE_RELEASE_DATE)),
									data.getFloat(data.getColumnIndex(TableHelperFavourites.COL_MOVIE_AVG_VOTE)),
									data.getString(data.getColumnIndex(TableHelperFavourites.COL_MOVIE_OVERVIEW)),
									data.getString(data.getColumnIndex(TableHelperFavourites.COL_MOVIE_IMG_PATH)));
							items.add(fav);
							data.moveToNext();
						}
					}
					Log.d(FragmentMain.class.getSimpleName(), "items size:" + items.size());
					mAdapter.updateItems(items);
				}
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		//Not used
	}
}