package com.android.test.popularmoviestwo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class TableHelperFavourites {

	public static final String TABLE_NAME = "items";

	public static final String COL_ID = "_id";
	public static final String COL_MOVIE_ID = "movie_id";
	public static final String COL_MOVIE_TITLE = "name";

	public static final String[] AVAILABLE_COLUMNS = {
			COL_ID,
			COL_MOVIE_ID,
			COL_MOVIE_TITLE
	};

	// Database creation SQL statement
	private static final String TABLE_CREATE = 	"create table "
			+ TableHelperFavourites.TABLE_NAME
			+ "("
			+ TableHelperFavourites.COL_ID + " integer primary key autoincrement, "
			+ TableHelperFavourites.COL_MOVIE_ID + " integer not null, "
			+ TableHelperFavourites.COL_MOVIE_TITLE + " string not null "
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(TableHelperFavourites.TABLE_CREATE);
	}

	public static void onUpgrade(Context context, SQLiteDatabase database, int oldVersion, int newVersion) {

	}
}