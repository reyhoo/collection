package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;

/**
 * 类 PrintLogProcessor 的实现描述：设备控制台输出处理
 */
public class PrintLogProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
    }

    @Override
    public int getType() {
        return DynamicProcessorType.CONSOLE_OUTPUT_EQUIPMENT;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        try {
            LogWebRequestMode requestModel = (LogWebRequestMode) model
                    .getRequestModel();
            if (requestModel == null)
                throw new Exception("数据上下文出现异常 LogWebRequestMode == null");
            for (String content : requestModel.getLogArray()) {
                printLog(model.getAction(), content);
            }
            setRespAndCallWeb(model, createSuccessResponse(null));
        } catch (Exception e) {
            UmsLog.e("", e);
            apiRunExceptionCallBack(model, e);
        }
    }

    private void printLog(String _level, String msg) {
        if ("debug".equals(_level)) {
            UmsLog.d(msg);
        } else if ("warning".equals(_level)) {
            UmsLog.w(msg);
        } else if ("error".equals(_level)) {
            UmsLog.e(msg);
        } else if ("info".equals(_level)) {
            UmsLog.i(msg);
        }
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new LogWebRequestMode(model.getRequestObj());
    }

    private class LogWebRequestMode extends AbsWebRequestModel {

        /**
         * 打印内容，为要输出的字符串数组
         */
        private String logArray[];

        public LogWebRequestMode(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            logArray = JsonUtils.fromJsonString(
                    getRequest().getJSONObject("data").getString("logArray"),
                    String[].class);
            UmsLog.e("", logArray[0]);
        }

        public String[] getLogArray() {
            return logArray;
        }
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

}
