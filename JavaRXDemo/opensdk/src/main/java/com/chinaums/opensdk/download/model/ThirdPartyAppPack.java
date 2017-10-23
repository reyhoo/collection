package com.chinaums.opensdk.download.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicBizShowState;
import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.exception.UnableProcessException;
import com.chinaums.opensdk.manager.OpenDynamicFileManager;
import com.chinaums.opensdk.net.http.HttpRequest;
import com.chinaums.opensdk.net.http.HttpRequest.ContentType;
import com.chinaums.opensdk.net.http.HttpRequest.RequestMethod;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.File;


public class ThirdPartyAppPack extends AppPack {

    /**
     * activityPackage
     */
    private String activityPackage;

    /**
     * activityName
     */
    private String activityName;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 版本号
     */
    private int activityVersion;

    /**
     * 当前版本号
     */
    private int activityCurVersion;

    /**
     * 是否需要安装
     */
    private boolean isNeedInstall = true;

    @Override
    protected boolean initCustom() throws Exception {
        JSONObject paramsObj = JsonUtils.convert2Json(getParams());
        activityPackage = paramsObj.getString("androidPackage");
        activityName = paramsObj.getString("androidEntry");
        downloadUrl = paramsObj.getString("androidUrl");
        activityVersion = paramsObj.getIntValue("androidAppVer");
        if (UmsStringUtils.isBlank(activityPackage)
                || UmsStringUtils.isBlank(activityName)
                || UmsStringUtils.isBlank(downloadUrl) || activityVersion <= 0)
            throw new Exception(getName() + "的配置参数错误,没有获得主要参数.");
        setResUrl(downloadUrl);
        return true;
    }

    @Override
    protected String getOpenUrlBySelfParam(String param) {
        try {
            JSONObject json = new JSONObject();
            json.put("activityPackage", activityPackage);
            json.put("activityName", activityName);
            json.putAll(JSON.parseObject(getParams()));
            return "ums://page/dynamicApp?param="
                    + Base64Utils.encrypt(json.toString());
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    @Override
    public void prepare(final ResourceManagerListener listener,
                        boolean useBackup) throws Exception {
        boolean flag = false;
        isNeedInstall = true;
        try {
            log(Log.DEBUG, "进行预处理.");
            onProgress("开始处理", 0, listener);
            if (!initPack())
                throw new UnableProcessException("数据初始化失败");
            hasEnoughSpace();
            flag = checkAppVersion();
            if (flag) {
                isNeedInstall = false;
                onProgress("完成版本校验.", 70, listener);
                prepareOk(listener);
                return;
            }
            isNeedInstall = true;
            onProgress("当前不是最新版本,开始查看历史下载记录.", 5, listener);
            File historyFile = new File(getResOriginalPath(),
                    getResOriginalFileName());
            flag = historyFile.exists();
            if (flag) {
                onProgress("发现历史下载记录.", 70, listener);
                prepareOk(listener);
                return;
            }
            log(Log.DEBUG, "进行下载操作.");
            stopResourceMonitorWatch();
            download(listener);
            log(Log.DEBUG, "完成下载操作.");
            onProgress("完成下载.", 70, listener);
            prepareOk(listener);
        } catch (Exception e) {
            onError("初始化失败", e, listener);
        }
    }

    @Override
    protected void changedShowState(boolean isPrepareSuccess, boolean isPreload)
            throws Exception {
        DynamicBizShowState bizShowState = DynamicBizShowState
                .getByState(getStatus());
        if (!isNeedInstall) {
            setStatus(bizShowState.toString());
        } else {
            setStatus(DynamicBizShowState.UPDATABLE.toString());
        }

    }

    /**
     * 动态文件名，根据androidAppVer参数变化，使用预装复制文件后才InitPack，因此此处直接解析参数获取androidAppVer。
     */
    @Override
    protected String getResOriginalFileName() {
        int versionCode = 0;
        try {
            JSONObject paramsObj = JsonUtils.convert2Json(getParams());
            versionCode = paramsObj.getIntValue("androidAppVer");
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return getCode() + "_" + versionCode
                + DynamicDownloadConf.BIZ_RES_APK_EXTENSION;
    }

    @Override
    protected String getResOriginalPathSuffix() {
        return DynamicDownloadConf.BIZ_ORIGINAL_RES_FILE_FOLDER
                + File.separator + getCode();
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        return DynamicDownloadConf.BIZ_RES_FILE_FOLDER + File.separator
                + getCode();
    }

    private void prepareOk(final ResourceManagerListener listener)
            throws Exception {
        onFinish(listener);
        log(Log.DEBUG, "完成.");
    }

    private boolean checkAppVersion() throws Exception {
        int curVersion = OpenDynamicFileManager
                .getAppVersionCode(getActivityPackage());
        if (curVersion == -2)
            throw new Exception(getName() + "初始化packagemanage出错.");
        else if (curVersion == -1)
            log(Log.DEBUG, getName() + "获取版本号出错");
        if (curVersion >= activityVersion)
            return true;
        return false;
    }

    public void installApk(Context context) throws Exception {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getResOriginalPath() + File.separator
                        + getResOriginalFileName()),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public final String getDownloadUrl() {
        return downloadUrl;
    }

    public final int getActivityVersion() {
        return activityVersion;
    }

    public final int getActivityCurVersion() {
        return activityCurVersion;
    }

    public final boolean isNeedInstall() {
        return isNeedInstall;
    }

    public final String getActivityPackage() {
        return activityPackage;
    }

    public final String getActivityName() {
        return activityName;
    }

    protected String getResOriginalPath() {
        return getResPathPrefix() + getResOriginalPathSuffix();
    }

    private String getResPathPrefix() {
        return OpenDynamicFileManager.getSdCardPath();
    }

    public HttpRequest getRequestParam() {
        return new HttpRequest(getDownloadUrl(), ContentType.Html_UTF8,
                RequestMethod.Get);
    }

}
