package com.chinaums.opensdk.load.process.listener;

import java.util.Map;

public interface ProcessListener {

    /**
     * 处理完成
     */
    public void onSuccess(Map<String, Object> resp);

    /**
     * 处理出错
     */
    public void onError(String errCode, String errInfo);

}
