package com.chinaums.opensdk.exception;

public class ResourceCreateFileException extends Exception {

    private static final long serialVersionUID = -1019231312850658441L;

    public ResourceCreateFileException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ResourceCreateFileException(String detailMessage) {
        super(detailMessage);
    }

}
