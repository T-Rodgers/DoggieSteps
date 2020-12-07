package com.tdr.app.doggiesteps.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.adapters.TabsAdapter;
import com.tdr.app.doggiesteps.database.DogDatabase;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.utils.AppExecutors;
import com.tdr.app.doggiesteps.utils.Constants;
import com.tdr.app.doggiesteps.utils.ReminderUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 125;

    @BindView(R.id.main_toolbar_title)
    TextView toolBarTextView;
    @BindView(R.id.main_snackbar_view)
    View snackBarView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_bar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private DogDatabase dogDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
            }
        }


        ReminderUtilities.scheduleChargingReminder(this);

        fab.setOnClickListener(v -> addPet());

        dogDatabase = DogDatabase.getInstance(this);

        TabsAdapter tabsAdapter =
                new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                switch (position) {
                    case 0:
                        toolBarTextView.setText(R.string.my_doggies_toolbar_title);

                        break;
                    case 1:
                        toolBarTextView.setText(R.string.favorites_toolbar_title);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void addPet() {
        Intent petEntryIntent = new Intent(this, PetEntryActivity.class);
        startActivityForResult(petEntryIntent, Constants.NEW_PET_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.NEW_PET_REQUEST_CODE) {

            Dog dog = data.getParcelableExtra(Constants.EXTRA_SAVED_PET);

            if (dog != null) {
                AppExecutors.getInstance().diskIO().execute(() ->
                        dogDatabase.dogDao().insert(dog));

                Snackbar.make(snackBarView,
                        dog.getPetName() + getResources().getString(R.string.pet_added_snackbar_message),
                        Snackbar.LENGTH_SHORT)
                        .setAnchorView(fab)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                        .show();
            }
        }
    }
}
