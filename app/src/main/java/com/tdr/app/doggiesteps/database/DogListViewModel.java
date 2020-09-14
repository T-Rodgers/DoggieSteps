package com.tdr.app.doggiesteps.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdr.app.doggiesteps.model.Dog;

import java.util.List;

public class DogListViewModel extends AndroidViewModel {

    private LiveData<List<Dog>> dogs;

    public DogListViewModel(@NonNull Application application) {
        super(application);
        DogDatabase database = DogDatabase.getInstance(this.getApplication());
        dogs = database.dogDao().getAllDogs();
    }

    public LiveData<List<Dog>> getDogs() {
        return dogs;
    }

}
