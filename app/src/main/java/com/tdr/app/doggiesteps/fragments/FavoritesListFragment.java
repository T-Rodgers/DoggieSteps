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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.adapters.FavoritesAdapter;
import com.tdr.app.doggiesteps.database.MainViewModel;
import com.tdr.app.doggiesteps.model.Favorite;
import com.tdr.app.doggiesteps.utils.CustomToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesListFragment extends Fragment implements FavoritesAdapter.FavoritesClickHandler {
    @BindView(R.id.favorite_list_recycler_view)
    RecyclerView favoritesRecyclerView;
    @BindView(R.id.empty_favorite_icon)
    ImageView emptyViewIcon;
    @BindView(R.id.empty_favorite_textview)
    TextView emptyViewTextView;

    private FavoritesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorites_list, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new FavoritesAdapter(getContext(), this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritesRecyclerView.setAdapter(adapter);

        initiateViewModel();

        return rootView;
    }

    private void initiateViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getFavorites().observe(getViewLifecycleOwner(), favorites -> {
            if (favorites.size() != 0) {
                emptyViewIcon.setVisibility(View.GONE);
                emptyViewTextView.setVisibility(View.GONE);
            } else {
                emptyViewIcon.setVisibility(View.VISIBLE);
                emptyViewTextView.setVisibility(View.VISIBLE);
            }
            adapter.setFavoriteList(favorites);
        });
    }

    @Override
    public void onClick(Favorite favoriteData) {
        CustomToastUtils.buildCustomToast(getContext(),
                getString(R.string.favorites_total_steps_toast_message,
                        String.valueOf(favoriteData.getTotalSteps())));
    }
}

