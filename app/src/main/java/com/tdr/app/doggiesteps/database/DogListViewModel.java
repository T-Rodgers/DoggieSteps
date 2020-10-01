package com.tdr.app.doggiesteps.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdr.app.doggiesteps.model.Dog;

import java.util.List;

public class DogListViewModel extends AndroidViewModel {

    private LiveData<List<Dog>> mAllDogs;

    public DogListViewModel(Application application) {
        super(application);
        DogDatabase database = DogDatabase.getInstance(this.getApplication());
        mAllDogs = database.dogDao().getAllDogs();
    }

    public LiveData<List<Dog>> getDogs() {
        return mAllDogs;
    }
}
