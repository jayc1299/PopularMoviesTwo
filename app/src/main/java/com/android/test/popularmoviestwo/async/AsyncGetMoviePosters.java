package com.android.test.popularmoviestwo.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.test.popularmoviestwo.MovieApi;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncGetMoviePosters extends AsyncTask<Context, Void, PojoMovies> {

	private final int mTimeout = 15000;

	public interface IAsyncMovies{
		void onMoviesReceived(PojoMovies movies);
	}

	private IAsyncMovies mListener;
	private String CLASS_TAG = "AsyncGetMoviePosters";

	public AsyncGetMoviePosters(Activity activity){
		mListener = (IAsyncMovies) activity;
	}

	public AsyncGetMoviePosters(Fragment fragment){
		mListener = (IAsyncMovies) fragment;
	}

	@Override
	protected PojoMovies doInBackground(Context... params) {
		InputStream is = null;
		Context context = params[0];
		MovieApi api = new MovieApi(context);

		try {
			URL url = api.getMoviesUrl();
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
			PojoMovies movies = gson.fromJson(contentAsString, PojoMovies.class);

			return movies;

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
	protected void onPostExecute(PojoMovies movies) {
		super.onPostExecute(movies);

		if(mListener != null){
			mListener.onMoviesReceived(movies);
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