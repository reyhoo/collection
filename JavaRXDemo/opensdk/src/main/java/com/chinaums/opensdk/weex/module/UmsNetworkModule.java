package com.chinaums.opensdk.weex.module;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.Map;

/**
*  @version 1.0
*  @description 网络类型获取模块
*/
public class UmsNetworkModule extends UmsBasicModule {

    @JSMethod
    public void getNetStatus(JSCallback callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("status", getNetStatus());
         callByResult(true, result, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    public String getNetStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager) mWXSDKInstance.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return "wifi";
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return "mobile";
            }
        } else {
            return "unknown";
        }
        return "unknown";
    }
}
