package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;


public class PublicSetResultProcessor extends AbsStdDynamicProcessor {

    /**
     * 全局变量界面返回传递数据
     */
    private JSONObject result;

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {

    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new PublicSetResultWebRequestModel(model.getRequestObj());
    }

    @Override
    public int getType() {
        return DynamicProcessorType.PUBLIC_SET_RESULT;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        try {
            PublicSetResultWebRequestModel requestModel = (PublicSetResultWebRequestModel) model
                    .getRequestModel();
            if (requestModel == null)
                throw new Exception("PublicSetResultWebRequestModel == null");
            String action = requestModel.getAction();
            if (action.equals("get")) {
                setRespAndCallWeb(model,
                        createSuccessResponse(result == null ? new JSONObject()
                                : result));
            } else if (action.equals("set")) {
                this.result = requestModel.getResult();
                setRespAndCallWeb(model, createSuccessResponse(result));
            }
        } catch (Exception e) {
            UmsLog.e("", e);
            apiRunExceptionCallBack(model, e);
        }
    }

    private class PublicSetResultWebRequestModel extends AbsWebRequestModel {

        /**
         * 存储的json对象
         */
        private JSONObject result;

        /**
         * 操作事件
         */
        private String action;

        public PublicSetResultWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                result = getRequest().getJSONObject("data");
                action = getRequest().getJSONObject("info").getString("action");
                if (UmsStringUtils.isBlank(action))
                    throw new Exception("API运行参数不全");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public final JSONObject getResult() {
            return result;
        }

        public String getAction() {
            return action;
        }

    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

}
