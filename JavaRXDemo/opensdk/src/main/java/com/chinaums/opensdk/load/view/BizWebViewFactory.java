package com.chinaums.opensdk.load.view;

import android.app.Activity;
import android.os.Handler;

import com.chinaums.opensdk.cons.OpenConst.DynamicBizIntentExtra;
import com.chinaums.opensdk.download.model.AppPack;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.model.ConfApiLevelPack;
import com.chinaums.opensdk.download.model.RemoteWebPack;
import com.chinaums.opensdk.download.model.ShowPack;
import com.chinaums.opensdk.download.process.DynamicResourceManager;
import com.chinaums.opensdk.download.process.ResourceManager;
import com.chinaums.opensdk.load.factory.UmsUrlFactory;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.load.model.url.LocalActivityUmsUrl;
import com.chinaums.opensdk.load.model.url.LocalWebBizUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteActivityUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteBizUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteWebUmsUrl;
import com.chinaums.opensdk.util.UmsStringUtils;
import com.chinaums.opensdk.util.UriUtils;

import java.util.List;

public final class BizWebViewFactory {

    public static AbsBizWebView createBizWebView(Activity activity,
                                                 Handler dynamicHandler) throws Exception {
        AbsUmsUrl from = UmsUrlFactory.getUmsUrl(activity.getIntent()
                .getStringExtra(DynamicBizIntentExtra.PAGE_FROM));
        AbsUmsUrl to = UmsUrlFactory.getUmsUrl(activity.getIntent()
                .getStringExtra(DynamicBizIntentExtra.PAGE_TO), from);
        Boolean needBackHome = activity.getIntent().getBooleanExtra(
                DynamicBizIntentExtra.PAGE_NEED_BACK_HOME, false);
        Boolean isFullscreen = activity.getIntent().getBooleanExtra(
                DynamicBizIntentExtra.PAGE_IS_FULLSCREEN, false);
        Boolean isShowBottomToolbar = activity.getIntent().getBooleanExtra(
                DynamicBizIntentExtra.PAGE_IS_SHOW_BOTTOMTOOLBAR, false);
        Boolean isShowArea = activity.getIntent().getBooleanExtra(
                DynamicBizIntentExtra.PAGE_IS_SHOW_AREA, false);
        Integer apiLevel = activity.getIntent().getIntExtra(
                DynamicBizIntentExtra.PAGE_BIZ_API_LEVEL, 0);
        String bizCode = activity.getIntent().getStringExtra(
                DynamicBizIntentExtra.PAGE_BIZ_CODE);

        if (to == null) {
            throw new Exception("要加载的页面地址无法解析");
        } else if (UmsStringUtils.isBlank(to.getUmsUrl())) {
            throw new Exception("要加载的页面地址错误");
        } else if (DynamicResourceManager.getInstance().isBlackUrl(
                to.getUmsUrl())) {
            throw new Exception("要加载的页面不安全");
        }

        isFullscreen = to.isFullscreen().booleanValue() ? true : isFullscreen;
        isShowBottomToolbar = to.isShowBottomToolbar() ? true
                : isShowBottomToolbar;
        isShowArea = to.isShowArea() ? true : isShowArea;
        BasePack pack = null;
        if (UmsStringUtils.isNotBlank(bizCode))
            pack = DynamicResourceManager.getInstance().getBiz(bizCode);
        else
            pack = getPackByUmsUrl(to);
        return createBizWebView(activity, pack, to, from, dynamicHandler,
                needBackHome, isFullscreen, isShowBottomToolbar, isShowArea,
                apiLevel);
    }

    private static BasePack getPackByUmsUrl(AbsUmsUrl umsUrl) throws Exception {
        if (!(umsUrl instanceof LocalWebBizUmsUrl)) {
            return null;
        }
        LocalWebBizUmsUrl localUmsUrl = (LocalWebBizUmsUrl) umsUrl;
        if (UmsStringUtils.isBlank(localUmsUrl.getCode())) {
            return null;
        }
        List<BasePack> basePacks = ResourceManager.getInstance()
                .getPacksByCode(null, localUmsUrl.getCode());
        for (BasePack pack : basePacks) {
            if (pack instanceof ShowPack) {
                continue;
            }
            return pack;
        }
        return null;
    }

