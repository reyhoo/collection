package com.chinaums.opensdk.download.model;

import android.content.Context;
import android.util.Log;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.cons.OpenConst.DynamicBizHistory;
import com.chinaums.opensdk.download.process.ResourceManager;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.download.service.ResourceMonitorService;
import com.chinaums.opensdk.exception.ResourceCheckOriginalException;
import com.chinaums.opensdk.exception.ResourceCreateFileException;
import com.chinaums.opensdk.exception.ResourceDownloadException;
import com.chinaums.opensdk.exception.ResourceInitDataException;
import com.chinaums.opensdk.exception.ResourceInitProcessFileException;
import com.chinaums.opensdk.exception.ResourceNotFoundException;
import com.chinaums.opensdk.exception.StorageSpaceLowException;
import com.chinaums.opensdk.exception.UnableProcessException;
import com.chinaums.opensdk.manager.OpenDynamicFileManager;
import com.chinaums.opensdk.net.IProgressUpdate;
import com.chinaums.opensdk.net.Timeout;
import com.chinaums.opensdk.net.UmsConnection;
import com.chinaums.opensdk.net.http.HttpRequest;
import com.chinaums.opensdk.net.http.HttpRequest.ContentType;
import com.chinaums.opensdk.net.http.HttpRequest.RequestMethod;
import com.chinaums.opensdk.net.http.HttpResponse;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UnzipUtils;

import java.io.File;

@SuppressWarnings("serial")
public abstract class AbsPack implements Resource, Cloneable {

    /**
     * 当前资源包的所在工作空间
     */
    private DynamicResourceWorkspace resourceWorkspace;

    /**
     * 资源签名：这个字段不应该放在基类里面，不具备普适性。有些资源包是基于配置的，有些资源包是基于文件的。
     */
    private String resSign;

    /**
     * 资源下载路径：这个字段不应该放在基类里面，不具备普适性。有些资源包是基于配置的，有些资源包是基于文件的。
     */
    private String resUrl;

    protected void log(int priority, String msg, Throwable throwable) {
        String printLog = getPrintLog(msg);
        switch (priority) {
            case Log.VERBOSE:
                UmsLog.v(printLog);
                break;
            case Log.INFO:
                UmsLog.i(printLog);
                break;
            case Log.WARN:
                UmsLog.w(printLog);
                break;
            case Log.ERROR:
                UmsLog.e(printLog, throwable);
                break;
            case Log.DEBUG:
            default:
                UmsLog.d(printLog);
        }
    }

    protected void log(int priority, String msg) {
        log(priority, msg, null);
    }

    protected abstract String getPrintLog(String msg);

    /**
     * eg."/download/list/area"
     */
    protected abstract String getResOriginalPathSuffix();

    /**
     * eg."/process/list/area"
     */
    protected abstract String getResProcessPathSuffix();

    /**
     * eg."area.zip"
     */
    protected abstract String getResOriginalFileName();

    /**
     * sign验证
     */
    protected abstract void verify(byte[] data) throws Exception;

    /**
     * 下载sign文件：这个函数只对AbsListPack系的压缩包起到下载sign文件的作用。对其他pack，实现为空。
     */
    protected abstract void initVerify(final ResourceManagerListener listener)
            throws Exception;

    /**
     * 文件信息内存对象化：比如areaListPack
     */
    protected abstract boolean initPack() throws Exception;

    /**
     * 磁盘里面的download目录下的zip文件解压到process目录下
     */
    protected abstract boolean initResProcessByOriginal() throws Exception;

    /**
     * 获取预安装sign值
     */
    protected abstract String getPreloadResSign() throws Exception;

    /**
     * 获取预安装压缩包目录
     */
    protected abstract String getPreloadResPath() throws Exception;

    /**
     * 获取最后一次sign签名
     */
    protected abstract String getHistoryResSign() throws Exception;

    /**
     * 改变UI图标显示状态
     */
    protected abstract void changedShowState(boolean isPrepareSuccess,
                                             boolean isPreload) throws Exception;

