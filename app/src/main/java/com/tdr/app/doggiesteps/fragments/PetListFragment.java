package com.tdr.app.doggiesteps.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.adapters.DogListAdapter;
import com.tdr.app.doggiesteps.database.DogListViewModel;
import com.tdr.app.doggiesteps.model.Dog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetListFragment extends Fragment {
    @BindView(R.id.pet_list_recycler_view)
    RecyclerView recyclerView;

    private DogListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.linear_pet_list, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new DogListAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        initiateViewModel();

        return rootView;
    }

    private void initiateViewModel() {
        DogListViewModel dogListViewModel = new ViewModelProvider(this).get(DogListViewModel.class);
        dogListViewModel.getAllDogs().observe(getViewLifecycleOwner(), new Observer<List<Dog>>() {
            @Override
            public void onChanged(List<Dog> dogs) {
                adapter.setDogList(dogs);
            }
        });

    }
}
