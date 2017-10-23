package com.chinaums.opensdk.event.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

import com.chinaums.opensdk.event.model.ShakeMotionEvent;
import com.chinaums.opensdk.util.UmsEventBusUtils;


/**
 * 设备摇晃监听
 */
public class SensorController {

    // 速度阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_SHRESHOLD = 7000;
    // 两次检测的时间间隔
    private static final int UPTATE_INTERVAL_TIME = 50;

    /**
     * 振动器
     */
    private Vibrator vibrator;

    /**
     * 传感器
     */
    private SensorManager sensorManager;

    /**
     * 传感器监听实现
     */
    private SensorListener sensorlistener;
    
    /**
     * 手机上一个位置时重力感应坐标
     */
    private float lastX;
    private float lastY;
    private float lastZ;

    /**
     * 上次检测时间
     */
    private long lastUpdateTime;

    public SensorController() {
        super();
    }

    public void registerListener(Context context) {
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        sensorlistener = new SensorListener();
        if (null != sensorManager) {
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
            sensorManager.registerListener(sensorlistener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void unregisterListener(Context context) {
        if (null != sensorManager) {
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
            sensorManager.unregisterListener(sensorlistener);
        }
    }

    public class SensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            // 现在检测时间
            long currentUpdateTime = System.currentTimeMillis();
            // 两次检测的时间间隔
            long timeInterval = currentUpdateTime - lastUpdateTime;
            // 判断是否达到了检测时间间隔
            if (timeInterval < UPTATE_INTERVAL_TIME)
                return;
            // 现在的时间变成last时间
            lastUpdateTime = currentUpdateTime;
            // 获得x,y,z坐标
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // 获得x,y,z的变化值
            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;
            // 将现在的坐标变成last坐标
            lastX = x;
            lastY = y;
            lastZ = z;
            double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                    * deltaZ)
                    / timeInterval * 10000;
            // 达到速度阀值，发出提示
            if (speed >= SPEED_SHRESHOLD) {
                UmsEventBusUtils.post(new ShakeMotionEvent());
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}
