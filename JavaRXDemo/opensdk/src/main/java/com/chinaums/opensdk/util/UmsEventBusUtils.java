package com.chinaums.opensdk.util;

import de.greenrobot.event.EventBus;


public final class UmsEventBusUtils {

    /**
     * @param subscriber
     */
    public static void register(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            return;
        }
        EventBus.getDefault().register(subscriber);
    }

    /**
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * @param event
     */
    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }
    
}
