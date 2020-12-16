package com.tdr.app.doggiesteps.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.utils.CustomToastUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.tdr.app.doggiesteps.utils.Constants.ADD_BUTTON_VISIBILITY_STATE;
import static com.tdr.app.doggiesteps.utils.Constants.EMPTY_BACKGROUND_VISIBILITY_STATE;
import static com.tdr.app.doggiesteps.utils.Constants.EXTRA_PET_DETAILS;
import static com.tdr.app.doggiesteps.utils.Constants.EXTRA_SAVED_PET;
import static com.tdr.app.doggiesteps.utils.Constants.PET_PHOTO_VISIBILITY_STATE;
import static com.tdr.app.doggiesteps.utils.Constants.PREFERENCES_PHOTO_PATH;
import static com.tdr.app.doggiesteps.utils.Constants.REQUEST_IMAGE_CAPTURE;

public class PetEntryActivity extends AppCompatActivity {
    private static final String TAG = PetEntryActivity.class.getSimpleName();

    private String currentPhotoPath;
    private Dog dogToBeUpdated;

    @BindView(R.id.entry_toolbar_title)
    TextView entryToolbarTitle;
    @BindView(R.id.empty_add_photo_background)
    View emptyViewBackground;
    @BindView(R.id.petImage)
    ImageView petImageView;
    @BindView(R.id.details_snackbar_view)
    View contextView;
    @BindView(R.id.pet_name_edittext)
    EditText nameEntry;
    @BindView(R.id.pet_breed_edittext)
    EditText breedEntry;
    @BindView(R.id.pet_age_edittext)
    EditText ageEntry;
    @BindView(R.id.pet_bio_edittext)
    EditText bioEntry;
    @BindView(R.id.save_button)
    Button saveButton;
    @BindView(R.id.add_photo_button)
    Button addPhotoButton;
    @BindView(R.id.entry_toolbar)
    MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_entry);
        ButterKnife.bind(this);

        setSupportActionBar(materialToolbar);

        materialToolbar.setNavigationOnClickListener(v -> finish());

        Intent petData = getIntent();
        if (petData != null) {
            dogToBeUpdated = petData.getParcelableExtra(EXTRA_PET_DETAILS);
            if (dogToBeUpdated != null) {
                setEntryDetails(dogToBeUpdated);
            }
        }


        if (savedInstanceState != null) {
            petImageView.setVisibility(View.VISIBLE);
            currentPhotoPath = savedInstanceState.getString(PREFERENCES_PHOTO_PATH);
            Glide.with(this)
                    .load(currentPhotoPath)
                    .transition(withCrossFade())
                    .placeholder(R.drawable.ic_action_pet_favorites)
                    .centerCrop()
                    .into(petImageView);
        }

        saveButton.setOnClickListener(v -> savePet());
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PetEntryActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(PetEntryActivity.this, new String[]{Manifest.permission.CAMERA},
                            REQUEST_IMAGE_CAPTURE);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });
    }

    public void savePet() {
        if (dogToBeUpdated != null) {
            updatePet();
        } else {
            Intent savedPetDataIntent = new Intent();
            if (TextUtils.isEmpty(nameEntry.getText())) {

                Snackbar.make(contextView, getString(R.string.empty_name_message), Snackbar.LENGTH_LONG).show();

            } else {
                String dogName = nameEntry.getText().toString().trim();
                String breed = breedEntry.getText().toString().trim();
                if (breed.equals("")) {
                    breed = getString(R.string.empty_breed_message);
                }
                String age = ageEntry.getText().toString().trim();
                if (age.equals("")) {
                    age = getString(R.string.empty_age_message);
                }
                String bio = bioEntry.getText().toString().trim();
                if (bio.equals("")) {
                    bio = getString(R.string.empty_bio_message);
                }
                int numOfSteps = 0;
                Dog newDog = new Dog(dogName, breed, age, bio, currentPhotoPath, numOfSteps);
                savedPetDataIntent.putExtra(EXTRA_SAVED_PET, newDog);
                setResult(RESULT_OK, savedPetDataIntent);
                finish();
            }
        }
    }

    private void updatePet() {
        Intent updatePetDataIntent = new Intent();
        if (TextUtils.isEmpty(nameEntry.getText())) {

            Snackbar.make(contextView, getString(R.string.empty_name_message), Snackbar.LENGTH_LONG).show();

        } else {
            String dogName = nameEntry.getText().toString().trim();
            String breed = breedEntry.getText().toString().trim();
            if (breed.equals("")) {
                breed = getString(R.string.empty_breed_message);
            }
            String age = ageEntry.getText().toString().trim();
            if (age.equals("")) {
                age = getString(R.string.empty_age_message);
            }
            String bio = bioEntry.getText().toString().trim();
            if (bio.equals("")) {
                bio = getString(R.string.empty_bio_message);
            }
            String updatedPhotoPath = dogToBeUpdated.getPhotoPath();
            int id = dogToBeUpdated.getPetId();
            int numOfSteps = dogToBeUpdated.getNumOfSteps();
            Dog updatedDog = new Dog(id, dogName, breed, age, bio, updatedPhotoPath, numOfSteps);
            updatePetDataIntent.putExtra(EXTRA_SAVED_PET, updatedDog);
            setResult(RESULT_OK, updatePetDataIntent);
            finish();
        }

    }

    private void dispatchTakePictureIntent() {

            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException e) {
                // display error state to the user
                e.printStackTrace();
            }
            Uri imageUri =
                    null;
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.tdr.app.doggiesteps.fileprovider", photoFile);
            }

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            emptyViewBackground.setVisibility(View.GONE);
            addPhotoButton.setVisibility(View.GONE);
            petImageView.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(currentPhotoPath)
                    .placeholder(R.drawable.ic_action_pet_favorites)
                    .centerCrop()
                    .into(petImageView);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ADD_BUTTON_VISIBILITY_STATE, addPhotoButton.getVisibility());
        outState.putInt(EMPTY_BACKGROUND_VISIBILITY_STATE, emptyViewBackground.getVisibility());
        outState.putInt(PET_PHOTO_VISIBILITY_STATE, petImageView.getVisibility());
        if (dogToBeUpdated != null) {
            outState.putString(PREFERENCES_PHOTO_PATH, dogToBeUpdated.getPhotoPath());
        } else {
            outState.putString(PREFERENCES_PHOTO_PATH, currentPhotoPath);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        addPhotoButton.setVisibility(savedInstanceState.getInt(ADD_BUTTON_VISIBILITY_STATE));
        emptyViewBackground.setVisibility(savedInstanceState.getInt(EMPTY_BACKGROUND_VISIBILITY_STATE));
        petImageView.setVisibility(savedInstanceState.getInt(PET_PHOTO_VISIBILITY_STATE));
    }

    private void setEntryDetails(Dog dog) {
        entryToolbarTitle.setText(R.string.update_title_text);
        emptyViewBackground.setVisibility(View.GONE);
        addPhotoButton.setVisibility(View.GONE);
        petImageView.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(dog.getPhotoPath())
                .placeholder(R.drawable.ic_action_pet_favorites)
                .fallback(R.drawable.ic_action_pet_favorites)
                .centerCrop()
                .into(petImageView);
        nameEntry.setText(dog.getPetName());
        breedEntry.setText(dog.getBreed());
        ageEntry.setText(dog.getAge());
        bioEntry.setText(dog.getPetBio());
        saveButton.setText(R.string.update_button_text);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                CustomToastUtils.buildCustomToast(this, getString(R.string.camera_permission_denied_message));
            }
        }
    }
}
