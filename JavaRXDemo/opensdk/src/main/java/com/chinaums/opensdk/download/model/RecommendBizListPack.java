package com.chinaums.opensdk.download.model;

import android.content.Context;

import com.chinaums.opensdk.data.model.ResourceListHistory;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;


public class RecommendBizListPack extends AbsNetPack {

    /**
     * biz
     */
    private String biz;

    @Override
    public void prepareByPreload(Context context,
                                 ResourceManagerListener listener) throws Exception {
        biz = OpenDynamicBizHistoryManager.getInstance().getResourcePreload()
                .getRecommendBizCodes();
        onFinish(listener);
    }

    @Override
    public void prepareByHistory(Context context,
                                 ResourceManagerListener listener) throws Exception {
        biz = ResourceListHistory.getRecommendBizCodes();
        onFinish(listener);
    }

    @Override
    protected void updateHistory() throws Exception {
        ResourceListHistory.setRecommendBizCodes(getBiz());
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

}
