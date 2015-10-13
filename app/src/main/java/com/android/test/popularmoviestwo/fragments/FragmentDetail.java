package com.android.test.popularmoviestwo.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.test.popularmoviestwo.async.Result;
import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.activities.ActivityDetail;
import com.android.test.popularmoviestwo.database.MoviesContract;
import com.android.test.popularmoviestwo.database.TableHelperFavourites;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FragmentDetail extends Fragment{

	Result mMovie;
	MovieApi mAPi;
	IFragmentDetailCallback mCallback;
	ImageView mImage;
	TextView mFavourites;

	public interface IFragmentDetailCallback{
		void onMoviePosterLoaded(View v);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail, container, false);

		mFavourites = (TextView) view.findViewById(R.id.fragment_detail_favourites);

		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mCallback = (IFragmentDetailCallback) context;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mFavourites.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mMovie != null){
					ContentValues values = new ContentValues();
					values.put(TableHelperFavourites.COL_MOVIE_ID, mMovie.id);
					values.put(TableHelperFavourites.COL_MOVIE_TITLE, mMovie.title);
					Uri insertedUri = getActivity().getContentResolver().insert(MoviesContract.URI_FAVOURITES_INSERT, values);
				}
			}
		});

		mAPi = new MovieApi(getActivity());

		//Get movie passed in from activity
		Intent intent = getActivity().getIntent();
		if(intent != null){
			mMovie = intent.getParcelableExtra(ActivityDetail.TAG_MOVIE_OBJECT);
		}

		if(mMovie != null) {
			//Title
			ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
			actionBar.setTitle(mMovie.title);

			//Text
			((TextView) getView().findViewById(R.id.fragment_detail_title)).setText(mMovie.title);
			((TextView) getView().findViewById(R.id.fragment_detail_release_date)).setText(mMovie.releaseDate);
			((TextView) getView().findViewById(R.id.fragment_detail_vote_average)).setText(String.valueOf(mMovie.voteAverage));
			((TextView) getView().findViewById(R.id.fragment_detail_synopsis)).setText(String.valueOf(mMovie.overview));

			//Set image
			mImage = (ImageView) getView().findViewById(R.id.fragment_detail_image);
			String path = mAPi.getImgUrl(mMovie.posterPath, true);

			//Callback to activity to give go ahead to load.
			Picasso.with(getActivity()).load(path).into(mImage, new Callback() {
				@Override
				public void onSuccess() {
					mCallback.onMoviePosterLoaded(mImage);
				}

				@Override
				public void onError() {
					mCallback.onMoviePosterLoaded(mImage);
				}
			});
		}
	}
}