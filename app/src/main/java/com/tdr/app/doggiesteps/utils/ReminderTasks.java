package com.tdr.app.doggiesteps.utils;

import android.content.Context;

public class ReminderTasks {
    public static final String ACTION_CHARGING_REMINDER = "charging_reminder";

    public static void executeTask(Context context, String action) {
        if (ACTION_CHARGING_REMINDER.equals(action)) {
            issueChargingReminder(context);
        }
    }

    private static void issueChargingReminder(Context context) {
        NotificationUtils.remindBecauseCharging(context);
    }


}
