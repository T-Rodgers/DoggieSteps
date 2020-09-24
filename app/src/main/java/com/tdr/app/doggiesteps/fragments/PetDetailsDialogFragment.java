package com.tdr.app.doggiesteps.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.model.Dog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetDetailsDialogFragment extends DialogFragment {

    private Dog dog;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            dog = args.getParcelable("SELECTED_PET");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details_dialog, container, false);
        ButterKnife.bind(this, rootView);

        setPetData();

        return rootView;
    }

    public void setPetData() {
        dialogPetName.setText(dog.getDogName());
        dialogPetBreed.setText(dog.getBreed());
        dialogPetAge.setText(dog.getAge());
        dialogPetBio.setText(dog.getPetBio());

    }
}
