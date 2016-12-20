package com.yaolei.alarmmanagertest;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yaolei on 2016/8/20.
 */
public class AlarmUtil {


    public static void setOnce(Context context, String uniqueId, int seconds) {
        context = context.getApplicationContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.test.Alarm");
        intent.setData(Uri.parse("http://" + uniqueId));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long now = System.currentTimeMillis();
        manager.set(AlarmManager.RTC_WAKEUP, now + 1000 * seconds, pendingIntent);
    }

    public static void setRepeat(Context context, String uniqueId, int seconds) {
        context = context.getApplicationContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.test.Alarm");
        intent.setData(Uri.parse("http://" + uniqueId));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long now = System.currentTimeMillis();
        manager.setRepeating(AlarmManager.RTC_WAKEUP, now + seconds * 1000, seconds * 1000, pendingIntent);
    }

    public static void cancel(Context context, String uniqueId) {
        context = context.getApplicationContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.test.Alarm");
        intent.setData(Uri.parse("http://" + uniqueId));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setExact(Context context, long triggerAtMillis) {
        Toast.makeText(context, "设置闹钟:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(triggerAtMillis)), Toast.LENGTH_SHORT).show();
        Log.i("AlarmUtil_","AlarmUtil_:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(triggerAtMillis)));
        context = context.getApplicationContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.test.Alarm");
        intent.setData(Uri.parse("http://exact"));
        intent.putExtra("triggerAtMillis", triggerAtMillis);
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        preference.edit().putLong("triggerAtMillis",triggerAtMillis).commit();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }

    }


    public static void cancelExact(Context context) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        preference.edit().putLong("triggerAtMillis",0).commit();
        context = context.getApplicationContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.test.Alarm");
        intent.setData(Uri.parse("http://exact"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }

}
