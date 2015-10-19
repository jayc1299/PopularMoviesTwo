package com.android.test.popularmoviestwo.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PojoTrailers{

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("results")
	@Expose
	private List<Trailer> trailers = new ArrayList<>();

	/**
	 *
	 * @return
	 * The id
	 */
	public long getId() {
		return id.longValue();
	}

	/**
	 * @param id
	 * The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return
	 * The movies
	 */
	public List<Trailer> getResults() {
		return trailers;
	}

	/**
	 * @param trailers
	 * The movies
	 */
	public void setResults(List<Trailer> trailers) {
		this.trailers = trailers;
	}
}