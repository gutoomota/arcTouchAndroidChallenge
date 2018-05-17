package com.arctouch.codechallenge.model;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Movie extends RealmObject implements Serializable {
    @PrimaryKey
    public int id;
    public String title;
    public String overview;
    public boolean favorite = false;

    public String genres;
    @Ignore
    @Json(name = "genre_ids")
    public List<Integer> genreIds;
    @Json(name = "poster_path")
    public String posterPath;
    @Json(name = "backdrop_path")
    public String backdropPath;
    @Json(name = "release_date")
    public String releaseDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        if (id != movie.id) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (overview != null ? !overview.equals(movie.overview) : movie.overview != null)
            return false;
        if (genres != null ? !genres.equals(movie.genres) : movie.genres != null) return false;
        if (genreIds != null ? !genreIds.equals(movie.genreIds) : movie.genreIds != null)
            return false;
        if (posterPath != null ? !posterPath.equals(movie.posterPath) : movie.posterPath != null)
            return false;
        if (backdropPath != null ? !backdropPath.equals(movie.backdropPath) : movie.backdropPath != null)
            return false;
        return releaseDate != null ? releaseDate.equals(movie.releaseDate) : movie.releaseDate == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        result = 31 * result + (genres != null ? genres.hashCode() : 0);
        result = 31 * result + (genreIds != null ? genreIds.hashCode() : 0);
        result = 31 * result + (posterPath != null ? posterPath.hashCode() : 0);
        result = 31 * result + (backdropPath != null ? backdropPath.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", genres=" + genres +
                ", genreIds=" + genreIds +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    public Movie() {}

    public Movie (int id, String title, String overview, boolean favorite,
                  String genres, String posterPath,
                  String backdropPath, String releaseDate){
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.favorite = favorite;
        this.genres = genres;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
    }

    public Movie copy() {
        return new Movie(id, title, overview, favorite, genres, posterPath, backdropPath, releaseDate);
    }
}
