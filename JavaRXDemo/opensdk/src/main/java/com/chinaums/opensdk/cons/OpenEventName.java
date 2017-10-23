package com.chinaums.opensdk.cons;


public enum OpenEventName {

    // 客户端传给H5应用的通知类型
    NETSTATUS("netStatusNoti"), SHAKESTATUS("shakeMotionNoti"), RESUME("resume");

    private String eventName;

    OpenEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return eventName;
    }

}
