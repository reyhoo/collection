package com.chinaums.opensdk.exception;

public class ResourceCheckOriginalException extends Exception {

    private static final long serialVersionUID = 6379485460189820228L;

    public ResourceCheckOriginalException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ResourceCheckOriginalException(String detailMessage) {
        super(detailMessage);
    }

}
