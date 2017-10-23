package com.chinaums.opensdk.download.model;

import android.graphics.Bitmap;

import com.chinaums.opensdk.cons.DynamicBizShowState;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;

import java.io.File;

public abstract class ShowPack extends BasePack {

    public abstract Bitmap getBitMap();

    public String getBitmapRes() {
        return getResOriginalPath() + File.separator + getResOriginalFileName();
    }

    @Override
    public void refresh(final ResourceManagerListener listener,
                        boolean useBackup) throws Exception {
        prepare(listener, useBackup);
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
        return null;
    }

    @Override
    protected boolean initResProcessByOriginal() throws Exception {
        return copyResOriginal2Process();
    }

}
