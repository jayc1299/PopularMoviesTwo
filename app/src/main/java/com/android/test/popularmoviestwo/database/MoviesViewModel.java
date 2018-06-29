package com.android.test.popularmoviestwo.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.test.popularmoviestwo.objects.Movie;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel{

    private static final String TAG = MoviesViewModel.class.getSimpleName();
    private LiveData<List<Movie>> movies;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Getting Movies");
        movies = database.movieDao().getAllMovies();
    }

    public LiveData<List<Movie>> getMovies(){
        return movies;
    }
}