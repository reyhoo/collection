package com.chinaums.opensdk.load.model.url;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.OpenConst.DynamicUmsUrlParam;
import com.chinaums.opensdk.load.model.data.DynamicExtraParam;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;
import com.chinaums.opensdk.util.UriUtils;

import java.net.URI;
import java.util.Map;

/**
 * 所有打开网页或本地activity页面的URL
 */
public abstract class AbsUmsUrl {

    protected static final String PARAM_DATA_KEY = "param";

    /**
     * 请求地址：这个地址是完整的路径。
     */
    private String umsUrl;

    /**
     * 协议
     */
    private String scheme;

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 服务器端口
     */
    private int port;

    /**
     * 访问路径
     */
    private String path;

    /**
     * 请求参数字符串
     */
    private String queryStr;

    /**
     * 请求参数
     */
    private JSONObject data;

    /**
     * 是否全屏
     */
    private Boolean isFullscreen = false;

    /**
     * 是否显示底部工具栏
     */
    private Boolean isShowBottomToolbar = false;

    /**
     * 是否显示区域选择
     */
    private Boolean isShowArea = false;

    public AbsUmsUrl(String umsUrl) {
        this.umsUrl = umsUrl;
        initUri();
        initParam();
        initByCustome();
    }

    private void initUri() {
        try {
            URI uri = UriUtils.format2Uri(getUrl());
            scheme = uri.getScheme();
            host = uri.getHost();
            port = uri.getPort() == -1 ? 80 : uri.getPort();
            path = uri.getPath();
            queryStr = uri.getQuery();
            queryStr = DynamicExtraParam.getInstance()
                    .convertDynamicExtraParam(queryStr);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    private void initParam() {
        try {
            Map<String, String> queryMap = UriUtils
                    .getQueryParamByQueryStr(queryStr);
            if (queryMap == null || queryMap.isEmpty()
                    || !queryMap.containsKey(PARAM_DATA_KEY))
                return;
            String param = queryMap.get(PARAM_DATA_KEY);
            if (UmsStringUtils.isBlank(param)) {
                return;
            }
            data = JSON.parseObject(Base64Utils.decryptBase64(param));
            isFullscreen = data
                    .getBoolean(DynamicUmsUrlParam.PARAM_IS_FULL_SCREEN);
            isShowBottomToolbar = data
                    .getBoolean(DynamicUmsUrlParam.PARAM_IS_SHOW_BOTTOM_TOOLBAR);
            isShowArea = data
                    .getBoolean(DynamicUmsUrlParam.PARAM_IS_SHOW_AREA);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    protected abstract void initByCustome();

    public abstract String generateUmsUrl(String toUrl) throws Exception;

    protected boolean checkEffective() {
        if (UmsStringUtils.isBlank(getScheme())
                || UmsStringUtils.isBlank(getHost())) {
            return false;
        }
        return true;
    }

    protected String getUrl() {
        return umsUrl;
    }

    public abstract String getUmsUrl();

    protected void setIsFullscreen(Boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
    }

    protected void setIsShowBottomToolbar(Boolean isShowBottomToolbar) {
        this.isShowBottomToolbar = isShowBottomToolbar;
    }

    protected void setIsShowArea(Boolean isShowArea) {
        this.isShowArea = isShowArea;
    }

    public Boolean isFullscreen() {
        return isFullscreen == null ? false : isFullscreen;
    }

    public Boolean isShowBottomToolbar() {
        return isShowBottomToolbar == null ? false : isShowBottomToolbar;
    }

    public Boolean isShowArea() {
        return isShowArea == null ? false : isShowArea;
    }

    public JSONObject getData() {
        return data;
    }

    public final String getScheme() {
        return scheme;
    }

    public final String getHost() {
        return host;
    }

    public final int getPort() {
        return port;
    }

    public final String getPath() {
        return path;
    }

    public final String getQueryStr() {
        return queryStr;
    }


}
