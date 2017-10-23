package com.chinaums.opensdk.net.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    /**
     * status
     */
    public int status = 0;

    /**
     * 报文内容
     */
    public byte[] payload;

    /**
     * headers
     */
    public Map<String, String> headers = new HashMap<String, String>();

    public boolean hasError() {
        return status != 200;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

}
