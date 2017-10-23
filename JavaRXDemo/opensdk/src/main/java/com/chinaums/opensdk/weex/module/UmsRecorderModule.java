package com.chinaums.opensdk.weex.module;

import android.media.MediaRecorder;

import com.chinaums.opensdk.weex.utils.AppletConstant;
import com.chinaums.opensdk.weex.utils.Utils;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *  @version 1.0
 *  @description         录音模块
 */

public class UmsRecorderModule extends UmsBasicModule {
    private MediaRecorder recorder;
    private String recordPath;

    /**
     * 初始化 音频记录器
     * @param callback JS回调
     */
    private void initRecorder(JSCallback callback) {
        try {
            if (recorder == null) {
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                File fileDir = Utils.FileUtil.newFile(AppletConstant.Path.SOUND);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
            }

            if (recordPath == null) {
                File file = new File(AppletConstant.Path.SOUND, System.currentTimeMillis() + AppletConstant.Suffix.AUDIO);
                recordPath = file.getAbsolutePath();
                recorder.setOutputFile(recordPath);
                recorder.prepare();
            }

        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 开始录制
     * @param callback JS回调
     */
    @JSMethod
    public void startRecord(JSCallback callback) {
        try {
            initRecorder(callback);
            recorder.start();

            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 停止录制
     * @param callback JS回调
     */
    @JSMethod
    public void stopRecord(JSCallback callback) {
        initRecorder(callback);
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (recorder != null && recordPath != null) {
                recorder.stop();
                recorder.release();
                recorder = null;

                result.put("path", recordPath);
                callByResult(true, result, callback);
                recordPath = null;
            } else {
                callBySimple(false, callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.clear();
            callBySimple(false, callback);
        }
    }
}
