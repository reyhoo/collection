package com.chinaums.opensdk.download.listener;

import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.model.Resource;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class ResourceManagerMultiListener implements
        ResourceManagerListener {

    /**
     * 需要运行的数量
     */
    private AtomicInteger count = new AtomicInteger(0);

    /**
     * 当前需要运行的数量
     */
    private AtomicInteger runCount = new AtomicInteger(0);

    /**
     * 成功个数
     */
    private AtomicInteger successCount = new AtomicInteger(0);

    /**
     * 失败个数
     */
    private AtomicInteger errorCount = new AtomicInteger(0);

    /**
     * 可忽略的失败个数
     */
    private AtomicInteger ignoreResouceErrorCount = new AtomicInteger(0);

    /**
     * 错误信息
     */
    private Map<String, ErrorResult> errorMap = new ConcurrentHashMap<String, ErrorResult>();

    /**
     * 错误信息
     */
    private Map<String, ErrorResult> ignoreErrorMap = new ConcurrentHashMap<String, ErrorResult>();

    /**
     * 是否有不可处理的异常
     */
    private AtomicBoolean isUnableProcessExcepiton = new AtomicBoolean(false);

    public void doInit(int count) {
        this.count.set(count);
        this.runCount.set(count);
        this.successCount.set(0);
        this.errorCount.set(0);
        this.ignoreResouceErrorCount.set(0);
        this.isUnableProcessExcepiton.set(false);
        this.errorMap.clear();
    }

    /**
     * 总进度处理
     */
    private void onTotalProgress() {
        if (isUnableProcessExcepiton.get())
            return;
        int progress = (int) (((successCount.get() + errorCount.get() + ignoreResouceErrorCount
                .get()) / (count.get() * 1.0)) * 100);
        onTotalProgress(progress);
    }

    /**
     * 总进度
     */
    public abstract void onTotalProgress(int progress);

    @Override
    public void onUpdated(Resource pack) {
        if (isUnableProcessExcepiton.get())
            return;
        successCount.incrementAndGet();
        int curIndex = runCount.decrementAndGet();
        onTotalProgress();
        if (curIndex <= 0)
            onFinish();
    }

    @Override
    public void onError(Resource pack, boolean isIgnoreResouce,
                        String errorInfo, Exception e) {
        if (isUnableProcessExcepiton.get()) {
            return;
        }
        if (isIgnoreResouce) {
            ignoreResouceErrorCount.incrementAndGet();
        } else {
            errorCount.incrementAndGet();
        }
        int curIndex = runCount.decrementAndGet();
        String code = "";
        ErrorResult errorResult = null;
        if (pack != null && pack instanceof BasePack) {
            code = ((BasePack) pack).getCode();
            errorResult = new ErrorResult(errorInfo, e);
        } else {
            code = pack.getClass().toString();
            errorResult = new ErrorResult(errorInfo, e);
        }
        if (isIgnoreResouce) {
            ignoreErrorMap.put(code, errorResult);
        } else {
            errorMap.put(code, errorResult);
        }
        onTotalProgress();
        if (curIndex <= 0)
            onFinish();
    }

    /**
     * 不能处理的总体异常
     */
    public void sendUnableProcessError(String errorInfo, Exception e) {
        isUnableProcessExcepiton.set(true);
        onUnableProcessError(errorInfo, e);
    }

    public abstract void onUnableProcessError(String errorInfo, Exception e);

    public abstract void onFinish();

    public AtomicInteger getRunCount() {
        return runCount;
    }
    
    public AtomicInteger getSuccessCount() {
        return successCount;
    }

    public AtomicInteger getErrorCount() {
        return errorCount;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public AtomicInteger getIgnoreResouceErrorCount() {
        return ignoreResouceErrorCount;
    }

    public Map<String, ErrorResult> getErrorMap() {
        return Collections.unmodifiableMap(errorMap);
    }

    public Map<String, ErrorResult> getIgnoreErrorMap() {
        return Collections.unmodifiableMap(ignoreErrorMap);
    }

    public static class ErrorResult {

        /**
         * errorInfo
         */
        public String errorInfo;

        /**
         * exception
         */
        public Exception exception;

        public ErrorResult(String errorInfo, Exception exception) {
            this.errorInfo = errorInfo;
            this.exception = exception;
        }

    }

}
