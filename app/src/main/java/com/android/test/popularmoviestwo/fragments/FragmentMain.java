package com.android.test.popularmoviestwo.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.adapters.AdapterMovies;
import com.android.test.popularmoviestwo.async.AsyncGetMovies;
import com.android.test.popularmoviestwo.database.AppDatabase;
import com.android.test.popularmoviestwo.objects.Movie;
import com.android.test.popularmoviestwo.objects.PojoMovies;

import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends Fragment implements AsyncGetMovies.IAsyncMovies {

    private static final String TAG = FragmentMain.class.getSimpleName();
    public static final String TAG_TABLET_MODE = "tag_tablet_mode";
    private static final String SELECTED_KEY = "selected_position";

    private GridView mGridview;
    private AdapterMovies mAdapter;
    private SharedPreferences mPrefs;
    private boolean mTabletMode = false;
    private IFragmentMainListener mCallback;
    private int mPosition = ListView.INVALID_POSITION;

    public interface IFragmentMainListener {
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

        if (getArguments() != null) {
            mTabletMode = getArguments().getBoolean(TAG_TABLET_MODE, false);
        }

        //Set empty adapter, items added later
        mAdapter = new AdapterMovies(getActivity(), R.layout.item_movie, new ArrayList<Movie>());
        mGridview.setAdapter(mAdapter);

        showTiles();

        if (mTabletMode) {
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
                mPosition = position;
                if (mAdapter != null && mAdapter.getCount() > 0) {
                    mCallback.onMovieClicked(mAdapter.getItem(position));
                }
            }
        });


        AppDatabase mDb = AppDatabase.getInstance(getActivity());
        LiveData<List<Movie>> movies = mDb.movieDao().getAllMovies();

        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Movies recicved:" + movies.size());
                onMoviesReceived(movies);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (IFragmentMainListener) context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
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
    public void showTiles() {
        getMovies();
    }

    /**
     * Get movies from web.
     */
    private void getMovies() {
        AsyncGetMovies async = new AsyncGetMovies(this);
        async.execute(getActivity());
    }

    @Override
    public void onMoviesReceived(List<Movie> movies) {
        if (movies != null) {
            Log.d(FragmentMain.class.getSimpleName(), "movies:" + movies.size());
            mAdapter.updateItems(movies);
            if (mPosition != ListView.INVALID_POSITION) {
                // If we don't need to restart the loader, and there's a desired position to restore to, do so now.
                mGridview.setSelection(mPosition);
            }
        }
    }

    private boolean isShowFavourites() {
        return mPrefs.getBoolean(getActivity().getString(R.string.pref_favs_key), false);
    }
}