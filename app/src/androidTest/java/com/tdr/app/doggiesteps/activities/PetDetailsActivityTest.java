package com.tdr.app.doggiesteps.activities;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.tdr.app.doggiesteps.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_AGE;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_BIO;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_BREED;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_NAME;

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
        onView(withId(R.id.add_widget_icon))
                .perform(click());
    }

    @Test
    public void openPetEntry_verifyText() {
        onView(withId(R.id.pet_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.edit_pet_icon))
                .perform(click());

        onView(withId(R.id.pet_name_edittext)).check(matches(withText(TEST_PET_NAME)));
        onView(withId(R.id.pet_breed_edittext)).check(matches(withText(TEST_PET_BREED)));
        onView(withId(R.id.pet_age_edittext)).check(matches(withText(TEST_PET_AGE)));
        onView(withId(R.id.pet_bio_edittext)).check(matches(withText(TEST_PET_BIO)));
    }
}