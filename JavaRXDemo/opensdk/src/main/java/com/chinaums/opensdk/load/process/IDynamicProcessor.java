package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.Intent;

import com.chinaums.opensdk.load.model.data.DynamicWebModel;


public interface IDynamicProcessor {

    enum DynamicRequestType {
        SYNCHRONIZED, ASYNCHRONIZED
    }

    /**
     * 进行处理
     */
    public void process(DynamicWebModel model) throws Exception;

    /**
     * 处理类回调
     */
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception;

    public void onDestory(Activity activity) throws Exception;

    /**
     * 处理类型
     */
    public int getType();

    /**
     * 请求类型，同步还是异步
     */
    public DynamicRequestType getProcessorType();

    /**
     * 判断是否是同步类型
     */
    public Boolean IsSynchronizedProcessor();

    /**
     * 获取level
     */
    public int getLevel();

    /**
     * 根据action获取level
     */
    public int getLevel(String action);
    
}