    private static AbsBizWebView createBizWebView(Activity activity,
                                                  BasePack pack, AbsUmsUrl to, AbsUmsUrl from,
                                                  Handler dynamicHandler, Boolean needBackHome, Boolean isFullscreen,
                                                  Boolean isShowBottomToolbar, Boolean isShowArea, Integer apiLevel)
            throws Exception {
        if (to instanceof LocalWebBizUmsUrl) {
            return createBizWebView(activity, pack, (LocalWebBizUmsUrl) to,
                    from, dynamicHandler, needBackHome, isFullscreen,
                    isShowBottomToolbar, isShowArea, apiLevel);
        } else if (to instanceof RemoteWebUmsUrl) {
            return createBizWebView(activity, pack, (RemoteWebUmsUrl) to, from,
                    dynamicHandler, needBackHome, isFullscreen,
                    isShowBottomToolbar, isShowArea, apiLevel);
        } else if (to instanceof LocalActivityUmsUrl) {
            return new NativeWebView(activity, to, from, pack, dynamicHandler,
                    needBackHome, isFullscreen, isShowBottomToolbar,
                    isShowArea, null);
        } else if (to instanceof RemoteActivityUmsUrl) {
            return new NativeWebView(activity, to, from, pack, dynamicHandler,
                    needBackHome, isFullscreen, isShowBottomToolbar,
                    isShowArea, null);
        } else if (to instanceof RemoteBizUmsUrl) {
            return new RemoteBizView(activity, to, from, null, dynamicHandler,
                    needBackHome, isFullscreen, isShowBottomToolbar,
                    isShowArea, null);
        } else {
            throw new Exception("不能处理要加载的地址");
        }
    }

    private static AbsBizWebView createBizWebView(Activity activity,
                                                  BasePack pack, LocalWebBizUmsUrl to, AbsUmsUrl from,
                                                  Handler dynamicHandler, Boolean needBackHome, Boolean isFullscreen,
                                                  Boolean isShowBottomToolbar, Boolean isShowArea, Integer apiLevel)
            throws Exception {
        if (pack == null) {
            throw new Exception("不能处理要加载的地址");
        } else if (pack instanceof RemoteWebPack || pack instanceof AppPack) {
            return createBizWebView(activity, pack,
                    UmsUrlFactory.getUmsUrl(pack), from, dynamicHandler,
                    needBackHome, isFullscreen, isShowBottomToolbar,
                    isShowArea, pack.getApiLevel());
        }
        return new LocalBizWebView(activity, to, from, pack, dynamicHandler,
                needBackHome, isFullscreen, isShowBottomToolbar, isShowArea,
                pack.getApiLevel());
    }

    private static AbsBizWebView createBizWebView(Activity activity,
                                                  BasePack pack, RemoteWebUmsUrl to, AbsUmsUrl from,
                                                  Handler dynamicHandler, Boolean needBackHome, Boolean isFullscreen,
                                                  Boolean isShowBottomToolbar, Boolean isShowArea, Integer apiLevel)
            throws Exception {
        if (from instanceof RemoteWebUmsUrl) {
            return createBizWebView(activity, to, (RemoteWebUmsUrl) from,
                    dynamicHandler, needBackHome, isFullscreen,
                    isShowBottomToolbar, isShowArea, apiLevel);
        } else if (pack != null) {
            return new RemoteWebView(activity, to, from, pack, dynamicHandler,
                    needBackHome, isFullscreen, isShowBottomToolbar,
                    isShowArea, pack.getApiLevel());
        } else {
            return new RemoteWebView(activity, to, from, null, dynamicHandler,
                    needBackHome, isFullscreen, isShowBottomToolbar,
                    isShowArea, ConfApiLevelPack.DEFAULT_LEVEL);
        }
    }

    private static AbsBizWebView createBizWebView(Activity activity,
                                                  RemoteWebUmsUrl to, RemoteWebUmsUrl from, Handler dynamicHandler,
                                                  Boolean needBackHome, Boolean isFullscreen,
                                                  Boolean isShowBottomToolbar, Boolean isShowArea, Integer apiLevel)
            throws Exception {
        if (UriUtils.checkEquals(to.getUmsUrl(), from.getUmsUrl())) {
            return new RemoteWebView(activity, to, from, null, dynamicHandler,
                    needBackHome, isFullscreen, isShowBottomToolbar,
                    isShowArea, apiLevel);
        } else {
            return new RemoteWebView(activity, to, from, null, dynamicHandler,
                    needBackHome, isFullscreen, isShowBottomToolbar,
                    isShowArea, ConfApiLevelPack.DEFAULT_LEVEL);
        }
    }

}
