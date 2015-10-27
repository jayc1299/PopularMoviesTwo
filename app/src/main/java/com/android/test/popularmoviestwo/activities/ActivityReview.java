package com.android.test.popularmoviestwo.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.fragments.FragmentReview;

public class ActivityReview extends AppCompatActivity {

	public static String TAG_REVIEW = "tag_review";
	public static String TAG_AUTHOR = "tag_author";
	public static String TAG_TITLE = "tag_title";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_default);

		//Title
		Bundle bundle = getIntent().getExtras();
		if(bundle != null && bundle.getString(TAG_TITLE) != null) {
			ActionBar actionBar = this.getSupportActionBar();
			actionBar.setTitle(getString(R.string.fragment_reviews_title, bundle.getString(TAG_TITLE)));
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.activity_default_container, new FragmentReview(), FragmentReview.class.getSimpleName());
		ft.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}