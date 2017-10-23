package com.chinaums.opensdk.download.model;

import android.content.Context;

import com.chinaums.opensdk.download.listener.ResourceManagerListener;

import java.io.Serializable;


public interface Resource extends Serializable {

    /**
     * 准备进度
     */
    public void onProgress(String msg, int progress,
                           final ResourceManagerListener listener);

    /**
     * 处理完成回调
     */
    public void onFinish(final ResourceManagerListener listener)
            throws Exception;

    /**
     * 处理失败回调
     */
    public void onError(String msg, Exception e,
                        final ResourceManagerListener listener) throws Exception;

    /**
     * 对资源文件进行准备，useBackup表示另外一个工作空间。
     * 这个函数对于AbsListPack系的资源包的sign都是一定是从网络上下载的最新的sign，然后用这个sign，去分析。
     * 这个函数的目的是保证download目录下压缩
     * 文件的正确性。流程是：先检查download目录下压缩包sign验证，如果验证不过，取另外一个工作空间
     * 上的压缩文件到当前工作目录下来，再验证sign的合法性，如果再次验证不过，重新下载最新的压缩包
     * 文件，并验证通过。这里面没有从预装的资源包里面获取文件的逻辑。如果都出错，就抛异常，不会从历史
     * 的sign中恢复。
     */
    public void prepare(final ResourceManagerListener listener,
                        boolean useBackup) throws Exception;

    /**
     * 这个是试图从网络上获取最新的全资源列表后刷新页面。
     */
    public void refresh(final ResourceManagerListener listener,
                        boolean useBackup) throws Exception;

    /**
     * 对预安装文件进行预装处理：就是把预安装目录下的文件复制到当前工作空间下的download目录下的文件
     * 初始化sign验证、解压、信息内存化，如果出错就抛异常。
     */
    public void prepareByPreload(Context context,
                                 ResourceManagerListener listener) throws Exception;

    /**
     * 对历史文件进行预装处理：就是用当前工作空间下的download目录下的文件初始化sign验证、解压、
     * 信息内存化，如果出错就抛异常。
     */
    public void prepareByHistory(Context context,
                                 ResourceManagerListener listener) throws Exception;

    /**
     * 对资源文件进行校验：检查download目录下zip文件的监控情况和process目录下解压后文件的监控情况
     */
    public boolean check() throws Exception;

}
