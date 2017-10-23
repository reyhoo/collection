package com.chinaums.opensdk.net.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    /**
     * url
     */
    public String url;

    /**
     * params
     */
    public Map<String, String> params = new HashMap<String, String>();

    /**
     * headers
     */
    public Map<String, String> headers = new HashMap<String, String>();

    /**
     * contentType
     */
    public ContentType contentType = ContentType.Form;

    /**
     * requestMethod
     */
    public RequestMethod requestMethod = RequestMethod.Get;

    /**
     * 报文内容
     */
    public byte[] payload;

    public HttpRequest() {
        super();
    }

    public HttpRequest(String url, ContentType contentType,
                       RequestMethod requestMethod) {
        super();
        this.url = url;
        this.contentType = contentType;
        this.requestMethod = requestMethod;
    }

    public static enum RequestMethod {
        Post, Get
    }

    public static enum ContentType {

        Html("text/html"), Html_UTF8("text/html;charset=UTF-8"), Stream(
                "application/octet-stream"), Json("application/json"), Text(
                "text/plain"), Form("application/x-www-form-urlencoded");

        private String value;

        private ContentType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

}
