package com.tdr.app.doggiesteps.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tdr.app.doggiesteps.model.Dog;

import java.util.List;

@Dao
public interface DogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Dog dog);

    @Query("DELETE FROM my_pets")
    void deleteAll();

    @Query("SELECT * FROM my_pets ORDER BY petName ASC")
    LiveData<List<Dog>> getAllDogs();

    @Delete
    void delete(Dog dog);
}
