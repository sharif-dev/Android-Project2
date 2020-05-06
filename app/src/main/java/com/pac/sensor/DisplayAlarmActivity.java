package com.pac.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayAlarmActivity extends AppCompatActivity
{
    private ImageView toggleSwitch;
    private TimePicker timePicker;
    private EditText speed;
    private ImageView submitButton;
    private ImageView shake;
    private ImageView sleep;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
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

    }

    public void displayActivity(String activity)
    {
        Intent intent = null;
        if( activity.equals("alarm") )
            intent = new Intent(this, DisplayAlarmActivity.class);
        if( activity.equals("shake") )
            intent = new Intent(this, DisplayShakeActivity.class);
        if( activity.equals("sleep") )
            intent = new Intent(this, DisplaySleepActivity.class);
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
