package com.tdr.app.doggiesteps.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tdr.app.doggiesteps.model.Favorite;

import java.util.List;

@Dao
public interface FavoriteDao {


    @Query("SELECT * FROM favorites ORDER BY id ASC")
    LiveData<List<Favorite>> getAllFavorites();

    @Insert
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);
}
