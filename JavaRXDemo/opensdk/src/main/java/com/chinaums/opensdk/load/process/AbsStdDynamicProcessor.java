package com.chinaums.opensdk.load.process;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.download.process.DynamicResourceManager;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebResponseModel;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.load.view.LocalBizWebView;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;


public abstract class AbsStdDynamicProcessor implements IDynamicProcessor {

    final public int DEFAULT_PROCESS_LEVEL = 1;

    @Override
    public void process(DynamicWebModel model) throws Exception {
        UmsLog.d("进行处理开始。");
        model.setRequestModel(makeNewWebRequestModel(model));
        if (checkLevel(model)) {
            execute(model);
        } else {
            setRespAndCallWeb(
                    model,
                    createErrorResponse("noauth",
                            DynamicCallback.CALLBACK_STATE_ERROR));
        }
        UmsLog.d("完成处理。");
    }

    @Override
    public void onDestory(Activity activity) throws Exception {

    }

    /**
     * 创建请求对象
     */
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new DefaultWebRequestModel(model.getRequestObj());
    }

    /**
     * 主处理函数
     */
    protected abstract void execute(DynamicWebModel model) throws Exception;

    protected class DefaultWebRequestModel extends AbsWebRequestModel {

        public DefaultWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            return;
        }

    }

    protected class DefaultWebResponseModel extends AbsWebResponseModel {

        public DefaultWebResponseModel(JSON response) {
            super(response);
        }
    }

    /**
     * 异常处理返回
     */
    protected void apiRunExceptionCallBack(DynamicWebModel model,
                                           Exception exception) {
        UmsLog.e("", exception);
        setRespAndCallWeb(
                model,
                createErrorResponse(exception.getMessage(),
                        DynamicCallback.CALLBACK_STATE_ERROR));
    }

    /**
     * 是否是同步方法
     */
    public Boolean IsSynchronizedProcessor() {
        return getProcessorType() == DynamicRequestType.SYNCHRONIZED;
    }

    /**
     * 异步调用设置响应与回调
     */
    protected void setRespAndCallWeb(DynamicWebModel model,
                                     AbsWebResponseModel responseModel) {
        model.setResponseModel(responseModel);
        if (!IsSynchronizedProcessor()) {
            ((AbsBizWebView) model.getWebView()).callWeb(model);
        }
    }

    /**
     * 设置成功响应
     */
    protected AbsWebResponseModel createSuccessResponse(JSON bizData) {
        return createResponse(DynamicCallback.RESP_CODE_OK,
                DynamicCallback.RESP_MESSAGE_OK,
                DynamicCallback.CALLBACK_STATE_SUCCESS, bizData);
    }

    /**
     * 设置失败响应
     */
    protected AbsWebResponseModel createErrorResponse(String errInfo,
                                                      String callResultStatus) {
        return createResponse(DynamicCallback.RESP_CODE_NO_OK, errInfo,
                callResultStatus, null);
    }

    /**
     * 设置失败响应
     */
    protected AbsWebResponseModel createErrorResponse(String errCode,
                                                      String errInfo, String callResultStatus) {
        return createResponse(errCode, errInfo, callResultStatus, null);
    }

    protected AbsWebResponseModel createResponse(String errCode,
                                                 String errInfo, String callResultStatus, JSON bizData) {
        JSONObject response = new JSONObject();
        response.put(
                "errCode",
                UmsStringUtils.isBlank(errCode) ? DynamicCallback.RESP_CODE_NO_OK
                        : errCode);
        response.put(
                "errInfo",
                UmsStringUtils.isBlank(errInfo) ? DynamicCallback.RESP_MESSAGE_NO_OK
                        : errInfo);
        response.put(
                "callResultStatus",
                UmsStringUtils.isBlank(callResultStatus) ? DynamicCallback.CALLBACK_STATE_SUCCESS
                        : callResultStatus);
        response.put("bizData", bizData == null ? new JSONObject() : bizData);
        UmsLog.d("输出的回调信息为:{}", response);
        return new DefaultWebResponseModel(response);
    }

    /**
     * api调用检查，每个process都有自己的level，每个资源包有自己的level
     */
    private Boolean checkLevel(DynamicWebModel model) {
        try {
            // 配置资源包中的level
            int pLevel = DynamicResourceManager.getInstance().getProcessLevel(
                    String.valueOf(getType()), model.getAction());
            // 客户端process中定义的level
            if (pLevel == 0) {
                pLevel = getLevel(model.getAction());
            }
            // 获取资源包中的level
            AbsBizWebView webview = (AbsBizWebView) model.getWebView();
            int packLevel = DEFAULT_PROCESS_LEVEL;
            if (webview instanceof LocalBizWebView
                    && null != webview.getBasePack()) {
                packLevel = webview.getBasePack().getApiLevel();
            }
            if (packLevel < pLevel) {
                return false;
            }
            return true;
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return false;
    }

    @Override
    public int getLevel() {
        return getLevel(null);
    }

    @Override
    public int getLevel(String action) {
        if (action == null)
            return DEFAULT_PROCESS_LEVEL;
        return DEFAULT_PROCESS_LEVEL;
    }

}
