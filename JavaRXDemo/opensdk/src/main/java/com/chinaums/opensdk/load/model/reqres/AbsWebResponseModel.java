package com.chinaums.opensdk.load.model.reqres;

import com.alibaba.fastjson.JSON;

public abstract class AbsWebResponseModel implements IWebModel {

    /**
     * 响应报文
     */
    private JSON response;

    /**
     * 默认构造函数
     */
    public AbsWebResponseModel(JSON response) {
        this.response = response;
    }

    public JSON getResponse() {
        return response;
    }

}
