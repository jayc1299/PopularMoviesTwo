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

public class ActivityMain extends AppCompatActivity implements FragmentMain.IFragmentMainListener {

	public static final int REQUEST_CODE_SETTINGS = 1;
	public static final int REQUEST_CODE_DETAIL = 2;

	SharedPreferences mPrefs;
	boolean mTwoPainMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		if(findViewById(R.id.activity_main_detail_container) != null){
			mTwoPainMode = true;
		}else{
			mTwoPainMode = false;
		}

		if(savedInstanceState == null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			FragmentMain frag = new FragmentMain();
			Bundle args = new Bundle();
			args.putBoolean(FragmentMain.TAG_TABLET_MODE, mTwoPainMode);
			frag.setArguments(args);
			ft.replace(R.id.activity_main_movies_container, frag, FragmentMain.class.getSimpleName());
			ft.commit();
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

		switch (id){
			case R.id.action_settings:
				//Start for result as settings must always refresh grid
				startActivityForResult(new Intent(ActivityMain.this, ActivitySettings.class), REQUEST_CODE_SETTINGS);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(!isFinishing()){
			if(requestCode == REQUEST_CODE_SETTINGS){
				//Settings always causes refresh
				refreshMovieList();
			}else if(requestCode == REQUEST_CODE_DETAIL && isShowFavourites()){
				//On detail close, only refresh if showing favourites.
				refreshMovieList();
			}
		}
	}

	/**
	 * Force fragmentMain to refresh movie list
	 */
	private void refreshMovieList(){
		FragmentMain fm = (FragmentMain) getSupportFragmentManager().findFragmentByTag(FragmentMain.class.getSimpleName());
		if(fm != null){
			fm.showTiles();
		}
		updateActionBarTitle();
	}

	/**
	 * Update action bar
	 */
	private void updateActionBarTitle(){
		if(isShowFavourites()){
			getSupportActionBar().setTitle(getString(R.string.activity_favourites_title));
		}else{
			getSupportActionBar().setTitle(getString(R.string.app_name));
		}
	}

	private boolean isShowFavourites(){
		return mPrefs.getBoolean(getString(R.string.pref_favs_key), false);
	}

	/**
	 * Callback from Fragment Main if we need to show movie details in a split screen pane, instead of opening new activity
	 * @param movie the movie details to show
	 */
	@Override
	public void onMovieClicked(Movie movie) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		FragmentDetail frag = new FragmentDetail();
		Intent intent = getIntent();
		intent.putExtra(ActivityDetail.TAG_MOVIE_OBJECT, movie);
		intent.putExtra(ActivityDetail.TAG_IS_ON_FAVOURITES, isShowFavourites());
		ft.replace(R.id.activity_main_detail_container, frag, FragmentDetail.class.getSimpleName());
		ft.commit();
	}
}