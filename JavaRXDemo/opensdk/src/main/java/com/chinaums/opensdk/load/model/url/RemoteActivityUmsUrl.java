package com.chinaums.opensdk.load.model.url;


/**
 * 打开第三方应用程序的URL，比如打开支付宝应用程序的客户端页面的URL
 */
public class RemoteActivityUmsUrl extends AbsActivityUmsUrl {

    /**
     * 第三方程序的包名
     */
    private String activityPackage;

    /**
     * 第三方程序的类名
     */
    private String activityName;

    /**
     * @param umsUrl
     */
    public RemoteActivityUmsUrl(String umsUrl) {
        super(umsUrl);
    }

    /**
     * 好奇怪：这个函数的参数一点用都没有，哈哈。这个函数的参数是
     * 打开本地页面所需的参数url中？号后面的参数。
     * 这个函数的作用是解析出需要打开哪个组件以及打开的参数
     */
    @Override
    protected void initDataCustom(String[] urlArray) throws Exception {
        activityPackage = getData().getString("activityPackage");
        activityName = getData().getString("activityName");
    }

    @Override
    protected String getActivityPackage() throws Exception {
        return activityPackage;
    }

    @Override
    protected String getActivityName() throws Exception {
        return activityName;
    }

}
