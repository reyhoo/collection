package com.yaolei.alarmmanagertest;

import android.app.Application;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by yaolei on 2016/8/20.
 */
public class App extends Application {

    private PowerManager.WakeLock mLock;
    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyAlarmLock");
        mLock.acquire();
        Log.i("App_", "App_onCreate");
    }

    @Override
    public void onTerminate() {
        mLock.release();
        super.onTerminate();
    }
}
