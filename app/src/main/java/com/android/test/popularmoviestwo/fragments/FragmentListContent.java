package com.android.test.popularmoviestwo.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.activities.ActivityDetail;
import com.android.test.popularmoviestwo.activities.ActivityReview;
import com.android.test.popularmoviestwo.adapters.AdapterDetails;
import com.android.test.popularmoviestwo.async.AsyncGetMovieReviews;
import com.android.test.popularmoviestwo.async.AsyncGetMovieTrailers;
import com.android.test.popularmoviestwo.objects.AsyncArgs;
import com.android.test.popularmoviestwo.objects.DetailDisplay;
import com.android.test.popularmoviestwo.objects.HeaderObject;
import com.android.test.popularmoviestwo.objects.Movie;
import com.android.test.popularmoviestwo.objects.PojoReviews;
import com.android.test.popularmoviestwo.objects.PojoTrailers;
import com.android.test.popularmoviestwo.objects.Review;
import com.android.test.popularmoviestwo.objects.Trailer;

import java.util.ArrayList;
import java.util.List;

public class FragmentListContent extends Fragment implements AsyncGetMovieTrailers.IAsyncTrailers, AsyncGetMovieReviews.IAsyncReviews {

    private static final String TAG = FragmentListContent.class.getSimpleName();

    private Movie mMovie;
    private RecyclerView mRecyclerview;

    private List<Review> mReviews;
    private List<Trailer> mTrailers;
    private int mRunCount = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_content, container, false);

        mRecyclerview = view.findViewById(R.id.list_content_recycler);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get movie passed in from activity
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            mMovie = intent.getParcelableExtra(ActivityDetail.TAG_MOVIE_OBJECT);
        }

        AsyncArgs args = new AsyncArgs();
        args.setContext(getActivity());
        args.setMovieId(mMovie.getId());
        //Get trailers
        AsyncGetMovieTrailers asyncT = new AsyncGetMovieTrailers(FragmentListContent.this);
        asyncT.execute(args);

        //Get reviews
        AsyncGetMovieReviews asyncR = new AsyncGetMovieReviews(FragmentListContent.this);
        asyncR.execute(args);
    }

    @Override
    public void onTrailersReceived(PojoTrailers trailers) {
        if (trailers != null && trailers.getResults() != null && trailers.getResults().size() > 0) {
            Log.d("FragmentDetail", "trailers:" + trailers.getResults().size());
            //Set trailers
            mTrailers = trailers.getResults();
        } else {
            mTrailers = new ArrayList<>();
        }
        onReceiveAsyncResults();
    }

    @Override
    public void onReviewsReceived(PojoReviews reviews) {
        if (reviews != null && reviews.getResults() != null && reviews.getResults().size() > 0) {
            Log.d("FragmentDetail", "reviews:" + reviews.getResults().size());
            mReviews = reviews.getResults();
        } else {
            mReviews = new ArrayList<>();
        }
        onReceiveAsyncResults();
    }

    /**
     * We only want to create the adapter when we have both results from the asyncs.
     * So we need to combine the results into one arrayList and then create the adapter.
     */
    private void onReceiveAsyncResults() {
        mRunCount++;
        Log.d(TAG, "onReceiveAsyncResults: " + mRunCount);
        if (mRunCount == 2 && getView() != null) {
            ArrayList<DetailDisplay> details = new ArrayList<>();

            details.add(new HeaderObject(getString(R.string.fragment_detail_trailers)));
            details.addAll(mTrailers);
            details.add(new HeaderObject(getString(R.string.fragment_detail_reviews)));
            details.addAll(mReviews);

            AdapterDetails mAdapter = new AdapterDetails(details, listItemClickListener);
            mRecyclerview.setAdapter(mAdapter);
            mRunCount = 0;
        }
    }

    private Intent getShareTrailerIntent() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.fragment_detail_share));
        //share.putExtra(Intent.EXTRA_TEXT, getString(R.string.youtube_link) + mFirstTrailer);

        return share;
    }

    private void showYouTubeTrailer(String key){
        PackageManager packageManager = getActivity().getPackageManager();
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_link) + key));
        //Can we open a youtube intent
        if(youTubeIntent.resolveActivity(packageManager) != null){
            startActivity(youTubeIntent);
        }else{
            //no, open it in webview
            startActivity(webIntent);
        }
    }

    private void openReviewDetail(String movieTitle, Review review){
    	Intent reviewIntent = new Intent(getActivity(), ActivityReview.class);
    	reviewIntent.putExtra(ActivityReview.TAG_TITLE, movieTitle);
    	reviewIntent.putExtra(ActivityReview.TAG_AUTHOR, review.getAuthor());
    	reviewIntent.putExtra(ActivityReview.TAG_REVIEW, review.getContent());
    	startActivity(reviewIntent);
	}

    private AdapterDetails.IAdapterDetailItemClicked listItemClickListener = new AdapterDetails.IAdapterDetailItemClicked() {
        @Override
        public void onTrailerClicked(String key) {
            showYouTubeTrailer(key);
        }

		@Override
		public void onReviewClicked(Review review) {
			openReviewDetail(mMovie.getTitle(), review);
		}
	};
}