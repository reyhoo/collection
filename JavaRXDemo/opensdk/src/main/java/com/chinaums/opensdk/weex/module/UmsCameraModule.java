package com.chinaums.opensdk.weex.module;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.chinaums.opensdk.weex.utils.AppletConstant;
import com.chinaums.opensdk.weex.utils.Utils;
import com.chinaums.opensdk.weex.widget.ScanQRCodeDialog;
import com.covics.zxingscanner.OnDecodeCompletionListener;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.io.File;
import java.util.HashMap;

/**
 * @Version :  1.0
 * @Description : 摄像头操作模块
 */

public class UmsCameraModule extends UmsBasicModule {
    private File temp;
    private JSCallback imageCallback;
    private JSCallback videoCallback;

    /**
     * 拍摄照片
     * @param callback JS回调
     */
    @JSMethod
    public void captureImage(JSCallback callback) {
        imageCallback = callback;
        try {
            File dir = Utils.FileUtil.newFile(AppletConstant.Path.PHOTO);
            temp = new File(dir.getAbsolutePath(), AppletConstant.Prefix.IMAGE + System.currentTimeMillis() + AppletConstant.Suffix.JPG);
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));

            getActivity().startActivityForResult(intent, AppletConstant.Code.CAPTURE_IMAGE_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
            imageCallback = null;
        }
    }

    /**
     * 拍摄视频
     * @param callback JS回调
     */
    @JSMethod
    public void captureVideo(JSCallback callback) {
        videoCallback = callback;
        try {
            File dir = Utils.FileUtil.newFile(AppletConstant.Path.VEDIO);
            temp = new File(dir.getAbsolutePath(), AppletConstant.Prefix.VEDIO + System.currentTimeMillis() + AppletConstant.Suffix.VEDIO);
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));

            getActivity().startActivityForResult(intent, AppletConstant.Code.CAPTURE_VIDEO_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
            videoCallback = null;
        }
    }

    /**
     * 扫描二维码
     * @param callback JS回调
     */
    @JSMethod
    public void scanQRCode(final JSCallback callback) {
        try {
            new ScanQRCodeDialog(getActivity(), new OnDecodeCompletionListener() {
                @Override
                public void onDecodeCompletion(String barcodeFormat, String barcode, Bitmap bitmap) {
                    HashMap<String, Object> result = new HashMap<String, Object>();
                    result.put("content", barcode);
                    callByResult(true, result, callback);
                }
            }).show();

        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);

        }
    }

    /**
     *调起相机界面返回的结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        HashMap<String, Object> result = new HashMap<String, Object>();
        if (resultCode != Activity.RESULT_OK) {
            callBySimple(false, imageCallback);
            callBySimple(false, videoCallback);
            videoCallback = null;
            imageCallback = null;
            return;
        }
        if (requestCode == AppletConstant.Code.CAPTURE_IMAGE_RESULT) {
            String path = temp.getAbsolutePath();
            result.put("path", path);
            callByResult(true, result, imageCallback);
            imageCallback = null;
        }

        if (requestCode == AppletConstant.Code.CAPTURE_VIDEO_RESULT) {
            String path = temp.getAbsolutePath();
            result.put("path", path);
            callByResult(true, result, videoCallback);
            videoCallback = null;
        }
    }
}
