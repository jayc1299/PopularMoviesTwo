package com.android.test.popularmoviestwo.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.objects.Detail;

import java.util.List;

public class AdapterDetails extends ArrayAdapter<Detail>{

	LayoutInflater mInflater;
	Context mContext;
	MovieApi mApi;

	public AdapterDetails(Context context, int resource, List<Detail> detailObjects) {
		super(context, resource, detailObjects);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mApi = new MovieApi(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		Detail detail = getItem(position);

		//We are a divider (heading) just return the view.
		if(detail.getIsReviewHeading()){
			View view = mInflater.inflate(R.layout.item_review_heading, null, false);
			view.setTag(null);
			return view;
		}

		if(convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.item_detail, null, false);
			viewHolder.title = (TextView) convertView.findViewById(R.id.item_detail_text);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.item_detail_img);
			viewHolder.seperator = convertView.findViewById(R.id.item_detail_seperator);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.title.setText(detail.getTitle());
		viewHolder.img.setImageDrawable(ContextCompat.getDrawable(mContext, detail.getDrawable()));

		//Is our next item a divider
		boolean nextIsDivider = false;
		if(position + 1 < getCount() && getItem(position + 1).getIsReviewHeading()){
			nextIsDivider = true;
		}

		//Hide seperator if we're on the last line (offset for header) OR next item is divider
		if(getCount() < position - 1 || nextIsDivider){
			viewHolder.seperator.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.seperator.setVisibility(View.VISIBLE);
		}

		convertView.setTag(viewHolder);
		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		return getItem(position).getUrl() != null || getItem(position).getAuthor() != null;
	}

	public class ViewHolder {
		TextView title;
		ImageView img;
		View seperator;
	}
}