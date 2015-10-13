package com.android.test.popularmoviestwo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper{

	// If you change the database schema, you must increment the database version.
	private static final int DATABASE_VERSION = 1;
	static final String DATABASE_NAME = "movies.db";

	private Context mContext;

	public MovieDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableHelperFavourites.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		TableHelperFavourites.onUpgrade(mContext, db, oldVersion, newVersion);
	}
}