package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.load.process.listener.DynamicAPICallback;

/**
 * 打开本地界面的处理类接口
 */
public interface IOpenDynamicPageProcessorDelegate {

    public void processNativePage(Activity activity, DynamicAPICallback apiCallback, int requestCode, JSONObject jsonData);

    public JSONObject processCallback(Activity activity, DynamicAPICallback apiCallback, int resultCode, Intent data);

}
