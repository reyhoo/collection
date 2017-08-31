package com.example.administrator.hello;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by yaolei on 2016/9/1.
 */
public class Util {

    public static final String getProcessName(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info :
                processes) {
            if (info.pid == pid) {
                return info.processName;
            }
        }
        return "";
    }
}
