package com.arctouch.codechallenge.api;

import com.arctouch.codechallenge.Controller;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.GenreResponse;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbApi {

    String URL = Controller.getInstance().getResources().getString(R.string.url);
    String DEFAULT_LANGUAGE = Controller.getInstance().getResources().getString(R.string.default_language);
    String DEFAULT_REGION = Controller.getInstance().getResources().getString(R.string.default_region);

    //API_KEY is Distributed on string_array to make it harder to reassemble in case of a reverse engineering on the .ap;k file
    String API_KEY =
                    Controller.getInstance().getResources().getStringArray(R.array.api_key)[6] +
                    Controller.getInstance().getResources().getStringArray(R.array.api_key)[2] +
                    Controller.getInstance().getResources().getStringArray(R.array.api_key)[7] +
                    Controller.getInstance().getResources().getStringArray(R.array.api_key)[1] +
                    Controller.getInstance().getResources().getStringArray(R.array.api_key)[4];

    @GET("genre/movie/list")
    Observable<GenreResponse> genres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/upcoming")
    Observable<UpcomingMoviesResponse> upcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") Long page,
            @Query("region") String region
    );

    @GET("search/movie")
    Observable<UpcomingMoviesResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") Long page,
            @Query("region") String region
    );
}
