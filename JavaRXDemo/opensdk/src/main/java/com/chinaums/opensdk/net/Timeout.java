package com.chinaums.opensdk.net;

public enum Timeout {

    /**
     * 快速返回，如版本更新检查
     */
    FAST(10000),

    /**
     * 正常时间
     */
    NORMAL(30000),

    /**
     * 较慢的操作，如登陆、订单查询
     */
    SLOW(60000),

    /**
     * 非常慢的操作，如支付、历史订单查询
     */
    VERY_SLOW(90000);

    private int value;

    private Timeout(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}