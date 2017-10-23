package com.chinaums.opensdk.exception;


public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = -1019231312850658441L;

    public ResourceNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ResourceNotFoundException(String detailMessage) {
        super(detailMessage);
    }

}
