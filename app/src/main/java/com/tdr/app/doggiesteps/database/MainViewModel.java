package com.tdr.app.doggiesteps.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.model.Favorite;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final LiveData<List<Dog>> mAllDogs;
    private final LiveData<List<Favorite>> mAllFavorites;

    public MainViewModel(Application application) {
        super(application);
        DogDatabase database = DogDatabase.getInstance(this.getApplication());
        mAllDogs = database.dogDao().getAllDogs();
        mAllFavorites = database.favoriteDao().getAllFavorites();
    }

    public LiveData<List<Dog>> getDogs() {
        return mAllDogs;
    }

    public LiveData<List<Favorite>> getFavorites() {
        return mAllFavorites;
    }
}
