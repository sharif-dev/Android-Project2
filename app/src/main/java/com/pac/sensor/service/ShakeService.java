package com.pac.sensor.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.pac.sensor.R;

public class ShakeService extends IntentService implements SensorEventListener {
    private SensorManager sensorManager;

    private boolean shouldStop = false;
    private boolean isShakeDetected = false;
    private String mode = "mode";
    private static float accelerationLimit = 10;

    private float xAcceleration = 0;
    private float yAcceleration = 0;
    private float zAcceleration = 0;

    public ShakeService() {
        super("ShakeService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mode = (String) intent.getExtras().get(getString(R.string.sleepingModeAngle));
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String mode = intent.getStringExtra(this.mode);
        setAccelerationLimit(mode);
        setSensor();

        while (!shouldStop) {
            if (isShakeDetected)
                Log.v("Shake", "shake detected!!!!!!!!!!!");
            //TODO: code for unlock the screen
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setSensor() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mACCELEROMETER = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, mACCELEROMETER, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setAccelerationLimit(String mode) {
        switch (mode) {
            case "high":
                accelerationLimit = (float) 60;
                break;
            case "low":
                accelerationLimit = (float) 20;
                break;
            default:
                accelerationLimit = (float) 40;
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
        Log.v("Shake", Float.toString(xAcceleration));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

}
