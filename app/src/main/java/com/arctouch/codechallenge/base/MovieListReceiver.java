package com.arctouch.codechallenge.base;

import com.arctouch.codechallenge.model.Movie;
import java.util.List;

public interface MovieListReceiver extends UILogHandler{

    void updateList(List<Movie> movies, int totalPages, int currentPage, String requestKey);
}
