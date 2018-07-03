package com.android.test.popularmoviestwo.database;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.test.popularmoviestwo.objects.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesViewModel extends AndroidViewModel{

    private static final String TAG = MoviesViewModel.class.getSimpleName();
    private MediatorLiveData<List<Movie>> movies;
    private AppDatabase database;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        movies = new MediatorLiveData<>();

        getAllMovies();
    }

    public void getFavourites(){
		movies.setValue(null);
		movies.addSource(database.movieDao().getOnlyFavourites(), new Observer<List<Movie>>() {
			@Override
			public void onChanged(@Nullable List<Movie> changedMovies) {
				movies.setValue(changedMovies);
			}
		});
    }

    public void getAllMovies(){
		movies.setValue(null);
		movies.addSource(database.movieDao().getAllMovies(), new Observer<List<Movie>>() {
			@Override
			public void onChanged(@Nullable List<Movie> changedMovies) {
				movies.setValue(changedMovies);
			}
		});
    }

    public LiveData<List<Movie>> getMovies(){
        return movies;
    }
}