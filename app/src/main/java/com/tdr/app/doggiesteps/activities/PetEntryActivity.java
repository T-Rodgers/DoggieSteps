package com.tdr.app.doggiesteps.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.model.Dog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetEntryActivity extends AppCompatActivity {

    private Dog dog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_entry);
        ButterKnife.bind(this);

        Bundle petData = getIntent().getExtras();
        if (petData != null) {
            dog = petData.getParcelable("EXTRA_PET_EDIT");
            assert dog != null;
            retrievePetToBeEdited(dog);
        }

        saveButton.setOnClickListener(v -> savePet());
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
            savedPetDataIntent.putExtra("SAVED_DOG", newDog);
            setResult(RESULT_OK, savedPetDataIntent);
            finish();

        }

    }

    public void retrievePetToBeEdited(Dog dog) {
        nameEntry.setText(dog.getDogName());
        breedEntry.setText(dog.getBreed());
        ageEntry.setText(dog.getAge());
        bioEntry.setText(dog.getPetBio());
    }

    // TODO: (1) PetEntryActivity: Check for id of pet. If it is null we are entering a new pet.
    // TODO: (2) PetEntryActivity: Edit retrievePetToBeEdited method to load pet into fields based on petId from db
    // TODO: (3) PetDetailsDialogFragment: Continue editing editFab clickListener
    // Create interface????


}