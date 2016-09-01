package com.yl.mutlichannelpkg;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by yaolei on 2016/9/1.
 */
public class App extends Application {
    private static String currChannelName;
    public static String getCurrChannelName() {
        return currChannelName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.yl.mutlichannelpkg", PackageManager.GET_META_DATA);
            currChannelName = applicationInfo.metaData.getString("CHANNEL_NAME");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
