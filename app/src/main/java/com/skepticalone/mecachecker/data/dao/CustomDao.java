package com.skepticalone.mecachecker.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.db.AppDatabase;

@Dao
abstract class CustomDao<Entity> {
    @NonNull
    private final AppDatabase database;
    CustomDao(@NonNull AppDatabase database) {
        this.database = database;
    }
    @NonNull
    final AppDatabase getDatabase() {
        return database;
    }

    abstract LiveData<Entity> getItem(long id);

}
