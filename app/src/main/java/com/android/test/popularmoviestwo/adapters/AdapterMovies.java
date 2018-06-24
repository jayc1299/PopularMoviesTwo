package com.android.test.popularmoviestwo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.Utils;
import com.android.test.popularmoviestwo.objects.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMovies extends ArrayAdapter<Movie>{

	LayoutInflater mInflater;
	List<Movie> mMovies;
	Context mContext;
	MovieApi mApi;
	boolean mHasInternet = true;

	public AdapterMovies(Context context, int resource, List<Movie> movies) {
		super(context, resource, movies);
		mInflater = LayoutInflater.from(context);
		mMovies = movies;
		mContext = context;
		mApi = new MovieApi(mContext);
		mHasInternet = Utils.isInternetAvailable(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();

		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.item_movie, null, false);
			viewHolder.thumbImage = (ImageView) convertView.findViewById(R.id.item_movie_image);
			viewHolder.altText = (TextView) convertView.findViewById(R.id.image_movie_alt_text);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(!mHasInternet) {
			viewHolder.altText.setText(getItem(position).getTitle());
			viewHolder.altText.setVisibility(View.VISIBLE);
			viewHolder.thumbImage.setVisibility(View.GONE);
		}else{
			viewHolder.altText.setVisibility(View.GONE);
			viewHolder.thumbImage.setVisibility(View.VISIBLE);
		}

		//Get image path and use picasso to load it.
		String path = mApi.getImgUrl(getItem(position).getPosterPath(), false);
		Picasso.with(mContext).load(path).into(viewHolder.thumbImage);

		convertView.setTag(viewHolder);
		return convertView;
	}

	public void updateItems(List<Movie> movies){
		mHasInternet = Utils.isInternetAvailable(mContext);
		clear();
		addAll(movies);
	}

	public class ViewHolder {
		ImageView thumbImage;
		TextView altText;
	}
}