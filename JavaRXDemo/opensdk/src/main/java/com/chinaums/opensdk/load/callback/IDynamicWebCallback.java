package com.chinaums.opensdk.load.callback;

import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebResponseModel;


public interface IDynamicWebCallback {
    
    public String getWebCallback(AbsWebRequestModel requestModel,
                                 AbsWebResponseModel responseModel) throws Exception;

}
