package com.android.test.popularmoviestwo.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.test.popularmoviestwo.objects.Movie;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private static final String TAG = MoviesViewModel.class.getSimpleName();
    private MutableLiveData<List<Movie>> movies;
    private List<Movie> allMovies;
    private AppDatabase database;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        movies = new MutableLiveData<>();
    }

    public void saveFullMovieList(List<Movie> newMovies) {
        Log.d(TAG, "saveFullMovieList: " + newMovies.size());
        allMovies = newMovies;
        movies.postValue(newMovies);
    }

    public void getFavourites() {
        movies.postValue(database.movieDao().getOnlyFavourites().getValue());
    }

    public void getAllMovies() {
        movies.setValue(allMovies);
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}