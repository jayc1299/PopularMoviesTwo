package com.android.test.popularmoviestwo.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import com.android.test.popularmoviestwo.objects.Result;
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
		void onMovieUnFavoured();
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

		mAPi = new MovieApi(getActivity());

		//Get movie passed in from activity
		Intent intent = getActivity().getIntent();
		if(intent != null){
			mMovie = intent.getParcelableExtra(ActivityDetail.TAG_MOVIE_OBJECT);
		}

		//Toggle making movie a favourite
		mFavourites.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mMovie != null){
					if(!isFavourite()) {
						//Add new favourite
						ContentValues values = new ContentValues();
						values.put(TableHelperFavourites.COL_MOVIE_ID, mMovie.id);
						values.put(TableHelperFavourites.COL_MOVIE_TITLE, mMovie.title);
						values.put(TableHelperFavourites.COL_MOVIE_RELEASE_DATE, mMovie.releaseDate);
						values.put(TableHelperFavourites.COL_MOVIE_AVG_VOTE, mMovie.voteAverage);
						values.put(TableHelperFavourites.COL_MOVIE_OVERVIEW, mMovie.overview);
						values.put(TableHelperFavourites.COL_MOVIE_IMG_PATH, mMovie.posterPath);
						Uri insertedUri = getActivity().getContentResolver().insert(MoviesContract.URI_FAVOURITES_INSERT, values);
						v.setSelected(true);
					}else{
						//Delete from favourites for this movie
						String where = TableHelperFavourites.COL_MOVIE_ID + "=?";
						String[] args = {String.valueOf(mMovie.id)};
						getActivity().getContentResolver().delete(MoviesContract.URI_FAVOURITES_INSERT, where, args);
						v.setSelected(false);
						mCallback.onMovieUnFavoured();
					}
				}
			}
		});

		if(mMovie != null) {
			setupMovieDetails();
			//Set opening state for if movie is a favourite or not
			mFavourites.setSelected(isFavourite());
		}
	}

	/**
	 * Is this movie in favourite list?
	 * @return boolean true if it is
	 */
	private boolean isFavourite(){
		String[] args = {String.valueOf(mMovie.id)};
		String selection = TableHelperFavourites.COL_MOVIE_ID + " = ?";
		Cursor cursor = getActivity().getContentResolver().query(MoviesContract.URI_FAVOURITES_GET_ALL, new String[]{TableHelperFavourites.COL_MOVIE_ID}, selection, args, null);

		boolean isFavourite = false;
		if(cursor != null) {
			isFavourite = cursor.getCount() > 0;
			cursor.close();
		}

		return isFavourite;
	}

	private void setupMovieDetails(){
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