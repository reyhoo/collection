package com.example.administrator.hello;

import android.app.Application;
import android.os.Process;
import android.util.Log;

/**
 * Created by yaolei on 2016/9/1.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("info_","info_"+Util.getProcessName(this, Process.myPid()));
    }
}
