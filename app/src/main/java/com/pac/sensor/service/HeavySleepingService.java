package com.pac.sensor.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.pac.sensor.AlarmActivity;

public class HeavySleepingService extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        Intent intent1 = new Intent(context, AlarmActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("RINGTONE", intent.getStringExtra("RINGTONE"));
        intent.putExtra("SPEED", intent.getStringExtra("SPEED"));
        context.startActivity(intent1);
    }

}
