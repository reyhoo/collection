package com.chinaums.opensdk.weex;

import android.content.Context;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ExecutorManager {

    /**
     * 线程池大小
     */
    private static final int MAX_THREADS = 20;

    /**
     * instance
     */
    private static ExecutorManager instance;

    /**
     * 线程容器
     */
    private ExecutorService executor;

    private ExecutorManager() {

    }

    synchronized public static ExecutorManager getInstance() {
        if (instance == null) {
            instance = new ExecutorManager();
        }
        return instance;
    }

    public void init(Context context) {
        executor = Executors.newFixedThreadPool(MAX_THREADS);
    }

    public void destroy() {
        executor.shutdownNow();
    }

    public void execute(Runnable r) {
        executor.execute(r);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

}
