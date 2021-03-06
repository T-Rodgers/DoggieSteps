package com.tdr.app.doggiesteps.activities;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.database.DogDatabase;
import com.tdr.app.doggiesteps.database.FavoritesViewModel;
import com.tdr.app.doggiesteps.database.FavoritesViewModelFactory;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.model.Favorite;
import com.tdr.app.doggiesteps.services.StepCounterService;
import com.tdr.app.doggiesteps.utils.AppExecutors;
import com.tdr.app.doggiesteps.utils.Constants;
import com.tdr.app.doggiesteps.utils.CustomToastUtils;
import com.tdr.app.doggiesteps.utils.DogAppWidget;
import com.tdr.app.doggiesteps.utils.FitnessUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tdr.app.doggiesteps.utils.Constants.EXTRA_PET_DETAILS;
import static com.tdr.app.doggiesteps.utils.Constants.EXTRA_SAVED_PET;
import static com.tdr.app.doggiesteps.utils.Constants.EXTRA_SELECTED_PET;
import static com.tdr.app.doggiesteps.utils.Constants.NOTIFICATION_PET_NAME;
import static com.tdr.app.doggiesteps.utils.Constants.PREFERENCES_ACTIVE_STATE;
import static com.tdr.app.doggiesteps.utils.Constants.PREFERENCES_ID;
import static com.tdr.app.doggiesteps.utils.Constants.PREFERENCES_PET_NAME;
import static com.tdr.app.doggiesteps.utils.Constants.PREFERENCES_STEPS;
import static com.tdr.app.doggiesteps.utils.Constants.PREFERENCE_ID;
import static com.tdr.app.doggiesteps.utils.Constants.PREFERENCE_ISFAVORITED;
import static com.tdr.app.doggiesteps.utils.Constants.REQUEST_OAUTH_REQUEST_CODE;
import static com.tdr.app.doggiesteps.utils.Constants.UPDATE_PET_REQUEST_CODE;
import static com.tdr.app.doggiesteps.utils.Constants.WIDGET_NULL_PHOTO;
import static com.tdr.app.doggiesteps.utils.Constants.WIDGET_PET_NAME;
import static com.tdr.app.doggiesteps.utils.Constants.WIDGET_PHOTO_PATH;
import static com.tdr.app.doggiesteps.utils.Constants.WIDGET_TOTAL_STEPS;

public class PetDetailsActivity extends AppCompatActivity {

    private static final String TAG = PetDetailsActivity.class.getSimpleName();


    private int numOfSteps;
    private int resumedSteps;
    private int totalSteps;
    private OnDataPointListener myStepCountListener;

    private Dog dog;
    private boolean isActive;
    private Favorite favorite;
    private int petId;
    private String photoPath;
    private String favoritePetName;
    private GoogleSignInAccount googleSignInAccount;

    @BindView(R.id.edit_pet_icon)
    ImageButton editPetButton;
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
    private DogDatabase dogDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        detailsPetBio.setMovementMethod(new ScrollingMovementMethod());

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        dogDatabase = DogDatabase.getInstance(this);

        GoogleSignInOptionsExtension fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .build();

        googleSignInAccount = GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        stopButton.setEnabled(false);

        Intent petData = getIntent();
        if (petData != null) {
            dog = petData.getParcelableExtra(EXTRA_SELECTED_PET);
            if (dog != null) {
                petId = dog.getPetId();
                photoPath = dog.getPhotoPath();
                favoritePetName = dog.getPetName();
            }
        }

        int savedID = preferences.getInt(PREFERENCES_ID, 0);
        int savedSteps = preferences.getInt(PREFERENCES_STEPS, numOfSteps);
        isActive = preferences.getBoolean(PREFERENCES_ACTIVE_STATE, false);
        if (savedID == petId && isActive && isServiceRunning()) {
            registerSensorListener();
            numOfSteps = savedSteps;
            loadNumOfSteps();
        } else {
            numOfSteps = 0;
        }
        setPetData(dog);

        favorite = new Favorite(petId, photoPath, favoritePetName, dog.getNumOfSteps());

