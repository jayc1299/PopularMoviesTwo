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
import com.android.test.popularmoviestwo.async.Result;
import com.android.test.popularmoviestwo.objects.Favourite;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFavourites extends ArrayAdapter<Favourite>{

	LayoutInflater mInflater;
	List<Favourite> mFavourites;
	Context mContext;
	MovieApi mApi;

	public AdapterFavourites(Context context, int resource, List<Favourite> favs) {
		super(context, resource, favs);
		mInflater = LayoutInflater.from(context);
		mFavourites = favs;
		mContext = context;
		mApi = new MovieApi(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();

		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.item_favourite, null, false);
			viewHolder.title = (TextView) convertView;
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.title.setText(mFavourites.get(position).getTitle());

		convertView.setTag(viewHolder);
		return convertView;
	}

	public class ViewHolder {
		TextView title;
	}
}