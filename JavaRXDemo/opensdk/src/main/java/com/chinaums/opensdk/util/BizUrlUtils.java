package com.chinaums.opensdk.util;

import android.annotation.SuppressLint;

import com.chinaums.opensdk.cons.OpenConst.BizUrlStartFlag;

@SuppressLint("DefaultLocale")
public final class BizUrlUtils {

    /**
     * 是否本地资源包页面
     *
     * @param url
     * @return
     */
    public static boolean isLocalBiz(String url) {
        return isAbsolutelyLocalBiz(url);
    }

    /**
     * 是否绝对路径的本地资源包页面
     *
     * @param url
     * @return
     */
    public static boolean isAbsolutelyLocalBiz(String url) {
        if (UmsStringUtils.isBlank(url))
            return false;
        if (url.startsWith(getLocalBizStartFlag()))
            return true;
        return false;
    }

    /**
     * 是否相对路径的本地资源包页面
     *
     * @param url
     * @return
     */
    public static boolean isRelativeBiz(String url) {
        if (!isAbsolutelyLocalBiz(url) && !isRemoteBiz(url)
                && !isNativeBiz(url) && url.contains("param")
                && !url.startsWith("http"))
            return true;
        return false;
    }

    /**
     * 是否远程页面
     *
     * @param url
     * @return
     */
    public static boolean isRemoteBiz(String url) {
        if (UmsStringUtils.isBlank(url))
            return false;
        if (url.startsWith(getRemoteBizStartFlag()))
            return true;
        return false;
    }

    /**
     * @param url
     * @return
     */
    public static boolean isRemoteWeb(String url) {
        if (UmsStringUtils.isBlank(url))
            return false;
        if (url.startsWith(getRemoteHttpWebStartFlag())
                || url.startsWith(getRemoteHttpsWebStartFlag()))
            return true;
        return false;
    }

    /**
     * 是否本地app页面
     *
     * @param url
     * @return
     */
    public static boolean isNativeBiz(String url) {
        if (UmsStringUtils.isBlank(url))
            return false;
        if (url.startsWith(getNativeBizStartFlag()))
            return true;
        return false;
    }

    /**
     * 是第三方app页面
     *
     * @param url
     * @return
     */
    public static boolean isThirdAppBiz(String url) {
        if (UmsStringUtils.isBlank(url))
            return false;
        if (url.startsWith(getThirdAppBizStartFlag()))
            return true;
        return false;
    }

    /**
     * @return
     */
    public static String getLocalBizStartFlag() {
        return BizUrlStartFlag.LOCAL_WEB;
    }

    /**
     * @return
     */
    public static String getRemoteBizStartFlag() {
        return BizUrlStartFlag.REMOTE_BIZ_WEB;
    }

    /**
     * @return
     */
    public static String getRemoteHttpWebStartFlag() {
        return BizUrlStartFlag.REMOTE_HTTP_WEB;
    }

    /**
     * @return
     */
    public static String getRemoteHttpsWebStartFlag() {
        return BizUrlStartFlag.REMOTE_HTTPS_WEB;
    }

    /**
     * @return
     */
    public static String getNativeBizStartFlag() {
        return BizUrlStartFlag.NATIVE;
    }

    /**
     * @return
     */
    public static String getThirdAppBizStartFlag() {
        return BizUrlStartFlag.THIRD_BIZ_APP;
    }

}
