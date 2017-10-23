package com.chinaums.opensdk.download.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicBizShowState;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;


public class LocalAppPack extends AppPack {

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
    protected boolean initPack() throws Exception {
        return true;
    }

    @Override
    protected boolean initCustom() throws Exception {
        return true;
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
        if (json.containsKey("bizCode")) {
            json.put("openBizCode", json.getString("bizCode"));
        } else {
            json.put("bizCode", this.getCode());
        }
        json.put("confMenu", this.getConfMenu());
        json.put("bizName", this.getName());
        String url = json.getString("url");
        json.remove("url");
        return url + "?param=" + Base64Utils.encrypt(json.toJSONString());
    }

}
