package com.android.test.popularmoviestwo.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.objects.ArgsAsyncTrailers;
import com.android.test.popularmoviestwo.objects.PojoMovies;
import com.android.test.popularmoviestwo.objects.PojoTrailers;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncGetMovieTrailers extends AsyncTask<ArgsAsyncTrailers, Void, PojoTrailers> {

	private final int mTimeout = 15000;

	public interface IAsyncTrailers{
		void onTrailersReceived(PojoTrailers trailers);
	}

	private IAsyncTrailers mListener;
	private String CLASS_TAG = "AsyncGetMovieTrailers";

	public AsyncGetMovieTrailers(Activity activity){
		mListener = (IAsyncTrailers) activity;
	}

	public AsyncGetMovieTrailers(Fragment fragment){
		mListener = (IAsyncTrailers) fragment;
	}

	@Override
	protected PojoTrailers doInBackground(ArgsAsyncTrailers... params) {
		InputStream is = null;
		Context context = params[0].getContext();
		int movieId = params[0].getMovieId();

		MovieApi api = new MovieApi(context);

		try {
			URL url = api.getTrailersUrl(movieId);
			Log.d(CLASS_TAG, "myUrl:" + url.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(mTimeout);
			conn.setConnectTimeout(mTimeout);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			int response = conn.getResponseCode();
			Log.d(CLASS_TAG, "The response is: " + response);
			is = conn.getInputStream();

			// Convert the InputStream into a string
			String contentAsString = readIt(is);

			Gson gson = new Gson();
			PojoTrailers trailers = gson.fromJson(contentAsString, PojoTrailers.class);

			return trailers;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
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
	protected void onPostExecute(PojoTrailers trailers) {
		super.onPostExecute(trailers);

		if(trailers != null && trailers.getResults() != null) {
			Log.d(CLASS_TAG, "Trailers: " + trailers.getResults().size());
		}else{
			Log.d(CLASS_TAG, "onPostExecute: 0");
		}

		if(mListener != null){
			mListener.onTrailersReceived(trailers);
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