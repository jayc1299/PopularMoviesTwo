package com.android.test.popularmoviestwo.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.objects.AsyncArgs;
import com.android.test.popularmoviestwo.objects.PojoReviews;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncGetMovieReviews extends AsyncTask<AsyncArgs, Void, PojoReviews> {

	private static final String TAG = AsyncGetMovieReviews.class.getSimpleName();
	private static final int mTimeout = 15000;
	private IAsyncReviews mListener;

	public interface IAsyncReviews{
		void onReviewsReceived(PojoReviews reviews);
	}

	public AsyncGetMovieReviews(Activity activity){
		mListener = (IAsyncReviews) activity;
	}

	public AsyncGetMovieReviews(Fragment fragment){
		mListener = (IAsyncReviews) fragment;
	}

	@Override
	protected PojoReviews doInBackground(AsyncArgs... params) {
		InputStream is = null;
		Context context = params[0].getContext();
		int movieId = params[0].getMovieId();

		MovieApi api = new MovieApi(context);

		try {
			URL url = api.getReviewsUrl(movieId);
			Log.d(TAG, "myUrl:" + url.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(mTimeout);
			conn.setConnectTimeout(mTimeout);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			int response = conn.getResponseCode();
			Log.d(TAG, "The response is: " + response);
			is = conn.getInputStream();

			// Convert the InputStream into a string
			String contentAsString = readIt(is);

			Gson gson = new Gson();
			return gson.fromJson(contentAsString, PojoReviews.class);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	@Override
	protected void onPostExecute(PojoReviews reviews) {
		super.onPostExecute(reviews);

		if(reviews != null && reviews.getResults() != null) {
			Log.d(TAG, "reviews: " + reviews.getResults().size());
		}else{
			Log.d(TAG, "onPostExecute: 0");
		}

		if(mListener != null){
			mListener.onReviewsReceived(reviews);
		}
	}

	public String readIt(InputStream stream) throws IOException{
		BufferedReader r = new BufferedReader(new InputStreamReader(stream));
		StringBuilder total = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			total.append(line);
		}

		return total.toString();
	}
}