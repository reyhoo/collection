package com.chinaums.opensdk.manager;


public interface IOpenClientInfo {

    /**
     * 客户端编码
     */
    public String getClientId();

    /**
     * 客户端类型
     */
    public String getClientType();

    /**
     * 客户端版本
     */
    public String getClientVersion();

}
