package com.arctouch.codechallenge.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.Controller;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.base.UILogHandler;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailsActivity extends AppCompatActivity implements UILogHandler{

    private Controller controller;
    private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

    private TextView tvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        controller = (Controller)getApplication();

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra("movie");

        tvLog = findViewById(R.id.tvLog);
        ImageView ivBackdropImage = findViewById(R.id.ivBackdropImage);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvRelease = findViewById(R.id.tvRelease);
        TextView tvGenre = findViewById(R.id.tvGenre);
        TextView tvOverview = findViewById(R.id.tvOverview);
        ImageView ivPosterImage = findViewById(R.id.ivPosterImage);

        tvTitle.setText(movie.title);
        tvRelease.setText(movie.releaseDate);
        tvGenre.setText(TextUtils.join(", ", movie.genres));
        tvOverview.setText(movie.overview);

        String backdropPath = movie.backdropPath;
        if (!TextUtils.isEmpty(backdropPath)) {
            Glide.with(this)
                    .load(movieImageUrlBuilder.buildBackdropUrl(backdropPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(ivBackdropImage);
        }

        String posterPath = movie.posterPath;
        if(!TextUtils.isEmpty(posterPath)) {
            Glide.with(this)
                    .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(ivPosterImage);
        }


    }

    @Override
    public void displayLog(String log) {
        tvLog.setVisibility(View.VISIBLE);
        tvLog.setText(log);
    }

    @Override
    public void hideLog() {
        tvLog.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!controller.networkObserver.isNetworkAvailable(controller))
            displayLog(getResources().getStringArray(R.array.warning)[0]);
    }
}
