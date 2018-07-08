package com.android.test.popularmoviestwo.database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class AddFavouriteViewModelFactory extends ViewModelProvider.NewInstanceFactory{

	private final AppDatabase appDatabase;
	private final int movieId;

	public AddFavouriteViewModelFactory(AppDatabase appDatabase, int movieId) {
		this.appDatabase = appDatabase;
		this.movieId = movieId;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		//noinspection unchecked
		return (T) new AddFavouritesViewModel(appDatabase, movieId);
	}
}