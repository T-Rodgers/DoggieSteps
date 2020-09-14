package com.tdr.app.doggiesteps.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.adapters.DogListAdapter;
import com.tdr.app.doggiesteps.model.Dog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetListFragment extends Fragment {
    @BindView(R.id.pet_list_recycler_view)
    RecyclerView recyclerView;

    private DogListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Dog dog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.linear_pet_list, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new DogListAdapter(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return rootView;
    }
}
