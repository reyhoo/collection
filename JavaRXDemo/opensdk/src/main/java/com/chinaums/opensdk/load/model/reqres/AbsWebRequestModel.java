package com.chinaums.opensdk.load.model.reqres;

import com.alibaba.fastjson.JSONObject;

public abstract class AbsWebRequestModel implements IWebModel {

    /**
     * 请求对象
     */
    private final JSONObject request;

    /**
     * 构造函数
     */
    public AbsWebRequestModel(JSONObject request) {
        this.request = request;
        initByRequest();
    }

    /**
     * 根据请求对象初始化其它参数
     */
    protected abstract void initByRequest();

    public JSONObject getRequest() {
        return request;
    }

}
