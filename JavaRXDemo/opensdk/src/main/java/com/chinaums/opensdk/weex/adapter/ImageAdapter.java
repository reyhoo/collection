package com.chinaums.opensdk.weex.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

import java.io.FileInputStream;


public class ImageAdapter implements IWXImgLoaderAdapter {

    @Override
    public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
        // 传进来的url为绝对地址
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        if (!TextUtils.isEmpty(url)) {
            try {
                FileInputStream fis = new FileInputStream(url);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                view.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
