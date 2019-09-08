package com.groot.basemodule.imageloader;

import android.content.Context;
import android.widget.ImageView;

/**
 * author: 肖雷
 * created on: 2019/9/8
 * description:
 */
public class ImageLoader {


    public static void loadImage(Context context, ImageView imageView) {
        GlideApp.with(fragment)
                .load(myUrl)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .into(imageView);
    }




}
