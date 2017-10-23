package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;


public class PageBackProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        try {
            int backNum = data.getIntExtra("backNum", Integer.MAX_VALUE);
            String from = data.getStringExtra("from");
            JSONObject requestObj = new JSONObject();
            JSONObject dataObj = new JSONObject();
            dataObj.put("from", from);
            dataObj.put("backNum", backNum);
            requestObj.put("data", dataObj);
            model.setRequestModel(new PageBackWebRequestModel(requestObj));
            ((AbsBizWebView) model.getWebView()).finishProcess(model.getId());
            if (backNum == 0) {
                ((AbsBizWebView) model.getWebView()).sendResume(model);
            } else {
                postHandler(model, model.getHandler());
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    @Override
    public int getType() {
        return DynamicProcessorType.NAVIGATOR_PAGE_BACK;
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        PageBackWebRequestModel backWebRequestModel = new PageBackWebRequestModel(
                model.getRequestObj());
        AbsUmsUrl from = ((AbsBizWebView) model.getWebView()).getTo();
        backWebRequestModel.setFrom(from == null ? "" : from.getUmsUrl());
        return backWebRequestModel;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        Handler handler = model.getHandler();
        if (UmsStringUtils.isNotBlank(((AbsBizWebView) model.getWebView())
                .getBackType())) {//经过3008设置返回属性
            postReturnHandler(model, handler);
            return;
        }
        postHandler(model, handler);
    }

    private void postHandler(final DynamicWebModel model, final Handler handler) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    PageBackWebRequestModel requestModel = (PageBackWebRequestModel) model
                            .getRequestModel();

                    if ("goHome".equals(requestModel.getAction())) {
                        OpenDelegateManager.getProcessDelegate()
                                .getOpenDynamicProcessorDelegate()
                                .navToMainPage(model.getActivity());
                        setRespAndCallWeb(model, createSuccessResponse(null));
                        return;
                    }
                    int backNum = requestModel.getBackNum();
                    String from = requestModel.getFrom();
                    Intent intent = new Intent();
                    intent.putExtra("backNum", backNum > 0 ? backNum - 1
                            : backNum);
                    intent.putExtra("from", from);
                    intent.putExtra("type", getType());
                    model.getActivity().setResult(Activity.RESULT_OK, intent);
                    model.getActivity().finish();
                } catch (Exception e) {
                    UmsLog.e("", e);
                }
            }
        });
    }

    private void postReturnHandler(final DynamicWebModel model,
                                   final Handler handler) {
        handler.post(new Runnable() {
            public void run() {
                try {
                    if (((AbsBizWebView) model.getWebView()).getBackType()
                            .equals("SPABack")) {
                        setRespAndCallWeb(model,
                                createErrorResponse("0001", "未定义参数"));
                        return;
                    }
                    if (((AbsBizWebView) model.getWebView()).getBackType()
                            .equals("NormalBack")) {
                        if (((AbsBizWebView) model.getWebView()).getBackNum() == -2) {
                            OpenDelegateManager.getProcessDelegate()
                                    .getOpenDynamicProcessorDelegate()
                                    .navToMainPage(model.getActivity());
                            setRespAndCallWeb(model,
                                    createSuccessResponse(null));
                        } else if (((AbsBizWebView) model.getWebView())
                                .getBackNum() == -1) {
                            Intent intent = new Intent();
                            intent.putExtra("backNum", 1);
                            intent.putExtra("type",
                                    DynamicProcessorType.NAVIGATOR_PAGE_BACK);
                            model.getActivity().setResult(Activity.RESULT_OK,
                                    intent);
                            model.getActivity().finish();
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("backNum", ((AbsBizWebView) model
                                    .getWebView()).getBackNum() - 1);
                            intent.putExtra("type",
                                    DynamicProcessorType.NAVIGATOR_PAGE_BACK);
                            model.getActivity().setResult(Activity.RESULT_OK,
                                    intent);
                            model.getActivity().finish();
                        }
                    }
                    setRespAndCallWeb(model, createSuccessResponse(null));
                } catch (Exception e) {
                    setRespAndCallWeb(model,
                            createErrorResponse("0001", e.getMessage()));
                    UmsLog.e("", e);
                }
            }
        });

    }

    private class PageBackWebRequestModel extends AbsWebRequestModel {

        /**
         * 回退页数
         */
        private int backNum;

        /**
         * 来源
         */
        private String from;

        /**
         * 回退到首页的action
         */
        private String action;

        public PageBackWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                backNum = getRequest().getJSONObject("data").getIntValue(
                        "backNum");
                from = getRequest().getJSONObject("data").getString("from");
                action = getRequest().getJSONObject("info").getString("action");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public final int getBackNum() {
            return backNum;
        }

        public final String getFrom() {
            return from;
        }

        public final void setFrom(String from) {
            this.from = from;
            try {
                getRequest().getJSONObject("data").put("from", from);
            } catch (Exception e) {
                UmsLog.e("", e);
            }

        }

        public String getAction() {
            return action;
        }
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }
    
}
