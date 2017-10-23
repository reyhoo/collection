package com.chinaums.opensdk.load.callback;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebResponseModel;
import com.chinaums.opensdk.util.Base64Utils;

public abstract class AbsStdDynamicWebCallback extends AbsDynamicWebCallback {

    @Override
    protected String getCallbackParam(AbsWebRequestModel requestModel,
                                      AbsWebResponseModel responseModel) throws Exception {
        JSONObject ret = new JSONObject();
        ret.put("info", requestModel.getRequest().getJSONObject("info"));
        ret.put("data", responseModel.getResponse());
        return "'" + Base64Utils.encrypt(ret.toString()) + "'";
    }
    
}
