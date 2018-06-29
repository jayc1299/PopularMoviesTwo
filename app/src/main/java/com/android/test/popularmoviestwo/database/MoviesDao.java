package com.android.test.popularmoviestwo.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.android.test.popularmoviestwo.objects.Movie;

import java.util.List;

@Dao
public interface MoviesDao {

    String TABLE_NAME = "movie";

    @Query("select * from " + TABLE_NAME)
    LiveData<List<Movie>> getAllMovies();

    @Query("select * from " + TABLE_NAME + " where isFavourite = 1")
    LiveData<List<Movie>> getOnlyFavourites();

    @Insert
    void insertMovie(Movie movie);

    @Update
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("delete from " + TABLE_NAME)
    public void deleteAllMovies();
}