package com.chinaums.opensdk.net.http;

import com.chinaums.opensdk.net.IProgressUpdate;

/**
 * HTTP通讯接口
 */
public interface IHttpTransport {

    /**
     * 进行Http请求
     */
    public HttpResponse doHttpRequest(HttpRequest request, int timeout,
                                      IProgressUpdate progress) throws Exception;
    
}
