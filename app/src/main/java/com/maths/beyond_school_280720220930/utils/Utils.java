package com.maths.beyond_school_280720220930.utils;

import com.bumptech.glide.Glide;
import com.maths.beyond_school_280720220930.R;

public final class Utils {


    // Extension Function To load image in imageview Using Glide Library
    public static void loadImage(String url, android.widget.ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.cartoon_image_1)
                .into(imageView);
    }
}
