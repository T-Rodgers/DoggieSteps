package com.tdr.app.doggiesteps.activities;

import android.os.Parcel;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.model.Dog;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_AGE;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_BIO;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_BREED;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PET_NAME;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_PHOTO_PATH;
import static com.tdr.app.doggiesteps.utils.Constants.TEST_STEP_COUNT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PetEntryActivityTest {

    @Rule
    public ActivityScenarioRule<PetEntryActivity> activityScenarioRule =
            new ActivityScenarioRule<>(PetEntryActivity.class);

    @Test
    public void savedPetParcelable() {
        Dog dog = new Dog(TEST_PET_NAME, TEST_PET_BREED, TEST_PET_AGE, TEST_PET_BIO, TEST_PHOTO_PATH, TEST_STEP_COUNT);

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
    public void verifyDataEntryAndButtonClick() {

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

    }
}