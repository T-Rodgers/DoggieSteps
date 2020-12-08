package com.tdr.app.doggiesteps.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.database.DogDatabase;
import com.tdr.app.doggiesteps.database.FavoritesViewModel;
import com.tdr.app.doggiesteps.database.FavoritesViewModelFactory;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.model.Favorite;
import com.tdr.app.doggiesteps.utils.AppExecutors;
import com.tdr.app.doggiesteps.utils.Constants;
import com.tdr.app.doggiesteps.utils.DogAppWidget;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tdr.app.doggiesteps.utils.Constants.BUNDLE_ACTIVE_STATE;
import static com.tdr.app.doggiesteps.utils.Constants.BUNDLE_ID;
import static com.tdr.app.doggiesteps.utils.Constants.BUNDLE_STEPS;
import static com.tdr.app.doggiesteps.utils.Constants.EXTRA_SELECTED_PET;
import static com.tdr.app.doggiesteps.utils.Constants.PREFERENCE_ID;
import static com.tdr.app.doggiesteps.utils.Constants.WIDGET_PET_NAME;
import static com.tdr.app.doggiesteps.utils.Constants.WIDGET_PHOTO_PATH;
import static com.tdr.app.doggiesteps.utils.Constants.WIDGET_TOTAL_STEPS;

public class PetDetailsActivity extends AppCompatActivity {

    private static final String TAG = PetDetailsActivity.class.getSimpleName();
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x6884;

    private int savedID;
    private int savedSteps;
    private int numOfSteps;
    private int totalSteps;
    private OnDataPointListener myStepCountListener;

    private Dog dog;
    private boolean isActive;
    private Favorite favorite;
    private int petId;
    private String photoPath;
    private String favoritePetName;
    private GoogleSignInAccount googleSignInAccount;

    @BindView(R.id.details_snackbar_view)
    View snackBarView;
    @BindView(R.id.add_widget_icon)
    ImageButton addWidgetIcon;
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
    @BindView(R.id.details_toolbar)
    MaterialToolbar toolbar;

    private SharedPreferences preferences;
    private DogDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        GoogleSignInOptionsExtension fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .build();

        googleSignInAccount = GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        if (!GoogleSignIn.hasPermissions(googleSignInAccount, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    googleSignInAccount,
                    fitnessOptions);
        }
        // TODO: IMPLEMENT SERVICE TO KEEP STEPS COUNTING IN BACKGROUND. FOREGROUND SERVICE.

        toolbar.setNavigationOnClickListener(v -> {
            if (isActive) {
                saveStepsAndId();

            }
            finish();
        });

        stopButton.setEnabled(false);

        Intent petData = getIntent();
        if (petData != null) {
            dog = petData.getParcelableExtra(EXTRA_SELECTED_PET);
            petId = dog.getPetId();
            photoPath = dog.getPhotoPath();
            favoritePetName = dog.getPetName();
        }

        savedID = preferences.getInt(BUNDLE_ID, 0);
        savedSteps = preferences.getInt(BUNDLE_STEPS, numOfSteps);
        isActive = preferences.getBoolean(BUNDLE_ACTIVE_STATE, false);
        if (savedID == petId && isActive) {
            numOfSteps = savedSteps;
            loadNumOfSteps();
        }

        setPetData(dog);

        database = DogDatabase.getInstance(this);
        favorite = new Favorite(petId, photoPath, favoritePetName);

        addWidgetIcon.setOnClickListener(v -> addToWidgets());

        favoriteButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                addToFavorites();
            } else {
                removeFromFavorites();
            }
        });

        takeWalkButton.setOnClickListener(v -> {
            registerSensorListener();
            isActive = true;
            preferences.edit()
                    .putBoolean(BUNDLE_ACTIVE_STATE, isActive)
                    .apply();
            stopButton.setEnabled(true);

        });

        stopButton.setOnClickListener(v -> {
            isActive = false;
            preferences.edit()
                    .putBoolean(BUNDLE_ACTIVE_STATE, isActive)
                    .apply();
            totalSteps = dog.getNumOfSteps();
            totalSteps = totalSteps + numOfSteps;
            numOfSteps = 0;
            dog.setNumOfSteps(totalSteps);
            totalStepsTextView.setText(String.valueOf(totalSteps));
            stopButton.setEnabled(false);
            unregisterSensorListener();
            AppExecutors.getInstance().diskIO().execute(() -> database.dogDao().updateSteps(dog.getPetId(), dog.getNumOfSteps()));
            stepsTextView.setText("");
        });


        initiateViewModel();
    }

    public void setPetData(Dog dog) {
        String dogPhotoPath = dog.getPhotoPath();
        Glide.with(this)
                .load(dogPhotoPath)
                .centerCrop()
                .error(R.drawable.ic_action_pet_favorites)
                .fallback(R.drawable.ic_action_pet_favorites)
                .into(detailsPetImage);
        detailsPetImage.setContentDescription(
                getString(R.string.details_image_content_description, dog.getPetName()));
        detailsPetName.setText(dog.getPetName());
        detailsPetBreed.setText(dog.getBreed());
        detailsPetAge.setText(dog.getAge());
        detailsPetBio.setText(dog.getPetBio());
        totalStepsTextView.setText(String.valueOf(dog.getNumOfSteps()));
    }

    public void addToFavorites() {

        favorite = new Favorite(petId, photoPath, favoritePetName);
        AppExecutors.getInstance().diskIO().execute(() -> database.favoriteDao().insert(favorite));
    }

    public void removeFromFavorites() {
        AppExecutors.getInstance().diskIO().execute(() -> database.favoriteDao().delete(favorite));
        Toast.makeText(
                this,
                dog.getPetName() + R.string.pet_removal_message,
                Toast.LENGTH_SHORT).show();
    }

    public void registerSensorListener() {
        stepsTextView.setText(String.valueOf(numOfSteps));
        myStepCountListener = dataPoint -> {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value value = dataPoint.getValue(field);
                int previousSteps = value.asInt();
                // Previous steps returned will be steps that are from last read. Therefore
                // We have to set them to "0" or else our initial value will be the total of all
                // prior steps from sensors.
                if (previousSteps > 0) {
                    previousSteps = 0;
                    previousSteps++;
                    Log.i(TAG, "Initial Step Count: " + previousSteps);
                }
                numOfSteps = numOfSteps + previousSteps;
                runOnUiThread(() -> stepsTextView.setText(String.valueOf(numOfSteps)));
            }
        };
        Fitness.getSensorsClient(this, googleSignInAccount)
                .add(
                        new SensorRequest.Builder()
                                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        myStepCountListener)
                .addOnSuccessListener(this, aVoid -> Log.i(TAG, getString(R.string.sensor_client_success)))
                .addOnFailureListener(e -> Log.e(TAG, getString(R.string.sensor_client_error), e));
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
    public void onBackPressed() {
        if (!stopButton.isEnabled()) {
            finish();
        } else {
            saveStepsAndId();
        }
        super.onBackPressed();

    }

    public void saveStepsAndId() {
        preferences.edit()
                .putInt(Constants.BUNDLE_STEPS, numOfSteps)
                .putInt(Constants.BUNDLE_ID, dog.getPetId())
                .apply();
    }

    public void loadNumOfSteps() {
        int savedSteps = preferences.getInt(Constants.BUNDLE_STEPS, numOfSteps);
        if (savedSteps == 0) {
            stepsTextView.setText("");
        } else {
            stepsTextView.setText(String.valueOf(savedSteps));
            registerSensorListener();
            stopButton.setEnabled(true);
        }
    }

    public void addToWidgets() {
        preferences.edit()
                .putInt(PREFERENCE_ID, dog.getPetId())
                .putString(WIDGET_PET_NAME, dog.getPetName())
                .putString(WIDGET_TOTAL_STEPS, String.valueOf(dog.getNumOfSteps()))
                .putInt("Fallback Photo", R.drawable.ic_action_pet_favorites)
                .putString(WIDGET_PHOTO_PATH, dog.getPhotoPath())
                .apply();

        ComponentName provider = new ComponentName(this, DogAppWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] ids = appWidgetManager.getAppWidgetIds(provider);
        DogAppWidget dogAppWidget = new DogAppWidget();
        dogAppWidget.onUpdate(this, appWidgetManager, ids);

        Snackbar.make(snackBarView,
                dog.getPetName() + getResources().getString(R.string.added_to_widgets_message),
                Snackbar.LENGTH_SHORT)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show();

    }

    private void unregisterSensorListener() {
        Fitness.getSensorsClient(this, googleSignInAccount)
                .remove(myStepCountListener)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, getString(R.string.unregistered_listener_message));
                    }
                });
    }

    @Override
    protected void onPause() {
        if (!stopButton.isEnabled()) {
            finish();
        } else {
            saveStepsAndId();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (savedID == petId && isActive) {
            numOfSteps = savedSteps;
            loadNumOfSteps();
        }
        super.onResume();
    }
}