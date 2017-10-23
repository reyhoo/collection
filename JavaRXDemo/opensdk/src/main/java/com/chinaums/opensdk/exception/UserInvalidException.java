package com.chinaums.opensdk.exception;

public class UserInvalidException extends Exception {

    public static final String DEFAULT_MSG = "当前用户已失效";
    private static final long serialVersionUID = -3068355667826258511L;

    public UserInvalidException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UserInvalidException(String detailMessage) {
        super(detailMessage);
    }

    public UserInvalidException() {
        super(DEFAULT_MSG);
    }

}
