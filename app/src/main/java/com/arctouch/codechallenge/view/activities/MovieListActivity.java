package com.arctouch.codechallenge.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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

public class MovieListActivity extends AppCompatActivity implements MovieListReceiver {

    private Controller controller;
    private final RequestKeyBuilder requestKeyBuilder = new RequestKeyBuilder();

    private EditText etSearch;
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
        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = String.valueOf(charSequence);
                resetSearch();

                searchMovie(query);
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

                if (!recyclerView.canScrollVertically(1))  {
                    currentPage++;

                    String query = etSearch.getText().toString();
                    if (query.isEmpty())
                        requestMoviePage();
                    else
                        searchMovie(query);
                }
            }
        });
    }

    private void searchMovie(String query) {
        requestKey = requestKeyBuilder.generateRandomKey(keyLength);

        if ((query == null) || (query.isEmpty())) {
            resetSearch();
            requestMoviePage();
        } else {
            if (currentPage <= totalPages) {
                progressBar.setVisibility(View.VISIBLE);

                controller.getMovieByQuery(query, currentPage, requestKey);
            }
        }
    }

    private void requestMoviePage(){
        assert (currentPage != null) && (totalPages != null);
        if (currentPage <= totalPages) {
            progressBar.setVisibility(View.VISIBLE);

            controller.getUpcomingMovies(currentPage, requestKey);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorites:
                startActivity(new Intent(this, FavoriteListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            String query = etSearch.getText().toString();

            resetSearch();

            if (query.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);

                requestKey = requestKeyBuilder.generateRandomKey(keyLength);
                controller.getInitialData(requestKey);

                hideLog();
            } else {
                resetSearch();

                searchMovie(query);
            }

        }else
            displayLog(getResources().getStringArray(R.array.warning)[0]);
    }

    private void resetSearch(){
        totalPages = currentPage = 1;
        homeAdapter = null;
        movies = new ArrayList<>();
    }
}
