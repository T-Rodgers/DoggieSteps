package com.tdr.app.doggiesteps.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.adapters.DogListAdapter;
import com.tdr.app.doggiesteps.database.MainViewModel;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetListFragment extends Fragment implements DogListAdapter.DogListAdapterClickHandler {

    @BindView(R.id.pet_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view_photo)
    ImageView emptyViewPhoto;
    @BindView(R.id.empty_view_message)
    TextView emptyViewText;

    private DogListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pet_list, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new DogListAdapter(getContext(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        initiateViewModel();

        return rootView;
    }

    @Override
    public void onClick(Dog dogData) {
        showDialog(dogData);
    }

    private void showDialog(Dog dogData) {

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        Fragment prev = getParentFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);


        Bundle petData = new Bundle();
        petData.putParcelable(Constants.EXTRA_SELECTED_PET, dogData);
        petData.putInt("SELECTED_ID", dogData.getPetId());

        PetDetailsDialogFragment newFragment = new PetDetailsDialogFragment();
        newFragment.setArguments(petData);
        newFragment.show((ft), "dialog");
    }

    private void initiateViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getDogs().observe(getViewLifecycleOwner(), dogs -> {
            if (dogs.size() != 0) {
                emptyViewText.setVisibility(View.GONE);
                emptyViewPhoto.setVisibility(View.GONE);
            } else {
                emptyViewText.setVisibility(View.VISIBLE);
                emptyViewPhoto.setVisibility(View.VISIBLE);
            }
            adapter.setDogList(dogs);


        });

    }
}
