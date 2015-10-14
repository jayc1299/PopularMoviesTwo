package com.android.test.popularmoviestwo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.fragments.FragmentMain;

public class ActivityMain extends AppCompatActivity{

	public static final int REQUEST_CODE_SETTINGS = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.activity_main_container, new FragmentMain(), FragmentMain.class.getSimpleName());
		ft.commit();
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
				startActivityForResult(new Intent(ActivityMain.this, ActivitySettings.class), REQUEST_CODE_SETTINGS);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateActionBarTitle();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Refresh movies when settings page closes.
		if(!isFinishing() && requestCode == REQUEST_CODE_SETTINGS){
			FragmentMain fm = (FragmentMain) getSupportFragmentManager().findFragmentByTag(FragmentMain.class.getSimpleName());
			if(fm != null){
				fm.showTiles();
			}
		}
	}

	private void updateActionBarTitle(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean(this.getString(R.string.pref_favs_key), false)){
			getSupportActionBar().setTitle(getString(R.string.activity_favourites_title));
		}else{
			getSupportActionBar().setTitle(getString(R.string.app_name));
		}
	}
}