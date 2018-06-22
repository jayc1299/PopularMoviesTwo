package com.android.test.popularmoviestwo.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.objects.Detail;

import java.util.ArrayList;

public class AdapterDetails extends RecyclerView.Adapter<AdapterDetails.ViewHolder>{

	private ArrayList<Detail> reviews;

	public AdapterDetails(ArrayList<Detail> reviews) {
		this.reviews = reviews;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.mTextView.setText(reviews.get(position).getTitle());
	}

	@Override
	public int getItemCount() {
		return reviews.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		TextView mTextView;

		ViewHolder(View v) {
			super(v);
			mTextView = v.findViewById(R.id.item_detail_text);
		}
	}
}