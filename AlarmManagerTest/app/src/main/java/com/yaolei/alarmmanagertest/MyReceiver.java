package com.yaolei.alarmmanagertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yaolei on 2016/8/20.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("com.test.Alarm")){
            Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
            Log.i("App_","App_onReceive:"+intent.getData());
        }else if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
            AlarmUtil.setRepeat(context,"repeat5",5);
        }

    }
}
