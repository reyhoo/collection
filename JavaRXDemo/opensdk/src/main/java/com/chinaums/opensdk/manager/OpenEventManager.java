package com.chinaums.opensdk.manager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import com.chinaums.opensdk.event.controller.NetworkController;
import com.chinaums.opensdk.event.controller.SensorController;

import java.util.List;

/**
 * 类 EventManager 的实现描述：注册事件,取消注册事件,通知事件,添加事件处理器与移除事件处理器
 */
public class OpenEventManager implements IOpenManager {

    /**
     * instance
     */
    private static OpenEventManager instance;

    /**
     * mContext
     */
    private Context mContext;

    /**
     * sensor
     */
    private SensorController sensor;

    /**
     * network
     */
    private NetworkController network;

    synchronized public static OpenEventManager getInstance() {
        if (instance == null) {
            instance = new OpenEventManager();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        this.mContext = context;
        sensor = new SensorController();
        network = new NetworkController();
        sensor.registerListener(this.mContext);
        network.registerListener(this.mContext);
    }

    @Override
    public void destroy() {
        sensor.unregisterListener(mContext);
        network.unregisterListener(mContext);
    }

    private boolean isBackground() {
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(mContext.getPackageName())) {
                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }


    public static class EventObject {

        /**
         * type
         */
        private EventType type;

        /**
         * params
         */
        private String params;

        public EventObject(EventType type, String params) {
            super();
            this.type = type;
            this.params = params;
        }

        public EventType getType() {
            return type;
        }

        public String getParams() {
            return params;
        }

    }

    public enum EventType {

        NETSTATUS("netStatusNoti"),
        SHAKESTATUS("shakeMotionNoti"),
        RESUME("resume"),
        LOCALUPDATE("localUpdate"),// 本地更新成功
        ASYNUPDATE("asynUpdate"),// 动态下载更新成功
        USERLOGOUT("userlogout"),//用户注销
        USERINVALID("userInvalid");//用户失效

        private String eventName;

        private EventType(String eventName) {
            this.eventName = eventName;
        }

        public String getEventName() {
            return eventName;
        }

    }

}
