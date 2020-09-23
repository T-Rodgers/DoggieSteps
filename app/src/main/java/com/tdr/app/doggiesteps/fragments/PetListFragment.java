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
import com.tdr.app.doggiesteps.database.DogListViewModel;
import com.tdr.app.doggiesteps.model.Dog;

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

        DogListViewModel dogListViewModel = new ViewModelProvider(this).get(DogListViewModel.class);
        dogListViewModel.getAllDogs().observe(getViewLifecycleOwner(), dogs -> {
            if (dogs.size() == 0) {
                emptyViewPhoto.setVisibility(View.VISIBLE);
                emptyViewText.setVisibility(View.VISIBLE);
            } else {
                emptyViewPhoto.setVisibility(View.GONE);
                emptyViewText.setVisibility(View.GONE);
            }
            adapter.setDogList(dogs);
        });

        return rootView;
    }

    @Override
    public void onClick(Dog dogData) {
        showDialog();
    }

    private void showDialog() {

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        Fragment prev = getParentFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);

        PetDetailsDialogFragment newFragment = new PetDetailsDialogFragment();
        newFragment.show((ft), "dialog");
    }
}
