package com.chinaums.opensdk.download.listener;

import com.chinaums.opensdk.manager.OpenDelegateDefined.INetPackResponse;


public interface ResourceNetProcessListener<T extends INetPackResponse> {

    /**
     * 更新完成
     */
    public void onUpdated(T t);

    /**
     * 出错
     */
    public void onError(String errorInfo, Exception e);

}
