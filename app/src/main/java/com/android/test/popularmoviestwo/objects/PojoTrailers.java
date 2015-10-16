package com.android.test.popularmoviestwo.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PojoTrailers {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("results")
	@Expose
	private List<Trailers> results = new ArrayList<Trailers>();

	/**
	 *
	 * @return
	 * The id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 * The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public PojoTrailers withId(Integer id) {
		this.id = id;
		return this;
	}

	/**
	 *
	 * @return
	 * The results
	 */
	public List<Trailers> getResults() {
		return results;
	}

	/**
	 *
	 * @param results
	 * The results
	 */
	public void setResults(List<Trailers> results) {
		this.results = results;
	}

	public PojoTrailers withResults(List<Trailers> results) {
		this.results = results;
		return this;
	}

}