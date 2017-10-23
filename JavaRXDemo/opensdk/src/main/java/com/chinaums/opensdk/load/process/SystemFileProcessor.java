package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.FileHelper;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;

/**
 * 类 SystemFileProcessor 的实现描述：本地资源调用即得到系统文件
 */
public class SystemFileProcessor extends AbsStdDynamicProcessor {

    private static final String TAG = SystemFileProcessor.class.getSimpleName();

    @Override
    public int getType() {
        return DynamicProcessorType.SYSTEM_GET_FILE;
    }

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        switch (resultCode) {
            case Activity.RESULT_OK:
                // 操作成功
                handleFile(model, data);
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

    private void handleFile(DynamicWebModel model, Intent data)
            throws Exception {
        Uri uri = data.getData();
        String path = FileHelper.getRealPath(uri, model.getActivity());
        SystemFileResponse resp = new SystemFileResponse();
        resp.setFileUri(Base64Utils.decryptBase64(path));
        setRespAndCallWeb(model,
                createSuccessResponse(JsonUtils.convert2Json(resp)));
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        model.getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    // 正常处理
                    getFile(model);
                } catch (Exception e) {
                    // 异常处理
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    /**
     * 本地资源调用即得到系统文件
     */
    private void getFile(DynamicWebModel model) throws Exception {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain;text/html;application/pdf;application/x-chm;application/vnd.ms-powerpoint;application/vnd.ms-excel;application/msword");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        model.getActivity().startActivityForResult(intent,
                DynamicProcessorType.SYSTEM_GET_FILE);
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    private class SystemFileRequestModel extends AbsWebRequestModel {

        /**
         * 返回数据的格式: 1文件的uri。默认1
         */
        private String destinationType;

        /**
         * 文件来源。0：可以选择全部文件夹下的内容。默认0
         */
        private String sourceType;

        /**
         * 设置可选取的文件类型。2：all
         */
        private String fileType;

        public SystemFileRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                destinationType = getRequest().getJSONObject("data").getString(
                        "destinationType");
                sourceType = getRequest().getJSONObject("data").getString(
                        "sourceType");
                fileType = getRequest().getJSONObject("data").getString(
                        "fileType");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }
    }

    private class SystemFileResponse {

        /**
         * fileUri是获取的uri地址
         */
        private String fileUri;

        public String getFileUri() {
            return fileUri;
        }

        public void setFileUri(String fileUri) {
            this.fileUri = fileUri;
        }
    }
}
