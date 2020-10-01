package com.tdr.app.doggiesteps.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.model.Favorite;


@Database(entities = {Dog.class, Favorite.class}, version = 1, exportSchema = false)
public abstract class DogDatabase extends RoomDatabase {

    private static final String TAG = DogDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "my_pets";
    private static DogDatabase sInstance;

    public static DogDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DogDatabase.class, DogDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract DogDao dogDao();

    public abstract FavoriteDao favoriteDao();
}
