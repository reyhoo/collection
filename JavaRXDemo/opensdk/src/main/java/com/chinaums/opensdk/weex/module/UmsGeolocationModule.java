package com.chinaums.opensdk.weex.module;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
*  @version 1.0
*  @description 定位信息模块
*/
public class UmsGeolocationModule extends UmsBasicModule {

    private LocationClient locationClient = null;
    private BDLocation location;
    private ArrayList<String> eventNames;
    private Stack<JSCallback> satck;

    @Override
    public void onActivityResume() {
        super.onActivityResume();
    }

    private void initLocationClient() {
        eventNames = new ArrayList<String>();
        satck = new Stack<JSCallback>();
        //百度定位
        locationClient = new LocationClient(mWXSDKInstance.getContext().getApplicationContext());
        //声明LocationClient类
        locationClient.registerLocationListener(listener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        locationClient.setLocOption(option);
    }
    /**
     * 获取当前位置信息
     * @param callback JS回调
     */
    @JSMethod
    public void getLocation(JSCallback callback) {
        if (locationClient == null) {
            initLocationClient();
        }
        if (callback != null) {
            satck.push(callback);
            locationClient.start();
        }
    }

    /**
     * 持续监听位置变化
     * @param eventName
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

            if (locationClient == null) {
                initLocationClient();
            }
            if (!eventNames.contains(eventName)) {
                eventNames.add(eventName);
            }
            if (!locationClient.isStarted()) {
                locationClient.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", "fail");
            mWXSDKInstance.fireGlobalEventCallback(eventName, result);
        }
    }
    /**
     * 持续监听位置变化
     * @param eventName 全局通知标识
     * @param callback 监听标识 用于注册全局监听
     */
    @JSMethod
    public void watch(String eventName , String callback) {
        try {
            if (TextUtils.isEmpty(eventName) || TextUtils.isEmpty(callback)) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("result", "fail");
                mWXSDKInstance.fireGlobalEventCallback(eventName, result);
                return;
            }
            if (locationClient == null) {
                initLocationClient();
            }
            if (!eventNames.contains(eventName)) {
                registerEventToGlobal(eventName, callback);
                eventNames.add(eventName);
            }
            if (!locationClient.isStarted()) {
                locationClient.start();
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

            if (locationClient == null) {
                initLocationClient();
            }
            if (eventNames.contains(eventName)) {
                eventNames.remove(eventName);
            }
            if (eventNames.size() == 0 && satck.isEmpty()) {
                locationClient.stop();
            }
            unRegisterEventToGlobal(eventName);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    public BDLocationListener listener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Map<String, Object> result = new HashMap<String, Object>();
            try {
                location = bdLocation;
                result.put("result", "success");
                result.put("latitude", location.getLatitude() + "");
                result.put("longitude", location.getLongitude() + "");
                result.put("addr", location.getAddrStr());
                for (int i = 0; i < eventNames.size(); i++) {
                    mWXSDKInstance.fireGlobalEventCallback(eventNames.get(i), result);
                }

                while (!satck.isEmpty()) {
                    callByResult(true, result, satck.pop());
                }

                if (eventNames.size() == 0 && satck.isEmpty()) {
                    locationClient.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.clear();
                result.put("result", "fail");
                for (int i = 0; i < eventNames.size(); i++) {
                    mWXSDKInstance.fireGlobalEventCallback(eventNames.get(i), result);
                }
                while (!satck.isEmpty()) {
                    callBySimple(false, satck.pop());
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };
}
