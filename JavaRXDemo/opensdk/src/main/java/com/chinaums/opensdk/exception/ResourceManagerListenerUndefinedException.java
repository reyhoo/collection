package com.chinaums.opensdk.exception;


public class ResourceManagerListenerUndefinedException extends Exception {

    private static final long serialVersionUID = -845918710956527587L;

    public ResourceManagerListenerUndefinedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ResourceManagerListenerUndefinedException(String detailMessage) {
        super(detailMessage);
    }

}
