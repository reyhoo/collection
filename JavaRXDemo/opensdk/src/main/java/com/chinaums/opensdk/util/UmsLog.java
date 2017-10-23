//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.chinaums.opensdk.util;

import android.util.Log;
import com.chinaums.opensdk.manager.OpenConfigManager;
import com.chinaums.opensdk.util.UmsStringUtils;

public class UmsLog {
    private static String TAG = "UMSOPEN";

    public UmsLog() {
    }

    private static void println(int priority, String msg, Object... params) {
        if(UmsStringUtils.isNotBlank(msg) && params != null) {
            Object[] var3 = params;
            int var4 = params.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Object param = var3[var5];

                try {
                    msg = msg.replaceFirst("[{][}]", param.toString());
                } catch (Exception var8) {
                    ;
                }
            }
        }

        Log.println(priority, TAG, msg);
    }

    public static void v(String msg) {
        if(OpenConfigManager.isDebug()) {
            println(2, msg, new Object[0]);
        }

    }

    public static void v(String msg, Object... params) {
        if(OpenConfigManager.isDebug()) {
            println(2, msg, params);
        }

    }

    public static void d(String msg) {
        if(OpenConfigManager.isDebug()) {
            println(3, msg, new Object[0]);
        }

    }

    public static void d(String msg, Object... params) {
        if(OpenConfigManager.isDebug()) {
            println(3, msg, params);
        }

    }

    public static void i(String msg) {
        println(4, msg, new Object[0]);
    }

    public static void i(String msg, Object... params) {
        println(4, msg, params);
    }

    public static void e(String msg) {
        println(6, msg, new Object[0]);
    }

    public static void e(String msg, Throwable t) {
        e(msg);
        if(t != null) {
            t.printStackTrace();
        }

    }

    public static void e(String msg, Throwable t, Object... params) {
        e(msg, params);
        if(t != null) {
            t.printStackTrace();
        }

    }

    public static void e(String msg, Object... params) {
        println(6, msg, params);
    }

    public static void w(String msg) {
        println(5, msg, new Object[0]);
    }

    public static void w(String msg, Object... params) {
        println(5, msg, params);
    }
}