        addWidgetIcon.setOnClickListener(v -> addToWidgets());
        favoriteButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addToFavorites();
            } else {
                removeFromFavorites();
            }
        });

        editPetButton.setOnClickListener(v -> {
            if (isServiceRunning()) {
                CustomToastUtils.buildCustomToast(this, getString(R.string.edit_error_message));
            } else {
                preferences.edit()
                        .putBoolean(PREFERENCE_ISFAVORITED, favoriteButton.isChecked())
                        .apply();
                Intent editPetIntent = new Intent(PetDetailsActivity.this, PetEntryActivity.class);
                editPetIntent.putExtra(EXTRA_PET_DETAILS, dog);
                startActivityForResult(editPetIntent, UPDATE_PET_REQUEST_CODE);
            }
        });

        takeWalkButton.setOnClickListener(v -> {
            if (!GoogleSignIn.hasPermissions(googleSignInAccount, fitnessOptions)) {
                GoogleSignIn.requestPermissions(
                        this,
                        REQUEST_OAUTH_REQUEST_CODE,
                        googleSignInAccount,
                        fitnessOptions);

                CustomToastUtils.buildCustomToast(this, "Needs Access to read Steps");
            } else {

                startWalking();
            }
        });

        stopButton.setOnClickListener(v -> {
            isActive = false;
            preferences.edit()
                    .putBoolean(PREFERENCES_ACTIVE_STATE, isActive)
                    .apply();
            Intent intent = new Intent(this, StepCounterService.class);
            stopService(intent);
            totalSteps = dog.getNumOfSteps();
            totalSteps = totalSteps + numOfSteps;
            numOfSteps = 0;
            dog.setNumOfSteps(totalSteps);
            totalStepsTextView.setText(String.valueOf(totalSteps));
            takeWalkButton.setEnabled(true);
            stopButton.setEnabled(false);
            unregisterSensorListener();
            CustomToastUtils.buildCustomToast(this,
                    getString(R.string.finished_toast_message, dog.getPetName()));
            AppExecutors.getInstance().diskIO().execute(() -> {
                dogDatabase.dogDao().updateSteps(dog.getPetId(), dog.getNumOfSteps());
                dogDatabase.favoriteDao().updateSteps(favorite.getId(), totalSteps);

            });
            stepsTextView.setText(String.valueOf(0));
        });

        initiateViewModel();
    }

    private void startWalking() {
        if (isServiceRunning() && petId != preferences.getInt(PREFERENCES_ID, 0)) {
            CustomToastUtils.buildCustomToast(this,
                    getString(R.string.already_walking_toast_message,
                            preferences.getString(PREFERENCES_PET_NAME, "")));
        } else {
            Intent intent = new Intent(this, StepCounterService.class);
            intent.putExtra(NOTIFICATION_PET_NAME, dog.getPetName());
            preferences.edit()
                    .putInt(PREFERENCES_ID, dog.getPetId())
                    .putString(PREFERENCES_PET_NAME, dog.getPetName())
                    .apply();
            ContextCompat.startForegroundService(this, intent);
            registerSensorListener();
            takeWalkButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
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
        stepsTextView.setText(String.valueOf(numOfSteps));
        totalStepsTextView.setText(String.valueOf(dog.getNumOfSteps()));
    }

    public void addToFavorites() {
        favorite = new Favorite(petId, photoPath, favoritePetName, totalSteps);
        int totalFavoriteSteps = Integer.parseInt(totalStepsTextView.getText().toString());
        AppExecutors.getInstance().diskIO().execute(() -> dogDatabase.favoriteDao().insert(favorite));
        AppExecutors.getInstance().diskIO().execute(() -> dogDatabase.favoriteDao().updateSteps(petId, totalFavoriteSteps));
    }

    public void removeFromFavorites() {
        CustomToastUtils.buildCustomToast(this, getString(R.string.removed_from_favorites_message, favoritePetName));
        AppExecutors.getInstance().diskIO().execute(() -> dogDatabase.favoriteDao().delete(favorite));
    }

    public void registerSensorListener() {
        stepsTextView.setText(String.valueOf(numOfSteps));
        myStepCountListener = dataPoint -> {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value value = dataPoint.getValue(field);
                int steps = value.asInt();

                if (numOfSteps == 0) {
                    // Previous steps returned will be steps that are from last read. Therefore
                    // We have to set them to "0" or else our initial value will be the total of all
                    // prior steps from sensors.
                    steps = 0;
                    steps++;
                } else if (isActive) {
                    resumedSteps = numOfSteps + steps;
                    stepsTextView.setText(String.valueOf(resumedSteps));
                }
                numOfSteps = numOfSteps + steps;
                Log.i(TAG, "Number of Steps: " + numOfSteps);
                runOnUiThread(() -> stepsTextView.setText(String.valueOf(numOfSteps)));
            }
        };

        FitnessUtils.registerListener(this, googleSignInAccount, myStepCountListener);
        isActive = true;
        preferences.edit()
                .putBoolean(PREFERENCES_ACTIVE_STATE, isActive)
                .apply();
    }

    private void unregisterSensorListener() {
        FitnessUtils.unregisterListener(this, googleSignInAccount, myStepCountListener);
    }

    private void initiateViewModel() {
        FavoritesViewModelFactory factory = new FavoritesViewModelFactory(dogDatabase, favorite.getId());
        FavoritesViewModel viewModel = new ViewModelProvider(this, factory).get(FavoritesViewModel.class);
        viewModel.getFavorite().observe(this, new Observer<Favorite>() {
            @Override
            public void onChanged(Favorite favoriteData) {
                viewModel.getFavorite().removeObserver(this);
                if (favoriteData == null) {
                    favoriteButton.setChecked(false);
                } else
                    favoriteButton.setChecked((favorite.getId() == dog.getPetId()) && !favoriteButton.isChecked());
            }
        });
    }

    public void saveStepsAndId() {
        preferences.edit()
                .putBoolean(PREFERENCES_ACTIVE_STATE, isActive)
                .putInt(Constants.PREFERENCES_STEPS, numOfSteps)
                .putInt(Constants.PREFERENCES_ID, dog.getPetId())
                .apply();
    }

    public void loadNumOfSteps() {
        int savedSteps = preferences.getInt(Constants.PREFERENCES_STEPS, numOfSteps);
        stepsTextView.setText(String.valueOf(savedSteps));
        takeWalkButton.setEnabled(false);
        stopButton.setEnabled(true);

    }

    public void addToWidgets() {
        preferences.edit()
                .putInt(PREFERENCE_ID, dog.getPetId())
                .putString(WIDGET_PET_NAME, dog.getPetName())
                .putString(WIDGET_TOTAL_STEPS, String.valueOf(dog.getNumOfSteps()))
                .putInt(WIDGET_NULL_PHOTO, R.drawable.ic_action_pet_favorites)
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

    private boolean isServiceRunning() {
        boolean serviceRunning = false;
        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (StepCounterService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                serviceRunning = true;
            }
        }
        return serviceRunning;
    }

    @Override
    public void onBackPressed() {
        onPause();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (isActive && petId == preferences.getInt(PREFERENCES_ID, 0)) {
            saveStepsAndId();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (myStepCountListener != null) {
            unregisterSensorListener();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_OAUTH_REQUEST_CODE:
                    startWalking();
                    break;

                case UPDATE_PET_REQUEST_CODE:
                    if (data != null) {
                        Dog updatedDog = data.getParcelableExtra(EXTRA_SAVED_PET);
                        if (updatedDog != null) {
                            AppExecutors.getInstance().diskIO().execute(() ->
                                    dogDatabase.dogDao().insert(updatedDog));
                            dog = updatedDog;
                            setPetData(updatedDog);

                            favoriteButton.setChecked(preferences.getBoolean(PREFERENCE_ISFAVORITED, false));
                            if (favoriteButton.isChecked()) {
                                favorite = new Favorite(
                                        updatedDog.getPetId(),
                                        updatedDog.getPhotoPath(),
                                        updatedDog.getPetName(),
                                        updatedDog.getNumOfSteps());
                                AppExecutors.getInstance().diskIO().execute(() -> dogDatabase.favoriteDao().insert(favorite));
                            }

                            Snackbar.make(snackBarView,
                                    dog.getPetName() + getResources().getString(R.string.pet_updated_snackbar_message),
                                    Snackbar.LENGTH_SHORT)
                                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                                    .show();
                            break;
                        }
                    }

            }
        }
    }
}

