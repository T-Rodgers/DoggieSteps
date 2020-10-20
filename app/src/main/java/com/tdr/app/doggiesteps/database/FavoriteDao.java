package com.tdr.app.doggiesteps.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tdr.app.doggiesteps.model.Favorite;

import java.util.List;

@Dao
public interface FavoriteDao {


    @Query("SELECT * FROM favorites ORDER BY id ASC")
    LiveData<List<Favorite>> getAllFavorites();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("SELECT * FROM favorites WHERE id = :id")
    LiveData<Favorite> getFavoriteById(int id);
}
