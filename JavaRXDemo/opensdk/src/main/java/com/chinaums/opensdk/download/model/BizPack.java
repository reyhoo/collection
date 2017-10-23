package com.chinaums.opensdk.download.model;

import android.util.Log;

import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.exception.ResourceInitProcessFileException;
import com.chinaums.opensdk.exception.UnableProcessException;


public abstract class BizPack extends BasePack {

    /**
     * 菜单
     */
    private String confMenu;

    @Override
    protected boolean initPack() throws Exception {
        return true;
    }

    @Override
    public void refresh(final ResourceManagerListener listener,
                        boolean useBackup) throws Exception {
        if (isMustUpdate())
            prepare(listener, useBackup);
        else
            refresh(listener);
    }

    @Override
    public void refresh(ResourceManagerListener listener, boolean useBackup,
                        boolean needStdIcon, boolean needLargeIcon, boolean needAds,
                        boolean needPublic, boolean canRefresh) throws Exception {
        if (canRefresh) {
            refresh(listener, useBackup);
        } else {
            listener.onUpdated(this);
        }
    }

    private void refresh(final ResourceManagerListener listener)
            throws Exception {
        boolean flag = false;
        try {
            log(Log.DEBUG, "进行预处理.");
            onProgress("开始处理", 0, listener);
            hasEnoughSpace();
            flag = checkOriginalFile();
            if (flag) {
                onProgress("完成原始文件校验.", 70, listener);
                prepareOk(listener);
                return;
            }
            onProgress("校验原始文件出错,退出啦.", 5, listener);
            changedShowState(false, false);
            listener.onUpdated(this);
        } catch (Exception e) {
            onError("初始化失败", e, listener);
        }
    }

    private void prepareOk(final ResourceManagerListener listener)
            throws Exception {
        log(Log.DEBUG, "判断文件监控是否正常.");
        if (checkIsMonitoring()) {
            onFinish(listener);
            log(Log.DEBUG, "完成.");
            return;
        }
        log(Log.DEBUG, "停止校验处理文件夹.");
        stopProcessResourceMonitorWatch();
        log(Log.DEBUG, "校验通过进行解压缩.");
        if (!initResProcessByOriginal())
            throw new ResourceInitProcessFileException("解压失败啦");
        onProgress("完成文件解压缩,开始初始化数据.", 80, listener);
        log(Log.DEBUG, "解压缩完成进行数据初始化.");
        if (!initPack())
            throw new UnableProcessException("数据初始化失败");
        onProgress("完成数据初始化,开启文件监控.", 95, listener);
        log(Log.DEBUG, "初始化完成进行通知.");
        onFinish(listener);
        log(Log.DEBUG, "完成.");
    }

    public String getConfMenu() {
        return confMenu;
    }

    public void setConfMenu(String confMenu) {
        this.confMenu = confMenu;
    }

}
