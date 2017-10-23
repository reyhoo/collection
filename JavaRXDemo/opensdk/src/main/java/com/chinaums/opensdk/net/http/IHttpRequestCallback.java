package com.chinaums.opensdk.net.http;

import android.content.Context;

public interface IHttpRequestCallback {

    /**
     * 收到数据就算成功
     */
    public void onSuccess(Context context, HttpResponse resp);

    public void onException(Context context, Exception e);
    
}
