package com.chinaums.opensdk.download.model;

import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.util.UmsLog;

public abstract class AbsNetPack implements Resource {

    /**
     * 存储到SP中
     */
    protected abstract void updateHistory() throws Exception;

    /**
     * 在线网络请求获取新的网络数据
     */
    protected abstract void requestAndFill(ResourceManagerListener listener)
            throws Exception;

    @Override
    public void onProgress(String msg, int progress,
                           ResourceManagerListener listener) {
        listener.onProgress(this, msg, progress);
    }

    @Override
    public void onFinish(ResourceManagerListener listener) throws Exception {
        updateHistory();
        onProgress("处理结束.", 100, listener);
        listener.onUpdated(this);
    }

    @Override
    public void onError(String msg, Exception e,
                        ResourceManagerListener listener) throws Exception {
        UmsLog.e("", e);
        onProgress("处理异常", 100, listener);
        listener.onError(this, false, msg, e);
    }

    @Override
    public void refresh(ResourceManagerListener listener, boolean useBackup)
            throws Exception {
        // 厦门没有获取显示资源包列表更新的定制需求
        prepare(listener, useBackup);
    }

    @Override
    public synchronized void prepare(final ResourceManagerListener listener,
                                     boolean useBackup) throws Exception {
        prepareByHistory(null, listener);
    }

    @Override
    public boolean check() throws Exception {
        return false;
    }

}
