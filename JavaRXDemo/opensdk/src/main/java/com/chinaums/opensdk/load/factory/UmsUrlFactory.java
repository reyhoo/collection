package com.chinaums.opensdk.load.factory;

import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.model.ShowPack;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.load.model.url.LocalActivityUmsUrl;
import com.chinaums.opensdk.load.model.url.LocalWebBizUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteActivityUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteBizUmsUrl;
import com.chinaums.opensdk.load.model.url.RemoteWebUmsUrl;
import com.chinaums.opensdk.util.BizUrlUtils;
import com.chinaums.opensdk.util.UmsStringUtils;

public final class UmsUrlFactory {

    public static AbsUmsUrl getUmsUrl(String url) {
        if (UmsStringUtils.isBlank(url)) {
            // 为空则返回空对象
            return null;
        } else if (BizUrlUtils.isLocalBiz(url)) {
            // 如果是资源包地址则返回本地资源包解析器
            return new LocalWebBizUmsUrl(url);
        } else if (BizUrlUtils.isRemoteWeb(url)) {
            // 如果是远程网页则返回网页解析器
            return new RemoteWebUmsUrl(url);
        } else if (BizUrlUtils.isThirdAppBiz(url)) {
            // 如果是第三方app则返回app native解析器
            return new RemoteActivityUmsUrl(url);
        } else if (BizUrlUtils.isNativeBiz(url)) {
            // 如果是本地native则返回本地native解析器
            return new LocalActivityUmsUrl(url);
        } else if (BizUrlUtils.isRemoteBiz(url)) {
            // 如果是仿真地址则返回仿真资源包解析器
            return new RemoteBizUmsUrl(url);
        }
        return null;
    }

    public static AbsUmsUrl getUmsUrl(String url, AbsUmsUrl fromUrl) {
        AbsUmsUrl toUrl = getUmsUrl(url);
        if (UmsStringUtils.isBlank(url)) {
            // 为空则返回空对象
            return null;
        } else if (toUrl != null) {
            // 不为空则返回对象
            return toUrl;
        } else if (fromUrl instanceof LocalWebBizUmsUrl) {
            // 尝试使用本地资源包规则进行创建
            return new LocalWebBizUmsUrl(url);
        } else if (fromUrl instanceof RemoteBizUmsUrl) {
            // 尝试使用资源包挡板规则进行创建
            return new RemoteBizUmsUrl(url);
        }
        return null;
    }

    public static AbsUmsUrl getUmsUrl(BasePack basePack) {
        if (basePack == null || basePack instanceof ShowPack) {
            return null;
        }
        String url = basePack.getOpenUrl();
        return getUmsUrl(url);
    }

}
