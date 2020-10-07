package com.tdr.app.doggiesteps.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.utils.Constants;
import com.tdr.app.doggiesteps.utils.GlideUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetEntryActivity extends AppCompatActivity {

    private static final String TAG = PetEntryActivity.class.getSimpleName();

    String currentPhotoPath;
    private Uri mUri;

    @BindView(R.id.empty_add_photo_background)
    View emptyViewBackground;
    @BindView(R.id.petImage)
    ImageView petImage;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_entry);
        ButterKnife.bind(this);

        saveButton.setOnClickListener(v -> savePet());
        addPhotoButton.setOnClickListener(v -> {

        });
    }

    public void savePet() {
        Intent savedPetDataIntent = new Intent();
        if (TextUtils.isEmpty(nameEntry.getText()) ||
                TextUtils.isEmpty(breedEntry.getText())) {
            Snackbar.make(contextView, "Must have Name and Breed", Snackbar.LENGTH_LONG).show();

        } else {
            String dogName = nameEntry.getText().toString().trim();
            String breed = breedEntry.getText().toString().trim();
            String age = ageEntry.getText().toString().trim();
            String bio = bioEntry.getText().toString().trim();
            Dog newDog = new Dog(dogName, breed, age, bio);
            savedPetDataIntent.putExtra(Constants.EXTRA_SAVED_PET, newDog);
            setResult(RESULT_OK, savedPetDataIntent);
            finish();
        }
    }
}