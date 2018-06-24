package com.android.test.popularmoviestwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.fragments.FragmentDetail;
import com.android.test.popularmoviestwo.objects.Movie;
import com.squareup.picasso.Picasso;

public class ActivityDetail extends AppCompatActivity {

    public static String TAG_MOVIE_OBJECT = "tag_movie_obect";
    public static String TAG_IS_ON_FAVOURITES = "tag_is_on_favourites";

    boolean mIsFromFavourites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //movie details
        MovieApi api = new MovieApi(this);
        Movie mMovie = getIntent().getParcelableExtra(ActivityDetail.TAG_MOVIE_OBJECT);

        Toolbar actionBar = findViewById(R.id.toolbar);
        actionBar.setTitle(mMovie.getTitle());

        ImageView mImage = (ImageView) findViewById(R.id.image);
        String path = api.getImgUrl(mMovie.getPosterPath(), true);
        Picasso.with(this).load(path).into(mImage);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content, new FragmentDetail(), FragmentDetail.class.getSimpleName());
            ft.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}