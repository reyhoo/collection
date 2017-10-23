package com.chinaums.opensdk.load.process;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.util.JsonUtils;

/**
 * 类 GetNetTypeProcessor 的实现描述：获取网络状态信息API
 */
public class GetNetTypeProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {

    }

    @Override
    public int getType() {
        return DynamicProcessorType.GET_NET_STATE;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

    @Override
    protected void execute(DynamicWebModel model) throws Exception {
        NetworkStatus status = new NetworkStatus();
        status.setNetworkStatus(isNetworkConnected(model.getActivity()));
        setRespAndCallWeb(model,
                createSuccessResponse(JsonUtils.convert2Json(status)));
    }

    /**
     * 检测网络是否可用
     */
    private String isNetworkConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        String netChange = "unable";
        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            netChange = "wifi";
        }
        // 2G/3G/4G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            netChange = "wwan";
        }
        return netChange;
    }

    private class NetworkStatus {

        /**
         * 网络状态信息。unable = (无网络)，wifi = (WiFi)，wwan = (2G/3G/4G)。
         */
        private String networkStatus;

        public String getNetworkStatus() {
            return networkStatus;
        }

        public void setNetworkStatus(String networkStatus) {
            this.networkStatus = networkStatus;
        }

    }
}
