package com.tdr.app.doggiesteps.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.tdr.app.doggiesteps.R;

import java.util.concurrent.TimeUnit;

public class FitnessUtils {

    private static final String TAG = FitnessUtils.class.getSimpleName();

    public static void registerListener(Context context, GoogleSignInAccount account, OnDataPointListener listener) {
        // Previous steps returned will be steps that are from last read. Therefore
        // We have to set them to "0" or else our initial value will be the total of all
        Fitness.getSensorsClient(context, account)
                .add(
                        new SensorRequest.Builder()
                                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        listener)
                .addOnSuccessListener((Activity) context, aVoid -> Log.i(TAG, context.getString(R.string.sensor_client_success)))
                .addOnFailureListener(e -> Log.e(TAG, context.getString(R.string.sensor_client_error), e));
    }

    public static void unregisterListener(Context context, GoogleSignInAccount account, OnDataPointListener listener) {
        Fitness.getSensorsClient(context, account)
                .remove(listener)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, context.getString(R.string.unregistered_listener_message));
                    }
                });
    }

}
