package com.chinaums.opensdk.exception;

public class UnableProcessException extends Exception {

    private static final long serialVersionUID = 6379485460189820228L;

    public UnableProcessException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnableProcessException(String detailMessage) {
        super(detailMessage);
    }

}
