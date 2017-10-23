package com.chinaums.opensdk.load.process;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.process.listener.DynamicAPICallback;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.JsonUtils;

/**
 * 第三方快捷支付API
 */
public class FastPayExtProcessor extends AbsStdDynamicProcessor {

    public static final int FAST_PAY_RESULT = 1244;

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        switch (resultCode) {
            case FAST_PAY_RESULT:
                setRespAndCallWeb(model,
                        createSuccessResponse(JsonUtils.convert2Json(data
                                .getStringExtra("result"))));
                break;
            default:
                setRespAndCallWeb(
                        model,
                        createErrorResponse("支付失败",
                                DynamicCallback.CALLBACK_STATE_ERROR));
                break;
        }
    }

    @Override
    public int getType() {
        return DynamicProcessorType.NAVIGATOR_PAGE_QUICK_PAY;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        FastPayRequestModel requestModel = (FastPayRequestModel) model
                .getRequestModel();
        final Bundle bundle = new Bundle();
        bundle.putString("mobile", requestModel.getMobile());
        bundle.putString("merchantId", requestModel.getMerchantId());
        bundle.putString("merchantUserId", requestModel.getMerchantUserId());
        bundle.putString("sign", requestModel.getSign());
        bundle.putString("mode", requestModel.getMode());
        bundle.putString("merOrderId", requestModel.getMerOrderId());
        bundle.putString("amount", requestModel.getAmount());
        bundle.putString("agentMerchantId", requestModel.getAgentMerchantId());
        bundle.putString("notifyUrl", requestModel.getNotifyUrl());
        bundle.putString("signType", requestModel.getSignType());

        model.getHandler().post(new Runnable() {
            public void run() {
                try {
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
                    OpenDelegateManager
                            .getProcessDelegate()
                            .getOpenDynamicProcessorDelegate()
                            .processShowQuickPay(
                                    model.getActivity(),
                                    DynamicProcessorType.NAVIGATOR_PAGE_QUICK_PAY,
                                    bundle,
                                    ((AbsBizWebView) model.getWebView())
                                            .getBasePack().getCode(),
                                    callback);
                } catch (Exception e) {
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new FastPayRequestModel(model.getRequestObj());
    }

    private class FastPayRequestModel extends AbsWebRequestModel {

        private String mobile;
        private String merchantId;
        private String merchantUserId;
        private String sign;
        private String mode;
        private String merOrderId;
        private String amount;
        private String agentMerchantId;
        private String notifyUrl;
        private String signType;

        public FastPayRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            JSONObject data = getRequest().getJSONObject("data");
            mobile = data.getString("mobile");
            merchantId = data.getString("merchantId");
            merchantUserId = data.getString("merchantUserId");
            sign = data.getString("sign");
            mode = data.getString("mode");
            merOrderId = data.getString("merOrderId");
            amount = data.getString("amount");
            agentMerchantId = data.getString("agentMerchantId");
            notifyUrl = data.getString("notifyUrl");
            signType = data.getString("signType");
        }

        public String getMerchantId() {
            return merchantId;
        }

        public String getMobile() {
            return mobile;
        }

        public String getMerchantUserId() {
            return merchantUserId;
        }

        public String getSign() {
            return sign;
        }

        public String getMode() {
            return mode;
        }

        public String getMerOrderId() {
            return merOrderId;
        }

        public String getAmount() {
            return amount;
        }

        public String getAgentMerchantId() {
            return agentMerchantId;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public String getSignType() {
            return signType;
        }
    }
}
