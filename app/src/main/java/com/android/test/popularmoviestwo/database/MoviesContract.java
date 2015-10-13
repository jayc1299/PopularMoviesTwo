package com.android.test.popularmoviestwo.database;

import android.content.ContentUris;
import android.net.Uri;

public class MoviesContract {

	public static final String CONTENT_AUTHORITY = "com.android.test.popularmoviestwo";

	// Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider.
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

	public static final String PATH_FAVOUTIRES = "favourites";

	public static Uri URI_FAVOURITES_GET_ALL = MoviesContract.BASE_CONTENT_URI.buildUpon().appendPath(MoviesContract.PATH_FAVOUTIRES).build();
	public static Uri URI_FAVOURITES_INSERT = MoviesContract.BASE_CONTENT_URI.buildUpon().appendPath(MoviesContract.PATH_FAVOUTIRES).appendPath("insert").build();
}