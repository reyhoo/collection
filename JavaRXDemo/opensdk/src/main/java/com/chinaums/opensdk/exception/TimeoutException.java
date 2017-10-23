package com.chinaums.opensdk.exception;

public class TimeoutException extends Exception {

    private static final long serialVersionUID = -3327315854490959988L;

    public TimeoutException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TimeoutException(String detailMessage) {
        super(detailMessage);
    }

}
