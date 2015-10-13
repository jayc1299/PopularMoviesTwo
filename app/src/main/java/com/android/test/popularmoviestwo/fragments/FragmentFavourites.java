package com.android.test.popularmoviestwo.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.adapters.AdapterFavourites;
import com.android.test.popularmoviestwo.database.MoviesContract;
import com.android.test.popularmoviestwo.database.TableHelperFavourites;
import com.android.test.popularmoviestwo.objects.Favourite;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavourites extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

	ListView mListView;
	AdapterFavourites mAdapterFavs;

	private static final int FAVOURITES = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_favourites, container, false);

		mListView = (ListView) view.findViewById(R.id.fragment_favourites_listview);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().restartLoader(FAVOURITES, null, this);
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
		List<Favourite> items = new ArrayList<>();
		String name;
		int movieId;
		Favourite fav;
		if (getView() != null) {
			int id = loader.getId();
			switch (id) {
				case FAVOURITES: {
					if(data != null && data.moveToFirst()) {
						while (data.isAfterLast() == false) {
							movieId = data.getInt(data.getColumnIndex(TableHelperFavourites.COL_MOVIE_ID));
							name = data.getString(data.getColumnIndex(TableHelperFavourites.COL_MOVIE_TITLE));
							fav = new Favourite(movieId, name);
							items.add(fav);
							data.moveToNext();
						}
					}
				}
			}
		}

		mAdapterFavs = new AdapterFavourites(getActivity(), R.layout.item_favourite, items);
		mListView.setAdapter(mAdapterFavs);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}