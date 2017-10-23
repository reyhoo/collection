package com.chinaums.opensdk.download.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicBizShowState;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.util.Iterator;


public class RemoteWeexPack extends WebPack {

    @Override
    public void refresh(ResourceManagerListener listener, boolean useBackup)
            throws Exception {
        changedShowState(true, false);
        listener.onUpdated(this);
    }

    @Override
    public boolean checkOriginalFile() throws Exception {
        return true;
    }

    @Override
    public void prepare(ResourceManagerListener listener, boolean useBackup)
            throws Exception {
        onFinish(listener);
    }

    @Override
    public Class<?>[] getDependentClasses() throws Exception {
        return null;
    }

    @Override
    protected String getOpenUrlBySelfParam(String param) {
        try {
            JSONObject jsonObject = null;
            if (UmsStringUtils.isNotBlank(param)) {
                jsonObject = JSON.parseObject(param);
            }
            return getUrl(jsonObject);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    @Override
    public String getResSign() {
        return null;
    }

    @Override
    public String getResUrl() {
        return null;
    }

    @Override
    protected boolean initResProcessByOriginal() throws Exception {
        return true;
    }

    @Override
    protected void changedShowState(boolean isPrepareSuccess, boolean isPreload)
            throws Exception {
        DynamicBizShowState bizShowState = DynamicBizShowState
                .getByState(getStatus());
        setStatus(bizShowState.toString());
    }

    /**
     * 得到可跳转的地址
     */
    private String getUrl(JSONObject json) throws Exception {
        if (json == null || json.get("url") == null
                || UmsStringUtils.isBlank(json.getString("url")))
            return "";
        String url = json.getString("url");
        String urlParams = "";
        for (Iterator<?> iter = json.keySet().iterator(); iter.hasNext(); ) {
            String key = iter.next().toString();
            if ("url".equals(key))
                continue;
            urlParams += key + "=" + json.getString(key) + "&";
        }
        if (UmsStringUtils.isBlank(urlParams)) {
            return url;
        } else {
            return url + (url.indexOf("?") == -1 ? "?" : "&") + urlParams;
        }
    }

}
