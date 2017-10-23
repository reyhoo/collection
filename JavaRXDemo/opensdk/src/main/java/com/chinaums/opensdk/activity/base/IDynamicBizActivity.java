package com.chinaums.opensdk.activity.base;

import com.chinaums.opensdk.cons.DynamicWebviewLoadingState;

public interface IDynamicBizActivity {

    /**
     * 设置标题
     */
    public void setTitleText(String text);

    public void initView(boolean isFullscreen, boolean isShowBottomToolbar, boolean isShowArea);

    public void changedViewByLoadingState(
            DynamicWebviewLoadingState loadingState);

    public void registerEvent();

    public void unRegisterEvent();

    /**
     * 打开页面loadUrl出错的处理
     */
    public void onPageLoadException();

    /**
     * 打开页面onReceiveError的处理
     */
    public void onPageReceiveError();

}
