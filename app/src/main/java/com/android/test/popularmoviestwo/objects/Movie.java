package com.android.test.popularmoviestwo.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {

	@SerializedName("adult")
	@Expose
	private boolean adult;
	@SerializedName("backdrop_path")
	@Expose
	private String backdropPath;
	@SerializedName("genre_ids")
	@Expose
	private List<Integer> genreIds = new ArrayList<Integer>();
	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("original_language")
	@Expose
	private String originalLanguage;
	@SerializedName("original_title")
	@Expose
	private String originalTitle;
	@SerializedName("overview")
	@Expose
	private String overview;
	@SerializedName("release_date")
	@Expose
	private String releaseDate;
	@SerializedName("poster_path")
	@Expose
	private String posterPath;
	@SerializedName("popularity")
	@Expose
	private float popularity;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("video")
	@Expose
	private boolean video;
	@SerializedName("vote_average")
	@Expose
	private float voteAverage;
	@SerializedName("vote_count")
	@Expose
	private int voteCount;

	public Movie(){}

	public Movie(int id, String title, String releaseDate, float voteAverage, String overview, String posterPath){
		this.id = id;
		this.title = title;
		this.releaseDate = releaseDate;
		this.voteAverage = voteAverage;
		this.overview = overview;
		this.posterPath = posterPath;
	}

	public boolean isAdult() {
		return adult;
	}

	public void setAdult(boolean adult) {
		this.adult = adult;
	}

	public String getBackdropPath() {
		return backdropPath;
	}

	public void setBackdropPath(String backdropPath) {
		this.backdropPath = backdropPath;
	}

	public List<Integer> getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(List<Integer> genreIds) {
		this.genreIds = genreIds;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOriginalLanguage() {
		return originalLanguage;
	}

	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}

	public float getPopularity() {
		return popularity;
	}

	public void setPopularity(float popularity) {
		this.popularity = popularity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isVideo() {
		return video;
	}

	public void setVideo(boolean video) {
		this.video = video;
	}

	public float getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage(float voteAverage) {
		this.voteAverage = voteAverage;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	public static Creator<Movie> getCREATOR() {
		return CREATOR;
	}

	protected Movie(Parcel in) {
		adult = in.readByte() != 0x00;
		backdropPath = in.readString();
		if (in.readByte() == 0x01) {
			genreIds = new ArrayList<Integer>();
			in.readList(genreIds, Integer.class.getClassLoader());
		} else {
			genreIds = null;
		}
		id = in.readInt();
		originalLanguage = in.readString();
		originalTitle = in.readString();
		overview = in.readString();
		releaseDate = in.readString();
		posterPath = in.readString();
		popularity = in.readFloat();
		title = in.readString();
		video = in.readByte() != 0x00;
		voteAverage = in.readFloat();
		voteCount = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (adult ? 0x01 : 0x00));
		dest.writeString(backdropPath);
		if (genreIds == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(genreIds);
		}
		dest.writeInt(id);
		dest.writeString(originalLanguage);
		dest.writeString(originalTitle);
		dest.writeString(overview);
		dest.writeString(releaseDate);
		dest.writeString(posterPath);
		dest.writeFloat(popularity);
		dest.writeString(title);
		dest.writeByte((byte) (video ? 0x01 : 0x00));
		dest.writeFloat(voteAverage);
		dest.writeInt(voteCount);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
		@Override
		public Movie createFromParcel(Parcel in) {
			return new Movie(in);
		}

		@Override
		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};
}