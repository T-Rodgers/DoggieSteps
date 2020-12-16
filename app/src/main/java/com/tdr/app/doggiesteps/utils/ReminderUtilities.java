package com.tdr.app.doggiesteps.utils;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.tdr.app.doggiesteps.services.WalkReminderJobService;

import java.util.concurrent.TimeUnit;

import static com.tdr.app.doggiesteps.utils.Constants.WALK_REMINDER_JOB_TAG;

public class ReminderUtilities {

    private static final int REMINDER_INTERVAL_MINUTES = 480;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    private static boolean sInitialized;

    synchronized public static void scheduleChargingReminder(final Context context) {
        if (sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(WalkReminderJobService.class)
                .setTag(WALK_REMINDER_JOB_TAG)
                .setConstraints(Constraint.DEVICE_IDLE)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setRecurring(false)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(constraintReminderJob);
        sInitialized = true;
    }
}
