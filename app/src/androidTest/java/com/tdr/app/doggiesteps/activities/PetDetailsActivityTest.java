package com.tdr.app.doggiesteps.activities;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.tdr.app.doggiesteps.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class PetDetailsActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void openPetDetails_verifyButtons() {
        onView(withId(R.id.pet_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.details_walk_button))
                .perform(click());
        onView(withId(R.id.details_stop_button))
                .perform(click());

        // Turn Favorite ON/OFF
        onView(withId(R.id.details_favorite_button))
                .perform(click());
        onView(withId(R.id.details_favorite_button))
                .perform(click());


    }
}