package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.load.view.AbsBizWebView.RegisterStatus;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;


public class RegisterExtProcessorProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {

    }

    @Override
    public int getType() {
        return DynamicProcessorType.REGISTER_EXTRA_API;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

    @Override
    protected void execute(DynamicWebModel model) throws Exception {

        final RegisterExtProcessorRequestModel requestModel = (RegisterExtProcessorRequestModel) model
                .getRequestModel();
        if (requestModel == null)
            throw new Exception("AbsWebRequestModel == null");
        AbsBizWebView webview = (AbsBizWebView) model.getWebView();
        RegisterStatus status = webview.injectExtProcessorAndAction(
                requestModel.getType(), requestModel.getAction(),
                requestModel.getReflectClazz());
        RegisterResult result = new RegisterResult();
        if (status == RegisterStatus.SUCCESS) {
            result.setErrCode("0000");
            result.setErrInfo("注册成功");
        } else if (status == RegisterStatus.NOT_FOUND_REFLECTCLAZZ) {
            result.setErrCode("0001");
            result.setErrInfo("未找到reflectClazz映射");
        } else if (status == RegisterStatus.ACTION_REGISTERED) {
            result.setErrCode("0003");
            result.setErrInfo("type下的action已注册");
        } else {
            result.setErrCode("9999");
            result.setErrInfo("注册失败");
        }
        setRespAndCallWeb(model,
                createSuccessResponse(JsonUtils.convert2Json(result)));
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new RegisterExtProcessorRequestModel(model.getRequestObj());
    }

    private class RegisterExtProcessorRequestModel extends AbsWebRequestModel {

        /**
         * type为自定义的某一个具体交互的操作类型，该操作类型为数字类型，标识是打电话、弹出对话框等操作。
         */
        private int type;

        /**
         * action为该操作类型的某一个具体操作事件，如刷卡操作类型中的刷卡或二次授权等。主要用于进行共享操作类型里的变量。
         */
        private String action;

        /**
         * reflectClazz
         */
        private String reflectClazz;

        public RegisterExtProcessorRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                JSONObject data = getRequest().getJSONObject("data");
                type = data.getIntValue("type");
                action = data.getString("action");
                reflectClazz = data.getString("reflectClazz");
                if (UmsStringUtils.isBlank(reflectClazz))
                    throw new Exception("API运行参数不全");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getReflectClazz() {
            return reflectClazz;
        }

        public void setReflectClazz(String reflectClazz) {
            this.reflectClazz = reflectClazz;
        }

    }

    private class RegisterResult {

        /**
         * 注册状态码状态，0000为成功，0001为未找到reflectClazz映射，0002为type已注册，0003
         * 为type下的action已注册，其它都为错误
         */
        private String errCode;

        /**
         * 状态码的中文描述
         */
        private String errInfo;

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getErrInfo() {
            return errInfo;
        }

        public void setErrInfo(String errInfo) {
            this.errInfo = errInfo;
        }

    }

}
