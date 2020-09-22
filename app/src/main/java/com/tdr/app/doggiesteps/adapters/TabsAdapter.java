package com.tdr.app.doggiesteps.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tdr.app.doggiesteps.fragments.FavoritesListFragment;
import com.tdr.app.doggiesteps.fragments.PetListFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {
    Bundle bundle;
    int numberOfTabs;

    public TabsAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm, numberOfTabs);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PetListFragment();

            case 1:
                return new FavoritesListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
