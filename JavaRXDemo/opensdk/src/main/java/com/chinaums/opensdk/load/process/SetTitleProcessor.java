package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.activity.base.IDynamicBizActivity;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;

/**
 * 设置标题的API
 */
public class SetTitleProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
    }

    @Override
    public int getType() {
        return DynamicProcessorType.SET_TITLE;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        final SetTitleRequestModel requestModel = (SetTitleRequestModel) model
                .getRequestModel();
        if (requestModel == null) {
            throw new Exception("Null SetTitleRequestModel!");
        }
        final String title = requestModel.getTitle();
        model.getHandler().post(new Runnable() {
            public void run() {
                ((IDynamicBizActivity) model.getActivity()).setTitleText(title);
                setRespAndCallWeb(model, createSuccessResponse(null));
            }
        });

    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new SetTitleRequestModel(model.getRequestObj());
    }

    /**
     * 设置标题的requestModel
     */
    private class SetTitleRequestModel extends AbsWebRequestModel {

        /**
         * 标题
         */
        private String title;

        public SetTitleRequestModel(JSONObject request) {
            super(request);
        }

        public String getTitle() {
            return title;
        }

        @Override
        protected void initByRequest() {
            JSONObject data = getRequest().getJSONObject("data");
            title = data.getString("title");
        }
    }
}
