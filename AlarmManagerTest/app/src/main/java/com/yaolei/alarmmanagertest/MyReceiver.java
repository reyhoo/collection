package com.yaolei.alarmmanagertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by yaolei on 2016/8/20.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("com.test.Alarm")) {
            String uri = intent.getData().toString();
            Log.i("App_", "App_onReceive:" + intent.getData());

            if(uri.equals("http://exact")){
                clock(context,intent.getLongExtra("triggerAtMillis",0));
                return;
            }
            if (uri.equals("http://repeat20")) {
                boolean isRunning = AppUtil.isServiceRunning(context, MyService.class);
                Log.i("App_", "App_onReceive:isRunning:" + isRunning);
                if(!isRunning){
                    context.startService(new Intent(context,MyService.class));
                }
                return;
            }

            Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(context,MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent1);
        } else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, MyService.class));
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            long triggerAtMillis = preferences.getLong("triggerAtMillis", 0);
            if(triggerAtMillis<=0)return;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(triggerAtMillis));
            AlarmUtil.setExact(context,getTriggerTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)));
        }

    }
    private long getTriggerTime(int hour,int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long triggerTime = calendar.getTime().getTime();
        long now = System.currentTimeMillis();
        if (triggerTime <= now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            triggerTime = calendar.getTime().getTime();
        }
        return triggerTime;
    }
    private void clock(Context context,long triggerAtMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(triggerAtMillis));

        AlarmUtil.setExact(context,getTriggerTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)));

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
