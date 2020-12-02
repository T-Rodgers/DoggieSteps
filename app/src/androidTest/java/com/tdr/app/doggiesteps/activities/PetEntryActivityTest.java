package com.tdr.app.doggiesteps.activities;

import android.os.Parcel;

import androidx.test.espresso.action.ViewActions;
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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PetEntryActivityTest {

    public static final String TEST_PET_NAME = "Scooby-Doo";
    public static final String TEST_PET_BREED = "Great Dane";
    public static final String TEST_PET_AGE = "10";
    public static final String TEST_PET_BIO = "This is a test message";
    public static final String PACKAGE_NAME = "com.tdr.app.doggiesteps";

    @Rule
    public ActivityScenarioRule<PetEntryActivity> activityScenarioRule =
            new ActivityScenarioRule<>(PetEntryActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void savedPetParcelable() {
        Dog dog = new Dog(TEST_PET_NAME, TEST_PET_BREED, TEST_PET_AGE, TEST_PET_BIO, "photo_path", 0);

        Parcel parcel = Parcel.obtain();
        dog.writeToParcel(parcel, dog.describeContents());
        parcel.setDataPosition(0);

        Dog createdFromParcel = Dog.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getPetName(), is(TEST_PET_NAME));
        assertThat(createdFromParcel.getBreed(), is(TEST_PET_BREED));
        assertThat(createdFromParcel.getAge(), is(TEST_PET_AGE));
        assertThat(createdFromParcel.getPetBio(), is(TEST_PET_BIO));
    }

    @Test
    public void verifyCorrectPetWasSaved() {

        onView(withId(R.id.pet_name_edittext))
                .perform(typeText(TEST_PET_NAME), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.pet_breed_edittext))
                .perform(typeText(TEST_PET_BREED), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.pet_age_edittext))
                .perform(typeText(TEST_PET_AGE), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.pet_bio_edittext))
                .perform(typeText(TEST_PET_BIO), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.save_button))
                .perform(click());

        onView(withId(R.id.main_snackbar_view)).check(matches(withText(TEST_PET_NAME)));
    }
}