package com.chinaums.opensdk.download.model;

import android.content.Context;

import com.chinaums.opensdk.data.model.ResourceListHistory;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;

public class DisplayBizListPack extends AbsNetPack {

    /**
     * biz
     */
    private String biz;

    /**
     * ads
     */
    private String ads;

    @Override
    public void prepareByPreload(Context context,
                                 ResourceManagerListener listener) throws Exception {
        biz = OpenDynamicBizHistoryManager.getInstance().getResourcePreload()
                .getDisplayBizCodes();
        ads = OpenDynamicBizHistoryManager.getInstance().getResourcePreload()
                .getDisplayAdsCodes();
        onFinish(listener);
    }

    @Override
    public void prepareByHistory(Context context,
                                 ResourceManagerListener listener) throws Exception {
        biz = ResourceListHistory.getDisplayBizCodes();
        ads = ResourceListHistory.getDisplayAdsCodes();
        onFinish(listener);
    }

    @Override
    protected void updateHistory() throws Exception {
        ResourceListHistory.setDisplayAdsCodes(getAds());
        ResourceListHistory.setDisplayBizCodes(getBiz());
        ResourceListHistory.setLastDisplayBizCodes(getBiz());
    }

    @Override
    protected void requestAndFill(final ResourceManagerListener listener)
            throws Exception {
        // 厦门没有获取显示资源包列表的定制需求
        onFinish(listener);
    }

    public String getBiz() {
        return biz;
    }

    public String getAds() {
        return ads;
    }

}
