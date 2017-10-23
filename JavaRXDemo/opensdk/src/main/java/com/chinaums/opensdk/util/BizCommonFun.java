package com.chinaums.opensdk.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.chinaums.opensdk.activity.WeexActivity;
import com.chinaums.opensdk.cons.OpenConst;
import com.chinaums.opensdk.cons.OpenConst.CHAR;
import com.chinaums.opensdk.download.listener.ResourceManagerMultiListener;
import com.chinaums.opensdk.download.listener.ResourceProcessListener;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.model.Resource;
import com.chinaums.opensdk.download.model.ThirdPartyAppPack;
import com.chinaums.opensdk.download.process.DynamicResourceManager;
import com.chinaums.opensdk.load.factory.UmsUrlFactory;
import com.chinaums.opensdk.load.model.data.DynamicExtraParam;
import com.chinaums.opensdk.load.model.url.AbsActivityUmsUrl;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteActivityUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteWebUmsUrl;
import com.chinaums.opensdk.load.view.NativeWebView;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.manager.OpenDialogManager;

public class BizCommonFun {

    /**
     * mHandler
     */
    private Handler mHandler = new Handler();

    /**
     * 上下文
     */
    private Context context;

    /**
     * 待打开的资源包
     */
    private BasePack pack;

    /**
     * 打开资源包时的参数
     */
    private String param;

    /**
     * 回调函数
     */
    private BizCommonCallback callback;

    /**
     * 准备资源包后加载资源包的回调
     */
    private BizOpenCallback openCallback;

    public BizCommonFun(Context context, BasePack pack, String param,
                        BizCommonCallback callback) {
        this.context = context;
        this.pack = pack;
        this.param = param;
        this.callback = callback;
    }

    public BizCommonFun(Context context, BasePack pack, String param,
                        BizCommonCallback callback, BizOpenCallback openCallback) {
        this.context = context;
        this.pack = pack;
        this.param = param;
        this.callback = callback;
        this.openCallback = openCallback;
    }

    public void openBizByPack() throws Exception {
        if (pack == null) {
            OpenDialogManager.getInstance().showHint(context, "业务未定义");
            return;
        }
        if (pack instanceof ThirdPartyAppPack
                && ((ThirdPartyAppPack) pack).isNeedInstall()) {
            try {
                ((ThirdPartyAppPack) pack).installApk(context);
                return;
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }
        if (!UmsStringUtils.hasValue(pack.getOpenUrl())) {
            throw new Exception("跳转的Url为空或异常");
        }
        if (UmsStringUtils.isNotBlank(param)) {
            pack.setParams(param);
        }
        // 处理打开 第三方Web页面且包含#cpUser.token#
        AbsUmsUrl to = UmsUrlFactory.getUmsUrl(pack);
        if ((to instanceof RemoteWebUmsUrl || to instanceof RemoteActivityUmsUrl)
                && pack.getParams().contains(DynamicExtraParam.CP_USER_TOKEN)) {
            getCpUserToken();
            return;
        }
        openDynamicActivity(pack.getOpenUrl());
    }

    /**
     * 打开动态加载界面
     *
     * @throws Exception
     */
    private void openDynamicActivity(String openUrl) throws Exception {
        try {
            if (openCallback != null) {
                openCallback.onFinish(openUrl);
                return;
            }

            AbsUmsUrl to = UmsUrlFactory.getUmsUrl(openUrl);
            if (to instanceof AbsActivityUmsUrl) {
                NativeWebView webView = new NativeWebView(context, to, null,
                        pack, new Handler(), false, false, false, false, null);
                webView.loadUrl(new ResourceProcessListener() {
                    @Override
                    public void onUpdated() {
                        UmsLog.d("加载完成.");
                    }

                    @Override
                    public void onError(String errorInfo, Exception e) {
                        UmsLog.e(errorInfo, e);
                        OpenDialogManager.getInstance().showHint(context,
                                "页面加载出错，请退出客户端后重试.");
                    }
                });
                return;
            } else {
                Intent intent;
                if (OpenConst.DynamicBizType.WEEX_BIZ.equalsIgnoreCase(pack.getType())) {
                    // WeexActivity的实现暂时不用代理的方式开出去，目前暂没有遇到定制化的需求
                    intent = new Intent(context, WeexActivity.class);
                } else {
                    intent = new Intent(context, OpenDelegateManager
                            .getProcessDelegate().getOpenDynamicBizActivityClazz());
                }
                intent.putExtra(OpenConst.DynamicBizIntentExtra.PAGE_TO, openUrl);
                if (pack != null) {
                    intent.putExtra(OpenConst.DynamicBizIntentExtra.PAGE_BIZ_CODE,
                            pack.getCode());
                }
                context.startActivity(intent);
            }
        } catch (Exception e) {
            UmsLog.e("", e);
            OpenDialogManager.getInstance().showHint(context,
                    "页面加载出错，请退出客户端后重试.");
        }

    }

    /**
     * 打开资源包界面前的预处理，native资源包不再打开中间activity并关闭。
     *
     * @param openUrl
     * @throws Exception
     */
    private boolean prepareDynamicActivity(String openUrl) throws Exception {

        return false;
    }

    /**
     * 先对资源包进行prepare，再打开资源包
     */
    public void prepareNOpenPack() {
        try {
            DynamicResourceManager.getInstance().prepareBiz(
                    bizPackPrepareMultiListener, pack.getCode());
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    ResourceManagerMultiListener bizPackPrepareMultiListener = new ResourceManagerMultiListener() {

        @Override
        public void onTotalProgress(final int progress) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    UmsLog.d("总体进度如下：" + progress);
                }
            });

        }

        @Override
        public void onUnableProcessError(final String errorInfo,
                                         final Exception e) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (e == null)
                        UmsLog.e(errorInfo);
                    else
                        UmsLog.e(errorInfo, e);

                    OpenDialogManager.getInstance()
                            .showHint(context, errorInfo);
                    OpenDialogManager.getInstance().cancelLoading();
                    callback.onFinish();
                }
            });

        }

        @Override
        public void onFinish() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (callback != null)
                        callback.onFinish();

                    if (getErrorCount().get() > 0) {
                        OpenDialogManager.getInstance().showHint(context,
                                "进入资源包时出错了.");
                        return;
                    }
                    try {
                        openBizByPack();
                    } catch (Exception e) {
                        String eString = e.toString();
                        OpenDialogManager.getInstance()
                                .showHint(
                                        context,
                                        eString.substring(eString
                                                .indexOf(CHAR.COLON) + 1));
                    }
                }
            });

        }

        @Override
        public void onProgress(final Resource pack, final String msg,
                               final int progress) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (pack != null && pack instanceof BasePack)
                        UmsLog.d("[" + ((BasePack) pack).getCode() + "]进度如下："
                                + msg + "(" + progress + ")");
                }
            });
        }
    };

    public interface BizCommonCallback {
        public void onFinish();
    }

    /**
     * 资源包准备完成后打开资源包的处理回调
     *
     * @author ZhangZhao
     */
    public interface BizOpenCallback {
        public void onFinish(String openUrl);
    }

    /**
     * 从CP获取的token，用于支付验证 #cpUser.token#
     *
     * @throws Exception
     */
    public void getCpUserToken() throws Exception {
        // 从开放平台前置获取TOKEN改成从厦门手机前置获取TOKEN
    }

}
