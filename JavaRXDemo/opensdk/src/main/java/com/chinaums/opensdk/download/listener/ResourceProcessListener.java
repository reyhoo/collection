package com.chinaums.opensdk.download.listener;


public interface ResourceProcessListener {

    /**
     * 更新完成
     */
    public void onUpdated();

    /**
     * 出错
     */
    public void onError(String errorInfo, Exception e);

}
