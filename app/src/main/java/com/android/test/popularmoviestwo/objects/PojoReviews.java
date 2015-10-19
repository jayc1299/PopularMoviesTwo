package com.android.test.popularmoviestwo.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PojoReviews {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("results")
	@Expose
	private List<Review> reviews = new ArrayList<Review>();
	@SerializedName("total_pages")
	@Expose
	private Integer totalPages;
	@SerializedName("totalResults")
	@Expose
	private Integer totalResults;

	public PojoReviews withId(Integer id) {
		this.id = id;
		return this;
	}


	public PojoReviews withResults(List<Review> reviews) {
		this.reviews = reviews;
		return this;
	}

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

	/**
	 * @return
	 * The reviews
	 */
	public List<Review> getResults() {
		return reviews;
	}

	/**
	 * @param reviews
	 * The reviews
	 */
	public void setResults(List<Review> reviews) {
		this.reviews = reviews;
	}

	/**
	 * @return
	 * Get total pages of results
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @return int totalPages
	 * Total number of pages
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return
	 * Get total number of reviews
	 */
	public int getTotalResults() {
		return totalResults;
	}

	/**
	 * @return int totalPages
	 * Set total number of results
	 */
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}
}