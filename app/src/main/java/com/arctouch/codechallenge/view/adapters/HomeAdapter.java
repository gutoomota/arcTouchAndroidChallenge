package com.arctouch.codechallenge.view.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arctouch.codechallenge.Controller;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.arctouch.codechallenge.view.activities.DetailsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private AppCompatActivity activity;
    private List<Movie> movies;

    public HomeAdapter(List<Movie> movies, AppCompatActivity activity) {
        this.movies = movies;
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatActivity activity;

        private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

        private final RelativeLayout rlItem;
        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView releaseDateTextView;
        private final ImageView posterImageView;
        private final ImageView ivFavorite;

        public ViewHolder(View itemView, AppCompatActivity activity) {
            super(itemView);
            this.activity = activity;

            rlItem = itemView.findViewById(R.id.rlItem);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genresTextView = itemView.findViewById(R.id.genresTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }

        public void bind(Movie movie) {

            ivFavorite.setActivated(movie.favorite);

            ivFavorite.setOnClickListener(view -> {
                if (movie.favorite) {
                    Controller.getInstance().movieDao.deleteRegister(movie.id);
                    movie.favorite = false;
                } else {
                    movie.favorite = true;
                    Controller.getInstance().movieDao.insertRegister(movie);
                }

                ivFavorite.setActivated(movie.favorite);

            });

            rlItem.setOnClickListener(view -> {
                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("movie", movie);

                activity.startActivity(intent);
            });

            titleTextView.setText(movie.title);

            genresTextView.setText(movie.genres);

            releaseDateTextView.setText(movie.releaseDate);

            String posterPath = movie.posterPath;
            if (!TextUtils.isEmpty(posterPath)) {
                Glide.with(itemView)
                        .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(posterImageView);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view, activity);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }
}
