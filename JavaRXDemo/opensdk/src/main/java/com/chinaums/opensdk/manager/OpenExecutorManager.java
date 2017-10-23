package com.chinaums.opensdk.manager;

import android.content.Context;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池管理，为了避免到处new thread，引起系统线程过多，请大家使用本类来运行后台线程。
 */
public class OpenExecutorManager implements IOpenManager {

    private static final int MAX_THREADS = 5;

    /**
     * instance
     */
    private static OpenExecutorManager instance;

    /**
     * context
     */
    private Context context;

    /**
     * executor
     */
    private ExecutorService executor;

    synchronized public static OpenExecutorManager getInstance() {
        if (instance == null) {
            instance = new OpenExecutorManager();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        this.context = context;
        executor = Executors.newFixedThreadPool(MAX_THREADS);
    }

    @Override
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
