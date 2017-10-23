package com.chinaums.opensdk.load.model.url;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.OpenConst.CHAR;
import com.chinaums.opensdk.cons.OpenConst.DynamicUmsUrlParam;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;
import com.chinaums.opensdk.util.UriUtils;

import java.util.Map;

/**
 * 类 RemoteWebUmsUrl 的实现描述：比如打开www.baidu.com页面
 */
public class RemoteWebUmsUrl extends AbsUmsUrl {

    /**
     * url参数
     */
    private Map<String, String> urlQueryMap;

    /**
     * 开放平台拼装参数
     */
    private JSONObject umsOpenParam;

    public RemoteWebUmsUrl(String umsUrl) {
        super(umsUrl);
    }

    @Override
    protected void initByCustome() {
        try {
            urlQueryMap = UriUtils.getQueryParam(getUrl());
            if (urlQueryMap == null || urlQueryMap.isEmpty())
                return;
            String umsOpenParamString = getUrlParam(DynamicUmsUrlParam.PARAM_UMS_OPEN);
            if (!UmsStringUtils.isBlank(umsOpenParamString)) {
                umsOpenParam = JSON.parseObject(Base64Utils
                        .decryptBase64(umsOpenParamString));
            }
            if (CHAR.TRUE.equalsIgnoreCase(urlQueryMap
                    .get(DynamicUmsUrlParam.PARAM_IS_FULL_SCREEN))) {
                setIsFullscreen(true);
            }
            if (CHAR.TRUE.equalsIgnoreCase(urlQueryMap
                    .get(DynamicUmsUrlParam.PARAM_IS_SHOW_BOTTOM_TOOLBAR))) {
                setIsShowBottomToolbar(true);
            }
            if (CHAR.TRUE.equalsIgnoreCase(urlQueryMap
                    .get(DynamicUmsUrlParam.PARAM_IS_SHOW_AREA))) {
                setIsShowArea(true);
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    /**
     * 获取第三方url的参数
     */
    public String getUrlParam(String key) {
        if (urlQueryMap == null) {
            return null;
        }
        return urlQueryMap.get(key);
    }

    /**
     * 获取UmsOpenParam的参数
     */
    public Object getCustomeWebViewUrlParam(String key) {
        if (umsOpenParam == null) {
            return null;
        }
        return umsOpenParam.get(key);
    }

    @Override
    public String generateUmsUrl(String toUrl) throws Exception {
        return toUrl;
    }

    @Override
    public String getUmsUrl() {
        if (!checkEffective()) {
            return null;
        } else if (getUrl().endsWith("?param=e30=")) {
            return getUrl().split("\\?param=e30=")[0];
        } else {
            return getUrl();
        }
    }

}
