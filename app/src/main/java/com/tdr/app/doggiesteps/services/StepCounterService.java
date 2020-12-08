package com.tdr.app.doggiesteps.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tdr.app.doggiesteps.activities.MainActivity;
import com.tdr.app.doggiesteps.utils.NotificationUtils;

import static com.tdr.app.doggiesteps.utils.Constants.ACTIVE_NOTIFICATION_ID;
import static com.tdr.app.doggiesteps.utils.Constants.NOTIFICATION_PET_NAME;

public class StepCounterService extends Service {
    private static final String TAG = StepCounterService.class.getSimpleName();

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String petName = intent.getStringExtra(NOTIFICATION_PET_NAME);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                NotificationUtils.createStepCounterNotification(this, pendingIntent, petName);

        startForeground(ACTIVE_NOTIFICATION_ID, notification);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}