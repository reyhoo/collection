package com.uidemo;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Administrator on 2016/10/19.
 */

public class Utils {
    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number){
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg

                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {
            if(argb[i] != 0){
                argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
            }
        }

        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg

                .getHeight(), Bitmap.Config.ARGB_8888);

        return sourceImg;
    }
    public static Bitmap setBitmapAlpha(Bitmap bitmap, float alpha)
    {
        return bitmap;
    }

}
