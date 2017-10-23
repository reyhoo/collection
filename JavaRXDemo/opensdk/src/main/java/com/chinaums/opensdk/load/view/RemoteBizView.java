package com.chinaums.opensdk.load.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.chinaums.opensdk.cons.OpenConst.Message;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.listener.ResourceProcessListener;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;


@SuppressLint("ViewConstructor")
public class RemoteBizView extends AbsWebBizWebView {

    public RemoteBizView(Context context, AbsUmsUrl to, AbsUmsUrl from,
                         BasePack basePack, Handler handler, Boolean needBackHome,
                         Boolean isFullscreen, Boolean isShowBottomToolbar,
                         Boolean isShowArea, Integer apiLevel) {
        super(context, to, from, basePack, handler, needBackHome, isFullscreen,
                isShowBottomToolbar, isShowArea, apiLevel);
    }

    @Override
    protected void customeConfig() {
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.getSettings().setAppCacheEnabled(false);
        clearWebCache();
    }

    @Override
    protected WebViewClient makeNewWebViewClient() {
        return new WebBizWebViewClient();
    }

    @Override
    protected WebChromeClient makeNewWebChromeClient() {
        return new WebBizWebChromeClient();
    }

    @Override
    public void loadUrl(ResourceProcessListener listener) throws Exception {
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

}
