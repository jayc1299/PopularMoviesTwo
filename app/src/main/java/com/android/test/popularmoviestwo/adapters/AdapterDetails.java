package com.android.test.popularmoviestwo.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.objects.DetailDisplay;
import com.android.test.popularmoviestwo.objects.HeaderObject;
import com.android.test.popularmoviestwo.objects.Movie;
import com.android.test.popularmoviestwo.objects.Review;
import com.android.test.popularmoviestwo.objects.Trailer;

import java.util.ArrayList;

public class AdapterDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	public interface IAdapterDetailItemClicked{
		void onTrailerClicked(String key);
		void onReviewClicked(Review review);
	}

	public static final int DISPLAY_TYPE_HEADER = 1;
	public static final int DISPLAY_TYPE_REVIEW = 2;
	public static final int DISPLAY_TYPE_TRAILER = 3;

	private ArrayList<DetailDisplay> items;
	private IAdapterDetailItemClicked listener;

	public AdapterDetails(ArrayList<DetailDisplay> items, IAdapterDetailItemClicked listener) {
		this.items = items;
		this.listener = listener;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		switch (viewType){
			case DISPLAY_TYPE_HEADER:
				return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
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
		if(holder instanceof HeaderViewHolder){
			HeaderObject headerObject = (HeaderObject) items.get(position);
			((HeaderViewHolder) holder).mTextView.setText(headerObject.getHeader());
		}else if(holder instanceof ReviewViewHolder){
			final Review review = (Review) items.get(position);
			if(review != null && review.getContent() != null && review.getContent().length() > 50) {
				((ReviewViewHolder) holder).mTextView.setText(review.getContent().substring(0, 50));
			}else{
				((ReviewViewHolder) holder).mTextView.setText(review.getContent());
			}
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(listener != null){
						listener.onReviewClicked(review);
					}
				}
			});
		}else if(holder instanceof TrailerViewHolder){
			final Trailer trailer = (Trailer) items.get(position);
			((TrailerViewHolder) holder).mTextView.setText(trailer.getName());
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(listener != null){
						listener.onTrailerClicked(trailer.getKey());
					}
				}
			});
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

	class HeaderViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		TextView mTextView;

		HeaderViewHolder(View v) {
			super(v);
			mTextView = v.findViewById(R.id.item_header_text);
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