package com.yaolei.alarmmanagertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.setOnce).setOnClickListener(this);
        findViewById(R.id.setRepeat5).setOnClickListener(this);
        findViewById(R.id.setRepeat10).setOnClickListener(this);
        findViewById(R.id.cancelOnce).setOnClickListener(this);
        findViewById(R.id.cancelRepeat5).setOnClickListener(this);
        findViewById(R.id.cancelRepeat10).setOnClickListener(this);
        findViewById(R.id.setExact).setOnClickListener(this);
        findViewById(R.id.cancelExact).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setOnce:
                AlarmUtil.setOnce(getApplication(), "once", 5);
                break;
            case R.id.setRepeat5:
                AlarmUtil.setRepeat(getApplication(), "repeat5", 5);
                break;
            case R.id.setRepeat10:
                AlarmUtil.setRepeat(getApplication(), "repeat10", 10);
                break;
            case R.id.setExact:
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        AlarmUtil.setExact(getApplication(), getTriggerTime(hourOfDay,minute));
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();

                break;
            case R.id.cancelOnce:
                AlarmUtil.cancel(getApplication(), "once");
                break;
            case R.id.cancelRepeat5:
                AlarmUtil.cancel(getApplication(), "repeat5");
            case R.id.cancelRepeat10:
                AlarmUtil.cancel(getApplication(), "repeat10");
                break;
            case R.id.cancelExact:
                AlarmUtil.cancelExact(getApplication());
                break;
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
}
