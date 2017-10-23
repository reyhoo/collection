package com.chinaums.opensdk.download.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.exception.ResourceCheckOriginalException;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsMessageDigestUtils;

import java.io.File;

@SuppressWarnings("serial")
public class CategoryIconPack extends AbsPack {

    /**
     * categoryCode
     */
    private String categoryCode;

    public CategoryIconPack() {

    }

    public CategoryIconPack(String categoryCode, String resSign, String resUrl) {
        setCategoryCode(categoryCode);
        setResSign(resSign);
        setResUrl(resUrl);
    }

    @Override
    public void onProgress(String msg, int progress,
                           ResourceManagerListener listener) {
        listener.onProgress(this, msg, progress);
    }

    @Override
    public boolean check() throws Exception {
        log(Log.DEBUG, "开始校验.");
        if (!checkIsMonitoring()) {
            log(Log.DEBUG, "监控校验失败.");
            return false;
        }
        log(Log.DEBUG, "监控校验成功.");
        return true;
    }

    @Override
    protected String getPrintLog(String msg) {
        return this.getClass().toString() + " [" + getCategoryCode() + "] "
                + msg;
    }

    @Override
    protected String getResOriginalPathSuffix() {
        // eg. "/download/img/category/" + getCategoryCode()
        return DynamicDownloadConf.BIZ_ORIGINAL_CATEGORY_IMG_FILE_FOLDER
                + File.separator + getCategoryCode();
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        // eg."/img/category" + getCategoryCode()
        return DynamicDownloadConf.BIZ_CATEGORY_IMG_FILE_FOLDER
                + File.separator + getCategoryCode();
    }

    @Override
    protected String getResProcessPathSuffix() {
        // eg. "/process/img/category" + getCategoryCode()
        return DynamicDownloadConf.BIZ_PROCESS_CATEGORY_IMG_FILE_FOLDER
                + File.separator + getCategoryCode();
    }

    @Override
    protected String getResOriginalFileName() {
        return getCategoryCode() + DynamicDownloadConf.BIZ_RES_IMG_EXTENSION;
    }

    @Override
    protected void verify(byte[] data) throws Exception {
        boolean isSuccess = UmsMessageDigestUtils.encode(data)
                .equalsIgnoreCase(getResSign());
        if (!isSuccess) {
            throw new ResourceCheckOriginalException("原始文件验证失败.");
        }
    }

    @Override
    protected void initVerify(ResourceManagerListener listener)
            throws Exception {
        return;
    }

    @Override
    protected boolean initPack() throws Exception {
        return true;
    }

    @Override
    protected boolean initResProcessByOriginal() throws Exception {
        return copyResOriginal2Process();
    }

    @Override
    protected String getPreloadResSign() throws Exception {
        return getResSign();
    }

    @Override
    protected String getHistoryResSign() throws Exception {
        return getResSign();
    }

    @Override
    protected void changedShowState(boolean isPrepareSuccess, boolean isPreload)
            throws Exception {
        return;
    }

    public Bitmap getBitMap() {
        try {
            byte[] data = UmsFileUtils.readFile(getResOriginalPath(),
                    getResOriginalFileName());
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            return OpenDynamicBizHistoryManager.getInstance()
                    .getDefaultCategoryIcon();
        }
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

}
