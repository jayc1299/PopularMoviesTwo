package com.android.test.popularmoviestwo.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.fragments.FragmentDetail;

public class ActivityDetail extends AppCompatActivity implements FragmentDetail.IFragmentDetailCallback {

	public static String TAG_MOVIE_OBJECT = "tag_movie_obect";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Large artwork is loaded from web. So we need to delay the animation till this is done.
		//This allowed for a smoother animation using shared element.
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			postponeEnterTransition();
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.activity_main_container, new FragmentDetail(), FragmentDetail.class.getSimpleName());
		ft.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				//Finish after the transition is done.
				this.finishAfterTransition();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Direct the animation to continue as everything is now loaded.
	private void scheduleStartPostponedTransition(final View sharedElement) {
		sharedElement.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
							startPostponedEnterTransition();
						}
						return true;
					}
				});
	}

	//Callback from fragment.
	@Override
	public void onMoviePosterLoaded(View v) {
		scheduleStartPostponedTransition(v);
	}
}