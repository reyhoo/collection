package com.chinaums.opensdk.weex.module;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.chinaums.opensdk.weex.utils.AppletConstant;
import com.chinaums.opensdk.weex.utils.Utils;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Version :  1.0
 * @Description : 多媒体操作
 */

public class UmsMediaModule extends UmsBasicModule {

    //多媒体播放器
    private MediaPlayer player;
    private String audioPath;

    //JS回调
    private JSCallback imageCallback;

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        if (player == null) {
            player = new MediaPlayer();
        }
    }

    /**
     *
     * @param path 音频路径
     * @param callback JS回调
     */
    @JSMethod
    public void playAudio(String path, JSCallback callback) {
        try {
            if (!Utils.FileUtil.isFile(path)) {
                callBySimple(false, callback);
                return;
            }

            initPlayer();
            //如果产的音频路径不一样就重新播放,反之从上次暂停的开始
            if (!TextUtils.equals(audioPath, path)) {
                audioPath = path;
                player.setDataSource(path);
                player.prepare();
            }
            player.start();
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 暂停播放音频
     * @param callback JS回调
     */
    @JSMethod
    public void pauseAudio(JSCallback callback) {
        try {
            if (player != null && player.isPlaying()) {
                player.pause();
            }
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 停止播放音频
     * @param callback JS回调
     */
    @JSMethod
    public void stopAudio(JSCallback callback) {
        try {
            if (player != null) {
                player.stop();
                player.reset();
                audioPath = null;
            }
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }


    /**
     * 调用系统视频播放器
     * @param path 视频文件路径
     * @param callback JS回调
     */
    @JSMethod
    public void playVideo(String path, JSCallback callback) {
        try {
            if (!Utils.FileUtil.isFile(path)) {
                callBySimple(false, callback);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + path), "video/mp4");
            mWXSDKInstance.getContext().startActivity(intent);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }



    /**
     * 调用系统图库选择图片
     * @param callback JS回调
     */
    @JSMethod
    public void pickImage(JSCallback callback) {
        imageCallback = callback;
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            getActivity().startActivityForResult(intent, AppletConstant.Code.PICK_IMAGE_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
            imageCallback = null;
        }
    }

    /**
     * 获取图片大小信息
     * @param path 图片文件路径
     * @param callback JS回调
     */
    @JSMethod
    public void getImageInfo(String path, JSCallback callback) {
        try {
            if (!Utils.FileUtil.isFile(path)) {
                callBySimple(false, callback);
                return;
            }

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("width", bitmap.getWidth());
            result.put("height", bitmap.getHeight());
            result.put("size", bitmap.getByteCount());
            callByResult(true, result, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     *获取图片拍摄信息
     * @param path 图片文件路径
     * @param callback JS回调
     */
    @JSMethod
    public void getImageExif(String path, JSCallback callback) {
        try {
            if (!Utils.FileUtil.isFile(path)) {
                callBySimple(false, callback);
                return;
            }

            HashMap<String, Object> result = new HashMap<String, Object>();
            boolean isSuceess = getExifInterfaceInfo(path, result);
            callByResult(isSuceess, result, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * @param path
     * @param result
     * @return
     */
    private boolean getExifInterfaceInfo(String path, HashMap<String, Object> result) {
        try {
            ExifInterface exif = new ExifInterface(path);
            String[] atters = {ExifInterface.TAG_APERTURE,
                    ExifInterface.TAG_FLASH,
                    ExifInterface.TAG_FOCAL_LENGTH,
                    ExifInterface.TAG_GPS_ALTITUDE,
                    ExifInterface.TAG_GPS_ALTITUDE_REF,
                    ExifInterface.TAG_GPS_LONGITUDE,
                    ExifInterface.TAG_GPS_LATITUDE,
                    ExifInterface.TAG_IMAGE_LENGTH,
                    ExifInterface.TAG_IMAGE_WIDTH,
                    ExifInterface.TAG_ISO,
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.TAG_WHITE_BALANCE,
                    ExifInterface.TAG_EXPOSURE_TIME,
                    ExifInterface.TAG_DATETIME,
                    ExifInterface.TAG_MAKE,
                    ExifInterface.TAG_MODEL,
                    ExifInterface.TAG_ORIENTATION,
            };
            for (String atter : atters) {
                String str = exif.getAttribute(atter);
                result.put(atter, str);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            callBySimple(false, imageCallback);
            imageCallback = null;
            return;
        }
        if (requestCode == AppletConstant.Code.PICK_IMAGE_RESULT) {
            HashMap<String, Object> result = new HashMap<String, Object>();

            Uri image = data.getData();
            String[] column = {MediaStore.Images.Media.DATA};
            Cursor cursor = mWXSDKInstance.getContext().getContentResolver().query(image, column, null, null, null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(column[0]);
            String path = cursor.getString(index);
            cursor.close();

            result.put("path", path);
            callByResult(true, result, imageCallback);
            imageCallback = null;
        }

    }
}
