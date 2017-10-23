package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.Intent;

import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.process.delegate.OpenScanBarDelegateParamDefined.OpenScanBarRequest;
import com.chinaums.opensdk.load.process.delegate.OpenScanBarDelegateParamDefined.OpenScanBarResponse;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;

/**
 * 类 ScanBarCodeProcessor 的实现描述：二维码扫描处理 bundle.putString("result",
 * resultString); bundle.putParcelable("bitmap", barcode);
 */
public class ScanBarCodeProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        OpenScanBarResponse response = OpenDelegateManager.getProcessDelegate()
                .getOpenDynamicProcessorDelegate()
                .processScanBarCallback(resultCode, data);
        switch (response.resultCode) {
            case Activity.RESULT_OK:
                ScanBarCodeResponse resp = new ScanBarCodeResponse();
                resp.setBarCode(data.getStringExtra("result"));
                setRespAndCallWeb(model,
                        createSuccessResponse(JsonUtils.convert2Json(resp)));
                break;
            case Activity.RESULT_CANCELED:
                // 操作取消
                setRespAndCallWeb(
                        model,
                        createErrorResponse("canceled",
                                DynamicCallback.CALLBACK_STATE_CANCEL));
                break;
            default:
                // 其他操作
                setRespAndCallWeb(
                        model,
                        createErrorResponse("unknown",
                                DynamicCallback.CALLBACK_STATE_ERROR));
                break;
        }
    }

    @Override
    public int getType() {
        return DynamicProcessorType.SCAN_BAR_CODE;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        model.getHandler().post(new Runnable() {

            @Override
            public void run() {
                try {
                    OpenDelegateManager
                            .getProcessDelegate()
                            .getOpenDynamicProcessorDelegate()
                            .processScanBar(model.getActivity(), getType(),
                                    new OpenScanBarRequest());
                } catch (Exception e) {
                    UmsLog.e("", e);
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return super.makeNewWebRequestModel(model);
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    private class ScanBarCodeResponse {

        /**
         * barCode为二维码或条码的值
         */
        private String barCode;

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

    }

}
