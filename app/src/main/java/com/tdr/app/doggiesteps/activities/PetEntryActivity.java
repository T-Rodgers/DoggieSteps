package com.tdr.app.doggiesteps.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.utils.Constants;
import com.tdr.app.doggiesteps.utils.ImageProperties;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetEntryActivity extends AppCompatActivity {

    private static final String TAG = PetEntryActivity.class.getSimpleName();

    private String currentPhotoPath;
    private Uri mUri;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_entry);
        ButterKnife.bind(this);

        saveButton.setOnClickListener(v -> savePet());
        addPhotoButton.setOnClickListener(v -> {
            dispatchTakePictureIntent();
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
            Dog newDog = new Dog(dogName, breed, age, bio, currentPhotoPath);
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
                FileProvider.getUriForFile(this, "com.tdr.app.doggiesteps.fileprovider", photoFile);


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
    }





    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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

            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);

            Bitmap transformedBitmap =
                    new ImageProperties()
                            .rotateAndScaleImage(imageBitmap);

            emptyViewBackground.setVisibility(View.GONE);
            addPhotoButton.setVisibility(View.GONE);
            petImageView.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(transformedBitmap)
                    .into(petImageView);
        }
    }
}