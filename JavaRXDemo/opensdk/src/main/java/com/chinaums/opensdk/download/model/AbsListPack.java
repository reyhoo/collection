package com.chinaums.opensdk.download.model;

import android.util.Log;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.exception.ResourceCheckOriginalException;
import com.chinaums.opensdk.exception.ResourceDownloadException;
import com.chinaums.opensdk.manager.OpenDynamicSecurityManager;
import com.chinaums.opensdk.net.IProgressUpdate;
import com.chinaums.opensdk.net.Timeout;
import com.chinaums.opensdk.net.UmsConnection;
import com.chinaums.opensdk.net.http.HttpRequest;
import com.chinaums.opensdk.net.http.HttpRequest.ContentType;
import com.chinaums.opensdk.net.http.HttpRequest.RequestMethod;
import com.chinaums.opensdk.net.http.HttpResponse;
import com.chinaums.opensdk.util.UmsStringUtils;


@SuppressWarnings("serial")
public abstract class AbsListPack extends AbsPack {

    protected abstract String getSignUrl();

    protected abstract String getResProcessFileName();

    protected abstract void updateSignHistory(String sign) throws Exception;

    protected abstract String getPreloadResSignForUpdateSign() throws Exception;

    public AbsListPack(DynamicResourceWorkspace resourceWorkspace) {
        setResourceWorkspace(resourceWorkspace);
    }

    @Override
    protected boolean initResProcessByOriginal() throws Exception {
        return unzipResOriginal2Process();
    }

    @Override
    public boolean check() throws Exception {
        log(Log.DEBUG, "开始校验.");
        if (!checkIsMonitoring()) {
            log(Log.DEBUG, "监控校验失败.");
            return false;
        }
        log(Log.DEBUG, "校验成功.");
        return true;
    }

    @Override
    public void onProgress(String msg, int progress,
                           final ResourceManagerListener listener) {
        listener.onProgress(this, msg, progress);
    }

    @Override
    protected void initVerify(final ResourceManagerListener listener)
            throws Exception {
        // 下载最新的sign
        String sign = downloadSign(listener);
        setResSign(sign);
        // 下面这一步我个人觉得有点激进，如果sign下载成功，但是其他文件下载失败，但是已经更新历史了，导致
        // 从备份中无法恢复的问题
        updateSignHistory(sign);
    }

    @Override
    protected void verify(byte[] data) throws Exception {
        // sign验证
        boolean isSuccess = OpenDynamicSecurityManager.getInstance().verify(
                data, getResSign());
        if (!isSuccess) {
            throw new ResourceCheckOriginalException("原始文件验证失败.");
        }
    }

    @Override
    protected void changedShowState(boolean isPrepareSuccess, boolean isPreload)
            throws Exception {
        return;
    }

    @Override
    protected String getPreloadResSign() throws Exception {
        String sign = getPreloadResSignForUpdateSign();
        if (UmsStringUtils.isNotBlank(sign)) {
            updateSignHistory(sign);
        }
        return sign;
    }

    protected String downloadSign(final ResourceManagerListener listener)
            throws Exception {
        HttpResponse response = UmsConnection.doHttpRequest(null,
                getSignRequestParam(), Timeout.NORMAL, new IProgressUpdate() {
                    @Override
                    public void onProgressUpdate(int progressValue) {
                        if (progressValue <= 0)
                            return;
                        int progress = progressValue / 100 * 3;
                        onProgress("下载签名中.", progress, listener);
                    }
                });
        if (response.hasError())
            throw new ResourceDownloadException("下载签名信息出错");
        byte[] data = response.payload;
        if (data == null)
            throw new ResourceDownloadException("下载签名信息为空");
        return new String(data, "UTF-8").toString();
    }

    private HttpRequest getSignRequestParam() throws Exception {
        return new HttpRequest(getSignUrl(), ContentType.Text,
                RequestMethod.Get);
    }

}
