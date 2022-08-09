package com.maths.beyond_school_280720220930.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maths.beyond_school_280720220930.R;

public final class Utils {


    // Extension Function To load image in imageview Using Glide Library
    public static void loadImage(String url, android.widget.ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.cartoon_image_1)
                .error(R.drawable.cartoon_image_1)
                .into(imageView);
    }

    public static int getPendingIntentFlag() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_ONE_SHOT;
    }

    public static void simpleToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
