package com.chinaums.opensdk.exception;

public class ResourceInitProcessFileException extends Exception {

    private static final long serialVersionUID = -1019231312850658441L;

    public ResourceInitProcessFileException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ResourceInitProcessFileException(String detailMessage) {
        super(detailMessage);
    }

}
