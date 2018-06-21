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
/**
 * Helper class to build URLs for interacting with the Movie Api.
 *
 * Please note it is expected for their to be an gradle.properties file in values with the following entries:
 *
 * 1) apikey - This is your secret api key
 * 2) apimainurl - This is the base url for the movie api
 * 3) imgurl - This is the path to use for image urls.
 * 4) apiPathPopular - This is the url path for popular movies. Normally set to "popular"
 * 5) apiPathTopRated - This is the url path for top rated movies. Normally set to "top_rated"
 *
 * API urls are created below. Image urls are path separated / so are constructed by string substitution as such:
 * http://image.tmdb.org/t/p/%1$s/%2$s
 *
 */
public class MovieApi {

	private String mMainUrl;
	private Context mContext;

	/**
	 * Create MovieApi, supply context. Must have a movieapi.xml file supplied.
	 *
	 * @param context context
	 */
	public MovieApi(Context context){
		mContext = context;
		mMainUrl = getMainUrl();
	}


	/**
	 * Get Trailers Url for a specific movie
	 *
	 * @return URL the completed url with movie id and api key
	 */
	public URL getTrailersUrl(int movieId){
		Uri uri = Uri.parse(mMainUrl).buildUpon()
				.appendEncodedPath(mContext.getString(R.string.trailerpath, String.valueOf(movieId)))
				.appendQueryParameter("api_key", getApiKey())
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
	 * Get Reviews Url for a specific movie
	 *
	 * @return URL the completed url with movie id and api key
	 */
	public URL getReviewsUrl(int movieId){
		Uri uri = Uri.parse(mMainUrl).buildUpon()
				.appendEncodedPath(mContext.getString(R.string.reviewspath, String.valueOf(movieId)))
				.appendQueryParameter("api_key", getApiKey())
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
	public URL getMoviesUrl(){
		Uri uri = Uri.parse(mMainUrl).buildUpon()
				.appendPath(getSortByPath(mContext))
				.appendQueryParameter("api_key", getApiKey())
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
			return BuildConfig.IMG_URL.replace("%1$s", "w500").replace("%2$s", imgPath);
		}else{
			return BuildConfig.IMG_URL.replace("%1$s", "w342").replace("%2$s", imgPath);
		}
	}

	/**
	 * Get the main url
	 *
	 * @return main url
	 */
	private String getMainUrl(){
		return BuildConfig.MAIN_URL;
	}

	/**
	 * Get the path url segment for the ordering of movies.
	 *
	 * @param context context, used for shared prefs access
	 *
	 * @return String either popular or top_rated
	 */
	private String getSortByPath(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		//Get the preferences key. If it's set to popular return popular url path.
		if(prefs.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default)).equals(context.getString(R.string.preferences_key_popularity))){
			return BuildConfig.API_PATH_POPULAR;
		}else{
			return BuildConfig.API_PATH_TOP_RATED;
		}
	}

	private String getApiKey(){
		return BuildConfig.API_KEY;
	}
}