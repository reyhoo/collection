package com.chinaums.opensdk.load.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinaums.opensdk.cons.OpenConst.CHAR;
import com.chinaums.opensdk.cons.OpenConst.DynamicCommonConst;
import com.chinaums.opensdk.cons.OpenConst.DynamicUmsUrlParam;
import com.chinaums.opensdk.cons.OpenConst.Message;
import com.chinaums.opensdk.download.listener.ResourceManagerMultiListener;
import com.chinaums.opensdk.download.listener.ResourceProcessListener;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.model.Resource;
import com.chinaums.opensdk.download.process.ResourceManager;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.load.model.url.LocalWebBizUmsUrl;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsLog;


@SuppressLint("ViewConstructor")
public class LocalBizWebView extends AbsWebBizWebView {

    public LocalBizWebView(Context context, AbsUmsUrl to, AbsUmsUrl from,
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
        if (getTo() != null && getTo() instanceof LocalWebBizUmsUrl
                && ((LocalWebBizUmsUrl) getTo()).getData() != null) {
            if (((LocalWebBizUmsUrl) getTo()).getData().containsKey(
                    DynamicUmsUrlParam.PARAM_BACK_NUM)) {
                setBackNum(((LocalWebBizUmsUrl) getTo()).getData().getIntValue(
                        DynamicUmsUrlParam.PARAM_BACK_NUM));
            } else {
                setBackNum(1);
            }
        }
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
    public void loadUrl(final ResourceProcessListener listener)
            throws Exception {
        ResourceManager.getInstance().prepare(null, getBasePack(),
                new ResourceManagerMultiListener() {

                    @Override
                    public void onProgress(Resource pack, String msg,
                                           int progress) {

                    }

                    @Override
                    public void onUnableProcessError(String errorInfo,
                                                     Exception e) {
                        UmsLog.d("业务加载出现不可处理错误  errorInfo:{}", errorInfo);
                        UmsLog.e("", e);
                        listener.onError(Message.BIZ_LOAD_ERROR, e);
                    }

                    @Override
                    public void onTotalProgress(int progress) {
                        UmsLog.d("业务加载总体进度: {}", progress);

                    }

                    @Override
                    public void onFinish() {
                        UmsLog.d(
                                "业务加载完成  总数:{}  正确数 :{}  不可或略错误数:{}  忽略错误数:{} ",
                                getCount(), getSuccessCount(), getErrorCount(),
                                getIgnoreResouceErrorCount());
                        try {
                            if (getErrorCount().intValue() <= 0) {
                                loadData();
                                listener.onUpdated();
                            } else {
                                listener.onError(
                                        Message.BIZ_PREPARE_ERROR,
                                        new Exception(Message.BIZ_PREPARE_ERROR));
                            }
                        } catch (Exception e) {
                            UmsLog.e("", e);
                            listener.onError(Message.BIZ_PREPARE_ERROR,
                                    new Exception(Message.BIZ_PREPARE_ERROR));
                        }
                    }
                });
    }

    @Override
    protected void loadProcess() throws Exception {
        String filePath = getBasePack().getResProcessPath() + CHAR.SLASH
                + ((LocalWebBizUmsUrl) getTo()).getPage();
        String url = DynamicCommonConst.LOCAL_FILE_PREFIX + filePath;
        byte[] is = UmsFileUtils.readFile(filePath);
        if (is == null || is.length <= 0)
            throw new Exception("加载的资源不存在");
        loadUrl(url);
    }

    protected class LocalBizWebViewClient extends WebBizWebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                UmsLog.d(
                        "in LocalWebViewClient shouldOverrideUrlLoading url:{}",
                        url);
                // 发现这个跳转地址处理不了，则打开新activity并选择合适的webview处理该内容。
                DynamicWebModel model = DynamicWebModel.getPageForwardModel(
                        url, LocalBizWebView.this, (Activity) getMyContext(),
                        getMyHandler());
                if (model != null) {
                    model.getProcessor().process(model);
                    getDynamicModelMap().put(model.getId(), model);
                    return true;
                }
            } catch (Exception e) {
                UmsLog.e("", e);
            }
            return super.shouldOverrideUrlLoading(view, url); // 处理完毕
        }

    }
}
