package com.chinaums.opensdk.download.model;

import com.chinaums.opensdk.cons.DynamicBizShowState;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.UmsStringUtils;


public class LocalWeexPack extends WebPack {

    @Override
    public Class<?>[] getDependentClasses() throws Exception {
        return null;
    }

    @Override
    protected String getOpenUrlBySelfParam(String param) {
        return "ums://weexbiz/"
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

    @Override
    protected void changedShowState(boolean isPrepareSuccess, boolean isPreload)
            throws Exception {
        DynamicBizShowState bizShowState = DynamicBizShowState
                .getByState(getStatus());
        if (isPrepareSuccess) {
            setStatus(bizShowState.toString());
        } else {
            setStatus(DynamicBizShowState.UPDATABLE.toString());
        }
    }

}
