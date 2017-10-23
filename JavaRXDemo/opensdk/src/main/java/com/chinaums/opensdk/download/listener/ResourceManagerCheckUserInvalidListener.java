package com.chinaums.opensdk.download.listener;


public interface ResourceManagerCheckUserInvalidListener extends
        ResourceManagerListener {

    /**
     * 更新完成
     */
    public void onUserInvalid(Exception exception);

}
