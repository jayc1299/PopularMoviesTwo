package com.android.test.popularmoviestwo.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.test.popularmoviestwo.objects.DetailDisplay;
import com.android.test.popularmoviestwo.objects.Movie;
import com.android.test.popularmoviestwo.objects.PojoReviews;
import com.android.test.popularmoviestwo.objects.PojoTrailers;
import com.android.test.popularmoviestwo.objects.Review;
import com.android.test.popularmoviestwo.objects.Trailer;

import java.util.ArrayList;
import java.util.List;

public class FragmentDetail extends Fragment implements AsyncGetMovieTrailers.IAsyncTrailers, AsyncGetMovieReviews.IAsyncReviews {

	private static final String TAG = FragmentDetail.class.getSimpleName();

	private Movie mMovie;
	private MovieApi mAPi;
	private IFragmentDetailCallback mCallback;
	private ImageView mImage;
	private LinearLayout mFavourites;
	private RecyclerView mRecyclerview;
	private AdapterDetails mAdapter;
	private View mDetailView;
	private ShareActionProvider mShareActionProvider;
	private String mFirstTrailer;

	private List<Review> mReviews;
	private List<Trailer> mTrailers;
	private int mRunCount = 0;

	public interface IFragmentDetailCallback{
		void onMoviePosterLoaded(View v);
		void onMovieUnFavoured();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail, container, false);

		mRecyclerview = view.findViewById(R.id.fragment_detail_trailers);
		mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
		setHasOptionsMenu(true);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragment_details, menu);
		MenuItem menuItem = menu.findItem(R.id.action_share);

		// Get the provider and hold onto it to set/change the share intent.
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			mCallback = (IFragmentDetailCallback) context;
		}catch(ClassCastException e){
			//We don't really care, ActivityMain doesn't need to implement this.
		}
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
		args.setMovieId(mMovie.getId());
		AsyncGetMovieTrailers asyncT = new AsyncGetMovieTrailers(FragmentDetail.this);
		asyncT.execute(args);

		//Get reviews
		AsyncGetMovieReviews asyncR = new AsyncGetMovieReviews(FragmentDetail.this);
		asyncR.execute(args);

		/*
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
						if(mCallback != null) {
							mCallback.onMovieUnFavoured();
						}
					}
				}
			}
		});
		*/

		/*
		mRecyclerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position > 0) {
					//Offset item (-1) for header
					Detail detail = mAdapter.getItem(position - 1);
					String url = detail.getUrl();
					if(url != null) {
						//Luanch youtube viewer
						url = getString(R.string.youtube_link) + url;
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(intent);
					}else if(detail.getAuthor() != null){
						//Launch review detail page
						Intent intent = new Intent(getActivity(), ActivityReview.class);
						intent.putExtra(ActivityReview.TAG_REVIEW, detail.getTitle());
						intent.putExtra(ActivityReview.TAG_AUTHOR, detail.getAuthor());
						intent.putExtra(ActivityReview.TAG_TITLE, mMovie.title);
						startActivity(intent);
					}
				}
			}
		});
		*/

		if(mMovie != null) {
			setupMovieDetails();
		}
	}

	/**
	 * Is this movie in favourite list?
	 * @return boolean true if it is
	 */
	private boolean isFavourite(){
		String[] args = {String.valueOf(mMovie.getId())};
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
		actionBar.setTitle(mMovie.getTitle());
	}

	@Override
	public void onTrailersReceived(PojoTrailers trailers) {
		if(trailers != null && trailers.getResults() != null && trailers.getResults().size() > 0) {
			Log.d("FragmentDetail", "trailers:" + trailers.getResults().size());
			//Set trailers
			mTrailers = trailers.getResults();
		}else{
			mTrailers = new ArrayList<>();
		}
		onReceiveAsyncResults();
	}

	@Override
	public void onReviewsReceived(PojoReviews reviews) {
		if(reviews != null && reviews.getResults() != null && reviews.getResults().size() > 0) {
			Log.d("FragmentDetail", "reviews:" + reviews.getResults().size());
			mReviews = reviews.getResults();
		}else {
			mReviews = new ArrayList<>();
		}
		onReceiveAsyncResults();
	}

	/**
	 * We only want to create the adapter when we have both results from the asyncs.
	 * So we need to combine the results into one arrayList and then create the adapter.
	 *
	 */
	private void onReceiveAsyncResults(){
		mRunCount++;
		Log.d(TAG, "onReceiveAsyncResults: " + mRunCount);
		if(mRunCount == 2){
			ArrayList<DetailDisplay> details = new ArrayList<>();

			details.add(mMovie);
			details.addAll(mTrailers);
			details.addAll(mReviews);

			mAdapter = new AdapterDetails(details);
			mRecyclerview.setAdapter(mAdapter);
			//mRecyclerview.addHeaderView(mDetailView, null, false);
			mRunCount = 0;
		}
	}

	private Intent getShareTrailerIntent() {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.fragment_detail_share));
		share.putExtra(Intent.EXTRA_TEXT, getString(R.string.youtube_link) + mFirstTrailer);

		return share;
	}
}