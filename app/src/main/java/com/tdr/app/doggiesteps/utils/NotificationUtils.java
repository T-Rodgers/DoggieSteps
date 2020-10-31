package com.tdr.app.doggiesteps.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.activities.MainActivity;

public class NotificationUtils {

    private static PendingIntent contentIntent(Context context) {
        Intent startActivity = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                Constants.WALK_REMINDER_PENDING_INTENT_ID,
                startActivity,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();

        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.dog_photo);
        return largeIcon;
    }

    public static void remindBecauseCharging(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(
                    Constants.WALK_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(mChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                Constants.WALK_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.primaryLightColor))
                .setSmallIcon(R.drawable.ic_action_pet_favorites)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Let's Walk A Dog")
                .setContentText("Time to get active! Walk a pet!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        "Time to get active! Walk a Pet"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(Constants.WALK_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }


}
