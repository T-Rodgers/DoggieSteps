package com.tdr.app.doggiesteps.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdr.app.doggiesteps.model.Favorite;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private LiveData<List<Favorite>> mAllFavorites;

    public FavoritesViewModel(Application application) {
        super(application);
        DogDatabase database = DogDatabase.getInstance(this.getApplication());
        mAllFavorites = database.favoriteDao().getAllFavorites();
    }

    public LiveData<List<Favorite>> getFavorites() {
        return mAllFavorites;
    }
}
