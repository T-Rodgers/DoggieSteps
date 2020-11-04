package com.tdr.app.doggiesteps.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Query("UPDATE my_pets SET numOfSteps = :num_steps WHERE petId = :id")
    void updateSteps(int id, int num_steps);
}
