package com.chinaums.opensdk.weex.module;

import android.text.TextUtils;

import com.chinaums.opensdk.weex.utils.UmsSensorWatcher;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @description 方向传感器(罗盘)
 */

public class UmsCompassModule extends UmsBasicModule {
    private UmsSensorWatcher watcher;
    private UmsSensorWatcher.OnSensorChangeListenerImpl listener = new UmsSensorWatcher.OnSensorChangeListenerImpl();


    /**
     * 获取当前方向
     *
     * @param callback JS回调
     */
    @JSMethod
    public void getDirection(JSCallback callback) {
        try {
            watcher = UmsSensorWatcher.getInstance(mWXSDKInstance.getContext());
            listener.setWXSDKInstance(mWXSDKInstance);
            listener.setCallback(callback);
            watcher.getDirection(listener);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 持续监听方向变化
     *
     * @param eventName 全局通知标识
     */
    @JSMethod
    public void watch(String eventName) {
        try {
            if (TextUtils.isEmpty(eventName)) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("result", "fail");
                mWXSDKInstance.fireGlobalEventCallback(eventName, result);
                return;
            }

            watcher = UmsSensorWatcher.getInstance(mWXSDKInstance.getContext());
            listener.setWXSDKInstance(mWXSDKInstance);
            listener.setEventName(eventName);
            watcher.watchDirection(listener);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", "fail");
            mWXSDKInstance.fireGlobalEventCallback(eventName, result);
        }
    }

    /**
     * 持续监听方向变化
     *
     * @param eventName 全局通知标识
     * @param callback  监听标识 用于注册全局监听
     */
    @JSMethod
    public void watch(String eventName, String callback) {
        try {
            if (TextUtils.isEmpty(eventName) || TextUtils.isEmpty(callback)) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("result", "fail");
                mWXSDKInstance.fireGlobalEventCallback(eventName, result);
                return;
            }
            if (!TextUtils.equals(listener.getEventName(), eventName)) {
                registerEventToGlobal(eventName, callback);
                watcher = UmsSensorWatcher.getInstance(mWXSDKInstance.getContext());
                listener.setWXSDKInstance(mWXSDKInstance);
                listener.setEventName(eventName);
                watcher.watchDirection(listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", "fail");
            mWXSDKInstance.fireGlobalEventCallback(eventName, result);
        }
    }

    /**
     * 清除监听
     *
     * @param eventName 全局通知标识
     * @param callback  JS回调
     */
    @JSMethod
    public void clearWatch(String eventName, JSCallback callback) {
        try {
            if (TextUtils.isEmpty(eventName)) {
                callBySimple(false, callback);
                return;
            }

            watcher = UmsSensorWatcher.getInstance(mWXSDKInstance.getContext());
            watcher.clearWatchDirection(listener);
            listener.reset();
            unRegisterEventToGlobal(eventName);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    @Override
    public void onActivityDestroy() {
        super.onActivityDestroy();
        UmsSensorWatcher.getInstance(mWXSDKInstance.getContext()).clearWatchDirection(listener);
    }
}
