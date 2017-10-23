package com.chinaums.opensdk.load.callback;

import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebResponseModel;


public abstract class AbsDynamicWebCallback implements IDynamicWebCallback {

    @Override
    public String getWebCallback(AbsWebRequestModel requestModel,
                                 AbsWebResponseModel responseModel) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript").append(":");
        sb.append(getCallbackName(requestModel, responseModel));
        sb.append("(");
        sb.append(getCallbackParam(requestModel, responseModel));
        sb.append(")");
        return sb.toString();
    }

    /**
     * 得到响应的函数名称
     */
    protected abstract String getCallbackName(AbsWebRequestModel requestModel,
                                              AbsWebResponseModel responseModel) throws Exception;

    protected abstract String getCallbackParam(AbsWebRequestModel requestModel,
                                               AbsWebResponseModel responseModel) throws Exception;

}
