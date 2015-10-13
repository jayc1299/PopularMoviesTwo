package com.android.test.popularmoviestwo.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class FavouritesProvider extends ContentProvider {

	private MovieDbHelper mDbHelper;
	private static UriMatcher sUriMatcher;

	private static final int GET_FAVOURITES = 0x1;
	private static final int INSERT_FAVOURITE = 0x2;

	@Override
	public boolean onCreate() {
		sUriMatcher = buildUriMatcher();

		mDbHelper = new MovieDbHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor retCursor;
		SQLiteQueryBuilder sqlLiteBuilder = new SQLiteQueryBuilder();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		sqlLiteBuilder.setTables(TableHelperFavourites.TABLE_NAME);

		switch (sUriMatcher.match(uri)) {
			case GET_FAVOURITES:{
				retCursor = sqlLiteBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
				break;
			}
			default: {
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}

		retCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return retCursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri returnUri;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		switch (sUriMatcher.match(uri)) {
			case INSERT_FAVOURITE:{
				long id = db.insert(TableHelperFavourites.TABLE_NAME, null, values);
				if (id > 0)
					returnUri = ContentUris.withAppendedId(MoviesContract.URI_FAVOURITES_INSERT, id);
				else
					throw new android.database.SQLException("Failed to insert row into " + uri);
				break;
			}
			default: {
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.w(FavouritesProvider.class.getSimpleName(), "No code for delete");
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.w(FavouritesProvider.class.getSimpleName(), "No code for update");
		return 0;
	}

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

		// For each type of URI you want to add, create a corresponding code.
		matcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_FAVOUTIRES, GET_FAVOURITES);
		matcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_FAVOUTIRES + "/insert", INSERT_FAVOURITE);
		return matcher;
	}
}
