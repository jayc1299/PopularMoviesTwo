package com.android.test.popularmoviestwo.fragments;

import android.app.ActivityOptions;
import android.content.Context;
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
import android.widget.ListView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.activities.ActivityDetail;
import com.android.test.popularmoviestwo.activities.ActivityMain;
import com.android.test.popularmoviestwo.adapters.AdapterMovies;
import com.android.test.popularmoviestwo.async.AsyncGetMoviePosters;
import com.android.test.popularmoviestwo.database.MoviesContract;
import com.android.test.popularmoviestwo.database.TableHelperFavourites;
import com.android.test.popularmoviestwo.objects.Movie;
import com.android.test.popularmoviestwo.objects.PojoMovies;

import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends Fragment implements AsyncGetMoviePosters.IAsyncMovies, LoaderManager.LoaderCallbacks<Cursor>{

	public static String TAG_TABLET_MODE = "tag_tablet_mode";

	GridView mGridview;
	AdapterMovies mAdapter;
	SharedPreferences mPrefs;
	boolean mTabletMode = false;
	IFragmentMainListener mCallback;
	private int mPosition = ListView.INVALID_POSITION;

	private static final int FAVOURITES = 1;
	private static final String SELECTED_KEY = "selected_position";

	public interface IFragmentMainListener{
		void onMovieClicked(Movie movie);
	}

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

		if(getArguments() != null){
			mTabletMode = getArguments().getBoolean(TAG_TABLET_MODE, false);
		}

		//Set empty adapter, items added later
		mAdapter = new AdapterMovies(getActivity(), R.layout.item_movie, new ArrayList<Movie>());
		mGridview.setAdapter(mAdapter);

		showTiles();

		if(mTabletMode){
			mGridview.setNumColumns(1);
		}

		if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
			// The listview probably hasn't even been populated yet.  Actually perform the
			// swapout in onLoadFinished.
			mPosition = savedInstanceState.getInt(SELECTED_KEY);
		}


		mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i("hello", "position" + position);
				mPosition = position;
				if (mAdapter != null && mAdapter.getCount() > 0) {
					if (mTabletMode) {
						//In tablet mode send message back to activity, so it can load the details in a pane.
						mCallback.onMovieClicked(mAdapter.getItem(position));
					} else {
						//Not in tablet mode, open a new activity
						Intent intent = new Intent(getActivity(), ActivityDetail.class);
						intent.putExtra(ActivityDetail.TAG_MOVIE_OBJECT, mAdapter.getItem(position));
						intent.putExtra(ActivityDetail.TAG_IS_ON_FAVOURITES, isShowFavourites());

						ActivityOptions options = null;
						// create the transition animation - the images in the layouts of both activities are defined with android:transitionName="movieTransition"
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.movieTransitionName));
						}
						// start the new activity
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && options != null) {
							getActivity().startActivityForResult(intent, ActivityMain.REQUEST_CODE_DETAIL, options.toBundle());
						} else {
							getActivity().startActivityForResult(intent, ActivityMain.REQUEST_CODE_DETAIL);
						}
					}
				}
			}
		});
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mCallback = (IFragmentMainListener) context;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// When tablets rotate, the currently selected list item needs to be saved.
		// When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
		// so check for that before storing.
		if (mPosition != ListView.INVALID_POSITION) {
			outState.putInt(SELECTED_KEY, mPosition);
		}
		super.onSaveInstanceState(outState);
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
			Log.d(FragmentMain.class.getSimpleName(), "movies:" + movies.movies.size());
			mAdapter.updateItems(movies.movies);
			if (mPosition != ListView.INVALID_POSITION) {
				// If we don't need to restart the loader, and there's a desired position to restore
				// to, do so now.
				Log.i("hello", "jump too " + mPosition);
				mGridview.setSelection(mPosition);
			}
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
		List<Movie> items = new ArrayList<>();
		Movie fav;
		if (getView() != null && isShowFavourites()) {
			int id = loader.getId();
			switch (id) {
				case FAVOURITES: {
					if(data != null && data.moveToFirst()) {
						while (!data.isAfterLast()) {
							fav = new Movie(
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