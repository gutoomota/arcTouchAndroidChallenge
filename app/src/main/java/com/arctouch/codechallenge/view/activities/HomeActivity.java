package com.arctouch.codechallenge.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arctouch.codechallenge.Controller;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.base.MovieListReceiver;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.RequestKeyBuilder;
import com.arctouch.codechallenge.view.adapters.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MovieListReceiver {

    private Controller controller;
    private final RequestKeyBuilder requestKeyBuilder = new RequestKeyBuilder();

    private TextView tvLog;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ArrayList<Movie> movies;
    private HomeAdapter homeAdapter;

    private String requestKey;
    private Integer totalPages, currentPage;
    private final Integer keyLength = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        controller = (Controller)getApplication();
        controller.setMovieListReceiver(this);

        tvLog = findViewById(R.id.tvLog);
        EditText etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        movies = new ArrayList<>();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = String.valueOf(charSequence);
                requestKey = requestKeyBuilder.generateRandomKey(keyLength);

                totalPages = currentPage = 1;
                homeAdapter = null;
                movies = new ArrayList<>();

                if ((query == null) || (query.isEmpty()))
                    requestMoviePage();
                else {
                    progressBar.setVisibility(View.VISIBLE);

                    controller.getMovieByQuery(query, currentPage, requestKey);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                requestKey = requestKeyBuilder.generateRandomKey(keyLength);

                if (!recyclerView.canScrollVertically(1)) {
                    currentPage++;
                    requestMoviePage();
                }
            }
        });

        requestKey = requestKeyBuilder.generateRandomKey(keyLength);
        controller.getInitialData(requestKey);

    }

    private void requestMoviePage(){
        assert (currentPage != null) && (totalPages != null);
        if (currentPage <= totalPages) {
            progressBar.setVisibility(View.VISIBLE);

            controller.getUpcomingMovies(currentPage, requestKey);
        }
    }

    @Override
    public void updateList(List<Movie> movies, int totalPages, int currentPage, String requestKey) {

        if (this.requestKey.equals(requestKey)) {
            this.movies.addAll(movies);
            this.totalPages = totalPages;
            this.currentPage = currentPage;

            if (homeAdapter == null) {
                homeAdapter = new HomeAdapter(this.movies, this);
                recyclerView.setAdapter(homeAdapter);
            } else
                homeAdapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayLog(String log) {
        tvLog.setVisibility(View.VISIBLE);
        tvLog.setText(log);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideLog() {
        tvLog.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (controller.networkObserver.isNetworkAvailable(controller)) {
            if (homeAdapter == null)
                controller.getInitialData(requestKey);

            hideLog();
        }else
            displayLog(getResources().getStringArray(R.array.warning)[0]);
    }
}
