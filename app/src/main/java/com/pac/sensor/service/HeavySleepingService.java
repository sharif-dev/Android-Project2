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

public class HeavySleepingService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private double minRotationRate;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[2] >= minRotationRate)
            turnOff();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void turnOff(){
        //TODO remove alarm activity
        onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        minRotationRate = (double) intent.getExtras().get(getString(R.string.HSModeRotationRate)) * Math.PI / 180;
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
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

//    public static void setAlarm(Context context, long waitTimeMillis) {
//        Intent        intent        = new Intent(context, ExpirationListener.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager  alarmManager  = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//
//        alarmManager.cancel(pendingIntent);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + waitTimeMillis, pendingIntent);
//    }
}
