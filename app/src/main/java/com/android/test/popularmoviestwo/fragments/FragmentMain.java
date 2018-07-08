package com.android.test.popularmoviestwo.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.adapters.AdapterMovies;
import com.android.test.popularmoviestwo.async.AsyncGetMovies;
import com.android.test.popularmoviestwo.database.MoviesViewModel;
import com.android.test.popularmoviestwo.objects.Movie;
import com.android.test.popularmoviestwo.objects.PojoMovies;

import java.util.List;

public class FragmentMain extends Fragment {

    private static final String TAG = FragmentMain.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";

    private RecyclerView mGridview;
    private TextView noResults;
    private SharedPreferences mPrefs;
    private IFragmentMainListener mCallback;
    private int mPosition = ListView.INVALID_POSITION;
    private MoviesViewModel viewModel;


    public interface IFragmentMainListener {
        void onMovieClicked(Movie movie);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridview = view.findViewById(R.id.fragment_main_gridview);
        noResults = view.findViewById(R.id.fragment_main_no_results);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Init ViewModel
        viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        showDesiredMovieList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Set empty adapter, items added later
        mGridview = getView().findViewById(R.id.fragment_main_gridview);
        mGridview.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
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

    public void setListener(IFragmentMainListener listener) {
        mCallback = listener;
    }

    public void showDesiredMovieList(){
        if (isShowFavourites()) {
			viewModel.getMovies().removeObserver(movieObserver);
			viewModel.getFavourites().observe(this, favouritesObserver);
        } else {
			viewModel.getFavourites().removeObserver(favouritesObserver);
            getMovies();
			viewModel.getMovies().observe(this, movieObserver);
        }
    }

    Observer<List<Movie>> movieObserver = new Observer<List<Movie>>() {
		@Override
		public void onChanged(@Nullable List<Movie> movies) {
			showMovies(movies);
		}
	};

	Observer<List<Movie>> favouritesObserver = new Observer<List<Movie>>() {
		@Override
		public void onChanged(@Nullable List<Movie> movies) {
			showMovies(movies);
		}
	};

    /**
     * Get movies from web.
     */
    private void getMovies() {
        Log.d(TAG, "getMovies: ");
        AsyncGetMovies async = new AsyncGetMovies(moviesListener);
        async.execute(getActivity());
    }

    AsyncGetMovies.IAsyncMovies moviesListener = new AsyncGetMovies.IAsyncMovies() {
        @Override
        public void onMoviesReceived(PojoMovies movies) {
            if (getActivity() != null && !getActivity().isFinishing() && movies != null && movies.getMovies() != null) {
                viewModel.saveFullMovieList(movies.getMovies());
            }
        }
    };

    private void showMovies(List<Movie> movies) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            if (movies != null && movies.size() > 0) {
                Log.d(TAG, "showMovies: " + movies.size());

                AdapterMovies adapter = new AdapterMovies(getActivity(), movieClickedListener, movies);
                mGridview.setAdapter(adapter);
                noResults.setVisibility(View.GONE);
                mGridview.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "No Movies found");
                if(isShowFavourites()){
                    noResults.setText(getString(R.string.no_favourites_found));
                }else{
                    noResults.setText(getString(R.string.no_movies_found));
                }
                noResults.setVisibility(View.VISIBLE);
                mGridview.setVisibility(View.GONE);
            }
        }
    }

    private AdapterMovies.IMovieClickedListener movieClickedListener = new AdapterMovies.IMovieClickedListener() {
        @Override
        public void onMovieClickedListener(Movie movie, View view) {
            mCallback.onMovieClicked(movie);
        }
    };

    private boolean isShowFavourites() {
        return mPrefs.getBoolean(getActivity().getString(R.string.pref_favs_key), false);
    }
}