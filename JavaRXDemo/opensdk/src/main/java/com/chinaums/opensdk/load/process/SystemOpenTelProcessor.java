package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.util.UmsLog;

/**
 * 类 SystemOpenTelProcessor 的实现描述：打开拨号盘 API
 */
public class SystemOpenTelProcessor extends AbsStdDynamicProcessor {

    @Override
    public int getType() {
        return DynamicProcessorType.SYSTEM_OPEN_TEL;
    }

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        switch (resultCode) {
            case Activity.RESULT_OK:
                // 操作成功
                setRespAndCallWeb(model, createSuccessResponse(null));
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
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new SystemOpenTelWebRequestModel(model.getRequestObj());
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        model.getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    // 正常处理
                    SystemOpenTelWebRequestModel requestModel = (SystemOpenTelWebRequestModel) model
                            .getRequestModel();
                    if (requestModel == null)
                        throw new Exception(
                                " SystemOpenTelWebRequestModel == null");
                    openTel(model, requestModel.getPhoneNumber());
                } catch (Exception e) {
                    // 异常处理
                    UmsLog.e("", e);
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    /**
     * 打开拨号器，并显示要拨打的号码
     */
    private void openTel(DynamicWebModel model, String phoneNumber)
            throws Exception {
        String uri = "tel:" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        model.getActivity().startActivityForResult(intent,
                DynamicProcessorType.SYSTEM_OPEN_TEL);
    }

    private class SystemOpenTelWebRequestModel extends AbsWebRequestModel {

        /**
         * 拨号器要显示的电话号码
         */
        private String phoneNumber;

        public SystemOpenTelWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                phoneNumber = getRequest().getJSONObject("data").getString(
                        "phoneNumber");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
        
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }
}