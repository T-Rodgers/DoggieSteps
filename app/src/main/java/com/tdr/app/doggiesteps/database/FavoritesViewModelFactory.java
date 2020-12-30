package com.tdr.app.doggiesteps.database;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FavoritesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final DogDatabase mDb;
    private final int mFavoriteId;

    public FavoritesViewModelFactory(DogDatabase database, int favoriteId) {
        mDb = database;
        mFavoriteId = favoriteId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoritesViewModel(mDb, mFavoriteId);
    }
}
