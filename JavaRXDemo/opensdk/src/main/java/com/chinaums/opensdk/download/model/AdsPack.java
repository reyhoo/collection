package com.chinaums.opensdk.download.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.File;

public final class AdsPack extends ShowPack {

    /**
     * group
     */
    private String group;

    /**
     * 广告优先级
     */
    private Integer order;

    /**
     * openType
     */
    private String openType;

    /**
     * openUrl
     */
    private String openUrl;

    /**
     * size
     */
    private Integer size;

    /**
     * needLogin
     */
    private boolean needLogin;


    @Override
    public Bitmap getBitMap() {
        try {
            byte[] data = UmsFileUtils.readFile(getResOriginalPath(),
                    getResOriginalFileName());
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            UmsLog.e("", e);
            return null;
        }
    }

    @Override
    public void refresh(ResourceManagerListener listener, boolean useBackup,
                        boolean needStdIcon, boolean needLargeIcon, boolean needAds,
                        boolean needPublic, boolean canRefresh) throws Exception {
        if (canRefresh && needAds) {
            refresh(listener, useBackup);
        } else {
            listener.onUpdated(this);
        }
    }

    @Override
    protected String getResProcessPathSuffix() {
        return DynamicDownloadConf.BIZ_PROCESS_ADS_FILE_FOLDER + File.separator
                + getCode();
    }

    @Override
    protected String getResOriginalPathSuffix() {
        return DynamicDownloadConf.BIZ_ORIGINAL_ADS_FILE_FOLDER
                + File.separator + getCode();
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        return DynamicDownloadConf.BIZ_ADS_FILE_FOLDER + File.separator
                + getCode();
    }

    @Override
    protected String getResOriginalFileName() {
        return getCode() + DynamicDownloadConf.BIZ_RES_IMG_EXTENSION;
    }

    /*
     * "group":"welcome", "order":1, "openType":"self",
     * "openUrl":"http://www.chinaums.com/index.html", "size":1,
     * "needLogin":false
     */
    @Override
    protected boolean initPack() throws Exception {
        String params = getParams();
        if (UmsStringUtils.isBlank(params))
            throw new Exception(getName() + "的配置参数为空.");
        JSONObject paramsObj = JsonUtils.convert2Json(params);
        if (paramsObj == null)
            throw new Exception(getName() + "的配置参数错误,没有办法转换.");
        this.group = paramsObj.getString("group");
        this.order = paramsObj.getIntValue("order");
        this.openType = paramsObj.getString("openType");
        this.openUrl = paramsObj.getString("openUrl");
        this.size = paramsObj.getIntValue("size");
        this.needLogin = paramsObj.getBoolean("needLogin");
        return true;
    }

    @Override
    public String getResSign() {
        if (getRes() == null || getRes().getAds() == null)
            return null;
        return getRes().getAds().getSign();
    }

    @Override
    public String getResUrl() {
        if (getRes() == null || getRes().getAds() == null)
            return null;
        return getRes().getAds().getDownloadUrl();
    }

    public String getGroup() {
        return group;
    }

    public Integer getOrder() {
        return order;
    }

    public String getOpenType() {
        return openType;
    }

    public String getOpenUrl() {
        return openUrl;
    }

    public Integer getSize() {
        return size;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public String getImageUrl() {
        return "file:///" + getResOriginalPath() + File.separator + getResOriginalFileName();
    }

}
