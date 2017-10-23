package com.chinaums.opensdk.load.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinaums.opensdk.cons.OpenConst.CHAR;
import com.chinaums.opensdk.cons.OpenConst.Message;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.listener.ResourceProcessListener;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteWebUmsUrl;
import com.chinaums.opensdk.util.UmsLog;


@SuppressLint("ViewConstructor")
public class RemoteWebView extends AbsWebBizWebView {

    public RemoteWebView(Context context, AbsUmsUrl to, AbsUmsUrl from,
                         BasePack basePack, Handler handler, Boolean needBackHome,
                         Boolean isFullscreen, Boolean isShowBottomToolbar,
                         Boolean isShowArea, Integer apiLevel) {
        super(context, to, from, basePack, handler, needBackHome, isFullscreen,
                isShowBottomToolbar, isShowArea, apiLevel);
    }

    @Override
    protected void customeConfig() {
        this.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.getSettings().setAppCacheEnabled(true);
        // 启用数据库
        this.getSettings().setDatabaseEnabled(true);
        String dir = this.getMyContext().getApplicationContext()
                .getDir("database", Context.MODE_PRIVATE).getPath();
        // 启用地理定位
        this.getSettings().setGeolocationEnabled(true);
        // 设置定位的数据库路径
        this.getSettings().setGeolocationDatabasePath(dir);
        // 最重要的方法，一定要设置，这就是出不来的主要原因
        this.getSettings().setDomStorageEnabled(true);
        // setPluginEnable已经弃用了，使用webSettings.setPluginState(WebSettings.PluginState.ON);可以代替，但是这个方法在API18之后也弃用了
        this.getSettings().setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= 19) {
            this.getSettings().setLoadsImagesAutomatically(true);
        } else {
            this.getSettings().setLoadsImagesAutomatically(false);
        }
        configWithUmsOpenParam();
    }

    /**
     * 根据umsOpenParam的参数值进行部分设置。
     */
    private void configWithUmsOpenParam() {
        if (((RemoteWebUmsUrl) getTo())
                .getCustomeWebViewUrlParam("isUseOriginalViewPort") == null) {
            return;
        }
        Object param = ((RemoteWebUmsUrl) getTo())
                .getCustomeWebViewUrlParam("isUseOriginalViewPort");
        if (param == null) {
            return;
        }
        if (CHAR.TRUE.equals(param.toString())) { // 使用自定义的网页缩放格式
            this.getSettings().setSupportZoom(false);
            this.getSettings().setBuiltInZoomControls(false);
            this.getSettings().setUseWideViewPort(false);
            this.getSettings().setLoadWithOverviewMode(false);
            this.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
        }
    }

    @Override
    protected WebViewClient makeNewWebViewClient() {
        return new RemoteWebViewClient();
    }

    @Override
    protected WebChromeClient makeNewWebChromeClient() {
        return new RemoteWebChromeClient();
    }

    @Override
    public void loadUrl(final ResourceProcessListener listener)
            throws Exception {
        try {
            loadData();
            listener.onUpdated();
        } catch (Exception e) {
            listener.onError(Message.BIZ_LOAD_ERROR, e);
        }
    }

    @Override
    protected void loadProcess() throws Exception {
        this.loadUrl(getTo().getUmsUrl());
    }

    @Override
    public void back() {
        if (getNeedBackHome() != null && getNeedBackHome().booleanValue()
                || !this.canGoBack()) {
            clearWebCache();
            super.back();
        } else {
            this.goBack();
        }
    }

    protected class RemoteWebViewClient extends WebBizWebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            try {
                if (!getSettings().getLoadsImagesAutomatically()) {
                    getSettings().setLoadsImagesAutomatically(true);
                }
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

    }

    protected class RemoteWebChromeClient extends WebBizWebChromeClient {

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            OpenDialogWarning(message, result);
            return true;
        }
    }

}
