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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.activities.ActivityDetail;
import com.android.test.popularmoviestwo.adapters.AdapterDetails;
import com.android.test.popularmoviestwo.async.AsyncGetMovieReviews;
import com.android.test.popularmoviestwo.async.AsyncGetMovieTrailers;
import com.android.test.popularmoviestwo.database.MoviesContract;
import com.android.test.popularmoviestwo.database.TableHelperFavourites;
import com.android.test.popularmoviestwo.objects.ArgsAsyncTrailers;
import com.android.test.popularmoviestwo.objects.Detail;
import com.android.test.popularmoviestwo.objects.Movie;
import com.android.test.popularmoviestwo.objects.PojoReviews;
import com.android.test.popularmoviestwo.objects.PojoTrailers;
import com.android.test.popularmoviestwo.objects.Review;
import com.android.test.popularmoviestwo.objects.Trailer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FragmentDetail extends Fragment implements AsyncGetMovieTrailers.IAsyncTrailers, AsyncGetMovieReviews.IAsyncReviews {

	Movie mMovie;
	MovieApi mAPi;
	IFragmentDetailCallback mCallback;
	ImageView mImage;
	LinearLayout mFavourites;
	ListView mListview;
	AdapterDetails mAdapter;
	View mDetailView;

	public interface IFragmentDetailCallback{
		void onMoviePosterLoaded(View v);
		void onMovieUnFavoured();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail, container, false);

		mDetailView = inflater.inflate(R.layout.fragment_detail_header, container, false);

		mFavourites = (LinearLayout) mDetailView.findViewById(R.id.fragment_detail_favourites);
		mListview = (ListView) view.findViewById(R.id.fragment_detail_trailers);


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

		//Get trailers
		ArgsAsyncTrailers args = new ArgsAsyncTrailers();
		args.setContext(getActivity());
		args.setMovieId(mMovie.id);
		AsyncGetMovieTrailers asyncT = new AsyncGetMovieTrailers(FragmentDetail.this);
		asyncT.execute(args);

		//Get reviews
		AsyncGetMovieReviews asyncR = new AsyncGetMovieReviews(FragmentDetail.this);
		asyncR.execute(args);

		//Toggle making movie a favourite
		mFavourites.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mMovie != null) {
					if (!isFavourite()) {
						//Add new favourite
						ContentValues values = new ContentValues();
						values.put(TableHelperFavourites.COL_MOVIE_ID, mMovie.id);
						values.put(TableHelperFavourites.COL_MOVIE_TITLE, mMovie.title);
						values.put(TableHelperFavourites.COL_MOVIE_RELEASE_DATE, mMovie.releaseDate);
						values.put(TableHelperFavourites.COL_MOVIE_AVG_VOTE, mMovie.voteAverage);
						values.put(TableHelperFavourites.COL_MOVIE_OVERVIEW, mMovie.overview);
						values.put(TableHelperFavourites.COL_MOVIE_IMG_PATH, mMovie.posterPath);
						getActivity().getContentResolver().insert(MoviesContract.URI_FAVOURITES_INSERT, values);
						v.setSelected(true);
					} else {
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

		mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position > 0) {
					//Offset item (-1) for header
					String url = mAdapter.getItem(position - 1).getUrl();
					if(url != null) {
						//Luanch youtube viewer
						url = getString(R.string.youtube_link) + url;
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(intent);
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
		((TextView) mDetailView.findViewById(R.id.fragment_detail_title)).setText(mMovie.title);
		((TextView) mDetailView.findViewById(R.id.fragment_detail_release_date)).setText(mMovie.releaseDate);
		((TextView) mDetailView.findViewById(R.id.fragment_detail_vote_average)).setText(getString(R.string.fragment_detail_rating_text, String.valueOf(mMovie.voteAverage)));
		((TextView) mDetailView.findViewById(R.id.fragment_detail_synopsis)).setText(String.valueOf(mMovie.overview));

		//Set image
		mImage = (ImageView) mDetailView.findViewById(R.id.fragment_detail_image);
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

	@Override
	public void onTrailersReceived(PojoTrailers trailers) {
		Log.d("FragmentDetail", "trailers:" + trailers.getResults().size());
		mTrailers = trailers.getResults();
		onReceiveAsyncResults();
	}

	@Override
	public void onReviewsReceived(PojoReviews reviews) {
		Log.d("FragmentDetail", "reviews:" + reviews.getResults().size());
		mReviews = reviews.getResults();
		onReceiveAsyncResults();
	}

	List<Review> mReviews;
	List<Trailer> mTrailers;
	int runCount = 0;

	private void onReceiveAsyncResults(){
		runCount++;
		ArrayList<Detail> details = new ArrayList<>();
		if(runCount == 2){
			Detail detail;
			//Trailers must come first
			for(Trailer trailer: mTrailers){
				detail = new Detail();
				detail.setTitle(trailer.getName());
				detail.setUrl(trailer.getKey());
				detail.setDrawable(android.R.drawable.ic_media_play);
				details.add(detail);
			}

			if(mReviews.size() > 0) {
				//Insert a heading between two groups
				detail = new Detail();
				detail.setIsReviewHeading(true);
				details.add(detail);

				//Then reviews
				Review review;
				for (int i = 0; i < mReviews.size(); i++) {
					review = mReviews.get(i);
					if (i >= getResources().getInteger(R.integer.max_reviews)) {
						//Too many reviews, screen would be squashed.
						break;
					}
					detail = new Detail();
					detail.setTitle(review.getContent());
					detail.setDrawable(android.R.drawable.ic_menu_edit);
					details.add(detail);
				}
			}

			mAdapter = new AdapterDetails(getActivity(), R.layout.item_detail, details);
			mListview.setAdapter(mAdapter);
			mListview.addHeaderView(mDetailView, null, false);
		}
	}
}