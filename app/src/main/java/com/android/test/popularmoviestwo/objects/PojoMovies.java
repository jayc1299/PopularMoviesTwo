package com.android.test.popularmoviestwo.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PojoMovies {

	@SerializedName("page")
	@Expose
	public int page;
	@SerializedName("results")
	@Expose
	public List<Movie> movies = new ArrayList<Movie>();
	@SerializedName("total_pages")
	@Expose
	public int totalPages;
	@SerializedName("total_results")
	@Expose
	public int totalResults;
}