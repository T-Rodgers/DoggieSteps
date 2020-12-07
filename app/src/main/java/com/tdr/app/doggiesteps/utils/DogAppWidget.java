package com.tdr.app.doggiesteps.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.activities.MainActivity;

import static com.tdr.app.doggiesteps.utils.Constants.WIDGET_PHOTO_PATH;
import static com.tdr.app.doggiesteps.utils.Constants.WIDGET_TOTAL_STEPS;

public class DogAppWidget extends AppWidgetProvider {

    private static final String TAG = "DogAppWidget: ";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dog_app_widget);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.widget_pet_photo, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };

        Glide
                .with(context.getApplicationContext())
                .asBitmap()
                .placeholder(R.drawable.ic_action_pet_favorites)
                .error(R.drawable.dog_photo)
                .load(preferences.getString(WIDGET_PHOTO_PATH, ""))
                .circleCrop()
                .into(appWidgetTarget);

        views.setTextViewText(R.id.widget_total_steps, preferences.getString(WIDGET_TOTAL_STEPS, ""));

        Intent petDetailsIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, petDetailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_pet_photo, pendingIntent);
        views.setOnClickPendingIntent(R.id.widget_total_steps, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