    /**
     * 下载压缩包文件res
     */
    protected boolean download(final ResourceManagerListener listener)
            throws Exception {
        HttpResponse response = UmsConnection.doHttpRequest(null,
                getRequestParam(), Timeout.SLOW, new IProgressUpdate() {
                    @Override
                    public void onProgressUpdate(int progressValue) {
                        if (progressValue <= 0)
                            return;
                        // progressValue是百分制的进度，需要转换成六十的进度
                        int progress = progressValue / 100 * 60 + 5;
                        onProgress("下载中.", progress, listener);
                    }
                });
        if (response.hasError())
            throw new ResourceDownloadException("下载出错");
        byte[] data = response.payload;
        if (data == null)
            throw new ResourceDownloadException("下载数据为空");
        boolean createFile = UmsFileUtils.createFile(getResOriginalPath(),
                getResOriginalFileName(), data);
        if (!createFile)
            throw new ResourceCreateFileException("创建本地文件出错");
        return createFile;
    }

    protected boolean unzipResOriginal2Process() throws Exception {
        UmsFileUtils.removeAllFile(getResProcessPath());
        return UnzipUtils.Unzip(getResOriginalPath() + File.separator
                + getResOriginalFileName(), getResProcessPath());
    }

    protected boolean copyResOriginal2Process() throws Exception {
        UmsFileUtils.removeFile(getResProcessPath(), getResOriginalFileName());
        return UmsFileUtils.copyFile(getResOriginalPath(),
                getResOriginalFileName(), getResProcessPath(),
                getResOriginalFileName());
    }

    protected void startResourceMonitorWatch() {
        ResourceMonitorService monitorService = ResourceManager.getInstance()
                .getResourceMonitorService();
        if (monitorService == null)
            return;
        UmsLog.d("开启文件监控.");
        monitorService.startFileMonitor(getResOriginalPath());
        monitorService.startFileMonitor(getResProcessPath());
    }

    protected void stopResourceMonitorWatch() {
        stopOriginalResourceMonitorWatch();
        stopProcessResourceMonitorWatch();
    }

    protected void stopOriginalResourceMonitorWatch() {
        ResourceMonitorService monitorService = ResourceManager.getInstance()
                .getResourceMonitorService();
        if (monitorService == null)
            return;
        UmsLog.d("移除原始文件监控.");
        monitorService.removeFileMonitor(getResOriginalPath());
    }

    protected void stopProcessResourceMonitorWatch() {
        ResourceMonitorService monitorService = ResourceManager.getInstance()
                .getResourceMonitorService();
        if (monitorService == null)
            return;
        UmsLog.d("移除处理文件监控.");
        monitorService.removeFileMonitor(getResProcessPath());
    }

