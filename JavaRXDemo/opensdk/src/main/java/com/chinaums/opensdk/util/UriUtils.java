package com.chinaums.opensdk.util;

import android.annotation.SuppressLint;

import com.chinaums.opensdk.cons.OpenConst.CHAR;
import com.chinaums.opensdk.cons.OpenConst.ENCODING;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public final class UriUtils {

    /**
     * url中的参数做了转换
     */
    public static URI format2Uri(String url) {
        URI uri = null;
        try {
            if (UmsStringUtils.isBlank(url)) {
                return uri;
            }
            String uriStr = null;
            String urlArr[] = url.split("\\?");
            if (urlArr.length > 1) {
                uriStr = urlArr[0] + "?"
                        + URLEncoder.encode(urlArr[1], "UTF-8");
            } else {
                uriStr = urlArr[0];
            }
            uri = new URI(uriStr);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return uri;
    }

    /**
     * @param url
     * @return
     */
    public static Map<String, String> getQueryParam(String url) {
        Map<String, String> map = null;
        try {
            URI uri = null;
            String queryStr = null;
            if (UmsStringUtils.isBlank(url))
                return map;
            uri = new URI(url.replace("&", "%26").replace("#", "%23"));
            queryStr = uri.getQuery();
            if (UmsStringUtils.isBlank(queryStr)) {
                return map;
            } else {
                map = getQueryParamByQueryStr(queryStr);
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return map;
    }

    /**
     * @param url
     * @param toLowerCase
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String decode(String url, boolean toLowerCase) {
        String ret = null;
        try {
            if (UmsStringUtils.isNotBlank(url)) {
                ret = URLDecoder.decode(url, ENCODING.UTF8);
            }
            if (toLowerCase) {
                ret = ret.toLowerCase(Locale.ENGLISH);
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return ret;
    }

    /**
     * @param url1
     * @param url2
     * @return
     */
    public static boolean checkEquals(String url1, String url2) {
        boolean ret = false;
        try {
            if (UmsStringUtils.isBlank(url1) || UmsStringUtils.isBlank(url2)) {
                return ret;
            }
            URI uri1 = new URI(url1);
            URI uri2 = new URI(url2);
            if (!uri1.getScheme().equals(uri2.getScheme())) {
                return ret;
            } else if (!uri1.getHost().equals(uri2.getHost())) {
                return ret;
            } else if (uri1.getPort() != uri2.getPort()) {
                return ret;
            }
            ret = true;
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return ret;
    }

    /**
     * @param queryStr
     * @return
     * @throws Exception
     */
    public static Map<String, String> getQueryParamByQueryStr(String queryStr)
            throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        try {
            String[] queryArray = null;
            if (UmsStringUtils.isBlank(queryStr)) {
                return map;
            }
            queryArray = queryStr.split(CHAR.AMPERSAND);
            for (String paramStr : queryArray) {
                String[] paramArray = null;
                String key = null;
                String value = null;
                if (UmsStringUtils.isBlank(paramStr)) {
                    continue;
                }
                paramArray = paramStr.split(CHAR.EQUAL);
                if (paramArray == null || paramArray.length <= 0) {
                    continue;
                }
                key = paramArray[0];
                value = paramArray.length > 1 ? paramArray[1] : null;
                map.put(key, value);
            }
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    /**
     * @param url
     * @param paramKey
     * @param paramValue
     * @return
     */
    public static String addPararms(String url, String paramKey,
                                    String paramValue) {
        Map<String, String> urlQueryMap = UriUtils.getQueryParam(url);
        if (urlQueryMap == null || urlQueryMap.isEmpty()) {
            url += "?" + paramKey + "=" + paramValue;
        } else {
            url += "&" + paramKey + "=" + paramValue;
        }
        return url;
    }

}
