package com.chinaums.opensdk.weex.module;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.Map;

/**
*  @version 1.0
*  @description 设置获取媒体音量
*/
public class UmsVolumeModule extends UmsBasicModule {
    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;

    /**
     * @param progress 音量百分比
     * @param callback JS回调
     * @version 1.0
     * @description 设置媒体音量
     */
    @JSMethod
    public void setVolume(String progress, JSCallback callback) {
        try {
            if (TextUtils.isEmpty(progress)) {
                callBySimple(false, callback);
                return;
            }
            int pro = Integer.valueOf(progress);
            AudioManager audioManager = (AudioManager) mWXSDKInstance.getContext().getSystemService(Context.AUDIO_SERVICE);
            int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            //保持音量值在正常范围内
            pro = pro < MIN_PROGRESS ? MIN_PROGRESS : pro;
            pro = pro > MAX_PROGRESS ? MAX_PROGRESS : pro;
            int volume = (int) (max * pro / (float) MAX_PROGRESS);
            if (volume == 0 && pro > 0) {
                volume = 1;
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * @param callback S回调
     * @version 1.0
     * @description 获取媒体音量
     */
    @JSMethod
    public void getVolume(JSCallback callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            AudioManager audioManager = (AudioManager) mWXSDKInstance.getContext().getSystemService(Context.AUDIO_SERVICE);
            float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int progress = (int) (volume / max * 100);
            result.put("volume", progress + "");
            callByResult(true, result, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }

    }
}
