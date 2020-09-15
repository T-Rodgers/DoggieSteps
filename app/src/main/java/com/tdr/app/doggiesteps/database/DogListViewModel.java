package com.tdr.app.doggiesteps.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tdr.app.doggiesteps.model.Dog;

import java.util.List;

public class DogListViewModel extends AndroidViewModel {


    private DogRepository mRepository;
    private LiveData<List<Dog>> mAllDogs;

    public DogListViewModel(Application application) {
        super(application);
        mRepository = new DogRepository(application);
        mAllDogs = mRepository.getAllDogs();
    }

    public LiveData<List<Dog>> getAllDogs() {
        return mAllDogs;
    }

    public void insert(Dog dog) {
        mRepository.insert(dog);
    }

    public void delete(Dog dog) {
        mRepository.delete(dog);
    }

}
