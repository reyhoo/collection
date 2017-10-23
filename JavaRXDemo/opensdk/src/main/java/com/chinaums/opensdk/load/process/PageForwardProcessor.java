package com.chinaums.opensdk.load.process;

import android.content.Intent;
import android.os.Handler;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicBizIntentExtra;
import com.chinaums.opensdk.download.listener.ResourceProcessListener;
import com.chinaums.opensdk.load.factory.UmsUrlFactory;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.model.url.AbsActivityUmsUrl;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.load.view.NativeWebView;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.manager.OpenDialogManager;
import com.chinaums.opensdk.util.UmsLog;


public class PageForwardProcessor extends AbsStdDynamicProcessor {

    private IOpenDynamicPageProcessorDelegate delegate;

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        /* 处理回退N页面 */
        int type = data.getIntExtra("type", Integer.MAX_VALUE);
        ((AbsBizWebView) model.getWebView()).finishProcess(model.getId());
        if (type == DynamicProcessorType.NAVIGATOR_PAGE_BACK) {
            ((AbsBizWebView) model.getWebView()).getDynamicProcessor(
                    DynamicProcessorType.NAVIGATOR_PAGE_BACK).onCallback(model,
                    resultCode, data);
        } else if (type == DynamicProcessorType.NAVIGATOR_PAGE_BACK_3005) {
            ((AbsBizWebView) model.getWebView()).getDynamicProcessor(
                    DynamicProcessorType.NAVIGATOR_PAGE_BACK_3005).onCallback(
                    model, resultCode, data);
            return;
        }
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        PageForwardWebRequestModel forwardModel = new PageForwardWebRequestModel(
                model.getRequestObj());
        try {
            String toUrl = forwardModel.getUrl();
            toUrl = ((AbsBizWebView) model.getWebView()).getTo()
                    .generateUmsUrl(toUrl);
            forwardModel.setFrom(toUrl);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return forwardModel;
    }

    @Override
    public int getType() {
        return DynamicProcessorType.NAVIGATOR_PAGE_FORWARD;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        Handler handler = model.getHandler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    PageForwardWebRequestModel requestModel = (PageForwardWebRequestModel) model
                            .getRequestModel();
                    AbsBizWebView webview = (AbsBizWebView) model.getWebView();
                    String toUrl = requestModel.getUrl();
                    toUrl = webview.getTo().generateUmsUrl(toUrl);

                    AbsUmsUrl to = UmsUrlFactory.getUmsUrl(toUrl);
                    if (to instanceof AbsActivityUmsUrl) {
                        NativeWebView webView = new NativeWebView(model
                                .getActivity(), to, null, null, new Handler(),
                                false, false, false, false, null);
                        webView.loadUrl(new ResourceProcessListener() {
                            @Override
                            public void onUpdated() {
                                UmsLog.d("加载完成.");
                            }

                            @Override
                            public void onError(String errorInfo, Exception e) {
                                UmsLog.e(errorInfo, e);
                                OpenDialogManager.getInstance().showHint(
                                        model.getActivity(),
                                        "页面加载出错，请退出客户端后重试.");
                            }
                        });
                        return;
                    }

                    // 暂时不考虑intent通知的方式，而是直接用class的方式
                    Intent intent = new Intent(model.getActivity(),
                            OpenDelegateManager.getProcessDelegate()
                                    .getOpenDynamicBizActivityClazz());
                    intent.putExtra(DynamicBizIntentExtra.PAGE_FROM, webview
                            .getTo() == null ? "" : webview.getTo().getUmsUrl());
                    intent.putExtra(DynamicBizIntentExtra.PAGE_TO, toUrl);
                    intent.putExtra(DynamicBizIntentExtra.PAGE_NEED_BACK_HOME,
                            webview.getNeedBackHome());
                    intent.putExtra(DynamicBizIntentExtra.PAGE_IS_FULLSCREEN,
                            webview.isFullscreen());
                    intent.putExtra(
                            DynamicBizIntentExtra.PAGE_IS_SHOW_BOTTOMTOOLBAR,
                            webview.isShowBottomToolbar());
                    intent.putExtra(DynamicBizIntentExtra.PAGE_BIZ_API_LEVEL,
                            webview.getApiLevel());
                    model.getActivity().startActivityForResult(intent,
                            DynamicProcessorType.NAVIGATOR_PAGE_FORWARD);
                } catch (Exception e) {
                    UmsLog.e("", e);
                }
            }
        });
    }

    private class PageForwardWebRequestModel extends AbsWebRequestModel {

        /**
         * 要跳转的地址
         */
        private String url;

        /**
         * 来源
         */
        private String from;

        public PageForwardWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                url = getRequest().getJSONObject("data").getString("url");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public final String getUrl() {
            return url;
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

    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

}
