package com.tdr.app.doggiesteps.utils;

public class SensorFilter {

    private SensorFilter() {
    }

    public static float sum(float[] array) {
        float retval = 0;
        for (float v : array) {
            retval += v;
        }
        return retval;
    }

    public static float norm(float[] array) {
        float retval = 0;
        for (float v : array) {
            retval += v * v;
        }
        return (float) Math.sqrt(retval);
    }


    public static float dot(float[] a, float[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

}
