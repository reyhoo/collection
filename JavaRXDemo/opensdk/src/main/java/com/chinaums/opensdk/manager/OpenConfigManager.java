package com.chinaums.opensdk.manager;

import android.content.Context;

import com.chinaums.opensdk.cons.OpenConst.CHAR;
import com.chinaums.opensdk.cons.OpenConst.UmsOpenCore;
import com.chinaums.opensdk.cons.OpenEnv;
import com.chinaums.opensdk.cons.OpenEnvironment;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OpenConfigManager implements IOpenManager {

    public static final String UMS_OPEN_CORE = UmsOpenCore.UMS_OPEN_CORE; // 动态加载内核版本

    public static final String BCS_VERIFY_KEY_EXP = "bcs.verify_key_exp"; // 签名文件验签秘钥
    public static final String BCS_VERIFY_KEY_MOD = "bcs.verify_key_mod"; // 签名文件验签秘钥

    public static final String DOWNLOAD_FILE_BIZLIST_URL = "download.file.bizlist.url"; // 全业务列表下载地址
    public static final String DOWNLOAD_FILE_CATEGORY_URL = "download.file.category.url"; // 分类列表下载地址
    public static final String DOWNLOAD_FILE_AREA_URL = "download.file.area.url"; // 地区列表下载地址
    public static final String DOWNLOAD_FILE_CLIENT_UPDATE_URL = "download.file.client.update.url"; // 客户端升级列表下载地址
    public static final String DOWNLOAD_FILE_BIZLIST_SIGN_URL = "download.file.bizlist.sign.url"; // 全业务列表签名下载地址
    public static final String DOWNLOAD_FILE_CATEGORY_SIGN_URL = "download.file.category.sign.url"; // 分类列表签名下载地址
    public static final String DOWNLOAD_FILE_AREA_SIGN_URL = "download.file.area.sign.url"; // 地区列表签名下载地址
    public static final String DOWNLOAD_FILE_CLIENT_UPDATE_SIGN_URL = "download.file.client.update.sign.url"; // 客户端升级列表签名下载地址

    private Properties testConfig, uatConfig, prodConfig, prodVerifyConfig;

    private Map<OpenEnvironment, Properties> configs = new HashMap<OpenEnvironment, Properties>();

    private static OpenConfigManager instance;

    private void initTest() {
        testConfig = new Properties();
        testConfig.setProperty(BCS_VERIFY_KEY_EXP, "65537");
        testConfig
                .setProperty(
                        BCS_VERIFY_KEY_MOD,
                        "119365275342134182903604390714530224715254801668728152180700127490204975345104095541443702108856482668061922622849752192122860682153310127748031772057188423675601394634685622353125586639216120565242081130421026658161653963878324225669622683946671814449076107426853483243213280387762597149739968587102764679011");
        testConfig.setProperty(DOWNLOAD_FILE_BIZLIST_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/bizlist.zip");
        testConfig.setProperty(DOWNLOAD_FILE_CATEGORY_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/category.zip");
        testConfig.setProperty(DOWNLOAD_FILE_AREA_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/area.zip");
        testConfig.setProperty(DOWNLOAD_FILE_CLIENT_UPDATE_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + "/clientUpdate.zip");
        testConfig.setProperty(DOWNLOAD_FILE_BIZLIST_SIGN_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/bizlist.sign");
        testConfig.setProperty(DOWNLOAD_FILE_CATEGORY_SIGN_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/category.sign");
        testConfig.setProperty(DOWNLOAD_FILE_AREA_SIGN_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/area.sign");
        testConfig.setProperty(DOWNLOAD_FILE_CLIENT_UPDATE_SIGN_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + "/clientUpdate.sign");
    }

    private void initUat() {
        uatConfig = new Properties();
        uatConfig.setProperty(BCS_VERIFY_KEY_EXP, "65537");
        uatConfig
                .setProperty(
                        BCS_VERIFY_KEY_MOD,
                        "119365275342134182903604390714530224715254801668728152180700127490204975345104095541443702108856482668061922622849752192122860682153310127748031772057188423675601394634685622353125586639216120565242081130421026658161653963878324225669622683946671814449076107426853483243213280387762597149739968587102764679011");
        uatConfig.setProperty(DOWNLOAD_FILE_BIZLIST_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/bizlist.zip");
        uatConfig.setProperty(DOWNLOAD_FILE_CATEGORY_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/category.zip");
        uatConfig.setProperty(DOWNLOAD_FILE_AREA_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/area.zip");
        uatConfig.setProperty(DOWNLOAD_FILE_CLIENT_UPDATE_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + "/clientUpdate.zip");
        uatConfig.setProperty(DOWNLOAD_FILE_BIZLIST_SIGN_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/bizlist.sign");
        uatConfig.setProperty(DOWNLOAD_FILE_CATEGORY_SIGN_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/category.sign");
        uatConfig.setProperty(DOWNLOAD_FILE_AREA_SIGN_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/area.sign");
        uatConfig.setProperty(DOWNLOAD_FILE_CLIENT_UPDATE_SIGN_URL,
                "http://210.22.91.77:29014/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + "/clientUpdate.sign");
    }

    private void initProd() {
        prodConfig = new Properties();
        prodConfig.setProperty(BCS_VERIFY_KEY_EXP, "65537");
        prodConfig
                .setProperty(
                        BCS_VERIFY_KEY_MOD,
                        "137658193471711598104958374968725310608677239391337654420186364667627925068028680636693867490102018191644972490571399625525590366398364726142632719764806950879309548425426986174025833812086318367730423013855225402573267453693070362850158096410569448572839016797093738490734322596713574922444005398902195608863");
        prodConfig.setProperty(DOWNLOAD_FILE_BIZLIST_URL,
                "http://res.mop.chinaums.com/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/bizlist.zip");
        prodConfig.setProperty(DOWNLOAD_FILE_CATEGORY_URL,
                "http://res.mop.chinaums.com/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/category.zip");
        prodConfig.setProperty(DOWNLOAD_FILE_AREA_URL,
                "http://res.mop.chinaums.com/bcsres/bizlist/release/area.zip");
        prodConfig.setProperty(DOWNLOAD_FILE_CLIENT_UPDATE_URL,
                "http://res.mop.chinaums.com/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + "/clientUpdate.zip");
        prodConfig.setProperty(DOWNLOAD_FILE_BIZLIST_SIGN_URL,
                "http://res.mop.chinaums.com/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/bizlist.sign");
        prodConfig.setProperty(DOWNLOAD_FILE_CATEGORY_SIGN_URL,
                "http://res.mop.chinaums.com/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientType()
                        + CHAR.SLASH
                        + OpenEnvManager.getClientInfo().getClientVersion()
                        + "/category.sign");
        prodConfig.setProperty(DOWNLOAD_FILE_AREA_SIGN_URL,
                "http://res.mop.chinaums.com/bcsres/bizlist/release/area.sign");
        prodConfig.setProperty(DOWNLOAD_FILE_CLIENT_UPDATE_SIGN_URL,
                "http://res.mop.chinaums.com/bcsres/bizlist/release/"
                        + OpenEnvManager.getClientInfo().getClientId()
                        + "/clientUpdate.sign");
    }

    private void initProdVerify() {
        prodVerifyConfig = new Properties();
        prodVerifyConfig.setProperty(BCS_VERIFY_KEY_EXP,
                testConfig.getProperty(BCS_VERIFY_KEY_EXP));
        prodVerifyConfig.setProperty(BCS_VERIFY_KEY_MOD,
                testConfig.getProperty(BCS_VERIFY_KEY_MOD));
        prodVerifyConfig.setProperty(DOWNLOAD_FILE_BIZLIST_URL,
                testConfig.getProperty(DOWNLOAD_FILE_BIZLIST_URL));
        prodVerifyConfig.setProperty(DOWNLOAD_FILE_CATEGORY_URL,
                testConfig.getProperty(DOWNLOAD_FILE_CATEGORY_URL));
        prodVerifyConfig.setProperty(DOWNLOAD_FILE_AREA_URL,
                testConfig.getProperty(DOWNLOAD_FILE_AREA_URL));
        prodVerifyConfig.setProperty(DOWNLOAD_FILE_CLIENT_UPDATE_URL,
                testConfig.getProperty(DOWNLOAD_FILE_CLIENT_UPDATE_URL));
        prodVerifyConfig.setProperty(DOWNLOAD_FILE_BIZLIST_SIGN_URL,
                testConfig.getProperty(DOWNLOAD_FILE_BIZLIST_SIGN_URL));
        prodVerifyConfig.setProperty(DOWNLOAD_FILE_CATEGORY_SIGN_URL,
                testConfig.getProperty(DOWNLOAD_FILE_CATEGORY_SIGN_URL));
        prodVerifyConfig.setProperty(DOWNLOAD_FILE_AREA_SIGN_URL,
                testConfig.getProperty(DOWNLOAD_FILE_AREA_SIGN_URL));
        prodVerifyConfig.setProperty(DOWNLOAD_FILE_CLIENT_UPDATE_SIGN_URL,
                testConfig.getProperty(DOWNLOAD_FILE_CLIENT_UPDATE_SIGN_URL));
    }

    synchronized public static OpenConfigManager getInstance() {
        if (instance == null) {
            instance = new OpenConfigManager();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        UmsLog.i("init ConfigManager");
        initTest();
        initUat();
        initProd();
        initProdVerify();
        configs.put(OpenEnvironment.TEST, testConfig);
        configs.put(OpenEnvironment.UAT, uatConfig);
        configs.put(OpenEnvironment.PROD, prodConfig);
    }

    static public String getProperty(String name) {
        String property = null;
        try {
            property = OpenDelegateManager.getProcessDelegate()
                    .getOpenConfigProperty(name);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        if (OpenEnv.isProdVerify() && UmsStringUtils.isBlank(property)) {
            property = getInstance().prodVerifyConfig.getProperty(name);
        }
        if (UmsStringUtils.isBlank(property)) {
            property = getInstance().configs.get(
                    OpenEnv.getCurrentEnvironment()).getProperty(name);
        }
        return property;
    }

    static public boolean isDebug() {
        switch (OpenEnv.getCurrentEnvironment()) {
            case PROD:
                return false;
            default:
                return true;
        }
    }

    @Override
    public void destroy() {
        configs.clear();
        testConfig.clear();
        uatConfig.clear();
        prodConfig.clear();
        configs = null;
        testConfig = null;
        uatConfig = null;
        prodConfig = null;
    }

}
