package com.pac.sensor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pac.sensor.service.SleepService;

@SuppressLint("Registered")
public class SleepServiceActivity extends AppCompatActivity
{
    private ImageView toggleSwitch;
    private EditText angleText;
    private ImageView submitButton;

    private final static int DEFAULT_ANGLE = 10;
    private final static int SLEEPING_MODE_REQ_CODE = 1;

    private Intent sleepingServiceIntent;

    private ComponentName sleepingCompName;

    DevicePolicyManager deviceManger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        toggleSwitch = findViewById(R.id.sleep_toggle_switch);
        angleText = findViewById(R.id.angle);
        submitButton = findViewById(R.id.sleep_submit);
        ImageView alarm = findViewById(R.id.sleep_alarm);
        ImageView shake = findViewById(R.id.sleep_shake);

        loadState();

        alarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayActivity("alarm");
            }
        });

        shake.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayActivity("shake");
            }
        });

        init_sleeping_mode();

    }

    private void init_sleeping_mode()
    {
        sleepingServiceIntent = new Intent(SleepServiceActivity.this, SleepService.class);
        sleepingServiceIntent.putExtra(getString(R.string.sleepingModeAngle), getAngle());

        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        sleepingCompName = new ComponentName(this, DeviceAdmin.class);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean on = ((Integer)toggleSwitch.getTag() == R.drawable.switch_on);
                if (on)
                    enable(sleepingCompName, SLEEPING_MODE_REQ_CODE);
                else
                    disable(sleepingServiceIntent);
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

    private double getAngle()
    {
        double angle;
        if (angleText.getText().toString().isEmpty())
            angle = DEFAULT_ANGLE;
        else
            angle = Double.parseDouble(angleText.getText().toString());
        return angle;
    }

    public void enable(ComponentName componentName, int requestCode)
    {
        boolean active = deviceManger.isAdminActive(componentName);
        if (active)
            deviceManger.removeActiveAdmin(componentName);
        else
        {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivityForResult(intent, requestCode);
        }
    }

    public void disable(Intent intent)
    {
        deviceManger = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        try
        {
            sleepingCompName = new ComponentName(SleepServiceActivity.this, DeviceAdmin.class);
            boolean active = deviceManger.isAdminActive(sleepingCompName);
            if (active)
                deviceManger.removeActiveAdmin(sleepingCompName);
            stopService(intent);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            toggleSwitch.setImageResource(R.drawable.switch_on);
            toggleSwitch.setTag(R.drawable.switch_on);
            SleepService.devicePolicyManager = deviceManger;
            sleepingServiceIntent = new Intent(SleepServiceActivity.this, SleepService.class);
            sleepingServiceIntent.putExtra(getString(R.string.sleepingModeAngle), getAngle());
            startService(sleepingServiceIntent);
        }
        else
        {
            toggleSwitch.setImageResource(R.drawable.switch_off);
            toggleSwitch.setTag(R.drawable.switch_off);
        }
    }

    public void displayActivity(String activity)
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
        ServicesData.SLEEP_SERVICE_DATA.clear();
        if( (Integer)toggleSwitch.getTag() == R.drawable.switch_on )
            ServicesData.SLEEP_SERVICE_DATA.put("toggleSwitch", "on");
        else
            ServicesData.SLEEP_SERVICE_DATA.put("toggleSwitch", "off");
        if( angleText.getText().toString().isEmpty() )
            ServicesData.SLEEP_SERVICE_DATA.put("angle", "Rotation Angle To Turn On Screen");
        else
            ServicesData.SLEEP_SERVICE_DATA.put("angle", angleText.getText().toString());
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
        String angle = ServicesData.SLEEP_SERVICE_DATA.get("angle");
        if( angle.equals("Rotation Angle To Turn On Screen") )
            angleText.setHint(R.string.ScreenOnAngle);
        else
            angleText.setText(angle);
    }
}
