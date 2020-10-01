package com.tdr.app.doggiesteps.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideUtils {

    private Context context;

    public GlideUtils(Context context) {
        this.context = context;
    }

    public void loadImage(String fileName, ImageView imageView) {
        Glide.with(context)
                .load(Uri.parse("fine:///pet_image/" + fileName))
                .into(imageView);
    }
}
