package com.arctouch.codechallenge.dao;

import android.content.Context;

import com.arctouch.codechallenge.base.DaoAbstract;
import com.arctouch.codechallenge.model.Genre;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Guto on 03/03/2017.
 */

public class GenreDao extends DaoAbstract {

    public GenreDao(Context context) {
        super(context);
    }

    public Genre getRegister(int id) {
        return realm.where(Genre.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Genre> getAllRegisters() {
        return realm.where(Genre.class).findAll();
    }

    public boolean insertRegister(Genre genre) {
        realm.beginTransaction();
        realm.copyToRealm(genre);
        realm.commitTransaction();
        return true;
    }

    public void insertRegisterList(List<Genre> genres) {
        realm.beginTransaction();

        for (Genre g: genres)
            if (getRegister(g.id) == null)
                realm.copyToRealm(g);

        realm.commitTransaction();
    }

    public void deleteRegister(Genre genre) {
        realm.beginTransaction();
        genre.deleteFromRealm();
        realm.commitTransaction();
    }
}