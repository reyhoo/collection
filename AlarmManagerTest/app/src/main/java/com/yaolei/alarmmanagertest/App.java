package com.yaolei.alarmmanagertest;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
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
        startService(new Intent(this, MyService.class));
        AlarmUtil.setRepeat(this,"repeat20",20);
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
