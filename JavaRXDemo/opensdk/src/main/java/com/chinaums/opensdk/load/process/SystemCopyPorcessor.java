package com.chinaums.opensdk.load.process;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;

/**
 * 复制到剪切板的API
 */
public class SystemCopyPorcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
    }

    @Override
    public int getType() {
        return DynamicProcessorType.SYSTEM_COPY;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        final CopyRequestModel requestModel = (CopyRequestModel) model
                .getRequestModel();
        model.getHandler().post(new Runnable() {
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            public void run() {
                if (android.os.Build.VERSION.SDK_INT > 11) {
                    android.content.ClipboardManager c = (android.content.ClipboardManager) model
                            .getActivity().getSystemService(
                                    Context.CLIPBOARD_SERVICE);
                    c.setPrimaryClip(ClipData.newPlainText("APICopyText",
                            requestModel.getCopyString()));
                } else {
                    android.text.ClipboardManager c = (android.text.ClipboardManager) model
                            .getActivity().getSystemService(
                                    Context.CLIPBOARD_SERVICE);
                    c.setText(requestModel.getCopyString());
                }
                setRespAndCallWeb(model, createSuccessResponse(null));
            }
        });
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new CopyRequestModel(model.getRequestObj());
    }

    private class CopyRequestModel extends AbsWebRequestModel {

        /**
         * 需要复制的文本
         */
        private String copyString;

        public CopyRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            copyString = getRequest().getJSONObject("data").getString(
                    "copyString");
        }

        public String getCopyString() {
            return copyString;
        }

    }
}
