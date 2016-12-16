package com.yaolei.alarmmanagertest;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;

/**
 * Created by yaolei on 2016/8/20.
 */
public class AlarmUtil {


    public static void setOnce(Context context, String uniqueId, int seconds) {
        context = context.getApplicationContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.test.Alarm");
        intent.setData(Uri.parse("http://"+uniqueId));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long now = System.currentTimeMillis();
        manager.set(AlarmManager.RTC_WAKEUP,now+1000*seconds,pendingIntent);
    }

    public static void setRepeat(Context context, String uniqueId, int seconds) {
        context = context.getApplicationContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.test.Alarm");
        intent.setData(Uri.parse("http://"+uniqueId));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long now = System.currentTimeMillis();
        manager.setRepeating(AlarmManager.RTC_WAKEUP, now, seconds * 1000, pendingIntent);
    }

    public static void cancel(Context context, String uniqueId) {
        context = context.getApplicationContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent("com.test.Alarm");
        intent.setData(Uri.parse("http://"+uniqueId));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }

}
