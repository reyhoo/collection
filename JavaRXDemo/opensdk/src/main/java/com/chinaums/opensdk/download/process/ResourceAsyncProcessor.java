package com.chinaums.opensdk.download.process;

import android.content.Context;

import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.download.listener.ResourceManagerMultiListener;
import com.chinaums.opensdk.download.model.AbsPack;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.model.Resource;
import com.chinaums.opensdk.exception.ResourceNotFoundException;
import com.chinaums.opensdk.exception.StorageSpaceLowException;
import com.chinaums.opensdk.exception.TimeoutException;
import com.chinaums.opensdk.manager.OpenExecutorManager;

public class ResourceAsyncProcessor {

    /**
     * instance
     */
    private static ResourceAsyncProcessor instance = new ResourceAsyncProcessor();

    /**
     * 得到处理类
     */
    public static ResourceAsyncProcessor getInstance() {
        return instance;
    }

    private ResourceAsyncProcessor() {

    }

    public void refresh(final AbsPack resource,
                        final ResourceManagerMultiListener listener, final boolean useBackup)
            throws Exception {
        OpenExecutorManager.getInstance().execute(new Runnable() {
            public void run() {
                try {
                    resource.refresh(listener, useBackup);
                } catch (StorageSpaceLowException e) {
                    listener.sendUnableProcessError("存储已满", e);
                } catch (ResourceNotFoundException e) {
                    listener.sendUnableProcessError("资源未找到", e);
                } catch (TimeoutException e) {
                    listener.sendUnableProcessError("处理超时", e);
                } catch (Exception e) {
                    listener.sendUnableProcessError("更新资源失败了", e);
                }
            }
        });
    }

    public void refresh(final BasePack basePack,
                        final ResourceManagerMultiListener listener,
                        final boolean useBackup, final boolean needStdIcon,
                        final boolean needLargeIcon, final boolean needAds,
                        final boolean needPublic, final boolean canRefresh)
            throws Exception {
        OpenExecutorManager.getInstance().execute(new Runnable() {
            public void run() {
                try {
                    basePack.refresh(listener, useBackup, needStdIcon,
                            needLargeIcon, needAds, needPublic, canRefresh);
                } catch (StorageSpaceLowException e) {
                    listener.sendUnableProcessError("存储已满", e);
                } catch (ResourceNotFoundException e) {
                    listener.sendUnableProcessError("资源未找到", e);
                } catch (TimeoutException e) {
                    listener.sendUnableProcessError("处理超时", e);
                } catch (Exception e) {
                    listener.sendUnableProcessError("更新资源失败了", e);
                }
            }
        });
    }

    public void process(final AbsPack resource,
                        final ResourceManagerMultiListener listener) throws Exception {
        OpenExecutorManager.getInstance().execute(new Runnable() {
            public void run() {
                try {
                    resource.prepare(listener, false);
                } catch (StorageSpaceLowException e) {
                    listener.sendUnableProcessError("存储已满", e);
                } catch (ResourceNotFoundException e) {
                    listener.sendUnableProcessError("资源未找到", e);
                } catch (TimeoutException e) {
                    listener.sendUnableProcessError("处理超时", e);
                } catch (Exception e) {
                    listener.sendUnableProcessError("更新资源失败了", e);
                }
            }
        });
    }

    public void process(final AbsPack resource,
                        final ResourceManagerListener listener) throws Exception {
        OpenExecutorManager.getInstance().execute(new Runnable() {
            public void run() {
                try {
                    resource.prepare(listener, false);
                } catch (StorageSpaceLowException e) {
                    listener.onError(resource, false, "存储已满", e);
                } catch (ResourceNotFoundException e) {
                    listener.onError(resource, false, "资源未找到", e);
                } catch (TimeoutException e) {
                    listener.onError(resource, false, "处理超时", e);
                } catch (Exception e) {
                    listener.onError(resource, false, "更新资源失败", e);
                }
            }
        });
    }

    public void process(final Resource resource,
                        final ResourceManagerListener listener) throws Exception {
        OpenExecutorManager.getInstance().execute(new Runnable() {
            public void run() {
                try {
                    resource.prepare(listener, false);
                } catch (StorageSpaceLowException e) {
                    listener.onError(resource, false, "存储已满", e);
                } catch (ResourceNotFoundException e) {
                    listener.onError(resource, false, "资源未找到", e);
                } catch (TimeoutException e) {
                    listener.onError(resource, false, "处理超时", e);
                } catch (Exception e) {
                    listener.onError(resource, false, "更新资源失败", e);
                }
            }
        });
    }

    public void process(final Resource resource,
                        final ResourceManagerMultiListener listener) throws Exception {
        OpenExecutorManager.getInstance().execute(new Runnable() {
            public void run() {
                try {
                    resource.prepare(listener, false);
                } catch (StorageSpaceLowException e) {
                    listener.sendUnableProcessError("存储已满", e);
                } catch (ResourceNotFoundException e) {
                    listener.sendUnableProcessError("资源未找到", e);
                } catch (TimeoutException e) {
                    listener.sendUnableProcessError("处理超时", e);
                } catch (Exception e) {
                    listener.sendUnableProcessError("更新资源失败了", e);
                }
            }
        });
    }

    public void processListWithHistory(final AbsPack resource,
                                       final Context context, final ResourceManagerListener listener)
            throws Exception {
        OpenExecutorManager.getInstance().execute(new Runnable() {
            public void run() {
                try {
                    resource.prepareByHistory(context, listener);
                } catch (StorageSpaceLowException e) {
                    listener.onError(resource, false, "存储已满", e);
                } catch (ResourceNotFoundException e) {
                    listener.onError(resource, false, "资源未找到", e);
                } catch (TimeoutException e) {
                    listener.onError(resource, false, "处理超时", e);
                } catch (Exception e) {
                    listener.onError(resource, false, "从预装更新资源失败", e);
                }
            }
        });
    }

}
