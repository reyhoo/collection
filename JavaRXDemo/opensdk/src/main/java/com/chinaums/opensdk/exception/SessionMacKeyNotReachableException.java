package com.chinaums.opensdk.exception;

public class SessionMacKeyNotReachableException extends Exception {

    private static final long serialVersionUID = 2502027828471353645L;

    public static final String DEFAULT_MSG = "无法获取整合客户端会话的mackey";

    public SessionMacKeyNotReachableException(String detailMessage,
                                              Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SessionMacKeyNotReachableException(String detailMessage) {
        super(detailMessage);
    }

    public SessionMacKeyNotReachableException() {
        super(DEFAULT_MSG);
    }
    
}
