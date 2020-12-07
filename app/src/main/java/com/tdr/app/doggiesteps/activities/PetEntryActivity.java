package com.tdr.app.doggiesteps.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.tdr.app.doggiesteps.utils.Constants.BUNDLE_PHOTO_PATH;

public class PetEntryActivity extends AppCompatActivity {

    private String currentPhotoPath;

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

        materialToolbar.setNavigationOnClickListener(v -> finish());

        if (savedInstanceState != null) {
            petImageView.setVisibility(View.VISIBLE);
            currentPhotoPath = savedInstanceState.getString(BUNDLE_PHOTO_PATH);
            Glide.with(this)
                    .load(currentPhotoPath)
                    .transition(withCrossFade())
                    .placeholder(R.drawable.ic_action_pet_favorites)
                    .centerCrop()
                    .into(petImageView);
        }

        saveButton.setOnClickListener(v -> savePet());
        addPhotoButton.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    public void savePet() {
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
            savedPetDataIntent.putExtra(Constants.EXTRA_SAVED_PET, newDog);
            setResult(RESULT_OK, savedPetDataIntent);
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
        startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
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
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

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
        outState.putInt("add_button_visibility", addPhotoButton.getVisibility());
        outState.putInt("empty_background_visibility", emptyViewBackground.getVisibility());
        outState.putInt("pet_photo_visibility", petImageView.getVisibility());
        outState.putString("photo_path", currentPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        addPhotoButton.setVisibility(savedInstanceState.getInt("add_button_visibility"));
        emptyViewBackground.setVisibility(savedInstanceState.getInt("empty_background_visibility"));
        petImageView.setVisibility(savedInstanceState.getInt("pet_photo_visibility"));
    }
}