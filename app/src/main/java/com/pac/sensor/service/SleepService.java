package com.pac.sensor.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SleepService extends IntentService implements SensorEventListener {
    private final String mode = "mode";
    private boolean shouldStop = false;
    private float zLimit = 0;
    private float zForce;

    public SleepService() {
        super("SleepService");
    }

    public void startService(Context context, String mode) {
        Intent intent = new Intent(context, SleepService.class);
        intent.putExtra(this.mode, mode);


        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String mode = intent.getStringExtra(this.mode);
            setZLimit(mode);
            setSensor();

            while (!shouldStop){
                if (zForce < zLimit)
                    Log.v("gravity", "successsssssssss");
                    //TODO: code for lock the screen
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setSensor(){
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mLight = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setZLimit(String mode){
        switch (mode){
            case "high":
                zLimit = (float) 0.5;
                break;
            case "low":
                zLimit = (float) 2;
                break;
            default:
                zLimit = (float) 4;
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        zForce = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public void onDestroy() {
        //sensorManager.unregisterListener(this);
        super.onDestroy();
    }
}
