package com.android.test.popularmoviestwo.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.database.AddFavouriteViewModelFactory;
import com.android.test.popularmoviestwo.database.AddFavouritesViewModel;
import com.android.test.popularmoviestwo.database.AppDatabase;
import com.android.test.popularmoviestwo.fragments.FragmentDetail;
import com.android.test.popularmoviestwo.fragments.FragmentListContent;
import com.android.test.popularmoviestwo.objects.Movie;
import com.squareup.picasso.Picasso;


public class ActivityDetail extends AppCompatActivity {

	private static final String TAG = ActivityDetail.class.getSimpleName();
    public static String TAG_MOVIE_OBJECT = "tag_movie_obect";
	private AddFavouritesViewModel viewModel;
	private FloatingActionButton fabButton;
	private AppDatabase database;
	private Movie mMovie;
	private boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //movie details
        MovieApi api = new MovieApi(this);
        mMovie = getIntent().getParcelableExtra(ActivityDetail.TAG_MOVIE_OBJECT);

        fabButton = findViewById(R.id.fab_favourite);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbarLayout =  findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(mMovie.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        ImageView mImage = findViewById(R.id.image);
        String path = api.getImgUrl(mMovie.getPosterPath(), true);
        Picasso.with(this).load(path).into(mImage);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.detail_content, new FragmentDetail(), FragmentDetail.class.getSimpleName());
            ft.replace(R.id.list_content, new FragmentListContent(), FragmentListContent.class.getSimpleName());
            ft.commit();
        }

		database = AppDatabase.getInstance(this.getApplication());

		Log.d(TAG, "onCreate: " + mMovie.getId());
		AddFavouriteViewModelFactory factory = new AddFavouriteViewModelFactory(database, mMovie.getId());
		viewModel = ViewModelProviders.of(this, factory).get(AddFavouritesViewModel.class);
		viewModel.getFavouriteMovies().observe(ActivityDetail.this, new Observer<Movie>() {
			@Override
			public void onChanged(@Nullable Movie movie) {
				if(movie != null && movie.isFavourite()){
					Log.d(TAG, "Is Favourite");
					fabButton.setSelected(true);
					isFavourite = true;
				}else{
					Log.d(TAG, "Not Favourite");
					fabButton.setSelected(false);
					isFavourite = false;
				}
			}
		});

		fabButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isFavourite){
					deleteMovie();
				}else{
					insertMovie();
				}
			}
		});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertMovie(){
		//Insert into DB on a thread
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				mMovie.setFavourite(true);
				database.movieDao().insertMovie(mMovie);
			}
		});
		thread.start();
	}

	private void deleteMovie(){
		//Insert into DB on a thread
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				database.movieDao().deleteMovie(mMovie);
			}
		});
		thread.start();
	}


	private void deleteAllMovies(){
		//Insert into DB on a thread
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				database.movieDao().deleteAllMovies();
			}
		});
		thread.start();
	}
}