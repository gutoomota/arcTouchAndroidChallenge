package com.arctouch.codechallenge.view.activities;

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

public class FavoriteListActivity extends AppCompatActivity {

    private Controller controller;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<Movie> movies;
    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        controller = (Controller)getApplication();

        EditText etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);


        etSearch.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        movies = new ArrayList<>();
        movies.addAll(controller.movieDao.getAllRegisters());
        homeAdapter = new HomeAdapter(this.movies, this);
        recyclerView.setAdapter(homeAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorites:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
