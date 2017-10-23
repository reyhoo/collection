package com.chinaums.opensdk.download.model;

import android.graphics.Bitmap;

import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;

import java.io.File;

public class StdIconPack extends IconPack {

    @Override
    public void refresh(ResourceManagerListener listener, boolean useBackup,
                        boolean needStdIcon, boolean needLargeIcon, boolean needAds,
                        boolean needPublic, boolean canRefresh) throws Exception {
        if (canRefresh && needStdIcon) {
            refresh(listener, useBackup);
        } else {
            listener.onUpdated(this);
        }
    }

    @Override
    protected String getResProcessPathSuffix() {
        return DynamicDownloadConf.BIZ_PROCESS_STD_ICON_FILE_FOLDER
                + File.separator + getCode();
    }

    @Override
    protected String getResOriginalPathSuffix() {
        return DynamicDownloadConf.BIZ_ORIGINAL_STD_ICON_FILE_FOLDER
                + File.separator + getCode();
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        return DynamicDownloadConf.BIZ_STD_ICON_FILE_FOLDER + File.separator
                + getCode();
    }

    @Override
    protected String getResOriginalFileName() {
        return getCode() + DynamicDownloadConf.BIZ_RES_IMG_EXTENSION;
    }

    @Override
    public String getResSign() {
        if (getRes() == null || getRes().getStdIcon() == null)
            return null;
        return getRes().getStdIcon().getSign();
    }

    @Override
    public String getResUrl() {
        if (getRes() == null || getRes().getStdIcon() == null)
            return null;
        return getRes().getStdIcon().getDownloadUrl();
    }

    @Override
    protected Bitmap getDefaultBitMap() {
        return OpenDynamicBizHistoryManager.getInstance().getDefaultStdBizIcon();
    }
    
}
