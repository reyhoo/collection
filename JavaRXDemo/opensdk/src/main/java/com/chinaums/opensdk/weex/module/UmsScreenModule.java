package com.chinaums.opensdk.weex.module;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.chinaums.opensdk.weex.utils.AppletConstant;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.Map;

/**
 *  @version 1.0
 *  @description         屏幕操作模块
 */

public class UmsScreenModule extends UmsBasicModule {

    /**
     * 设置界面亮度  最低亮度为手机屏幕最低亮度
     * @param pro 亮度百分比
     * @param callback JS回调
     */
    @JSMethod
    public void setBrightness(String pro, JSCallback callback) {
        try {
            if (TextUtils.isEmpty(pro)) {
                callBySimple(false, callback);
                return;
            }
            float progress = Float.valueOf(pro);
            Window window = getActivity().getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            progress = progress < 0 && progress != WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE ? 0 : progress;
            progress = progress > AppletConstant.Number.MAX_BRIGHTNESS ? AppletConstant.Number.MAX_BRIGHTNESS : progress;
            if (progress == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE) {
                lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            } else {
                lp.screenBrightness = progress / AppletConstant.Number.MAX_BRIGHTNESS;
            }
            window.setAttributes(lp);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }
    /**
     * 获取界面亮度  最低亮度为手机屏幕最低亮度
     * @param callback JS回调
     */
    @JSMethod
    public void getBrightness(JSCallback callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Window window = getActivity().getWindow();
            float brightness = window.getAttributes().screenBrightness == -1 ? -1 : window.getAttributes().screenBrightness * 100;

            result.put("brightness", brightness + "");
            callByResult(true, result, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 获取屏幕方向类型
     * @param callback JS回调
     */
    @JSMethod
    public void getOrientation(JSCallback callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Activity activity = getActivity();
            result.put("orientation", activity.getRequestedOrientation() + "");
            callByResult(true, result, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 锁定当前屏幕方向
     * @param callback
     */
    @JSMethod
    public void lockOrientation(JSCallback callback) {
        try {
            Activity activity = getActivity();
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 解锁当前屏幕方向
     * @param callback JS回调
     */
    @JSMethod
    public void unlockOrientation(JSCallback callback) {
        try {
            Activity activity = getActivity();
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

}