    protected void hasEnoughSpace() throws Exception {
        if (!UmsFileUtils.hasEnoughSpace(getResPathPrefix())) {
            throw new StorageSpaceLowException("存储空间太少.");
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void refresh(final ResourceManagerListener listener,
                        boolean useBackup) throws Exception {
        prepare(listener, useBackup);
    }

    @Override
    public synchronized void prepare(final ResourceManagerListener listener,
                                     boolean useBackup) throws Exception {
        boolean flag = false;
        try {
            log(Log.DEBUG, "开始处理.");
            onProgress("开始处理", 0, listener);
            hasEnoughSpace();
            log(Log.DEBUG, "完成存储空间校验,开始校验信息初始化.");
            onProgress("完成存储空间校验,开始校验信息初始化.", 1, listener);
            initVerify(listener);
            log(Log.DEBUG, "完成校验信息初始化.进行原始文件校验.");
            onProgress("完成校验信息初始化,开始校验原始文件.", 3, listener);
            flag = checkOriginalFile();
            if (flag) {
                onProgress("完成原始文件校验.", 70, listener);
                prepareOk(listener);
                return;
            }
            if (useBackup) {
                log(Log.DEBUG, "原始文件校验失败,从备份空间复制文件.");
                onProgress("原始文件校验失败,从备份空间复制文件.", 5, listener);
                boolean copyFileFlag = UmsFileUtils.copyFile(
                        getBackupResOriginalPath(), getResOriginalFileName(),
                        getResOriginalPath(), getResOriginalFileName());
                log(Log.DEBUG, "完成从备份空间复制文件,状态为" + copyFileFlag);
                if (copyFileFlag) {
                    log(Log.DEBUG, "完成从备份空间复制文件,进行文件校验.");
                    flag = checkOriginalFile();
                    log(Log.DEBUG, "完成备份空间文件校验,状态为" + flag);
                    if (flag) {
                        onProgress("完成原始文件校验.", 70, listener);
                        prepareOk(listener);
                        return;
                    }
                }
            }
            onProgress("校验原始文件出错,开始进行下载.", 5, listener);
            log(Log.DEBUG, "校验失败，进行下载操作.");
            download(listener);
            log(Log.DEBUG, "完成下载操作.");
            onProgress("完成文件下载,开始进行校验.", 65, listener);
            flag = checkOriginalFile();
            log(Log.DEBUG, "完成文件校验,校验状态为" + flag);
            onProgress("完成文件校验成功.", 70, listener);
            if (flag) {
                prepareOk(listener);
            } else {
                log(Log.DEBUG, "文件校验失败,开始清除所有数据.");
                removeResOriginlaAndProcessFile();
                log(Log.DEBUG, "校验失败后清除所有数据完成.");
                log(Log.DEBUG, "还是校验失败.");
                throw new UnableProcessException("文件下载完成后再次校验失败");
            }
        } catch (Exception e) {
            onError("初始化失败", e, listener);
        }
    }

    @Override
    public synchronized void prepareByHistory(Context context,
                                              ResourceManagerListener listener) throws Exception {
        try {
            log(Log.DEBUG, "根据历史进行处理.设置下载地址和签名.");
            setResSign(getHistoryResSign());
            log(Log.DEBUG, "完成设置下载地址和签名.开始停止监控.");
            stopResourceMonitorWatch();
            log(Log.DEBUG, "完成停止监控处理.判断空间是否足够.");
            hasEnoughSpace();
            log(Log.DEBUG, "存储空间足够.开始校验原始文件.");
            if (!checkOriginalFile()) {
                throw new ResourceCheckOriginalException("原始文件校验失败.");
            }
            log(Log.DEBUG, "原始文件校验成功.");
            if (!initResProcessByOriginal()) {
                throw new ResourceCheckOriginalException("解压缩失败.");
            }
            log(Log.DEBUG, "解压缩成功.开始初始化数据.");
            if (!initPack()) {
                log(Log.DEBUG, "数据初始化失败。");
                throw new ResourceInitDataException("数据初始化失败");
            }
            log(Log.DEBUG, "初始化数据成功.开始开启监控.");
            startResourceMonitorWatch();
            log(Log.DEBUG, "开启监控成功.");
            changedShowState(true, true);
            log(Log.DEBUG, "设置状态成功.");
            listener.onUpdated(this);
        } catch (Exception e) {
            // 注意：这里的流程特别，在当前的工作区中如果sign校验失败或者其他原因失败，没有从备份的工作区中恢复的逻辑
            // 直接从预安装的流程中恢复。因为资源包等文件有备份的工作区，而sign在历史中只有一份。sign不可能对应两个工作区。
            log(Log.ERROR, "使用历史情况出错.重新使用预装初始化", e);
            prepareByPreload(context, listener);
        }
    }

    @Override
    public synchronized void prepareByPreload(Context context,
                                              ResourceManagerListener listener) throws Exception {
        try {
            log(Log.DEBUG, "根据预装进行处理.设置下载地址和签名.");
            setResSign(getPreloadResSign());
            log(Log.DEBUG, "完成设置下载地址和签名.开始停止监控.");
            stopResourceMonitorWatch();
            log(Log.DEBUG, "完成停止监控处理.判断空间是否足够.");
            if (!UmsFileUtils.hasEnoughSpace(getResPathPrefix()))
                throw new StorageSpaceLowException("存储空间太少.");
            log(Log.DEBUG, "存储空间足够.开始创建原始文件.");
            boolean copyFileFlag = UmsFileUtils.copyFile(
                    context.getAssets().open(
                            DynamicBizHistory.PRELOAD_PROFIX
                                    + getPreloadResPath() + File.separator
                                    + getResOriginalFileName()),
                    getResOriginalPath(), getResOriginalFileName());
            log(Log.DEBUG, "复制文件完成，标识为" + copyFileFlag);
            if (copyFileFlag == false)
                throw new ResourceNotFoundException("无文件或文件不可用.");
            log(Log.DEBUG, "创建原始文件成功.开始原始文件校验.");
            if (!checkOriginalFile()) {
                throw new ResourceCheckOriginalException("原始文件校验失败.");
            }
            log(Log.DEBUG, "原始文件校验成功.");
            if (!initResProcessByOriginal()) {
                throw new ResourceInitProcessFileException("原始文处理失败.");
            }
            log(Log.DEBUG, "原始文件处理成功.开始初始化数据.");
            if (!initPack()) {
                log(Log.DEBUG, "数据初始化失败.");
                throw new ResourceInitDataException("数据初始化失败");
            }
            log(Log.DEBUG, "初始化数据成功.开始开启监控.");
            startResourceMonitorWatch();
            log(Log.DEBUG, "开启监控成功.");
            changedShowState(true, true);
            log(Log.DEBUG, "设置状态成功.");
            listener.onUpdated(this);
        } catch (Exception e) {
            log(Log.ERROR, "根据历史情况预装出错", e);
            changedShowState(true, false);
            listener.onError(this, true, e.getMessage(), e);
        }
    }

    @Override
    public void onError(String msg, Exception e,
                        final ResourceManagerListener listener) throws Exception {
        log(Log.ERROR, msg, e);
        stopResourceMonitorWatch();
        UmsFileUtils.removeAllFile(getResOriginalPath());
        UmsFileUtils.removeAllFile(getResProcessPath());
        onProgress("处理异常", 100, listener);
        listener.onError(this, false, msg, e);
    }

    @Override
    public void onFinish(ResourceManagerListener listener) throws Exception {
        startResourceMonitorWatch();
        onProgress("处理结束.", 100, listener);
        listener.onUpdated(this);
    }

    /**
     * 完成从download目录到process目录，然后内存对象化。这个函数只有在prepare中调用到，在prepareByHistory和
     * prepareByPreload中调用不到。
     */
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
        log(Log.DEBUG, "校验通过进行工作文件夹初始化.");
        if (!initResProcessByOriginal())
            throw new ResourceInitProcessFileException("工作文件夹初始化失败");
        onProgress("完成工作文件夹初始化,开始初始化数据.", 80, listener);
        log(Log.DEBUG, "工作文件夹初始化完成进行数据初始化.");
        if (!initPack())
            throw new UnableProcessException("数据初始化失败");
        onProgress("完成数据初始化,开启文件监控.", 95, listener);
        log(Log.DEBUG, "初始化完成进行通知.");
        onFinish(listener);
        log(Log.DEBUG, "完成.");
    }

    /**
     * dowmload目录下资源文件验证sign
     */
    protected boolean checkOriginalFile() throws Exception {
        try {
            log(Log.DEBUG, "开始文件校验.");
            byte[] data = UmsFileUtils.readFile(getResOriginalPath(),
                    getResOriginalFileName());
            if (data == null) {
                log(Log.DEBUG, "获取的原始文件为空.");
                return false;
            }
            log(Log.DEBUG, "进行原始文件验证签名.");
            verify(data);
            log(Log.DEBUG, "验证成功.");
            return true;
        } catch (Exception e) {
            log(Log.ERROR, "校验原始文件失败", e);
            stopResourceMonitorWatch();
            log(Log.DEBUG, "校验失败,开始清除所有文件.");
            removeResOriginlaAndProcessFile();
        }
        return false;
    }

    private void removeResOriginlaAndProcessFile() {
        UmsFileUtils.removeFile(getResOriginalPath(), getResOriginalFileName());
        UmsFileUtils.removeAllFile(getResProcessPath());
        log(Log.DEBUG, "完成清除所有文件.");
    }

    protected boolean checkIsMonitoring() throws Exception {
        log(Log.DEBUG, "判断是否被监听.");
        try {
            if (!checkResOriginalIsMonitoring())
                return false;
            if (!checkResProcessIsMonitoring())
                return false;
            return true;
        } catch (Exception e) {
            log(Log.ERROR, "检查监控失败", e);
        }
        return false;
    }

    protected boolean checkResOriginalIsMonitoring() throws Exception {
        try {
            log(Log.DEBUG, "判断是否原始文件被监听.");
            ResourceMonitorService monitorService = ResourceManager
                    .getInstance().getResourceMonitorService();
            if (monitorService == null) {
                log(Log.DEBUG, "获取不到监控服务.");
                return false;
            } else if (!monitorService
                    .checkHasFileMonitorAndStartWatching(getResOriginalPath())) {
                log(Log.DEBUG, "不包含原始文件的监控.");
                return false;
            }
            log(Log.DEBUG, "包含了原始文件监控，但监控状态未知，现已启动监控。");
            return true;
        } catch (Exception e) {
            log(Log.ERROR, "判断原始文件是否有监听失败", e);
        }
        return false;
    }

    protected boolean checkResProcessIsMonitoring() throws Exception {
        try {
            log(Log.DEBUG, "判断是否处理文件被监听.");
            ResourceMonitorService monitorService = ResourceManager
                    .getInstance().getResourceMonitorService();
            if (!monitorService
                    .checkHasFileMonitorAndStartWatching(getResProcessPath())) {
                log(Log.DEBUG, "不包含处理文件的监控.");
                return false;
            }
            log(Log.DEBUG, "包含了处理文件监控，但监控状态未知，现已启动监控。");
            return true;
        } catch (Exception e) {
            log(Log.ERROR, "检查运行文件的监听失败", e);
        }
        return false;
    }

    public HttpRequest getRequestParam() {
        return new HttpRequest(getResUrl(), ContentType.Html_UTF8,
                RequestMethod.Get);
    }

    public String getResSign() {
        return resSign;
    }

    public void setResSign(String resSign) {
        this.resSign = resSign;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public void setResourceWorkspace(DynamicResourceWorkspace resourceWorkspace) {
        this.resourceWorkspace = resourceWorkspace;
    }

    protected String getBackupResOriginalPath() {
        //eg."/data/data/com.chinaums.xmsmk/bizspaceb" + getResOriginalPathSuffix();
        return getBackupResPathPrefix() + getResOriginalPathSuffix();
    }

    protected String getResOriginalPath() {
        //eg."/data/data/com.chinaums.xmsmk/bizspacea" + getResOriginalPathSuffix()
        return getResPathPrefix() + getResOriginalPathSuffix();
    }

    public String getResProcessPath() {
        //eg."/data/data/com.chinaums.xmsmk/bizspacea" + getResProcessPathSuffix()
        return getResPathPrefix() + getResProcessPathSuffix();
    }

    protected DynamicResourceWorkspace getResourceWorkspace() {
        if (resourceWorkspace == null)
            return ResourceManager.getInstance().getResourceWorkspace()
                    .getResourceWorkspace();
        else
            return resourceWorkspace;
    }

    protected DynamicResourceWorkspace getBackupResourceWorkspace() {
        DynamicResourceWorkspace temp = ResourceManager.getInstance()
                .getResourceBackspace().getResourceWorkspace();
        if (resourceWorkspace == null) {
            return temp;
        } else if (resourceWorkspace.equals(temp)) {
            return ResourceManager.getInstance().getResourceWorkspace()
                    .getResourceWorkspace();
        } else {
            return temp;
        }
    }

    private String getResPathPrefix() {
        //eg."/data/data/com.chinaums.xmsmk/bizspacea"
        return OpenDynamicFileManager.getDataFilePathPrefix() + File.separator
                + getResourceWorkspace().getValue();
    }

    private String getBackupResPathPrefix() {
        //eg."/data/data/com.chinaums.xmsmk/bizspaceb"
        return OpenDynamicFileManager.getDataFilePathPrefix() + File.separator
                + getBackupResourceWorkspace().getValue();
    }

}
