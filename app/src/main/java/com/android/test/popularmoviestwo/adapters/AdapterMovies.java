package com.android.test.popularmoviestwo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.async.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMovies extends ArrayAdapter<Result>{

	LayoutInflater mInflater;
	List<Result> mMovies;
	Context mContext;
	MovieApi mApi;

	public AdapterMovies(Context context, int resource, List<Result> movies) {
		super(context, resource, movies);
		mInflater = LayoutInflater.from(context);
		mMovies = movies;
		mContext = context;
		mApi = new MovieApi(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();

		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.item_movie, null, false);
			viewHolder.thumbImage = (ImageView) convertView;
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		//Get image path and use picasso to load it.
		String path = mApi.getImgUrl(getItem(position).posterPath, false);
		Picasso.with(mContext).load(path).into(viewHolder.thumbImage);

		convertView.setTag(viewHolder);
		return convertView;
	}

	public void updateItems(List<Result> movies){
		clear();
		addAll(movies);
	}

	public class ViewHolder {
		ImageView thumbImage;
	}
}