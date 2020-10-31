package com.tdr.app.doggiesteps.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.tdr.app.doggiesteps.utils.ReminderTasks;

public class WalkReminderIntentService extends IntentService {

    public WalkReminderIntentService() {
        super("WalkReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            ReminderTasks.executeTask(this, action);
        }
    }
}
