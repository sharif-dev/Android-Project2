package com.pac.sensor.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class HeavySleepingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
