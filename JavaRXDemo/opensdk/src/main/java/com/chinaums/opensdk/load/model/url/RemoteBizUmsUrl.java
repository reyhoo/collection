package com.chinaums.opensdk.load.model.url;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.BizUrlUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

/**
 * 类 RemoteBizUmsUrl 的实现描述：
 * 类实现描述：这个URL用于跳转到仿真页面方便资源包调试：ums://http/host:port/page?param=业务参数
 */
public class RemoteBizUmsUrl extends AbsUmsUrl {

    public RemoteBizUmsUrl(String umsUrl) {
        super(umsUrl);
    }

    @Override
    protected void initByCustome() {

    }

    @Override
    public String generateUmsUrl(String toUrl) throws Exception {
        StringBuilder ret = new StringBuilder(50);
        if (UmsStringUtils.isBlank(toUrl) || !BizUrlUtils.isRelativeBiz(toUrl)) {
            return toUrl;
        }
        ret.append(getUrlPrefix()).append(toUrl);
        return ret.toString();
    }

    public String getUrlPrefix() {
        String urlPrefix = "";
        try {
            String url = getUrl();
            url = url.substring(0, url.indexOf("?"));
            url = url.substring(0, url.lastIndexOf("/"));
            urlPrefix = url + "/";
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return urlPrefix;
    }

    public JSONObject getUrlParam() {
        JSONObject jsonObject = null;
        try {
            String url = getUrl();
            url = url.substring(url.indexOf("?"));
            url = url.replaceAll("param=", "");
            jsonObject = JSON.parseObject(Base64Utils.decryptBase64(url));
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return jsonObject;
    }

    @Override
    public String getUmsUrl() {
        try {
            if (!checkEffective()) {
                return null;
            } else {
                return getUrl().replaceAll(BizUrlUtils.getRemoteBizStartFlag(),
                        BizUrlUtils.getRemoteHttpWebStartFlag());
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return getUmsUrl();
    }
    
}
