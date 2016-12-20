package com.yaolei.alarmmanagertest;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class AppUtil {

    public static boolean isServiceRunning(Context context, Class<? extends Service>serviceCls){
       ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        ComponentName componentName = new ComponentName(context,serviceCls);
        for (ActivityManager.RunningServiceInfo serviceInfo :
                serviceList) {
            boolean isRunning = serviceInfo.service.equals(componentName);
            if(isRunning){
                return true;
            }
        }
        return false;
    }
}
