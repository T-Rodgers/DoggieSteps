package com.tdr.app.doggiesteps.activities;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.fragments.PetListFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void recyclerViewClick() {
        FragmentScenario<PetListFragment> fragmentScenario =
                FragmentScenario.launchInContainer(PetListFragment.class);
        fragmentScenario.moveToState(Lifecycle.State.CREATED);

        onView(withId(R.id.pet_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.details_pet_name)).check(matches(withText("Scooby-Doo")));
        onView(withId(R.id.details_pet_breed)).check(matches(withText("Great Dane")));
    }


    @Test
    public void testAddPet() {
        onView(withId(R.id.fab))
                .perform(click());
    }

}