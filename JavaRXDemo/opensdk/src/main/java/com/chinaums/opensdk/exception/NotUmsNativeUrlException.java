package com.chinaums.opensdk.exception;

public class NotUmsNativeUrlException extends Exception {

    private static final long serialVersionUID = -3327315854490959988L;

    public NotUmsNativeUrlException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotUmsNativeUrlException(String detailMessage) {
        super(detailMessage);
    }

}
