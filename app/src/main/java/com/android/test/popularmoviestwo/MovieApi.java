package com.android.test.popularmoviestwo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Helper class to build URLs for interacting with the Movie Api.
 *
 * Please note it is expected for their to be an xml file in values with the following entries:
 *
 * 1) apikey - This is your secret api key
 * 2) apimainurl - This is the base url for the movie api
 * 3) imgurl - This is the path to use for image urls.
 *
 * API urls are created below. Image urls are path seperated / so are constructed by string substitution as such:
 * http://image.tmdb.org/t/p/%1$s/%2$s
 *
 */
public class MovieApi {

	private String mMainUrl;
	private Context mContext;

	/**
	 * Create MovieApi, supply context. Must have a movieapi.xml file supplied.
	 *
	 * @param context
	 */
	public MovieApi(Context context){
		mContext = context;
		mMainUrl = getMainUrl(context);
	}

	/**
	 * Get Movie Url for most popular movies.
	 *
	 * @return URL the completed url with api key and sort by ordering.
	 */
	public URL getMoviesUrl(){
		Uri uri = Uri.parse(mMainUrl).buildUpon()
				.appendEncodedPath(mContext.getString(R.string.moviepath))
				.appendQueryParameter("api_key", getApiKey(mContext))
				.appendQueryParameter("sort_by", getSortBy(mContext))
				.build();
		URL url = null;
		try {
			url = new URL(uri.toString());
		} catch (MalformedURLException e) {
			Log.e(MovieApi.class.getSimpleName(), "MalformedURLException");
			return null;
		}
		return url;
	}

	/**
	 * Get Movie Url for most popular movies.
	 *
	 * @return URL the completed url with api key and sort by ordering.
	 */
	public URL getTrailersUrl(int movieId){
		Uri uri = Uri.parse(mMainUrl).buildUpon()
				.appendEncodedPath(mContext.getString(R.string.trailerpath, String.valueOf(movieId)))
				.appendQueryParameter("api_key", getApiKey(mContext))
				.build();
		URL url = null;
		try {
			url = new URL(uri.toString());
		} catch (MalformedURLException e) {
			Log.e(MovieApi.class.getSimpleName(), "MalformedURLException");
			return null;
		}
		return url;
	}

	/**
	 * Get IMG url used for displaying images.
	 *
	 * Url need to know size to request and the path to append to end.
	 *
	 * @param imgPath path of actual image to be appended to url
	 * @param large boolean if we require a large image or now.
	 * @return String url.
	 */
	public String getImgUrl(String imgPath, boolean large){
		if(large) {
			return mContext.getString(R.string.imgurl, "w500", imgPath);
		}else{
			return mContext.getString(R.string.imgurl, "w342", imgPath);
		}
	}

	private String getMainUrl(Context context){
		return context.getString(R.string.apimainurl);
	}

	private String getSortBy(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default));
	}

	private String getApiKey(Context context){
		return context.getString(R.string.apikey);
	}
}