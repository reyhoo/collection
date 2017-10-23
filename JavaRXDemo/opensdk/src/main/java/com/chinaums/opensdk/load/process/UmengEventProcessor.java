package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.UmsLog;

import java.util.HashMap;

/**
 * 友盟统计的processor
 */
public class UmengEventProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {

    }

    @Override
    public int getType() {
        return DynamicProcessorType.UMENG_EVENT;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        final UmengEventRequestModel requestModel = (UmengEventRequestModel) model
                .getRequestModel();
        model.getHandler().post(new Runnable() {
            public void run() {
                try {
                    OpenDelegateManager
                            .getProcessDelegate()
                            .getOpenDynamicProcessorDelegate()
                            .umengEvent(model.getActivity(),
                                    requestModel.getEventId(),
                                    requestModel.getEventParam());
                    setRespAndCallWeb(model, createSuccessResponse(null));
                } catch (Exception e) {
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new UmengEventRequestModel(model.getRequestObj());
    }

    private class UmengEventRequestModel extends AbsWebRequestModel {

        /**
         * 友盟时间ID
         */
        private String eventId;

        /**
         * 自定义行为参数
         */
        private HashMap<String, String> eventParam;

        public UmengEventRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                eventId = getRequest().getJSONObject("data").getString(
                        "eventId");
                String param = getRequest().getJSONObject("data").getString(
                        "eventParam");
                eventParam = JSON.parseObject(param,
                        new TypeReference<HashMap<String, String>>() {
                        });
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public String getEventId() {
            return eventId;
        }

        public HashMap<String, String> getEventParam() {
            return eventParam;
        }
    }
}
