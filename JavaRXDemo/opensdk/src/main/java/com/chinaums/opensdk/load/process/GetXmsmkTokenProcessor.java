package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.process.listener.DynamicAPICallback;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

/**
 * 类 GetXmsmkTokenProcessor 的实现描述：得到用户token
 */
public class GetXmsmkTokenProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        if (UmsStringUtils.isNotBlank(data
                .getStringExtra("result"))) {
            JSONObject json = JsonUtils.convert2Json(data
                    .getStringExtra("result"));
            String errorCode = json.getString("errCode");
            String errorInfo = json.getString("errInfo");
            if (UmsStringUtils.isNotBlank(errorCode)) {
                if ("0000".equals(errorCode)) {
                    setRespAndCallWeb(model, createSuccessResponse(json));
                } else {
                    setRespAndCallWeb(model, createErrorResponse(errorCode, errorInfo, OpenConst.DynamicCallback.CALLBACK_STATE_ERROR));
                }
                return;
            }
        }
        setRespAndCallWeb(model, createErrorResponse("获取token失败",
                OpenConst.DynamicCallback.CALLBACK_STATE_ERROR));
    }

    @Override
    public int getType() {
        return DynamicProcessorType.GET_XMSMK_TOKEN;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        final DynamicAPICallback callback = new DynamicAPICallback() {
            @Override
            public void onAPICallback(int resultCode, Intent data) {
                try {
                    onCallback(model, resultCode, data);
                } catch (Exception e) {
                    apiRunExceptionCallBack(model, e);
                }
            }
        };

        model.getHandler().post(new Runnable() {
            public void run() {
                try {
                    GetXmsmkWebRequestModel requestModel = (GetXmsmkWebRequestModel) model.getRequestModel();
                    OpenDelegateManager.getProcessDelegate().getXmsmkToken(model.getActivity(), requestModel.tokenType, callback);
                } catch (Exception e) {
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new GetXmsmkTokenProcessor.GetXmsmkWebRequestModel(model.getRequestObj());
    }

    private class GetXmsmkWebRequestModel extends AbsWebRequestModel {
        
        /**
         * token类型，由第三方传入
         */
        private String tokenType;

        public GetXmsmkWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                JSONObject data = getRequest().getJSONObject("data");
                tokenType = data.getString("tokenType");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }
    }

}
