package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.process.listener.DynamicAPICallback;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.UmsStringUtils;


/**
 * 打开新支付中心的第三方API
 */
public class ShowPayCenterExtProcessor extends AbsStdDynamicProcessor {

    public static final String KEY_PAYCENTER_CALLBACK = "payCenterCallBackInfo";

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        String response = OpenDelegateManager
                .getProcessDelegate().getOpenDynamicProcessorDelegate()
                .processShowPayCenterCallback(resultCode, data, model.getActivity());
        if (UmsStringUtils.isNotBlank(response)) {
            JSONObject responseJs = JSON.parseObject(response);
            String errCode = responseJs.getString("errCode");
            String errInfo = responseJs.getString("errInfo");
            if (UmsStringUtils.isNotBlank(errCode)) {
                if ("0000".equals(errCode)) {
                    setRespAndCallWeb(model, createSuccessResponse(responseJs));
                } else if ("1000".equals(errCode)) {
                    setRespAndCallWeb(model, createErrorResponse(errCode, errInfo, DynamicCallback.CALLBACK_STATE_CANCEL));
                } else {
                    setRespAndCallWeb(model, createErrorResponse(errCode, errInfo, DynamicCallback.CALLBACK_STATE_ERROR));
                }
                return;
            }
        }
        setRespAndCallWeb(
                model,
                createErrorResponse("支付失败",
                        DynamicCallback.CALLBACK_STATE_ERROR));
    }

    @Override
    public int getType() {
        return DynamicProcessorType.NAVIGATOR_PAGE_PAY_CENTER;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        model.getHandler().post(new Runnable() {
            public void run() {
                try {
                    final DynamicAPICallback apiCallback = new DynamicAPICallback() {
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
                            .processShowPayCenter(
                                    model.getActivity(),
                                    DynamicProcessorType.NAVIGATOR_PAGE_PAY_CENTER,
                                    model.getRequestObj().toString(),
                                    ((AbsBizWebView) model.getWebView())
                                            .getBasePack().getCode(),
                                    apiCallback);
                } catch (Exception e) {
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }
}
