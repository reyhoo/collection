package com.yaolei.alarmmanagertest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yaolei on 2016/8/20.
 */
public class MyService extends Service {

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_TIME_CHANGED)){
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Toast.makeText(context,"time changed:"+sdf.format(new Date()),Toast.LENGTH_SHORT).show();
                Log.i("MyService_","MyService_action:"+action+":time changed:"+sdf.format(new Date()));
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i("MyService_","MyService_onCreate");
        super.onCreate();
        IntentFilter intentFilter  = new IntentFilter(Intent.ACTION_TIME_CHANGED);
        registerReceiver(mReceiver,intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
