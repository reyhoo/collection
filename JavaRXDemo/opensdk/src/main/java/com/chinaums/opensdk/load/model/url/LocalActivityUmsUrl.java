package com.chinaums.opensdk.load.model.url;

import android.app.Activity;

import com.chinaums.opensdk.manager.OpenDelegateManager;

import java.util.Map;

/**
 * 打开本应用的activity页面的url：{"url":"ums://page/TransitScanPay"}
 */
public class LocalActivityUmsUrl extends AbsActivityUmsUrl {

    /**
     * LocalActivityUmsUrl对应的打开本地Activity的类名
     */
    private Class<? extends Activity> openActivityClazz;

    /**
     * @param umsUrl
     */
    public LocalActivityUmsUrl(String umsUrl) {
        super(umsUrl);
    }

    /**
     * 好奇怪：这个函数的参数一点用都没有，哈哈。这个函数的参数是
     * 打开本地页面所需的参数url中？号后面的参数。
     * 这个函数的作用是解析出需要打开哪个组件以及打开的参数
     */
    @Override
    protected void initDataCustom(String[] urlArray) throws Exception {
        Map<String, Class<? extends Activity>> map = OpenDelegateManager
                .getProcessDelegate().getDynamicOpenPageMap();
        if (map == null || map.isEmpty())
            throw new Exception("没有设置要打开的本地APP的界面配置 ");
        Class<? extends Activity> clazz = map.get(getOpenPage());
        if (clazz == null)
            throw new Exception("找不到要打开本地APP的界面  [openPage=]" + getOpenPage());
        openActivityClazz = clazz;
    }

    @Override
    protected String getActivityPackage() throws Exception {
        return OpenDelegateManager.getPackageName();
    }

    @Override
    protected String getActivityName() throws Exception {
        return openActivityClazz.getName();
    }

}
