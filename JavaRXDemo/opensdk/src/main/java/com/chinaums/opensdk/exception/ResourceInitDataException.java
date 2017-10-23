package com.chinaums.opensdk.exception;

public class ResourceInitDataException extends Exception {

    private static final long serialVersionUID = 6379485460189820228L;

    public ResourceInitDataException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ResourceInitDataException(String detailMessage) {
        super(detailMessage);
    }

}
