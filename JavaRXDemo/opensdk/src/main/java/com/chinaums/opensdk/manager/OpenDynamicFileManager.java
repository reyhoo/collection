package com.chinaums.opensdk.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.chinaums.opensdk.cons.OpenEnv;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsLog;

public class OpenDynamicFileManager implements IOpenManager {

    private static final String NOMEDIA = "/.nomedia";

    /**
     * eg."/data/data/com.chinaums.xmsmk/.nomedia"
     */
    private String dataFilePathPrefix = null;

    /**
     * sdcardPath
     */
    private static String sdcardPath = null;

    /**
     * instance
     */
    private static OpenDynamicFileManager instance;

    /**
     * packageManager
     */
    private static PackageManager packageManager;

    synchronized public static OpenDynamicFileManager getInstance() {
        if (instance == null) {
            instance = new OpenDynamicFileManager();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        switch (OpenEnv.getCurrentEnvironment()) {
            case PROD:
                dataFilePathPrefix = context.getFilesDir().getAbsolutePath();
                sdcardPath = Environment.getExternalStorageDirectory().getPath();
                break;
            case UAT:
            case TEST:
            default:
                // 部分手机，比如P8没有外部存储空间
                dataFilePathPrefix = context.getFilesDir().getAbsolutePath();
                sdcardPath = android.os.Environment.getExternalStorageDirectory()
                        .getPath();
        }
        dataFilePathPrefix = dataFilePathPrefix + NOMEDIA;
        sdcardPath = sdcardPath + NOMEDIA;
        packageManager = context.getPackageManager();
    }

    @Override
    public void destroy() {
        dataFilePathPrefix = null;
        sdcardPath = null;
        instance = null;
    }

    public static int getAppVersionCode(String apkName) {
        int result;
        try {
            if (packageManager == null)
                return -2;
            PackageInfo info = packageManager.getPackageInfo(apkName, 0);
            result = info.versionCode;
        } catch (Exception e) {
            UmsLog.e("获取app version code出错", e);
            result = -1;
        }
        return result;
    }

    public static long getAvailableSpace() {
        return UmsFileUtils.getAvailableSpace(getDataFilePathPrefix());
    }

    public static boolean hasEnoughSpace() {
        return UmsFileUtils.hasEnoughSpace(getDataFilePathPrefix());
    }

    public static String getDataFilePathPrefix() {
        return getInstance().dataFilePathPrefix;
    }

    public static String getDataSubFilePathPrefix(String subFile) {
        return getDataFilePathPrefix() + subFile;
    }

    public static String getSdCardPath() {
        return sdcardPath;
    }

}
