package com.chinaums.opensdk.download.listener;

import com.chinaums.opensdk.download.model.Resource;


public interface ResourceManagerListener {

    /**
     * 进度信息
     */
    public void onProgress(Resource pack, String msg, int progress);

    /**
     * 更新完成
     */
    public void onUpdated(Resource pack);

    /**
     * 出错
     */
    public void onError(Resource pack, boolean isIgnoreResource,
                        String errorInfo, Exception e);

}
