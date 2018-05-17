package com.arctouch.codechallenge;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.base.MovieListReceiver;
import com.arctouch.codechallenge.dao.GenreDao;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;
import com.arctouch.codechallenge.util.NetworkObserver;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Controller extends Application{

    private static Controller instance;
    private MovieListReceiver movieListReceiver;
    public final NetworkObserver networkObserver = new NetworkObserver();

    public TmdbApi api;

    public GenreDao genreDao;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        api = new Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TmdbApi.class);

        genreDao = new GenreDao(this);
    }

    public static synchronized Controller getInstance() {
        return instance;
    }

    public void setMovieListReceiver (MovieListReceiver movieListReceiver){
        this.movieListReceiver = movieListReceiver;
    }

    @SuppressLint("CheckResult")
    public void getInitialData(String requestKey) {
        if (networkObserver.isNetworkAvailable(this)) {
            movieListReceiver.hideLog();

            api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        Cache.setGenres(response.genres);
                        genreDao.insertRegisterList(response.genres);
                        getUpcomingMovies(1, requestKey);
                    });
        } else {
            Cache.setGenres(genreDao.getAllRegisters());
            Toast.makeText(instance, Cache.getGenres().get(0).name, Toast.LENGTH_SHORT).show();
            movieListReceiver.displayLog(getResources().getStringArray(R.array.warning)[0]);
        }
    }

    @SuppressLint("CheckResult")
    public void getUpcomingMovies(Integer page, String requestKey){

        if (networkObserver.isNetworkAvailable(this)) {
            movieListReceiver.hideLog();
            api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, Long.valueOf(page), TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> returnDataToList(response, requestKey));
        } else
            movieListReceiver.displayLog(getResources().getStringArray(R.array.warning)[0]);
    }

    @SuppressLint("CheckResult")
    public void getMovieByQuery(String query, Integer page, String requestKey){

        if (networkObserver.isNetworkAvailable(this)) {
            movieListReceiver.hideLog();
            api.searchMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, query, Long.valueOf(page), TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> returnDataToList(response, requestKey));
        } else
            movieListReceiver.displayLog(getResources().getStringArray(R.array.warning)[0]);
    }

    private void returnDataToList(UpcomingMoviesResponse response, String requestKey){
        for (Movie movie : response.results) {
            movie.genres = new ArrayList<>();

            for (Genre genre : Cache.getGenres())
                if (movie.genreIds.contains(genre.id))
                    movie.genres.add(genre);
        }

        movieListReceiver.updateList(response.results, response.totalPages, response.page, requestKey);
    }
}
