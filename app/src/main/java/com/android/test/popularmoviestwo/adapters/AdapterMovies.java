package com.android.test.popularmoviestwo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.R;
import com.android.test.popularmoviestwo.objects.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMovies extends RecyclerView.Adapter<AdapterMovies.MovieViewHolder> {

	private List<Movie> mMovies;
	private Context mContext;
	private MovieApi mApi;
	private IMovieClickedListener listener;

	public AdapterMovies(Context context, IMovieClickedListener listener, List<Movie> movies) {
		this.listener = listener;
		mMovies = movies;
		mContext = context;
		mApi = new MovieApi(mContext);
	}

	public interface IMovieClickedListener {
		void onMovieClickedListener(Movie movie, View view);
	}

	@NonNull
	@Override
	public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new MovieViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
		String path = mApi.getImgUrl(mMovies.get(position).getPosterPath(), false);
		Picasso.with(mContext).load(path).into(holder.movieImageView);
		holder.movieImageView.setContentDescription(mMovies.get(position).getTitle());
	}

	@Override
	public int getItemCount() {
		return mMovies.size();
	}

	public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		ImageView movieImageView;

		MovieViewHolder(View itemView) {
			super(itemView);
			movieImageView = itemView.findViewById(R.id.item_movie_image);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (listener != null) {
				listener.onMovieClickedListener(mMovies.get(getAdapterPosition()), view);
			}
		}
	}
}