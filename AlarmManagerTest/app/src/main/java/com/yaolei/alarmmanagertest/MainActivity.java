package com.yaolei.alarmmanagertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AlarmManager manager;

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

        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        startService(new Intent(this, MyService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setOnce:
                AlarmUtil.setOnce(getApplication(),"once",5);
                break;
            case R.id.setRepeat5:
                AlarmUtil.setRepeat(getApplication(),"repeat5",5);
                break;
            case R.id.setRepeat10:
                AlarmUtil.setRepeat(getApplication(),"repeat10",10);
                break;
            case R.id.cancelOnce:
                AlarmUtil.cancel(getApplication(),"once");
                break;
            case R.id.cancelRepeat5:
                AlarmUtil.cancel(getApplication(),"repeat5");
                break;
            case R.id.cancelRepeat10:
                AlarmUtil.cancel(getApplication(),"repeat10");
                break;
        }
    }
}
