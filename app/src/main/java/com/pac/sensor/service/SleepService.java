package com.pac.sensor.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.pac.sensor.R;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class SleepService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private double maxAngle;
    public static DevicePolicyManager devicePolicyManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//private boolean flag = true;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
            System.out.println("sensor changed");
        if (calAngle(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]) <= maxAngle){
            ((DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE)).lockNow();
            if (devicePolicyManager != null) {
                devicePolicyManager.lockNow();
            } else {
                devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                devicePolicyManager.lockNow();
            }
        }
    }

    private double calAngle(double x, double y, double z){
        return Math.abs(Math.acos(z / sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2))));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onDestroy(){
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        maxAngle = (double) intent.getExtras().get(getString(R.string.sleepingModeAngle)) * Math.PI / 180;
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
    }


}
