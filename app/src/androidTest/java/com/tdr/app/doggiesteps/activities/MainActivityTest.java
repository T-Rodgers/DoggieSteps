package com.tdr.app.doggiesteps.activities;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.model.Dog;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_AGE;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_BIO;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_BREED;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_NAME;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_OBJECT;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PHOTO_PATH;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_STEP_COUNT;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testAddPet() {

        Dog dog = new Dog(TEST_PET_NAME, TEST_PET_BREED, TEST_PET_AGE, TEST_PET_BIO, TEST_PHOTO_PATH, TEST_STEP_COUNT);

        onView(withId(R.id.fab))
                .perform(click());

        Intent resultData = new Intent();
        resultData.putExtra(TEST_PET_OBJECT, dog);

        intending(hasComponent(PetEntryActivity.class.getName()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData));
    }

    @Test
    public void testViewPagerTab() {

        onView(withId(R.id.view_pager))
                .perform(swipeLeft());

        onView(withId(R.id.view_pager))
                .perform(swipeRight());
    }

    @Test
    public void openPetDetails_onRecyclerViewClick() {
        onView(withId(R.id.pet_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.details_pet_name)).check(matches(withText(TEST_PET_NAME)));
        onView(withId(R.id.details_pet_breed)).check(matches(withText(TEST_PET_BREED)));
        onView(withId(R.id.details_pet_age)).check(matches(withText(TEST_PET_AGE)));
        onView(withId(R.id.details_pet_bio)).check(matches(withText(TEST_PET_BIO)));

    }


}