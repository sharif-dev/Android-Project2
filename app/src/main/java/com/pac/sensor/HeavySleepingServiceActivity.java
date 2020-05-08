package com.pac.sensor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.pac.sensor.service.HeavySleepingService;

import java.util.Calendar;

public class HeavySleepingServiceActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener
{
    private ImageView toggleSwitch;
    private TimePicker timePicker;
    private EditText speed;
    private ImageView submitButton;
    private ImageView shake;
    private ImageView sleep;
    private Spinner ringtoneSpinner;
    private String ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heavy_sleep);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        toggleSwitch = findViewById(R.id.alarm_toggle_switch);
        timePicker = findViewById(R.id.time_picker);
        speed = findViewById(R.id.alarm_off_speed);
        submitButton = findViewById(R.id.alarm_submit);
        shake = findViewById(R.id.alarm_shake);
        sleep = findViewById(R.id.alarm_sleep);
        ringtoneSpinner = findViewById(R.id.ringtone_spinner);
        timePicker.setIs24HourView(true);

        loadState();

        shake.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayActivity("shake");
            }
        });

        sleep.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayActivity("sleep");
            }
        });

        init_alarm_mode();
    }

    private void init_alarm_mode()
    {
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean on = ((Integer)toggleSwitch.getTag() == R.drawable.switch_on);
                if (on)
                    enable();
                else
                    disable();
            }
        });

        toggleSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( (Integer)toggleSwitch.getTag() == R.drawable.switch_on )
                {
                    toggleSwitch.setImageResource(R.drawable.switch_off);
                    toggleSwitch.setTag(R.drawable.switch_off);
                }
                else
                {
                    toggleSwitch.setImageResource(R.drawable.switch_on);
                    toggleSwitch.setTag(R.drawable.switch_on);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        ringtone = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    private void enable()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, HeavySleepingService.class);
        intent.putExtra("RINGTONE", ringtone);
        intent.putExtra("SPEED", speed.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void disable()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, HeavySleepingService.class);
        intent.putExtra("RINGTONE", ringtone);
        intent.putExtra("SPEED", speed.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    private void displayActivity(String activity)
    {
        Intent intent = null;
        if( activity.equals("alarm") )
            intent = new Intent(this, HeavySleepingServiceActivity.class);
        if( activity.equals("shake") )
            intent = new Intent(this, ShakeServiceActivity.class);
        if( activity.equals("sleep") )
            intent = new Intent(this, SleepServiceActivity.class);
        saveState();
        startActivity(intent);
    }

    private void saveState()
    {
        ServicesData.ALARM_SERVICE_DATA.clear();
        if( (Integer)toggleSwitch.getTag() == R.drawable.switch_on )
            ServicesData.ALARM_SERVICE_DATA.put("toggleSwitch", "on");
        else
            ServicesData.ALARM_SERVICE_DATA.put("toggleSwitch", "off");
        ServicesData.ALARM_SERVICE_DATA.put("speed", speed.getText().toString());
        ServicesData.ALARM_SERVICE_DATA.put("hour", Integer.toString(timePicker.getHour()));
        ServicesData.ALARM_SERVICE_DATA.put("minute", Integer.toString(timePicker.getMinute()));
    }

    private void loadState()
    {
        if( ServicesData.SLEEP_SERVICE_DATA.get("toggleSwitch").equals("on") )
        {
            toggleSwitch.setImageResource(R.drawable.switch_on);
            toggleSwitch.setTag(R.drawable.switch_on);
        }
        else
        {
            toggleSwitch.setImageResource(R.drawable.switch_off);
            toggleSwitch.setTag(R.drawable.switch_off);
        }
        speed.setText(ServicesData.SLEEP_SERVICE_DATA.get("speed"));
        timePicker.setHour(Integer.parseInt(ServicesData.ALARM_SERVICE_DATA.get("hour")));
        timePicker.setMinute(Integer.parseInt(ServicesData.ALARM_SERVICE_DATA.get("minute")));
    }

}
