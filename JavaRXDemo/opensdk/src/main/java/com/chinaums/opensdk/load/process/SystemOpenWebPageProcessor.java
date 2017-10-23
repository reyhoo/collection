package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicBizIntentExtra;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.cons.OpenConst.DynamicUmsUrlParam;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;
import com.chinaums.opensdk.util.UriUtils;

/**
 * 打开浏览器
 */
public class SystemOpenWebPageProcessor extends AbsStdDynamicProcessor {

    @Override
    public int getType() {
        return DynamicProcessorType.SYSTEM_OPEN_WEB_PAGE;
    }

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        SystemOpenWebPageWebRequestModel requestModel = (SystemOpenWebPageWebRequestModel) model
                .getRequestModel();
        switch (resultCode) {
            case Activity.RESULT_OK:
                // 操作成功
                setRespAndCallWeb(model, createSuccessResponse(null));
                break;
            case Activity.RESULT_CANCELED:// openWebPageBySysBrowser成功返回为0
                if ("bySelf".equals(requestModel.getOpenBySelf())) {
                    // 操作取消
                    setRespAndCallWeb(
                            model,
                            createErrorResponse("canceled",
                                    DynamicCallback.CALLBACK_STATE_CANCEL));
                } else if ("bySysBrowser".equals(requestModel.getOpenBySelf())) {
                    // 操作成功
                    setRespAndCallWeb(model, createSuccessResponse(null));
                }
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

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        SystemOpenWebPageWebRequestModel openModel = new SystemOpenWebPageWebRequestModel(
                model.getRequestObj());
        try {
            String toUrl = openModel.getUrl();
            toUrl = ((AbsBizWebView) model.getWebView()).getTo()
                    .generateUmsUrl(toUrl);
            openModel.setFrom(toUrl);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return openModel;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        model.getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    // 正常处理
                    SystemOpenWebPageWebRequestModel requestModel = (SystemOpenWebPageWebRequestModel) model
                            .getRequestModel();
                    JSONObject json = new JSONObject();
                    String toUrl = requestModel.getUrl();
                    if (UmsStringUtils.isNotBlank(requestModel.getTitle())) {
                        json.put("title", requestModel.getTitle());
                        json.put("isUseOriginalViewPort", requestModel.isUseOriginalViewPort());
                        toUrl = UriUtils.addPararms(toUrl,
                                DynamicUmsUrlParam.PARAM_UMS_OPEN,
                                Base64Utils.encrypt(json.toJSONString()));
                    }
                    openWebPage(model, toUrl,
                            requestModel.getOpenBySelf());
                } catch (Exception e) {
                    // 异常处理
                    UmsLog.e("", e);
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    /**
     * 打开网页
     *
     * @param model
     * @param url        要打开的远程地址
     * @param openBySelf 是否由自身打开: 0使用其它浏览器打开， 1使用自身浏览器打开
     */
    private void openWebPage(final DynamicWebModel model, String url,
                             String openBySelf) throws Exception {
        AbsBizWebView webview = (AbsBizWebView) model.getWebView();
        if ("bySysBrowser".equals(openBySelf)) {
            // 使用其它浏览器打开
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            model.getActivity().startActivityForResult(intent,
                    DynamicProcessorType.SYSTEM_OPEN_WEB_PAGE);
        } else if ("bySelf".equals(openBySelf)) {
            // 使用自身浏览器打开
            Intent intent = new Intent(model.getActivity(), OpenDelegateManager
                    .getProcessDelegate().getOpenDynamicBizActivityClazz());
            intent.putExtra(DynamicBizIntentExtra.PAGE_TO, url);
            intent.putExtra(DynamicBizIntentExtra.PAGE_FROM,
                    webview.getTo() == null ? "" : webview.getTo().getUmsUrl());
            intent.putExtra(DynamicBizIntentExtra.PAGE_NEED_BACK_HOME,
                    webview.getNeedBackHome());
            model.getActivity().startActivityForResult(intent,
                    DynamicProcessorType.SYSTEM_OPEN_WEB_PAGE);
        }
    }

    private class SystemOpenWebPageWebRequestModel extends AbsWebRequestModel {

        /**
         * 要打开的远程地址
         */
        private String url;

        /**
         * 来源
         */
        private String from;

        /**
         * 是否由自身打开: 0使用其它浏览器打开， 1使用自身浏览器打开
         */
        private String openBySelf;

        /**
         * 是否使用原来的webview设置,默认为true。
         */
        private boolean isUseOriginalViewPort;

        /**
         * 第三方页面标题
         */
        private String title;

        public SystemOpenWebPageWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                url = getRequest().getJSONObject("data").getString("url");
                openBySelf = getRequest().getJSONObject("info").getString(
                        "action");
                title = getRequest().getJSONObject("data").getString("title");
                if (getRequest().getJSONObject("data").containsKey(
                        "isUseOriginalViewPort")) {
                    isUseOriginalViewPort = getRequest().getJSONObject("data")
                            .getBoolean("isUseOriginalViewPort");
                } else {
                    isUseOriginalViewPort = true;
                }

            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public String getUrl() {
            return url;
        }

        public String getOpenBySelf() {
            return openBySelf;
        }

        public String getFrom() {
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

        public boolean isUseOriginalViewPort() {
            return isUseOriginalViewPort;
        }

        public String getTitle() {
            return title;
        }

    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

}
