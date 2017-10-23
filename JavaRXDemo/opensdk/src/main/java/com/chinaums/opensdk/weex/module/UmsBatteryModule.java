package com.chinaums.opensdk.weex.module;

import android.content.Intent;
import android.content.IntentFilter;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.Map;

/**
 *  @version 1.0
 *  @description         电池信息模块
 */

public class UmsBatteryModule extends UmsBasicModule {

    /**
     *获取电池电量以及充电状态
     * @param callback JS回调
     */
    @JSMethod
    public void getBatteryInfo(JSCallback callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Intent intent = mWXSDKInstance.getContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int status = intent.getIntExtra("status", -1);
            boolean isCharging = status == 2 || status == 5;
            int level = intent.getIntExtra("level", 0);
            int scale = intent.getIntExtra("scale", 100);

            result.put("charging", isCharging + "");
            result.put("power", (int) (100 * (float) level / (float) scale) + "");

            callByResult(true, result, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }
}
