package com.tdr.app.doggiesteps.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tdr.app.doggiesteps.model.Favorite;

public class FavoritesViewModel extends ViewModel {

    private LiveData<Favorite> favorite;

    public FavoritesViewModel(DogDatabase database, int favoriteId) {
        favorite = database.favoriteDao().getFavoriteById(favoriteId);
    }

    public LiveData<Favorite> getFavorite() {
        return favorite;
    }
}



