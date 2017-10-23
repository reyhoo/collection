package com.chinaums.opensdk.download.model;

import android.util.Log;

import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.download.process.ResourceManager;
import com.chinaums.opensdk.exception.ResourceCheckOriginalException;
import com.chinaums.opensdk.load.model.data.DynamicExtraParam;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsMessageDigestUtils;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 这个文件里面定义的字段全部都是全资源列表中的各种类型的资源包的属性信息内存化
 */
@SuppressWarnings("serial")
public abstract class BasePack extends AbsPack {

    /**
     * apiLevel
     */
    private Integer apiLevel;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 参数是一个json格式（这个参数是全资源列表中，对某个资源包的属性信息里面的params字段）
     */
    private String params;

    /**
     * 提示
     */
    private String tip;

    /**
     * 备注
     */
    private String description;

    /**
     * 是否必须更新
     */
    private boolean mustUpdate;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 依赖索引
     */
    private BizSearchIndex searchIndex;

    /**
     * 资源
     */
    private BizResources res;

    /**
     * 产品ID
     */
    private String appId;

    /**
     * 置换参数中的参数代码
     */
    public String getOpenUrl() {
        String tempParams = null;
        try {
            if (UmsStringUtils.isNotBlank(this.params)) {
                tempParams = DynamicExtraParam.getInstance()
                        .getDynamicExtraParam(this.params);
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return getOpenUrlBySelfParam(tempParams);
    }

    /**
     * 置换参数中的参数代码
     */
    public String getOpenUrl(String params) {
        String tempParams = null;
        try {
            if (UmsStringUtils.isNotBlank(params)) {
                tempParams = DynamicExtraParam.getInstance()
                        .getDynamicExtraParam(params);
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return getOpenUrlBySelfParam(tempParams);
    }

    protected abstract String getOpenUrlBySelfParam(String param);

    public abstract Class<?>[] getDependentClasses() throws Exception;

    public abstract String getResSign();

    public abstract String getResUrl();

    public abstract void refresh(final ResourceManagerListener listener,
                                 boolean useBackup, boolean needStdIcon, boolean needLargeIcon,
                                 boolean needAds, boolean needPublic, boolean canRefresh)
            throws Exception;

    @Override
    public void onError(String msg, Exception e,
                        ResourceManagerListener listener) throws Exception {
        super.onError(msg, e, listener);
        changedShowState(false, false);
    }

    @Override
    public void onFinish(ResourceManagerListener listener) throws Exception {
        super.onFinish(listener);
        changedShowState(true, false);
    }

    @Override
    protected void initVerify(ResourceManagerListener listener)
            throws Exception {
        return;
    }

    @Override
    protected String getPrintLog(String msg) {
        return this.getClass().toString() + " [" + getName() + "(" + getCode()
                + ")] " + msg;
    }

    @Override
    public void onProgress(String msg, int progress,
                           final ResourceManagerListener listener) {
        listener.onProgress(this, msg, progress);
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
    protected String getResOriginalPathSuffix() {
        // eg. "/download/res/" + getCode()
        return DynamicDownloadConf.BIZ_ORIGINAL_RES_FILE_FOLDER
                + File.separator + getCode();
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        // eg. "/res/" + getCode()
        return DynamicDownloadConf.BIZ_RES_FILE_FOLDER + File.separator
                + getCode();
    }

    @Override
    protected String getResProcessPathSuffix() {
        // eg. "/process/res/" + getCode()
        return DynamicDownloadConf.BIZ_PROCESS_RES_FILE_FOLDER + File.separator
                + getCode();
    }

    @Override
    protected String getResOriginalFileName() {
        // eg. getCode() + ".zip"
        return getCode() + DynamicDownloadConf.BIZ_RES_FILE_EXTENSION;
    }

    @Override
    public boolean check() throws Exception {
        log(Log.DEBUG, "开始校验.");
        if (!checkIsMonitoring()) {
            log(Log.DEBUG, "监控校验失败.");
            return false;
        }
        log(Log.DEBUG, "监控校验成功.");
        if (!ResourceManager.getInstance().checkDependent(
                getResourceWorkspace(), this)) {
            log(Log.DEBUG, "依赖文件校验失败.");
            return false;
        }
        return true;
    }

    @Override
    protected String getPreloadResSign() throws Exception {
        return getResSign();
    }

    @Override
    protected String getHistoryResSign() throws Exception {
        return getResSign();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getParams() {
        return params;
    }

    public String getTip() {
        return tip;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMustUpdate() {
        return mustUpdate;
    }

    public BizSearchIndex getSearchIndex() {
        return searchIndex;
    }

    public BizResources getRes() {
        return res;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMustUpdate(boolean mustUpdate) {
        this.mustUpdate = mustUpdate;
    }

    public void setSearchIndex(BizSearchIndex searchIndex) {
        this.searchIndex = searchIndex;
    }

    public void setRes(BizResources res) {
        this.res = res;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Integer getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(Integer apiLevel) {
        this.apiLevel = apiLevel;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public static class BizSearchIndex {

        public static final String SEARCH_INDEX_KEY_CATEGORY = "category";

        /**
         * categoryCodes
         */
        private String categoryCodes;

        public String getCategoryCodes() {
            return categoryCodes;
        }

        public Set<String> getSearchCodeSet(String searchIndex)
                throws Exception {
            Set<String> set = new HashSet<String>();
            if (UmsStringUtils.isBlank(searchIndex))
                return set;
            String values = "";
            if (SEARCH_INDEX_KEY_CATEGORY.equals(searchIndex)) {
                values = getCategoryCodes();
            }
            if (UmsStringUtils.isBlank(values))
                return set;
            String[] array = values.split(",");
            for (String temp : array) {
                set.add(temp);
            }
            return set;
        }

        public boolean contains(String searchIndex, String value)
                throws Exception {
            if (UmsStringUtils.isBlank(value)) {
                return false;
            }
            Set<String> set = getSearchCodeSet(searchIndex);
            if (set == null || set.isEmpty()) {
                return false;
            }
            return set.contains(value);
        }

        public void setCategoryCodes(String categoryCodes) {
            this.categoryCodes = categoryCodes;
        }

    }

    public static class BizResourceDetail implements Serializable {

        private static final long serialVersionUID = -1967554253249236729L;

        /**
         * downloadUrl
         */
        private String downloadUrl;

        /**
         * sign
         */
        private String sign;

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public String getSign() {
            return sign;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

    }

    public static class BizResources implements Serializable {

        private static final long serialVersionUID = -8374146773501657224L;

        /**
         * stdIcon
         */
        private BizResourceDetail stdIcon;

        /**
         * largeIcon
         */
        private BizResourceDetail largeIcon;

        /**
         * bizPackage
         */
        private BizResourceDetail bizPackage;

        /**
         * ads
         */
        private BizResourceDetail ads;

        public BizResourceDetail getStdIcon() {
            return stdIcon;
        }

        public BizResourceDetail getLargeIcon() {
            return largeIcon;
        }

        public BizResourceDetail getBizPackage() {
            return bizPackage;
        }

        public BizResourceDetail getAds() {
            return ads;
        }

        public void setStdIcon(BizResourceDetail stdIcon) {
            this.stdIcon = stdIcon;
        }

        public void setLargeIcon(BizResourceDetail largeIcon) {
            this.largeIcon = largeIcon;
        }

        public void setBizPackage(BizResourceDetail bizPackage) {
            this.bizPackage = bizPackage;
        }

        public void setAds(BizResourceDetail ads) {
            this.ads = ads;
        }

    }

}
