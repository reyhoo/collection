package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.load.view.AbsBizWebView.CallWeb;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

/**
 * 类 PopListProcessor 的实现描述：选择列表处理
 */
public class PopListProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
    }

    @Override
    public int getType() {
        return DynamicProcessorType.SELECT_SHOW;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {

        model.getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    ListPickerWebRequestMode requestModel = (ListPickerWebRequestMode) model
                            .getRequestModel();
                    if (requestModel == null)
                        throw new Exception(" AbsWebRequestModel == null");
                    showSelect(model, requestModel.choices,
                            requestModel.selectIndex_);
                } catch (Exception e) {
                    // 异常处理
                    apiRunExceptionCallBack(model, e);
                }
            }
        });

    }

    /**
     * 弹出列表
     */
    private void showSelect(final DynamicWebModel model,
                            final String[] choices, final int selectIndex) {
        ((AbsBizWebView) (model.getWebView())).showSelect(model, choices,
                selectIndex, new CallWeb() {
                    @Override
                    public void onCallWeb(int position) {
                        PopListResponse resp = new PopListResponse();
                        resp.setSelectIndex(String.valueOf(position));
                        setRespAndCallWeb(model,
                                createSuccessResponse(JsonUtils
                                        .convert2Json(resp)));
                    }
                });
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new ListPickerWebRequestMode(model.getRequestObj());
    }

    private class ListPickerWebRequestMode extends AbsWebRequestModel {

        /**
         * choices
         */
        private String[] choices;

        /**
         * 界面中设置的选中索引
         */
        private String selectIndex;

        /**
         * 用逗号分隔的选择列表
         */
        private String selectNames;

        /**
         * 默认选中的索引
         */
        private int selectIndex_;

        private static final int DEFAULT_SELECT_INDEX = -1;

        public ListPickerWebRequestMode(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                JSONObject data = getRequest().getJSONObject("data");
                selectNames = data.getString("selectNames");
                selectIndex = data.getString("selectIndex");
                if (UmsStringUtils.isBlank(selectNames))
                    throw new Exception("选择列表值为空");
                choices = selectNames.split(",");
                selectIndex_ = UmsStringUtils.isBlank(selectIndex) ? DEFAULT_SELECT_INDEX
                        : Integer.parseInt(selectIndex);
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    private class PopListResponse {

        /**
         * selectIndex为选中的索引
         */
        private String selectIndex;

        public String getSelectIndex() {
            return selectIndex;
        }

        public void setSelectIndex(String selectIndex) {
            this.selectIndex = selectIndex;
        }

    }
}
