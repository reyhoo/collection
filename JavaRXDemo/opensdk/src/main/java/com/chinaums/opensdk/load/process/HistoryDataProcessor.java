package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.manager.OpenHistoryDataManager;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

/**
 * 类 HistoryDataProcessor 的实现描述：客户端持久化数据查询、更新、删除 API
 */
public class HistoryDataProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
    }

    @Override
    public int getType() {
        return DynamicProcessorType.CLIENT_HISTORY_DATA;
    }

    @Override
    protected void execute(DynamicWebModel model) throws Exception {
        try {
            HistoryDataRequestModel requestModel = (HistoryDataRequestModel) model
                    .getRequestModel();
            if (requestModel == null)
                throw new Exception("AbsWebRequestModel == null");
            String action = requestModel.getAction();
            if (action.equals("get")) {
                fetchLocalHistoryData(model, requestModel);
            } else if (action.equals("set")) {
                setLocalHistoryData(model, requestModel);
            } else if (action.equals("delete")) {
                deleteLocalHistoryData(model, requestModel);
            }
        } catch (Exception e) {
            UmsLog.e("", e);
            apiRunExceptionCallBack(model, e);
        }
    }

    /**
     * 客户端持久化查询
     */
    private void fetchLocalHistoryData(DynamicWebModel model,
                                       HistoryDataRequestModel requestModel) throws Exception {
        String historyDataStr = OpenHistoryDataManager
                .getHistoryStringData(requestModel.getKey());
        JSONObject jsonObject = JsonUtils.getJsonObject(historyDataStr);
        setRespAndCallWeb(model, createSuccessResponse(jsonObject));
    }

    /**
     * 客户端持久化更新
     */
    private void setLocalHistoryData(DynamicWebModel model,
                                     HistoryDataRequestModel requestModel) throws Exception {
        String historyDataStr = OpenHistoryDataManager
                .getHistoryStringData(requestModel.getKey());
        JSONObject jsonObject = new JSONObject();
        Value value = new Value();
        value.subKey = requestModel.getSubKey();
        value.subValue = requestModel.getSubValue();
        if (UmsStringUtils.hasValue(historyDataStr)) {
            jsonObject = JsonUtils.getJsonObject(historyDataStr);
            JSONArray array = JsonUtils.getJsonArray(jsonObject, "values");
            removeSubKey(array, requestModel.getSubKey());
            array.add(JsonUtils.convert2Json(value));
            jsonObject.put("values", array);
        } else {
            JSONArray array = new JSONArray();
            array.add(JsonUtils.convert2Json(value));
            jsonObject.put("values", array);
        }
        OpenHistoryDataManager.setHistoryData(requestModel.getKey(),
                jsonObject.toString());
        setRespAndCallWeb(model, createSuccessResponse(null));
    }

    /**
     * 客户端持久化删除
     */
    private void deleteLocalHistoryData(DynamicWebModel model,
                                        HistoryDataRequestModel requestModel) throws Exception {
        String historyDataStr = OpenHistoryDataManager
                .getHistoryStringData(requestModel.getKey());
        if (UmsStringUtils.isBlank(historyDataStr))
            throw new Exception(requestModel.getKey() + "不存在历史记录");
        JSONObject jsonObject = JsonUtils.getJsonObject(historyDataStr);
        JSONArray array = JsonUtils.getJsonArray(jsonObject, "values");
        if (UmsStringUtils.isBlank(requestModel.getSubKey())) {
            OpenHistoryDataManager.deleteHistoryData(requestModel.getKey());
            return;
        }
        if (removeSubKey(array, requestModel.getSubKey())) {
            jsonObject.put("values", array);
            OpenHistoryDataManager.setHistoryData(requestModel.getKey(),
                    jsonObject.toString());
        }
        setRespAndCallWeb(model, createSuccessResponse(null));
    }

    /**
     * 检查JSONArray是否包含指定的subKey，如果有则删除返回true
     */
    private boolean removeSubKey(JSONArray array, String subKey) {
        if (array == null) {
            return false;
        }
        for (int i = 0; i < array.size(); i++) {
            if (array.getJSONObject(i).getString("subKey").equals(subKey)) {
                array.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new HistoryDataRequestModel(model.getRequestObj());
    }

    private class HistoryDataRequestModel extends AbsWebRequestModel {

        /**
         * 本地保存文件里的数据的唯一标识
         */
        private String key;

        /**
         * 唯一索引
         */
        private String subKey;

        /**
         * 唯一索引对应的值
         */
        private String subValue;

        /**
         * 操作事件
         */
        private String action;

        public HistoryDataRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                action = getRequest().getJSONObject("info").getString("action");
                key = getRequest().getJSONObject("data").getString("key");
                if (action.equals("set")) {
                    subKey = getRequest().getJSONObject("data").getString(
                            "subKey");
                    subValue = getRequest().getJSONObject("data").getString(
                            "subValue");
                    if (UmsStringUtils.isBlank(subKey)
                            || UmsStringUtils.isBlank(subValue))
                        throw new Exception("API运行参数不全");
                } else if (action.equals("delete")) {
                    subKey = getRequest().getJSONObject("data").getString(
                            "subKey");
                    if (UmsStringUtils.isBlank(subKey))
                        throw new Exception("API运行参数不全");
                }
                if (UmsStringUtils.isBlank(key)
                        || UmsStringUtils.isBlank(action))
                    throw new Exception("API运行参数不全");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSubKey() {
            return subKey;
        }

        public void setSubKey(String subKey) {
            this.subKey = subKey;
        }

        public String getSubValue() {
            return subValue;
        }

        public void setSubValue(String subValue) {
            this.subValue = subValue;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    private class Value {

        /**
         * 唯一索引
         */
        private String subKey;

        /**
         * 唯一索引对应的值
         */
        private String subValue;

        public String getSubKey() {
            return subKey;
        }

        public void setSubKey(String subKey) {
            this.subKey = subKey;
        }

        public String getSubValue() {
            return subValue;
        }

        public void setSubValue(String subValue) {
            this.subValue = subValue;
        }
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }
    
}
