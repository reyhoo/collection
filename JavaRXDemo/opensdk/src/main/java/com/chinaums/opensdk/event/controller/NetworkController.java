package com.chinaums.opensdk.event.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.event.model.NetStatusChangedEvent;
import com.chinaums.opensdk.manager.OpenDialogManager;
import com.chinaums.opensdk.util.UmsEventBusUtils;
import com.chinaums.opensdk.util.UmsLog;

/**
 * 网络状态变更监听
 */
public class NetworkController {

    /**
     * 当前网络环境
     */
    private String netStatusLastTime = "unable";

    /**
     * 网络监听广播
     */
    private ConnectionChangeReceiver mReceiver;

    public NetworkController() {
        super();
    }

    /**
     * 注册监听
     */
    public void registerListener(Context mContext) {
        // 生成广播处理
        mReceiver = new ConnectionChangeReceiver();
        // 实例化过滤器并设置要过滤的广播
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        // 注册广播
        mContext.registerReceiver(mReceiver, mFilter);
    }

    /**
     * 取消监听
     */
    public void unregisterListener(Context mContext) {
        if (null != mReceiver) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    /**
     * 网络改变通知
     */
    public class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                    ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                String netChange = "unable";
                // Wifi
                State state = connManager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).getState();
                if (state == State.CONNECTED || state == State.CONNECTING) {
                    netChange = "wifi";
                }
                if (null != connManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)) {
                    // 3G
                    state = connManager.getNetworkInfo(
                            ConnectivityManager.TYPE_MOBILE).getState();
                }
                if (state == State.CONNECTED || state == State.CONNECTING) {
                    netChange = "wwan";
                }
                if (!netStatusLastTime.equalsIgnoreCase(netChange)) {
                    netStatusLastTime = netChange;
                    try {
                        JSONObject retJson = new JSONObject();
                        retJson.put("netStatus", netChange);
                        // 向所有实现接口传递消息
                        UmsEventBusUtils.post(new NetStatusChangedEvent(retJson
                                .toString()));
                    } catch (Exception e) {
                        UmsLog.e("", e);
                        OpenDialogManager.getInstance().showHint(context,
                                "异常:" + e.getMessage());
                    }
                }
            }
        }
    }

}
