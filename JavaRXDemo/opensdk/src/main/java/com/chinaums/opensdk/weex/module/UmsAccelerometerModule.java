package com.chinaums.opensdk.weex.module;

import android.text.TextUtils;

import com.chinaums.opensdk.weex.utils.UmsSensorWatcher;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @description 加速度传感器模块
 */

public class UmsAccelerometerModule extends UmsBasicModule {
    private UmsSensorWatcher watcher;
    private UmsSensorWatcher.OnSensorChangeListenerImpl listener1 = new UmsSensorWatcher.OnSensorChangeListenerImpl();

    /**
     * 获取当前加速度
     *
     * @param callback JS回调
     */
    @JSMethod
    public void getAcceleration(JSCallback callback) {
        try {
            watcher = UmsSensorWatcher.getInstance(mWXSDKInstance.getContext());
            listener1.setWXSDKInstance(mWXSDKInstance);
            listener1.setCallback(callback);
            watcher.getAcceleration(listener1);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 持续监听加速度变化
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
            listener1.setWXSDKInstance(mWXSDKInstance);
            listener1.setEventName(eventName);
            watcher.watchAcceleration(listener1);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<String, Object>();
            result.clear();
            result.put("result", "fail");
            mWXSDKInstance.fireGlobalEventCallback(eventName, result);
        }
    }

    /**
     * 持续监听加速度
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
            if (!TextUtils.equals(listener1.getEventName(), eventName)) {
                registerEventToGlobal(eventName, callback);
                watcher = UmsSensorWatcher.getInstance(mWXSDKInstance.getContext());
                listener1.setWXSDKInstance(mWXSDKInstance);
                listener1.setEventName(eventName);
                watcher.watchAcceleration(listener1);
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
     * @param callback
     */
    @JSMethod
    public void clearWatch(String eventName, JSCallback callback) {
        try {
            if (TextUtils.isEmpty(eventName)) {
                callBySimple(false, callback);
                return;
            }
            watcher = UmsSensorWatcher.getInstance(mWXSDKInstance.getContext());
            watcher.clearWatchAcceleration(listener1);
            listener1.reset();
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
        UmsSensorWatcher.getInstance(mWXSDKInstance.getContext()).clearWatchAcceleration(listener1);
    }
}
