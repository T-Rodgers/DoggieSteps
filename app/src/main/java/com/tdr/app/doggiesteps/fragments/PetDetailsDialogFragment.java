package com.tdr.app.doggiesteps.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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

public class PetDetailsDialogFragment extends DialogFragment implements SensorEventListener, StepListener {

    private static final String TAG = PetDetailsDialogFragment.class.getSimpleName();

    private SensorManager sensorManager;
    private StepDetector stepDetector;
    private Sensor sensor;
    private int numOfSteps;


    private Dog dog;
    private Favorite favorite;
    private int petId;
    private String photoPath;
    private String favoritePetName;

    @BindView(R.id.dialog_favorite_button)
    ToggleButton favoriteButton;
    @BindView(R.id.walk_button)
    MaterialButton takeWalkButton;
    @BindView(R.id.stop_button)
    MaterialButton stopButton;
    @BindView(R.id.dialog_pet_image)
    ImageView dialogPetImage;
    @BindView(R.id.dialog_pet_name)
    TextView dialogPetName;
    @BindView(R.id.dialog_pet_breed)
    TextView dialogPetBreed;
    @BindView(R.id.dialog_pet_age)
    TextView dialogPetAge;
    @BindView(R.id.dialog_pet_bio)
    TextView dialogPetBio;
    @BindView(R.id.steps_text_view)
    TextView stepsTextView;

    private DogDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");

        Bundle args = getArguments();
        if (args != null) {
            dog = args.getParcelable(Constants.EXTRA_SELECTED_PET);
            if (dog != null) {
                petId = dog.getPetId();
                photoPath = dog.getPhotoPath();
                favoritePetName = dog.getPetName();
            }
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");

        View rootView = inflater.inflate(R.layout.fragment_details_dialog, container, false);
        ButterKnife.bind(this, rootView);

        database = DogDatabase.getInstance(getContext());

        favorite = new Favorite(petId, photoPath, favoritePetName);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);

        favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    addToFavorites();
                } else {
                    removeFromFavorites();
                }

            }
        });

        takeWalkButton.setOnClickListener(v -> {
            numOfSteps = 0;

            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

        });

        stopButton.setOnClickListener(v -> {
            sensorManager.unregisterListener(this);
        });

        setPetData();
        initiateViewModel();

        return rootView;
    }

    public void setPetData() {
        String dogPhotoPath = dog.getPhotoPath();
        Glide.with(this)
                .load(dogPhotoPath)
                .circleCrop()
                .error(R.drawable.ic_action_pet_favorites)
                .into(dialogPetImage);
        dialogPetName.setText(dog.getPetName());
        dialogPetBreed.setText(dog.getBreed());
        dialogPetAge.setText(dog.getAge());
        dialogPetBio.setText(dog.getPetBio());
    }

    public void addToFavorites() {

        favorite = new Favorite(petId, photoPath, favoritePetName);
        AppExecutors.getInstance().diskIO().execute(() -> database.favoriteDao().insert(favorite));
    }

    public void removeFromFavorites() {
        AppExecutors.getInstance().diskIO().execute(() -> database.favoriteDao().delete(favorite));
        Toast.makeText(
                getContext(),
                dog.getPetName() + " has been removed from Favorites",
                Toast.LENGTH_SHORT).show();
    }

    private void initiateViewModel() {
        Log.d(TAG, "initiateViewModel: ");
        FavoritesViewModelFactory factory = new FavoritesViewModelFactory(database, favorite.getId());
        FavoritesViewModel viewModel = new ViewModelProvider(this, factory).get(FavoritesViewModel.class);
        viewModel.getFavorite().observe(getViewLifecycleOwner(), new Observer<Favorite>() {
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
        stepsTextView.setText(String.valueOf(numOfSteps));
    }
}
