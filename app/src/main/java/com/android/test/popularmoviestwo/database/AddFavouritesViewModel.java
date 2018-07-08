package com.android.test.popularmoviestwo.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.android.test.popularmoviestwo.objects.Movie;

public class AddFavouritesViewModel extends ViewModel {

    private static final String TAG = AddFavouritesViewModel.class.getSimpleName();
    private LiveData<Movie> favouriteMovies;

	public AddFavouritesViewModel(@NonNull AppDatabase appDatabase, int id) {
        favouriteMovies = appDatabase.movieDao().getMovieById(id);
    }

    public LiveData<Movie> getFavouriteMovies() {
        return favouriteMovies;
    }
}