package com.chinaums.opensdk.load.model.url;

import com.chinaums.opensdk.cons.OpenConst.CHAR;
import com.chinaums.opensdk.util.BizUrlUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

/**
 * 打开本地资源包的某个页面的URL
 */
public class LocalWebBizUmsUrl extends AbsUmsUrl {

    /**
     * 业务编码
     */
    private String code;

    /**
     * 页面名称
     */
    private String page;

    /**
     * 页面URI
     */
    private String pageFolder;

    public LocalWebBizUmsUrl(String umsUrl) {
        super(umsUrl);
    }

    @Override
    protected void initByCustome() {
        try {
            if (UmsStringUtils.isBlank(getPath())) {
                UmsLog.i("要加载的业务的路径为空 url:{}", getUrl());
                throw new Exception("要加载的业务的路径为空");
            }
            String[] pathArray = getPath().split(CHAR.SLASH); // 由于path规则是/a/b,所以数组第一个为空
            if (pathArray == null || pathArray.length <= 2) {
                UmsLog.i("要加载的业务的路径配置不正确 url:{}", getUrl());
                throw new Exception("要加载的业务的路径配置不正确");
            }
            String[] tempArray = new String[pathArray.length - 1];
            System.arraycopy(pathArray, 1, tempArray, 0, pathArray.length - 1);
            initDataForPath(tempArray);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    @Override
    public String generateUmsUrl(String toUrl) throws Exception {
        StringBuilder ret = new StringBuilder(50);
        if (UmsStringUtils.isBlank(toUrl) || !BizUrlUtils.isRelativeBiz(toUrl)) {
            return toUrl;
        }
        ret.append(getScheme()).append(CHAR.COLON).append(CHAR.SLASH)
                .append(CHAR.SLASH).append(getHost()).append(CHAR.SLASH)
                .append(getCode()).append(CHAR.SLASH).append(toUrl);
        return ret.toString();
    }

    private void initDataForPath(String[] pathArray) throws Exception {
        code = pathArray[0];
        String[] tempArray = new String[pathArray.length - 1];
        System.arraycopy(pathArray, 1, tempArray, 0, pathArray.length - 1);
        initDataForPathExcludeCode(tempArray);
    }

    private void initDataForPathExcludeCode(String[] pathArray)
            throws Exception {
        page = "";
        pageFolder = "";
        for (int i = 0; i < pathArray.length; i++) {
            page += pathArray[i];
            if (i + 1 < pathArray.length) {
                page += CHAR.SLASH;
                pageFolder = page;
            }
        }
    }

    @Override
    public String getUmsUrl() {
        StringBuilder ret = null;
        if (!checkEffective() || UmsStringUtils.isBlank(getCode())
                || UmsStringUtils.isBlank(getPage())) {
            return null;
        }
        ret = new StringBuilder(60);
        ret.append(getScheme()).append(CHAR.COLON).append(CHAR.SLASH)
                .append(CHAR.SLASH).append(getHost()).append(CHAR.SLASH)
                .append(getCode()).append(CHAR.SLASH).append(getPage())
                .append(CHAR.QUESTION_MARK).append(getQueryStr());
        return ret.toString();
    }

    public final String getCode() {
        return code;
    }

    public final String getPage() {
        return page;
    }

    public final String getPageFolder() {
        return pageFolder;
    }

}
