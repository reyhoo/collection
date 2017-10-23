package com.chinaums.opensdk.cons;


public final class OpenEnv {

    /**
     * 客户端环境总开关
     */
    private static OpenEnvironment currentEnvironment = OpenEnvironment.PROD;

    /**
     * 是否为生产验证版
     */
    private static boolean isProdVerify = false;

    /**
     * 后端环境---告诉服务器下面的路由走哪个环境。这个需求是全民付提过来的，需求的上下文就是开放平台只有两个环境
     * 生产环境和测试环境。但是全民付的有些业务有三个环境：生产环境、测试环境、UAT环境。全民付客户端请求发给开放
     * 平台，测试环境的开放平台不知道是连接具体业务的测试环境还是UAT环境。所以加上了这个状态位，为了给测试环境下
     * 的开放平台指定转发方向。目前这个字段只有在全民付的应用中使用，只有在测试环境的开放平台中识别，这个字段如果
     * 在不传任何值的情况下，默认走测试环境。这个字段只能有一个值为UAT。
     */
    private static String backendEnvironment = null;

    /**
     * 默认构造函数
     */
    private OpenEnv() {

    }

    public static OpenEnvironment getCurrentEnvironment() {
        return currentEnvironment;
    }

    public static void setCurrentEnvironment(OpenEnvironment currentEnvironment) {
        switch (currentEnvironment) {
            case PROD:
            case UAT:
            case TEST:
                OpenEnv.currentEnvironment = currentEnvironment;
                break;
            default:
                OpenEnv.currentEnvironment = OpenEnvironment.TEST;
                break;
        }
    }

    public static boolean isProdVerify() {
        return isProdVerify;
    }

    public static void setProdVerify(boolean isProdVerify) {
        OpenEnv.isProdVerify = isProdVerify;
    }

    public static String getBackendEnvironment() {
        return backendEnvironment;
    }

    public static void setBackendEnvironment(String backendEnvironment) {
        OpenEnv.backendEnvironment = backendEnvironment;
    }

}
