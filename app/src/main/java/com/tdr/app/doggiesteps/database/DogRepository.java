package com.tdr.app.doggiesteps.database;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

import com.tdr.app.doggiesteps.model.Dog;

public class DogRepository {

    private DogDao mDogDao;
    private LiveData<List<Dog>> mAllDogs;

    DogRepository(Application application) {
        DogDatabase db = DogDatabase.getInstance(application);
        mDogDao = db.dogDao();
        mAllDogs = mDogDao.getAllDogs();
    }

    LiveData<List<Dog>> getAllDogs() {
        return mAllDogs;
    }

    public void insert(Dog dog) {
        new insertAsyncTask(mDogDao).execute(dog);
    }

    public void delete(Dog dog) {
        new deleteDogAsyncTask(mDogDao).execute(dog);
    }

    private static class deleteDogAsyncTask extends AsyncTask<Dog, Void, Void> {

        private DogDao mAsyncTaskDao;

        deleteDogAsyncTask(DogDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Dog... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }



    private static class insertAsyncTask extends AsyncTask<Dog, Void, Void> {

        private DogDao mAsyncTaskDao;

        insertAsyncTask(DogDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Dog... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllDogsAsyncTask extends AsyncTask<Void, Void, Void> {

        private DogDao mAsyncTaskDao;

        deleteAllDogsAsyncTask(DogDao dao) {

            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void deleteAll() {

        new deleteAllDogsAsyncTask(mDogDao).execute();
    }


}

