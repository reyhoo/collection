package com.chinaums.opensdk.exception;

public class StorageSpaceLowException extends Exception {

    private static final long serialVersionUID = 6379485460189820228L;

    public StorageSpaceLowException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public StorageSpaceLowException(String detailMessage) {
        super(detailMessage);
    }

}
