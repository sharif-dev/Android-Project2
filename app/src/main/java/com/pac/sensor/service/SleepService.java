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
    private final String min = "com.pac.sensor.service.extra.PARAM1";
    private final String max = "com.pac.sensor.service.extra.PARAM2";
    private boolean shouldStop = false;

    private float xForce;
    private float yForce;
    private float zForce;

    private SensorManager sensorManager;
    private Sensor mLight;

    public SleepService() {
        super("SleepService");
    }

    public void startService(Context context, String max, String min) {
        Intent intent = new Intent(context, SleepService.class);
        intent.putExtra(this.min, max);
        intent.putExtra(this.max, min);


        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String param1 = intent.getStringExtra(min);
            final String param2 = intent.getStringExtra(max);
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mLight = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
            while (!shouldStop){
                Log.v("gravity", Float.toString(zForce));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        xForce = event.values[0];
        yForce = event.values[1];
        zForce = event.values[2];
//        Log.v("gravity", Float.toString(zForce));
        Log.v("gravity", "asdf");
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
