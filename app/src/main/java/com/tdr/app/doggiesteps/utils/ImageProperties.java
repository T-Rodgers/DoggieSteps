package com.tdr.app.doggiesteps.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageProperties {

    public ImageProperties() {

    }

    public Bitmap rotateAndScaleImage(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                bitmap.getWidth(),
                bitmap.getHeight(),
                true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap,
                0,
                0,
                scaledBitmap.getWidth(),
                scaledBitmap.getHeight(),
                matrix,
                true);

        return rotatedBitmap;
    }

}
