package com.chinaums.opensdk.weex.module;

import android.content.Context;
import android.os.Vibrator;
import android.text.TextUtils;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

/**
 * @version 1.0
 * @description 震动模块
 */
public class UmsVibrationModule extends UmsBasicModule {

    /**
     * 振动设备
     * @param time 震动时长 毫秒
     * @param callback JS回调
     */
    @JSMethod
    public void vibrate(String time, JSCallback callback) {
        try {
            if (TextUtils.isEmpty(time)) {
                callBySimple(false, callback);
                return;
            }
            long d = Long.valueOf(time);
            Vibrator vibrator = (Vibrator) mWXSDKInstance.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(d < 0 ? 0 : d);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }
}
