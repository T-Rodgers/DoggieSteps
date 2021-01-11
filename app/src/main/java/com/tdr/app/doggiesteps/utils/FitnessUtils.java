package com.tdr.app.doggiesteps.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tdr.app.doggiesteps.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FitnessUtils {

    private static final String TAG = FitnessUtils.class.getSimpleName();

    public static void registerListener(Context context, GoogleSignInAccount account, OnDataPointListener listener) {

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

    public static void readDailySteps(Context context, GoogleSignInAccount account) {
        Fitness.getHistoryClient(context, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        int totalSteps = dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                        CustomToastUtils.buildCustomToast(context, "Total steps for the Day: " + totalSteps);
                    }
                });

    }

}
