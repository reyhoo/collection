package com.chinaums.opensdk.download.model;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsStringUtils;


public abstract class AppPack extends BizPack {


    protected abstract boolean initCustom() throws Exception;

    @Override
    protected boolean initPack() throws Exception {
        String params = getParams();
        if (UmsStringUtils.isBlank(params))
            throw new Exception(getName() + "的配置参数为空.");
        JSONObject paramsObj = JsonUtils.convert2Json(params);
        if (paramsObj == null)
            throw new Exception(getName() + "的配置参数错误,没有办法转换.");
        return initCustom();
    }

    @Override
    public Class<?>[] getDependentClasses() throws Exception {
        return null;
    }

    @Override
    protected boolean initResProcessByOriginal() throws Exception {
        return true;
    }

    @Override
    public String getResSign() {
        return null;
    }

    @Override
    public String getResUrl() {
        return null;
    }

}
