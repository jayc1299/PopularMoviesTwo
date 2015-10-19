package com.android.test.popularmoviestwo.objects;

public class Detail {

	long id;
	String title;
	int drawable;
	String url;
	boolean isReviewHeading = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDrawable() {
		return drawable;
	}

	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean getIsReviewHeading() {
		return isReviewHeading;
	}

	public void setIsReviewHeading(boolean isReviewHeading) {
		this.isReviewHeading = isReviewHeading;
	}
}