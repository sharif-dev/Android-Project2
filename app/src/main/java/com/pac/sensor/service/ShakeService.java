package com.pac.sensor.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.pac.sensor.R;

public class ShakeService extends Service implements SensorEventListener {
    private SensorManager sensorManager;

    private boolean isShakeDetected = false;
    private static float accelerationLimit = 10;

    private float xAcceleration = 0;
    private float yAcceleration = 0;
    private float zAcceleration = 0;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        String mode = (String) intent.getExtras().get(getString(R.string.shakingModePower));
        setAccelerationLimit(mode);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    private void setAccelerationLimit(String mode) {
        switch (mode) {
            case "high":
                accelerationLimit = (float) -60;
                break;
            case "low":
                accelerationLimit = (float) -20;
                break;
            default:
                accelerationLimit = (float) -40;
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (xAcceleration * event.values[0] < accelerationLimit ||
                yAcceleration * event.values[1] < accelerationLimit ||
                zAcceleration * event.values[2] < accelerationLimit)
            isShakeDetected = true;

        xAcceleration = event.values[0];
        yAcceleration = event.values[1];
        zAcceleration = event.values[2];
        Log.v("Shake", Boolean.toString(isShakeDetected));


        if (isShakeDetected) {
            PowerManager pm = (PowerManager)getApplicationContext().getSystemService(POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
            wl.acquire(10 * 60 * 1000L /*10 minutes*/);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
