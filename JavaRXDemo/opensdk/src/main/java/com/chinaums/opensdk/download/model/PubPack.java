package com.chinaums.opensdk.download.model;

import com.chinaums.opensdk.cons.DynamicBizShowState;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.UmsStringUtils;

public abstract class PubPack extends BasePack {

    @Override
    public void refresh(final ResourceManagerListener listener,
                        boolean useBackup) throws Exception {
        prepare(listener, useBackup);
    }

    @Override
    public void refresh(ResourceManagerListener listener, boolean useBackup,
                        boolean needStdIcon, boolean needLargeIcon, boolean needAds,
                        boolean needPublic, boolean canRefresh) throws Exception {
        if (needPublic || canRefresh) {
            refresh(listener, useBackup);
        } else {
            listener.onUpdated(this);
        }
    }

    @Override
    public Class<?>[] getDependentClasses() throws Exception {
        return null;
    }

    @Override
    protected void changedShowState(boolean isPrepareSuccess, boolean isPreload)
            throws Exception {
        if (isPrepareSuccess)
            this.setStatus(DynamicBizShowState.NORMAL.toString());
        else
            this.setStatus(DynamicBizShowState.UNAVAILABLE.toString());
    }

    @Override
    protected String getOpenUrlBySelfParam(String param) {
        // 这个函数这个实现没有必要，配置资源包和共享资源包是没有url概念的。
        return "ums://biz/"
                + getCode()
                + "/index.html?param="
                + (UmsStringUtils.isBlank(param) ? "" : Base64Utils
                .encrypt(param));
    }

    @Override
    public String getResSign() {
        if (getRes() == null || getRes().getBizPackage() == null)
            return null;
        return getRes().getBizPackage().getSign();
    }

    @Override
    public String getResUrl() {
        if (getRes() == null || getRes().getBizPackage() == null)
            return null;
        return getRes().getBizPackage().getDownloadUrl();
    }

    @Override
    protected boolean initResProcessByOriginal() throws Exception {
        return unzipResOriginal2Process();
    }

}
