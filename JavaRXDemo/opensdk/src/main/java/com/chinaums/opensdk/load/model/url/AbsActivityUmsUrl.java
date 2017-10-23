package com.chinaums.opensdk.load.model.url;

import android.content.ComponentName;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.util.BizUrlUtils;
import com.chinaums.opensdk.util.UmsLog;

import java.util.Iterator;

/**
 * 类实现描述:所有打开本地activity页面的URL。URL的前缀为："ums://page/"
 */
public abstract class AbsActivityUmsUrl extends AbsUmsUrl {

    /**
     * openPage：页面的功能名
     */
    private String openPage;

    public AbsActivityUmsUrl(String umsUrl) {
        super(umsUrl);
    }

    @Override
    protected void initByCustome() {
        try {
            String url = getUmsUrl();
            url = url.substring(BizUrlUtils.getNativeBizStartFlag().length());
            // [?]后面跟着的是打开组件的参数，目前用不上
            String[] array = url.split("[?]");
            openPage = array[0];
            initDataCustom(array);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    @Override
    public String generateUmsUrl(String toUrl) throws Exception {
        return toUrl;
    }

    @Override
    public String getUmsUrl() {
        if (!checkEffective()) {
            return null;
        } else {
            return getUrl();
        }
    }

    /**
     * 得到要打开的页面的intent，打开activity的参数是从data中来的。
     */
    public Intent getOpenActivityIntent() throws Exception {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName(getActivityPackage(),
                getActivityName());
        intent.setComponent(comp);
        JSONObject json = getData();
        if (json == null)
            return intent;
        for (Iterator<?> iter = json.keySet().iterator(); iter.hasNext(); ) {
            String key = iter.next().toString();
            intent.putExtra(key, json.getString(key));
        }
        return intent;
    }

    public final String getOpenPage() {
        return openPage;
    }

    /**
     * 这个函数的参数一点用处也没有，这个参数的字符串数组是来自于url中?后面的参数
     */
    protected abstract void initDataCustom(String[] urlArray) throws Exception;

    protected abstract String getActivityPackage() throws Exception;

    protected abstract String getActivityName() throws Exception;

}
