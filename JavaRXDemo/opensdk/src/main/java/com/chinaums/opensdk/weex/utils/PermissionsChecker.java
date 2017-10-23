package com.chinaums.opensdk.weex.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * @Created at :  2017/9/19.
 * @Version :  1.0
 * @Description :
 */

public class PermissionsChecker {
    private Activity activity;

    /**
     * 初始化
     *
     * @param activity
     */
    private void Initialization(Activity activity) {
        this.activity = activity;
    }

    /**
     * 创建并初始化单例模式
     *
     * @param activity
     * @return
     */
    public static PermissionsChecker getIstance(Activity activity) {
        PermissionsChecker instance = SingletonHolder.instance;
        instance.Initialization(activity);
        return instance;
    }


    /**
     * 判断权限集合
     *
     * @param permissions 权限
     * @return
     */
    public boolean hasPermission(String... permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (!hasPermission(permissions[i])) {//无权限
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否缺少权限  无权限返回true
     *
     * @param permission 权限
     * @return
     */
    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 静态内部类单例模式
     */
    private static class SingletonHolder {
        private static PermissionsChecker instance = new PermissionsChecker();
    }
}
