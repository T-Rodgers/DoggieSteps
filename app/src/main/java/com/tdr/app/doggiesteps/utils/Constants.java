package com.tdr.app.doggiesteps.utils;

public class Constants {

    public static final int REQUEST_OAUTH_REQUEST_CODE = 0x6884;

    public static final String EXTRA_SAVED_PET = "saved_pet";
    public static final String EXTRA_PET_DETAILS = "pet_details";
    public static final int NEW_PET_REQUEST_CODE = 1;
    public static final int UPDATE_PET_REQUEST_CODE = 101;
    public static final String EXTRA_SELECTED_PET = "selected_pet";
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    public static final String EXTRA_SELECTED_ID = "selected_id";

    // Widget Constants
    public static final String WIDGET_TOTAL_STEPS = "widget_total_steps";
    public static final String WIDGET_PET_NAME = "widget_pet_name";
    public static final String WIDGET_PHOTO_PATH = "widget_photo_path";
    public static final String WIDGET_NULL_PHOTO = "null_photo";

    // Notification Constants
    public static final String WALK_REMINDER_JOB_TAG = "walk_reminder_tag";
    public static final int WALK_REMINDER_PENDING_INTENT_ID = 3;
    public static final String WALK_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    public static final int WALK_REMINDER_NOTIFICATION_ID = 4;
    public static final String ACTIVE_NOTIFICATION_CHANNEL_ID = "active_notification_channel";
    public static final int ACTIVE_NOTIFICATION_ID = 351;
    public static final String NOTIFICATION_PET_NAME = "notification_pet_name";

    // Preferences Constants
    public static final String PREFERENCES_ID = "preferences_active_id";
    public static final String PREFERENCE_ID = "preference_id";
    public static final String PREFERENCES_ACTIVE_STATE = "active_state";
    public static final String PREFERENCES_PET_NAME = "saved_name";
    public static final String PREFERENCES_PHOTO_PATH = "photo_path";
    public static final String PREFERENCES_STEPS = "num_of_steps";
    public static final String PREFERENCE_ISFAVORITED = "preference_isfavorited";

    // Testing Constants
    public static final String TEST_PET_OBJECT = "test_pet";
    public static final String TEST_PET_NAME = "Scooby-Doo";
    public static final String TEST_PET_BREED = "Great Dane";
    public static final String TEST_PET_AGE = "10";
    public static final String TEST_PET_BIO = "This is a test message";
    public static final String TEST_PHOTO_PATH = "_test_photo_path";
    public static final int TEST_STEP_COUNT = 123;

    // MISC Constants
    public static final String ADD_BUTTON_VISIBILITY_STATE = "add_button_visibility";
    public static final String EMPTY_BACKGROUND_VISIBILITY_STATE = "empty_background_visibility";
    public static final String PET_PHOTO_VISIBILITY_STATE = "pet_photo_visibility";
}
