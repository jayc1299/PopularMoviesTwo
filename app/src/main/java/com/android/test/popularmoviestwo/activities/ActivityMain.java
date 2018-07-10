package com.android.test.popularmoviestwo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.fragments.FragmentDetail;
import com.android.test.popularmoviestwo.fragments.FragmentMain;
import com.android.test.popularmoviestwo.objects.Movie;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = ActivityMain.class.getSimpleName();
    public static final int REQUEST_CODE_SETTINGS = 1;
    public static final int REQUEST_CODE_DETAIL = 2;

    private SharedPreferences mPrefs;
    private boolean mTwoPainMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (findViewById(R.id.activity_main_detail_container) != null) {
            mTwoPainMode = true;
        } else {
            mTwoPainMode = false;
        }

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentMain frag = new FragmentMain();
            Bundle args = new Bundle();
            frag.setArguments(args);
            frag.setListener(movieClickListener);
            ft.replace(R.id.activity_main_movies_container, frag, FragmentMain.class.getSimpleName());
            ft.commit();
        }else{
            FragmentMain frag = (FragmentMain) getSupportFragmentManager().findFragmentByTag(FragmentMain.class.getSimpleName());
            if (frag != null) {
                frag.setListener(movieClickListener);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                //Start for result as settings must always refresh grid
                startActivityForResult(new Intent(ActivityMain.this, ActivitySettings.class), REQUEST_CODE_SETTINGS);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!isFinishing()) {
            if (requestCode == REQUEST_CODE_SETTINGS) {
                //Settings always causes refresh
                refreshMovieList();
            }
        }
    }

    /**
     * Force fragmentMain to refresh movie list
     */
    private void refreshMovieList() {
        Log.d(TAG, "refreshMovieList: ");
        FragmentMain frag = (FragmentMain) getSupportFragmentManager().findFragmentByTag(FragmentMain.class.getSimpleName());
        if (frag != null) {
            frag.showDesiredMovieList();
            frag.setListener(movieClickListener);
        }
        updateActionBarTitle();
    }

    /**
     * Update action bar
     */
    private void updateActionBarTitle() {
        if (isShowFavourites()) {
            getSupportActionBar().setTitle(getString(R.string.activity_favourites_title));
        } else {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }

    private boolean isShowFavourites() {
        return mPrefs.getBoolean(getString(R.string.pref_favs_key), false);
    }

    FragmentMain.IFragmentMainListener movieClickListener = new FragmentMain.IFragmentMainListener() {
        @Override
        public void onMovieClicked(Movie movie) {
            if (mTwoPainMode) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FragmentDetail frag = new FragmentDetail();
                Intent intent = getIntent();
                intent.putExtra(ActivityDetail.TAG_MOVIE_OBJECT, movie);
                ft.replace(R.id.activity_main_detail_container, frag, FragmentDetail.class.getSimpleName());
                ft.commit();
            } else {
                Intent intent = new Intent(ActivityMain.this, ActivityDetail.class);
                intent.putExtra(ActivityDetail.TAG_MOVIE_OBJECT, movie);
                startActivity(intent);
            }
        }
    };
}