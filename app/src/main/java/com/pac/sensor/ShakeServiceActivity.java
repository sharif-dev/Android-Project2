package com.pac.sensor;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pac.sensor.service.ShakeService;


public class ShakeServiceActivity extends AppCompatActivity
{
    private ImageView toggleSwitch;
    private EditText shakingSensitivity;
    private ImageView submitButton;
    private final static String DEFAULT_SHAKING_POWER = "low";
    private final static int SHAKING_MODE_REQ_CODE = 2;
    private Intent shakingServiceIntent;

    private ComponentName shakingCompName;

    DevicePolicyManager deviceManger;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        toggleSwitch = findViewById(R.id.shaking_activity_toggle_switch);
        shakingSensitivity = findViewById(R.id.shaking_sensitivity);
        submitButton = findViewById(R.id.shaking_activity_submit);
        ImageView alarm = findViewById(R.id.shaking_activity_alarm);
        ImageView sleep = findViewById(R.id.shaking_activity_sleep);

        loadState();

        alarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayActivity("alarm");
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

        init_shaking_mode();
    }

    private void init_shaking_mode()
    {
        shakingServiceIntent = new Intent(ShakeServiceActivity.this, ShakeService.class);

        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        shakingCompName = new ComponentName(this, DeviceAdmin.class);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean on = ((Integer)toggleSwitch.getTag() == R.drawable.switch_on);
                if (on)
                    enable(shakingCompName, SHAKING_MODE_REQ_CODE);
                else
                    disable(shakingServiceIntent);
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

    private String getPower()
    {
        String power;
        if (shakingSensitivity.getText().toString().isEmpty())
            power = DEFAULT_SHAKING_POWER;
        else
            power = shakingSensitivity.getText().toString();
        return power;
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
            shakingCompName = new ComponentName(ShakeServiceActivity.this, DeviceAdmin.class);
            boolean active = deviceManger.isAdminActive(shakingCompName);
            if (active)
                deviceManger.removeActiveAdmin(shakingCompName);
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
        shakingServiceIntent = new Intent(ShakeServiceActivity.this, ShakeService.class);
        shakingServiceIntent.putExtra(getString(R.string.ShakingSensitivity), getPower());
        startService(shakingServiceIntent);
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
        ServicesData.SHAKE_SERVICE_DATA.clear();
        if( (Integer)toggleSwitch.getTag() == R.drawable.switch_on )
            ServicesData.SHAKE_SERVICE_DATA.put("toggleSwitch", "on");
        else
            ServicesData.SHAKE_SERVICE_DATA.put("toggleSwitch", "off");
        ServicesData.SHAKE_SERVICE_DATA.put("sensitivity", shakingSensitivity.getText().toString());
    }

    private void loadState()
    {
        if( ServicesData.SHAKE_SERVICE_DATA.get("toggleSwitch").equals("on") )
        {
            toggleSwitch.setImageResource(R.drawable.switch_on);
            toggleSwitch.setTag(R.drawable.switch_on);
        }
        else
        {
            toggleSwitch.setImageResource(R.drawable.switch_off);
            toggleSwitch.setTag(R.drawable.switch_off);
        }
        shakingSensitivity.setText(ServicesData.SHAKE_SERVICE_DATA.get("sensitivity"));
    }

}
