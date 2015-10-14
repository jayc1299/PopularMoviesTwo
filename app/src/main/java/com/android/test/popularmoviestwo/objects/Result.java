package com.android.test.popularmoviestwo.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Result implements Parcelable {

	@SerializedName("adult")
	@Expose
	public boolean adult;
	@SerializedName("backdrop_path")
	@Expose
	public String backdropPath;
	@SerializedName("genre_ids")
	@Expose
	public List<Integer> genreIds = new ArrayList<Integer>();
	@SerializedName("id")
	@Expose
	public int id;
	@SerializedName("original_language")
	@Expose
	public String originalLanguage;
	@SerializedName("original_title")
	@Expose
	public String originalTitle;
	@SerializedName("overview")
	@Expose
	public String overview;
	@SerializedName("release_date")
	@Expose
	public String releaseDate;
	@SerializedName("poster_path")
	@Expose
	public String posterPath;
	@SerializedName("popularity")
	@Expose
	public float popularity;
	@SerializedName("title")
	@Expose
	public String title;
	@SerializedName("video")
	@Expose
	public boolean video;
	@SerializedName("vote_average")
	@Expose
	public float voteAverage;
	@SerializedName("vote_count")
	@Expose
	public int voteCount;

	public Result(){}

	public Result(int id, String title, String releaseDate, float voteAverage, String overview, String posterPath){
		this.id = id;
		this.title = title;
		this.releaseDate = releaseDate;
		this.voteAverage = voteAverage;
		this.overview = overview;
		this.posterPath = posterPath;
	}

	protected Result(Parcel in) {
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
	public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
		@Override
		public Result createFromParcel(Parcel in) {
			return new Result(in);
		}

		@Override
		public Result[] newArray(int size) {
			return new Result[size];
		}
	};
}