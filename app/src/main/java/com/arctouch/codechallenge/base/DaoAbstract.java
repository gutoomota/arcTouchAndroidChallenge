package com.arctouch.codechallenge.base;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public abstract class DaoAbstract {

    protected Realm realm;
    //RealmMigration migration = new MigrationRealm();

    public DaoAbstract(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                //.migration(migration)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);
    }
}