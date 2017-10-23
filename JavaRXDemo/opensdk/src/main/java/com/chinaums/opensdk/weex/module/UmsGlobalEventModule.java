package com.chinaums.opensdk.weex.module;

import com.taobao.weex.WXGlobalEventModule;
import com.taobao.weex.annotation.JSMethod;

import java.util.HashMap;
import java.util.Map;


public class UmsGlobalEventModule extends WXGlobalEventModule {
    @JSMethod
    public void sendGlobalEvent(String envetName, String message) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("message", message);
            mWXSDKInstance.fireGlobalEventCallback(envetName, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
