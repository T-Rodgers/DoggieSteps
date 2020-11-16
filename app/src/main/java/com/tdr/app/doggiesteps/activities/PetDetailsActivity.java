package com.tdr.app.doggiesteps.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.database.DogDatabase;
import com.tdr.app.doggiesteps.database.FavoritesViewModel;
import com.tdr.app.doggiesteps.database.FavoritesViewModelFactory;
import com.tdr.app.doggiesteps.interfaces.StepListener;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.model.Favorite;
import com.tdr.app.doggiesteps.utils.AppExecutors;
import com.tdr.app.doggiesteps.utils.Constants;
import com.tdr.app.doggiesteps.utils.StepDetector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetDetailsActivity extends AppCompatActivity implements SensorEventListener, StepListener {

    private static final String TAG = PetDetailsActivity.class.getSimpleName();

    private SensorManager sensorManager;
    private StepDetector stepDetector;
    private Sensor sensor;
    private int numOfSteps;
    private int totalSteps;


    private int existingSteps;
    private Dog dog;
    private Favorite favorite;
    private int petId;
    private String photoPath;
    private String favoritePetName;

    @BindView(R.id.details_favorite_button)
    ToggleButton favoriteButton;
    @BindView(R.id.details_walk_button)
    MaterialButton takeWalkButton;
    @BindView(R.id.details_stop_button)
    MaterialButton stopButton;
    @BindView(R.id.details_pet_image)
    ImageView detailsPetImage;
    @BindView(R.id.details_pet_name)
    TextView detailsPetName;
    @BindView(R.id.details_pet_breed)
    TextView detailsPetBreed;
    @BindView(R.id.details_pet_age)
    TextView detailsPetAge;
    @BindView(R.id.details_pet_bio)
    TextView detailsPetBio;
    @BindView(R.id.steps_text_view)
    TextView stepsTextView;
    @BindView(R.id.total_steps_text_view)
    TextView totalStepsTextView;

    private DogDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);
        ButterKnife.bind(this);

        Intent petData = getIntent();
        if (petData != null) {
            dog = petData.getParcelableExtra(Constants.EXTRA_SELECTED_PET);
            petId = dog.getPetId();
            photoPath = dog.getPhotoPath();
            favoritePetName = dog.getPetName();
        }

        database = DogDatabase.getInstance(this);
        favorite = new Favorite(petId, photoPath, favoritePetName);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);

        favoriteButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                addToFavorites();
            } else {
                removeFromFavorites();
            }
        });

        takeWalkButton.setOnClickListener(v -> {
            numOfSteps = existingSteps;
            String stepCount = getString(R.string.steps_count_format, String.valueOf(numOfSteps));
            stepsTextView.setText(stepCount);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

            stopButton.setEnabled(true);

        });

        stopButton.setOnClickListener(v -> {
            totalSteps = dog.getNumOfSteps();
            totalSteps = totalSteps + numOfSteps;
            dog.setNumOfSteps(totalSteps);
            String allTimeTotalSteps = getResources().getString(
                    R.string.all_time_total_format,
                    String.valueOf(totalSteps));
            totalStepsTextView.setText(allTimeTotalSteps);
            stepsTextView.setText(R.string.steps_label_text);
            sensorManager.unregisterListener(this);

            stopButton.setEnabled(false);

            AppExecutors.getInstance().diskIO().execute(() -> database.dogDao().updateSteps(dog.getPetId(), dog.getNumOfSteps()));
        });

        setPetData(dog);
        initiateViewModel();
    }

    public void setPetData(Dog dog) {
        String dogPhotoPath = dog.getPhotoPath();
        Glide.with(this)
                .load(dogPhotoPath)
                .centerCrop()
                .error(R.drawable.ic_action_pet_favorites)
                .into(detailsPetImage);
        detailsPetImage.setContentDescription(
                getString(R.string.details_image_content_description, dog.getPetName()));
        detailsPetName.setText(dog.getPetName());
        detailsPetBreed.setText(dog.getBreed());
        detailsPetAge.setText(dog.getAge());
        detailsPetBio.setText(dog.getPetBio());
        totalStepsTextView.setText(getResources().getString(
                R.string.all_time_total_format,
                String.valueOf(dog.getNumOfSteps())));
    }

    public void addToFavorites() {

        favorite = new Favorite(petId, photoPath, favoritePetName);
        AppExecutors.getInstance().diskIO().execute(() -> database.favoriteDao().insert(favorite));
    }

    public void removeFromFavorites() {
        AppExecutors.getInstance().diskIO().execute(() -> database.favoriteDao().delete(favorite));
        Toast.makeText(
                this,
                dog.getPetName() + " has been removed from Favorites",
                Toast.LENGTH_SHORT).show();
    }

    private void initiateViewModel() {
        FavoritesViewModelFactory factory = new FavoritesViewModelFactory(database, favorite.getId());
        FavoritesViewModel viewModel = new ViewModelProvider(this, factory).get(FavoritesViewModel.class);
        viewModel.getFavorite().observe(this, new Observer<Favorite>() {
            @Override
            public void onChanged(Favorite favoriteData) {
                viewModel.getFavorite().removeObserver(this);
                if (favoriteData == null) {
                    favoriteButton.setChecked(false);
                } else if ((favorite.getId() == dog.getPetId()) && !favoriteButton.isChecked()) {
                    favoriteData.setId(dog.getPetId());
                    favoriteButton.setChecked(true);
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            stepDetector.updateAccel(
                    event.timestamp,
                    event.values[0],
                    event.values[1],
                    event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void step(long timeNs) {
        numOfSteps++;
        String stepCount = getString(R.string.steps_count_format, String.valueOf(numOfSteps));
        stepsTextView.setText(stepCount);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("BUNDLE_ID", petId);
        outState.putInt("BUNDLE_STEPS", numOfSteps);
    }


}