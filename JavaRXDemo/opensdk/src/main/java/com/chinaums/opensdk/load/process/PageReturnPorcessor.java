package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.util.UmsLog;

/**
 * 回退返回API
 */
public class PageReturnPorcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
    }

    @Override
    public int getType() {
        return DynamicProcessorType.NAVIGATOR_PAGE_RETURN;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        final PageReturnRequestModel requestModel = (PageReturnRequestModel) model
                .getRequestModel();
        ((AbsBizWebView) model.getWebView()).setBackType(requestModel
                .getBackType());
        ((AbsBizWebView) model.getWebView()).setBackNum(requestModel
                .getBackNum());
        setRespAndCallWeb(model, createSuccessResponse(null));
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new PageReturnRequestModel(model.getRequestObj());
    }

    private class PageReturnRequestModel extends AbsWebRequestModel {

        /**
         * SPABack单页回退 NormalBack正常回退
         */
        private String backType;

        /**
         * -1 回退最近本地页面 -2回退首页
         */
        private int backNum;

        public PageReturnRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            backType = getRequest().getJSONObject("data").getString("backType");
            try {
                backNum = Integer.parseInt(getRequest().getJSONObject("data")
                        .getString("backNum"));
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public String getBackType() {
            return backType;
        }

        public int getBackNum() {
            return backNum;
        }
    }
}
