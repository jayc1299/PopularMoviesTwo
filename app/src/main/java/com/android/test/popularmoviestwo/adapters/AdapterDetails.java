package com.android.test.popularmoviestwo.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.objects.Detail;
import com.android.test.popularmoviestwo.objects.DetailDisplay;
import com.android.test.popularmoviestwo.objects.Movie;
import com.android.test.popularmoviestwo.objects.Review;
import com.android.test.popularmoviestwo.objects.Trailer;

import java.util.ArrayList;

public class AdapterDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	public static final int DISPLAY_TYPE_DETAIL = 1;
	public static final int DISPLAY_TYPE_REVIEW = 2;
	public static final int DISPLAY_TYPE_TRAILER = 3;

	private ArrayList<DetailDisplay> items;

	public AdapterDetails(ArrayList<DetailDisplay> items) {
		this.items = items;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		switch (viewType){
			case DISPLAY_TYPE_DETAIL:
				return new DetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false));
			case DISPLAY_TYPE_REVIEW:
				return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false));
			case DISPLAY_TYPE_TRAILER:
				return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false));
			default:
				return null;
		}
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof DetailViewHolder){
			Movie movie = (Movie) items.get(position);
			((DetailViewHolder) holder).mTextView.setText(movie.getTitle());
		}else if(holder instanceof ReviewViewHolder){
			Review review = (Review) items.get(position);
			((ReviewViewHolder) holder).mTextView.setText(review.getContent().substring(0, 50));
		}else if(holder instanceof TrailerViewHolder){
			Trailer trailer = (Trailer) items.get(position);
			((TrailerViewHolder) holder).mTextView.setText(trailer.getName());
		}
	}

	@Override
	public int getItemViewType(int position) {
		return items.get(position).getDisplayType();
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	class DetailViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		TextView mTextView;

		DetailViewHolder(View v) {
			super(v);
			mTextView = v.findViewById(R.id.item_detail_text);
		}
	}

	class ReviewViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		TextView mTextView;

		ReviewViewHolder(View v) {
			super(v);
			mTextView = v.findViewById(R.id.review_title);
		}
	}

	class TrailerViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		TextView mTextView;

		TrailerViewHolder(View v) {
			super(v);
			mTextView = v.findViewById(R.id.item_trailer_text);
		}
	}
}