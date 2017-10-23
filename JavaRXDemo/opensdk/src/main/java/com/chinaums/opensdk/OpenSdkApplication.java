package com.chinaums.opensdk;

import android.app.Application;

/**
 * Created by yaolei on 2017/9/26.
 */

public class OpenSdkApplication extends Application {

    private static Application instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }
}
