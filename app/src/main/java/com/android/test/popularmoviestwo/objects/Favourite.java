package com.android.test.popularmoviestwo.objects;

public class Favourite {

	int id;
	String title;

	public Favourite(){}

	public Favourite(int id, String title){
		this.id = id;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}