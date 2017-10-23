package com.chinaums.opensdk.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;


public final class UmsActivityUtils {

    /**
     * @param activity
     * @return
     * @throws Exception
     */
    public static boolean isTopActivity(Activity activity) throws Exception {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().equals(activity.getComponentName().getClassName())) {
            isTop = true;
        } else if (activity.getParent() != null
                && cn.getClassName()
                .equals(activity.getParent().getComponentName()
                        .getClassName())) {
            isTop = true;
        }
        return isTop;
    }

}
