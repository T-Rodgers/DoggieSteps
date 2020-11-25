package com.tdr.app.doggiesteps.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private int numOfSteps;
    private int totalSteps;

    private Dog dog;
    private Favorite favorite;
    private int petId;
    private String photoPath;
    private String favoritePetName;
    private FitnessOptions fitnessOptions;

    @BindView(R.id.details_snackbar_view)
    View snackBarView;
    @BindView(R.id.add_widget_icon)
    ImageView addWidgetIcon;
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

        fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            subscribe();
        }


        // TODO BUILD FITNESS API! YOU CAN DO IT!

        toolbar.setNavigationOnClickListener(v -> finish());

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        stopButton.setEnabled(false);

        Intent petData = getIntent();
        if (petData != null) {
            dog = petData.getParcelableExtra(EXTRA_SELECTED_PET);
            petId = dog.getPetId();
            photoPath = dog.getPhotoPath();
            favoritePetName = dog.getPetName();
        }

        int savedID = preferences.getInt(BUNDLE_ID, 0);
        int savedSteps = preferences.getInt(BUNDLE_STEPS, numOfSteps);
        if (savedID == petId) {
            numOfSteps = savedSteps;
            loadNumOfSteps();
        }

        setPetData(dog);

        database = DogDatabase.getInstance(this);
        favorite = new Favorite(petId, photoPath, favoritePetName);

        addWidgetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWidgets();

            }
        });

        favoriteButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                addToFavorites();
            } else {
                removeFromFavorites();
            }
        });

        takeWalkButton.setOnClickListener(v -> {
            Fitness.getSensorsClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                    .findDataSources(
                            new DataSourcesRequest.Builder()
                                    .setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                                    .setDataSourceTypes(DataSource.TYPE_RAW)
                                    .build())
                    .addOnSuccessListener(this, new OnSuccessListener<List<DataSource>>() {
                        @Override
                        public void onSuccess(List<DataSource> dataSources) {
                            for (DataSource dataSource : dataSources) {
                                Log.i(TAG, "Data source found " + dataSource);
                                Log.i(TAG, "Data source type: " + dataSource.getDataType());
                                if (dataSource.getDataType() == DataType.TYPE_STEP_COUNT_CUMULATIVE) {
                                    Log.i(TAG, "Data source for STEP_COUNT_CUMULATIVE");
                                }

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Finding data sources request failed", e);
                        }
                    });


            stopButton.setEnabled(true);

        });

        stopButton.setOnClickListener(v -> {
            unsubscribe();
            stopButton.setEnabled(false);
//            AppExecutors.getInstance().diskIO().execute(() -> database.dogDao().updateSteps(dog.getPetId(), dog.getNumOfSteps()));
        });


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
    public void onBackPressed() {
        saveStepsAndId();
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
            stopButton.setEnabled(true);
        }
    }

    public void addToWidgets() {
        preferences.edit()
                .putInt(PREFERENCE_ID, dog.getPetId())
                .putString(WIDGET_PET_NAME, dog.getPetName())
                .putString(WIDGET_TOTAL_STEPS, String.valueOf(dog.getNumOfSteps()))
                .putString(WIDGET_PHOTO_PATH, dog.getPhotoPath())
                .apply();

        Log.d(TAG, "addToWidgets: " + preferences.getString(Constants.WIDGET_PET_NAME, ""));

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

    public void subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Successfully subscribed!");
                                } else {
                                    Log.w(TAG, "There was a problem subscribing.", task.getException());
                                }
                            }
                        });
    }

    public void unsubscribe() {
        Fitness.getRecordingClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                // This example shows unsubscribing from a DataType. A DataSource should be used where the
                // subscription was to a DataSource. Alternatively, a Subscription object can be used.
                .unsubscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully unsubscribed");
                    }
                })
                .addOnFailureListener(this,
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }

    private void readData() {
        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                                Toast.makeText(getApplicationContext(), "Total steps: " + total, Toast.LENGTH_LONG).show();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }
}