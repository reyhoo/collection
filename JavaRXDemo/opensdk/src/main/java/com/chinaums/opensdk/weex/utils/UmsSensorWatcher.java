package com.chinaums.opensdk.weex.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @Created at :  2017/6/12.
 * @Developer :  JiangBo
 * @Version :  1.0
 * @Description : 监听加速度传感器和地磁传感器
 */

public class UmsSensorWatcher {
    //记录rotationMatrix矩阵值
    private float[] matrix = new float[9];
    //记录通过getOrientation()计算出来的方位横滚俯仰值
    private float[] values = new float[3];
    private float[] accelerometer = null;
    private float[] geomagnetic = null;

    private Context mContext;
    //加速度传感器
    private Sensor acceleSensor;
    //磁场传感器
    private Sensor magSensor;

    //传感器管理器
    private SensorManager mSensorManager;

    //监听罗盘传感器
    private Set<OnSensorChangeListener> compassSet;
    private Stack<OnSensorChangeListener> compassStack;

    //监听加速度传感器
    private Set<OnSensorChangeListener> accelerometerSet;
    private Stack<OnSensorChangeListener> accelerometerStack;

    private UmsSensorWatcher() {
    }

    /**
     * 静态内部类单例模式
     */
    private static class SingleTon {
        static UmsSensorWatcher instance = new UmsSensorWatcher();
    }

    /**
     * 获取单列
     * @param context
     * @return UmsSensorWatcher
     */
    public static UmsSensorWatcher getInstance(Context context) {
        SingleTon.instance.initSensors(context);
        return SingleTon.instance;
    }

    /**
     * 初始化加速度和地磁传感器
     * @param context
     */
    private void initSensors(Context context) {
        mContext = context.getApplicationContext();
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

            compassSet = new HashSet<OnSensorChangeListener>();
            compassStack = new Stack<OnSensorChangeListener>();

            accelerometerSet = new HashSet<OnSensorChangeListener>();
            accelerometerStack = new Stack<OnSensorChangeListener>();
        }
        //注册加速度传感器监听  每秒一次的刷新
        acceleSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(sensorEventListener, acceleSensor, 1000 * 1000);
        //注册磁场传感器监听 每秒一次的刷新
        magSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(sensorEventListener, magSensor, 1000 * 1000);
    }

    /**
     * 获取罗盘指向
     * @param listener
     */
    public void getDirection(OnSensorChangeListener listener) {
        initSensors(mContext);
        compassStack.push(listener);
    }

    /**
     * 持续监听罗盘指向
     * @param listener
     */
    public void watchDirection(OnSensorChangeListener listener) {
        initSensors(mContext);
        compassSet.add(listener);
    }

    /**
     * 清除方向监听
     * @param listener
     */
    public void clearWatchDirection(OnSensorChangeListener listener) {
        compassSet.remove(listener);
    }

    /**
     * 获取当前加速度
     * @param listener
     */
    public void getAcceleration(OnSensorChangeListener listener) {
        initSensors(mContext);
        accelerometerStack.push(listener);
    }

    /**
     * 持续监听加速度变化
     * @param listener
     */
    public void watchAcceleration(OnSensorChangeListener listener) {
        initSensors(mContext);
        accelerometerSet.add(listener);
    }

    /**
     * 清除加速度监听
     * @param listener
     */
    public void clearWatchAcceleration(OnSensorChangeListener listener) {
        accelerometerSet.remove(listener);
    }

    /**
     * 通知监听加速度的对象
     * @param result
     */
    private void notifyAcceleration(Map<String, Object> result) {
        for (OnSensorChangeListener listener : accelerometerSet) {
            listener.onSensorChanged(result);
        }

        while (!accelerometerStack.isEmpty()) {
            accelerometerStack.pop().onSensorChanged(result);
        }
    }

    /**
     * 通知监听罗盘的对象
     * @param result
     */
    private void notifyDirection(Map<String, Object> result) {
        for (OnSensorChangeListener listener : compassSet) {
            listener.onSensorChanged(result);
        }

        while (!compassStack.isEmpty()) {
            compassStack.pop().onSensorChanged(result);
        }
    }

    /**
     * 传感器变化监听器
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // 360/0表示正北，90表示正东，180表示正南，270表示正西。

            try {
                if (mSensorManager != null && acceleSensor != null && magSensor != null && compassSet.isEmpty() && compassStack.isEmpty() && accelerometerSet.isEmpty() && accelerometerStack.isEmpty()) {
                    mSensorManager.unregisterListener(sensorEventListener, acceleSensor);
                    mSensorManager.unregisterListener(sensorEventListener, magSensor);
                }

                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {//加速度传感器
                    accelerometer = event.values;
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("x", accelerometer[0] + "");
                    result.put("y", accelerometer[1] + "");
                    result.put("z", accelerometer[2] + "");
                    result.put("result", "success");
                    notifyAcceleration(result);

                } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {//磁场传感器
                    geomagnetic = event.values;
                }

                if (accelerometer == null || geomagnetic == null) {
                    return;
                }

                if (SensorManager.getRotationMatrix(matrix, null, accelerometer, geomagnetic)) {
                    SensorManager.getOrientation(matrix, values);
                    float degree = (float) ((360f + values[0] * 180f / Math.PI) % 360);
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("direction", degree + "");
                    result.put("result", "success");

                    notifyDirection(result);

                    accelerometer = null;
                    geomagnetic = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, Object> result = new HashMap<String, Object>();
                result.clear();
                result.put("result", "fail");

                notifyDirection(result);
                notifyAcceleration(result);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * 监听接口
     */
    public interface OnSensorChangeListener {
        void onSensorChanged(Map<String, Object> result);
    }

    /**
     * 通用接口基本实现
     */
    public static class OnSensorChangeListenerImpl implements UmsSensorWatcher.OnSensorChangeListener {
        private JSCallback callback;
        private String eventName;
        private WXSDKInstance mWXSDKInstance;


        @Override
        public void onSensorChanged(Map<String, Object> result) {
            if (callback != null) {
                callback.invoke(result);
                callback = null;
            }
            if (eventName != null) {
                mWXSDKInstance.fireGlobalEventCallback(eventName, result);
            }
        }

        public void setWXSDKInstance(WXSDKInstance mWXSDKInstance) {
            this.mWXSDKInstance = mWXSDKInstance;
        }

        public void setCallback(JSCallback callback) {
            this.callback = callback;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public JSCallback getCallback() {
            return callback;
        }

        public String getEventName() {
            return eventName;
        }

        public void reset() {
            eventName = null;
        }
    }
}
