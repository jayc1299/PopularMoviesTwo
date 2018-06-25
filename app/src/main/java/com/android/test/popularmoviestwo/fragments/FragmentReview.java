package com.android.test.popularmoviestwo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.activities.ActivityReview;

public class FragmentReview extends Fragment{

	private TextView mAuthor;
	private TextView mReview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_review, container, false);

		mAuthor = view.findViewById(R.id.fragment_review_author);
		mReview = view.findViewById(R.id.fragment_review_review);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Intent intent = getActivity().getIntent();
		if(intent != null && intent.getExtras() != null){
			mAuthor.setText(intent.getStringExtra(ActivityReview.TAG_AUTHOR));
			mReview.setText(intent.getStringExtra(ActivityReview.TAG_REVIEW));
		}
	}
}
