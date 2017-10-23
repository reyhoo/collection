package com.chinaums.opensdk.load.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.chinaums.opensdk.cons.OpenConst.Message;
import com.chinaums.opensdk.download.listener.ResourceManagerMultiListener;
import com.chinaums.opensdk.download.listener.ResourceProcessListener;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.model.Resource;
import com.chinaums.opensdk.download.model.ThirdPartyAppPack;
import com.chinaums.opensdk.download.process.ResourceManager;
import com.chinaums.opensdk.exception.NotUmsNativeUrlException;
import com.chinaums.opensdk.load.model.url.AbsActivityUmsUrl;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.util.UmsLog;

@SuppressLint("ViewConstructor")
public class NativeWebView extends AbsBizWebView {

    public NativeWebView(Context context, AbsUmsUrl to, AbsUmsUrl from,
                         BasePack basePack, Handler handler, Boolean needBackHome,
                         Boolean isFullscreen, Boolean isShowBottomToolbar,
                         Boolean isShowArea, Integer apiLevel) {
        super(context, to, from, basePack, handler, needBackHome, isFullscreen,
                isShowBottomToolbar, isShowArea, apiLevel);
    }

    @Override
    public void config() throws Exception {

    }

    @Override
    public void loadUrl(final ResourceProcessListener listener)
            throws Exception {
        if (null == getBasePack()) {
            loadData();
            return;
        }
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
        AbsUmsUrl target = getTo();
        if (getBasePack() instanceof ThirdPartyAppPack
                && ((ThirdPartyAppPack) getBasePack()).isNeedInstall()) {
            ((ThirdPartyAppPack) getBasePack()).installApk(getMyContext());
        }
        if (target == null || !(target instanceof AbsActivityUmsUrl)) {
            throw new NotUmsNativeUrlException("要打开的内容异常.");
        }
        Intent intent = ((AbsActivityUmsUrl) target).getOpenActivityIntent();
        getMyContext().startActivity(intent);
    }

    @Override
    public void handleDestroy() {

    }
    
}
