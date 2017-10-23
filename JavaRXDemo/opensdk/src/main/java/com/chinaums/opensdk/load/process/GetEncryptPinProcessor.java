package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.load.view.AbsBizWebView.DialogCallback;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

public class GetEncryptPinProcessor extends AbsStdDynamicProcessor {

    @Override
    protected void execute(DynamicWebModel model) throws Exception {
        try {
            GetEncryptPinRequestModel requestModel = (GetEncryptPinRequestModel) model
                    .getRequestModel();
            if (requestModel == null)
                throw new Exception("我去，怎么没有，肯定哪里错了.");
            openDialog(model, requestModel);
        } catch (Exception e) {
            apiRunExceptionCallBack(model, e);
        }
    }

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        String encryptInfo = null;
        String respCode = null;
        String respMsg = null;
        try {
            Bundle bundle = data.getExtras();
            encryptInfo = bundle.getString("encryptInfo");
            respCode = bundle.getString("respCode");
            respMsg = bundle.getString("respMsg");
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        if (resultCode == Activity.RESULT_OK) {
            GetEncryptPinResponse resp = new GetEncryptPinResponse();
            resp.encryptedContent = encryptInfo;
            setRespAndCallWeb(model,
                    createSuccessResponse(JsonUtils
                            .convert2Json(resp)));
        } else {
            setRespAndCallWeb(
                    model,
                    createErrorResponse("未获取到密码",
                            DynamicCallback.CALLBACK_STATE_ERROR));
        }
    }

    /**
     * 打开对话框
     */
    private void openDialog(final DynamicWebModel model,
                            final GetEncryptPinRequestModel requestModel) {
        ((AbsBizWebView) (model.getWebView())).openInputPinDialog(
                model.getActivity(), requestModel.getSecurityKeyType(),
                requestModel.getInputMinLength(),
                requestModel.getInputMaxLength(), requestModel.getInputType(),
                requestModel.getMainAccount(), requestModel.getCalcFactor(),
                new DialogCallback() {
                    @Override
                    public void onCallback(Context context, String contentStr) {
                        GetEncryptPinResponse resp = new GetEncryptPinResponse();
                        if (UmsStringUtils.hasValue(contentStr)) {
                            resp.setEncryptedContent(contentStr);
                            setRespAndCallWeb(model,
                                    createSuccessResponse(JsonUtils
                                            .convert2Json(resp)));
                        } else {
                            setRespAndCallWeb(
                                    model,
                                    createErrorResponse(
                                            "未获取到密码",
                                            DynamicCallback.CALLBACK_STATE_ERROR));
                        }
                    }
                });
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new GetEncryptPinRequestModel(model.getRequestObj());
    }

    @Override
    public int getType() {
        return DynamicProcessorType.GET_ENCRYPT_PIN;
    }

    private class GetEncryptPinRequestModel extends AbsWebRequestModel {

        /**
         * 密钥类型，标示密码键盘初始化时使用的公钥所属平台。值如下：100001代表开放平台前置，100002代表账户前置
         */
        private String securityKeyType;

        /**
         * 可允许输入的值的最小长度
         */
        private String inputMinLength;

        /**
         * 可允许输入的值的最大长度
         */
        private String inputMaxLength;

        /**
         * 可允许输入的字符类型，是一个4位的字符串，每位代表一种类型是否可以输入。从左到右第一位代表是否接受数字，第二位代表是否接受字母，
         * 第三位代表是否接受特殊字符（如.%!@等），第四位代表是否接受中文。如：1100代表接受数字和字母
         */
        private String inputType;

        /**
         * 主账户，配合caclFactor使用。该内容可能是卡号，可能是账户号，根据securityKeyType和实际的需要设置。
         */
        private String mainAccount;

        /**
         * 计算因子。0：不变换格式；1：不带主账号X9.8格式；2：带主账号的X9.8格式
         */
        private String calcFactor;

        public GetEncryptPinRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            JSONObject data = getRequest().getJSONObject("data");
            securityKeyType = data.getString("securityKeyType");
            inputMinLength = data.getString("inputMinLength");
            inputMaxLength = data.getString("inputMaxLength");
            inputType = data.getString("inputType");
            mainAccount = data.getString("mainAccount");
            calcFactor = data.getString("calcFactor");
        }

        public String getSecurityKeyType() {
            return securityKeyType;
        }

        public String getInputMinLength() {
            return inputMinLength;
        }

        public String getInputMaxLength() {
            return inputMaxLength;
        }

        public String getInputType() {
            return inputType;
        }

        public String getMainAccount() {
            return mainAccount;
        }

        public String getCalcFactor() {
            return calcFactor;
        }

    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    private class GetEncryptPinResponse {

        /**
         * 密码密文
         */
        private String encryptedContent;

        public String getEncryptedContent() {
            return encryptedContent;
        }

        public void setEncryptedContent(String encryptedContent) {
            this.encryptedContent = encryptedContent;
        }

    }
}
