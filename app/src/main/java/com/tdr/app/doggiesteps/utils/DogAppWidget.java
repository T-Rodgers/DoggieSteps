package com.tdr.app.doggiesteps.utils;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.tdr.app.doggiesteps.R;

public class DogAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dog_app_widget);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // TODO: Finish creating widget, add method and icon to add widget using SharedPreferences

    }
}
