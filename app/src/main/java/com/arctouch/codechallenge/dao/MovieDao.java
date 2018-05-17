package com.arctouch.codechallenge.dao;

import android.content.Context;
import android.widget.Toast;

import com.arctouch.codechallenge.Controller;
import com.arctouch.codechallenge.base.DaoAbstract;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Guto on 03/03/2017.
 */

public class MovieDao extends DaoAbstract {

    public MovieDao(Context context) {
        super(context);
    }

    public Movie getRegister(int id) {
        return realm.where(Movie.class).equalTo("id", id).findFirst();
    }

    public List<Movie> getAllRegisters() {
        RealmResults<Movie> moviesRealm = realm.where(Movie.class).findAll();
        List<Movie> movies = new ArrayList<>();

        for (Movie m: moviesRealm)
            movies.add(m.copy());

        return movies;

    }

    public void insertRegister(Movie movie) {
        if (getRegister(movie.id) == null) {
            realm.beginTransaction();
            realm.copyToRealm(movie);
            realm.commitTransaction();
        }
    }

    public void insertRegisterList(List<Movie> movies) {
        realm.beginTransaction();

        for (Movie m: movies)
            if (getRegister(m.id) == null)
                realm.copyToRealm(m);

        realm.commitTransaction();
    }

    public void deleteRegister(int id) {
        realm.beginTransaction();
        getRegister(id).deleteFromRealm();
        realm.commitTransaction();
    }
}