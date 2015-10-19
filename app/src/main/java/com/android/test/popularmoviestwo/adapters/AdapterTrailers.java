package com.android.test.popularmoviestwo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.objects.Trailer;

import java.util.List;

public class AdapterTrailers extends ArrayAdapter<Trailer>{

	LayoutInflater mInflater;
	Context mContext;
	MovieApi mApi;

	public AdapterTrailers(Context context, int resource, List<Trailer> trailers) {
		super(context, resource, trailers);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mApi = new MovieApi(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();

		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.item_trailer, null, false);
			viewHolder.url = (TextView) convertView.findViewById(R.id.item_trailer_text);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		//Get image path and use picasso to load it.
		viewHolder.url.setText(getItem(position).getName());

		convertView.setTag(viewHolder);
		return convertView;
	}

	public class ViewHolder {
		TextView url;
	}
}