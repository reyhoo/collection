package com.chinaums.opensdk.load.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.activity.base.IDynamicBizActivity;
import com.chinaums.opensdk.cons.DynamicWebviewLoadingState;
import com.chinaums.opensdk.cons.OpenConst;
import com.chinaums.opensdk.cons.OpenConst.Message;
import com.chinaums.opensdk.download.listener.ResourceProcessListener;
import com.chinaums.opensdk.download.model.AreaListPack.Area;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.process.DynamicResourceManager;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteWebUmsUrl;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.manager.OpenDialogManager;
import com.chinaums.opensdk.manager.OpenDynamicWebProcessorManager;
import com.chinaums.opensdk.manager.OpenEnvManager;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.lang.reflect.Method;


@SuppressLint({"SetJavaScriptEnabled", "NewApi"})
public abstract class AbsWebBizWebView extends AbsBizWebView {

    /**
     * initJsFrameFlag
     */
    private volatile boolean initJsFrameFlag = false;

    public AbsWebBizWebView(Context context, AbsUmsUrl to, AbsUmsUrl from,
                            BasePack basePack, Handler handler, Boolean needBackHome,
                            Boolean isFullscreen, Boolean isShowBottomToolbar,
                            Boolean isShowArea, Integer apiLevel) {
        super(context, to, from, basePack, handler, needBackHome, isFullscreen,
                isShowBottomToolbar, isShowArea, apiLevel);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void config() throws Exception {
        this.setWebViewClient(makeNewWebViewClient());
        this.setWebChromeClient(makeNewWebChromeClient());
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setDatabaseEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.setBackgroundResource(android.R.color.white);// 然后设置背景图片
        // this.setBackgroundResource(R.drawable.webviewbg);// 然后设置背景图片
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setDefaultTextEncodingName("UTF-8");
        this.getSettings().setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= 16) {
            Class<?> clazz = this.getSettings().getClass();
            Method method = clazz.getMethod(
                    "setAllowUniversalAccessFromFileURLs", boolean.class);
            if (method != null) {
                method.invoke(this.getSettings(), true);
            }
        }
        // 添加下列设置项以便通过网址（HTTP格式）访问网页
        this.getSettings().setSupportZoom(true); // 设置允许缩放
        this.getSettings().setBuiltInZoomControls(true); // 设置允许缩放控件
        this.getSettings().setUseWideViewPort(true); // 设置此属性，可任意比例缩放。
        this.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        this.getSettings().setDatabaseEnabled(true);
        String dir = getMyContext().getDir("database", Context.MODE_PRIVATE)
                .getPath();
        this.getSettings().setGeolocationEnabled(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setGeolocationDatabasePath(dir);
        customeConfig();
        // this.setBackgroundColor(color.white);// 先设置背景色为白色
        ((IDynamicBizActivity) getMyContext()).initView(isFullscreen(),
                isShowBottomToolbar(), isShowArea());
        ((IDynamicBizActivity) getMyContext())
                .changedViewByLoadingState(DynamicWebviewLoadingState.WEBVIEW_LOADING_UNKNOWN);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        String userAgent = OpenEnvManager.getNetUserAgent();
        if (UmsStringUtils.isNotBlank(userAgent)) {
            this.getSettings().setUserAgentString(userAgent);
        }

        this.setLayoutParams(params);
        this.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getMyContext().startActivity(i);
                goBack();
            }
        });

    }

    /**
     * 设置缓存信息
     */
    protected abstract void customeConfig();

    protected abstract WebViewClient makeNewWebViewClient();

    protected abstract WebChromeClient makeNewWebChromeClient();

    protected void clearWebCache() {
        this.clearCache(true);
        this.clearHistory();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    protected class WebBizWebViewClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            UmsLog.d("onReceivedError {} {} {}", errorCode, description,
                    failingUrl);
            OpenDialogManager.getInstance().cancelLoading();
            OpenDialogManager.getInstance()
                    .showHint(getContext(), "网络环境或服务器异常");
            ((IDynamicBizActivity) getMyContext()).onPageReceiveError();
            ((IDynamicBizActivity) getMyContext())
                    .changedViewByLoadingState(DynamicWebviewLoadingState.WEBVIEW_LOADING_RECEIVED_ERROR);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            initJsFrameFlag = false;
            UmsLog.d("onPageStarted", url);
            OpenDialogManager.getInstance().showLoading(getContext(),
                    "载入中，请稍候", true);
            ((IDynamicBizActivity) getMyContext())
                    .changedViewByLoadingState(DynamicWebviewLoadingState.WEBVIEW_LOADING_PAGE_STARTED);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            try {
                UmsLog.d("onPageFinished", url);
                OpenDialogManager.getInstance().cancelLoading();
                final JSONObject loadData = new JSONObject();
                loadData.put("from", getFrom() != null ? getFrom().getUmsUrl()
                        : "");
                String param = getBasePack() != null ? getBasePack()
                        .getParams() : getFrom() != null ? getFrom().getData()
                        .toJSONString() : "";
                JSONObject paramJson = JsonUtils.mergeJSONString(param, getTo()
                        .getData() != null ? getTo().getData().toString() : "");
                paramJson = paramJson == null ? new JSONObject() : paramJson;
                if (isShowArea()) {// 发送国省市县信息
                    String areaCode = OpenDelegateManager.getProcessDelegate()
                            .getDynamicResourceHistoryKeyGenerateRule()
                            .getKeyForDisplayBizList();
                    Area area = DynamicResourceManager.getInstance()
                            .getAreaByCode(areaCode);
                    paramJson.put(area.getAreaType() + "Name",
                            area.getAreaName());
                    paramJson.put("showAreaName", area.getAreaName());
                    while (area.getParent() != null) {
                        area = area.getParent();
                        paramJson.put(area.getAreaType() + "Name",
                                area.getAreaName());
                    }
                }
                loadData.put("param", paramJson);
            } catch (Exception e) {
                UmsLog.e("onPageFinished", e);
            }
            ((IDynamicBizActivity) getMyContext())
                    .changedViewByLoadingState(DynamicWebviewLoadingState.WEBVIEW_LOADING_PAGE_FINISHED);
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("tel:")) {
                Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                view.getContext().startActivity(tel);
                return true;
            } else if (url.startsWith("sms:")) {
                Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                view.getContext().startActivity(sms);
                return true;
            } else if (url.startsWith("mailto:")) {
                Intent mail = new Intent(Intent.ACTION_SEND);
                mail.setType("application/octet-stream");
                mail.putExtra(Intent.EXTRA_EMAIL, Uri.parse(url));
                view.getContext().startActivity(mail);
                return true;
            }
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            }
            return true;
        }
    }

    protected class WebBizWebChromeClient extends WebChromeClient {

        /**
         * MAX_QUOTA
         */
        private long MAX_QUOTA = 104857600L;

        @Override
        public void onReceivedTitle(WebView view, String title) {
            String url = view.getUrl();
            UmsLog.d("onReceivedTitle title:{}  url:{}", title, url);
            try {
                if (AbsWebBizWebView.this instanceof RemoteWebView
                        && UmsStringUtils
                        .isNotBlank((String) ((RemoteWebUmsUrl) AbsWebBizWebView.this
                                .getTo())
                                .getCustomeWebViewUrlParam("title"))) {
                    return;
                }
            } catch (Exception e) {
                UmsLog.e("", e);
            }
            if (UmsStringUtils.hasValue(title))
                ((IDynamicBizActivity) getMyContext()).setTitleText(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress > 25 && !initJsFrameFlag) {
                    UmsLog.d("WebBizWebChromeClient onProgressChanged load api js");
                    initJsFrameFlag = true;
                    String remoteWebJs = DynamicResourceManager.getInstance()
                            .getApiJs();
                    UmsLog.d("load api js {}", remoteWebJs);
                    if (UmsStringUtils.isBlank(remoteWebJs)) {
                        throw new Exception("要加载的js为空");
                    }
                    view.loadUrl(remoteWebJs);
                }
            } catch (Exception e) {
                UmsLog.e("onProgressChanged", e);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, JsPromptResult result) {
            UmsLog.d(
                    "call AbsWebBizWebView onJsPrompt, message:{}  defaultValue:{}",
                    message, defaultValue);
            try {
                String apiOptions = Base64Utils.decryptBase64(message);
                String apiFlag = Base64Utils.decryptBase64(defaultValue);
                if (!OpenConst.DynamicCommonConst.UMS_API_FLAG.equals(apiFlag)) {
                    return super.onJsPrompt(view, url, message, defaultValue,
                            result);
                }
                DynamicWebModel model = DynamicWebModel.getModel(apiOptions,
                        AbsWebBizWebView.this, (Activity) getMyContext(),
                        getMyHandler());
                if (model == null) {
                    OpenDialogManager.getInstance().showHint(getMyContext(),
                            Message.BIZ_TIP_UPDATA_APP);
                    result.cancel();
                } else if (model.getProcessor().IsSynchronizedProcessor()) {
                    model.getProcessor().process(model);
                    JSONObject ret = new JSONObject();
                    ret.put("info", model.getRequestModel().getRequest()
                            .getJSONObject("info"));
                    ret.put("data", model.getResponseModel().getResponse());
                    result.confirm(Base64Utils.encrypt(ret.toString()));
                } else {
                    getDynamicModelMap().put(model.getId(), model);
                    result.confirm();
                    model.getProcessor().process(model);
                }
            } catch (Exception e) {
                UmsLog.e("onJsPrompt", e);
                result.cancel();
            }
            return true;
        }

        @Override
        public void onConsoleMessage(String message, int lineNumber,
                                     String sourceID) {
            UmsLog.v("{}: Line {} : {}", sourceID, Integer.valueOf(lineNumber),
                    message);
            super.onConsoleMessage(message, lineNumber, sourceID);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (consoleMessage.message() != null)
                UmsLog.d(consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public void onExceededDatabaseQuota(String url,
                                            String databaseIdentifier, long currentQuota,
                                            long estimatedSize, long totalUsedQuota,
                                            QuotaUpdater quotaUpdater) {
            UmsLog.v(
                    "AbsWebBizWebView:  onExceededDatabaseQuota estimatedSize: {}  currentQuota: {}  totalUsedQuota: {}",
                    Long.valueOf(estimatedSize), Long.valueOf(currentQuota),
                    Long.valueOf(totalUsedQuota));
            if (estimatedSize < this.MAX_QUOTA) {
                long newQuota = estimatedSize;
                UmsLog.v("calling quotaUpdater.updateQuota newQuota: {}",
                        Long.valueOf(newQuota));
                quotaUpdater.updateQuota(newQuota);
            } else {
                quotaUpdater.updateQuota(currentQuota);
            }
        }
    }

    @Override
    public void loadUrl(ResourceProcessListener listener) throws Exception {

    }

    @Override
    protected void loadProcess() throws Exception {

    }

    @Override
    public void handleDestroy() {
        UmsLog.i("动态加载引擎执行销毁处理.");
        try {
            UmsLog.d("开始执行标准processor的销毁处理");
            OpenDynamicWebProcessorManager
                    .getInstance()
                    .handleDestroyProcessorByActivity((Activity) getMyContext());
            UmsLog.d("开始执行扩展processor的销毁处理");
            handleDestoryExtProcessorByActivity((Activity) getMyContext());
            UmsLog.i("完成销毁处理");
        } catch (Exception e) {
            UmsLog.e("动态加载引擎执行销毁处理失败", e);
        }
    }
    
}