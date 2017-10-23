package com.chinaums.opensdk.download.model;

import android.graphics.Bitmap;

import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;

import java.io.File;


public class LargeIconPack extends IconPack {

    @Override
    public void refresh(ResourceManagerListener listener, boolean useBackup,
                        boolean needStdIcon, boolean needLargeIcon, boolean needAds,
                        boolean needPublic, boolean canRefresh) throws Exception {
        if (canRefresh && needLargeIcon) {
            refresh(listener, useBackup);
        } else {
            listener.onUpdated(this);
        }
    }

    @Override
    protected String getResProcessPathSuffix() {
        return DynamicDownloadConf.BIZ_PROCESS_LARGE_ICON_FILE_FOLDER
                + File.separator + getCode();
    }

    @Override
    protected String getResOriginalPathSuffix() {
        return DynamicDownloadConf.BIZ_ORIGINAL_LARGE_ICON_FILE_FOLDER
                + File.separator + getCode();
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        return DynamicDownloadConf.BIZ_LARGE_ICON_FILE_FOLDER + File.separator
                + getCode();
    }

    @Override
    protected String getResOriginalFileName() {
        return getCode() + DynamicDownloadConf.BIZ_RES_IMG_EXTENSION;
    }

    @Override
    public String getResSign() {
        if (getRes() == null || getRes().getLargeIcon() == null)
            return null;
        return getRes().getLargeIcon().getSign();
    }

    @Override
    public String getResUrl() {
        if (getRes() == null || getRes().getLargeIcon() == null)
            return null;
        return getRes().getLargeIcon().getDownloadUrl();
    }

    @Override
    protected Bitmap getDefaultBitMap() {
        return OpenDynamicBizHistoryManager.getInstance().getDefaultLargeBizIcon();
    }

}
