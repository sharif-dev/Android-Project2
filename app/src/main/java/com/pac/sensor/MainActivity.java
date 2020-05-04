package com.pac.sensor;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pac.sensor.service.ShakeService;
import com.pac.sensor.service.SleepService;

public class MainActivity extends AppCompatActivity {
    private Switch sleepingSwitch;
    private Switch shakingSwitch;
    private EditText sleepingAngle;
    private EditText shakingPower;
    private Button sleepingAngleButton;
    private Button shakingAngleButton;

    //    private Switch shakingSwitch;
    private final static int DEFAULT_ANGLE = 10;
    private final static String DEFAULT_Shak_POWER = "low";
    private final static int SLEEPING_MODE_REQ_CODE = 1;
    private final static int SHAKING_MODE_REQ_CODE = 2;

    private Intent sleepingServiceIntent;
    private Intent shakingServiceIntent;

    private ComponentName sleepingCompName;
    private ComponentName shakingCompName;

    DevicePolicyManager deviceManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_sleeping_mode();
        init_shaking_mode();
    }

    private void init_shaking_mode() {
        shakingSwitch = findViewById(R.id.shake_mode_sw);
        shakingAngleButton = findViewById(R.id.shake_mode_btn);
//        shakingAngle = findViewById(R.id.shak_mode_angle);

        shakingAngleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetShakingService(getPower());
            }
        });

        shakingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Is the switch is on?
                boolean on = ((Switch) v).isChecked();
                if (on) {
                    resetShakingService(getPower());
                } else {
                    if (shakingServiceIntent != null)
                        stopService(shakingServiceIntent);
                }
            }
        });
    }

    private String getPower() {
        String power;
        if (shakingPower.getText().toString().isEmpty())
            power = DEFAULT_Shak_POWER;
        else
            power = shakingPower.getText().toString();
        return power;
    }

    private void resetShakingService(String power) {
        if (shakingServiceIntent != null)
            stopService(shakingServiceIntent);
        shakingServiceIntent = new Intent(MainActivity.this, SleepService.class);
        shakingServiceIntent.putExtra(getString(R.string.shakingModeAngle), power);
        startService(shakingServiceIntent);
    }

    private void init_sleeping_mode() {
        sleepingSwitch = findViewById(R.id.sleeping_mode_switch);
        sleepingAngleButton = findViewById(R.id.submit_angle_button);
        sleepingAngle = findViewById(R.id.sleeping_mode_angle);

        sleepingServiceIntent = new Intent(MainActivity.this, SleepService.class);
        sleepingServiceIntent.putExtra(getString(R.string.sleepingModeAngle), getAngle());

        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        sleepingCompName = new ComponentName(this, DeviceAdmin.class);

        sleepingAngleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sleepingSwitch.isChecked()) {
                    sleepingSwitch.performClick();
                }
            }
        });

        sleepingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((Switch) v).isChecked();
                if (on)
                    enable(sleepingCompName);
                else
                    disable(sleepingServiceIntent);
            }
        });
    }

    private double getAngle() {
        double angle;
        if (sleepingAngle.getText().toString().isEmpty())
            angle = DEFAULT_ANGLE;
        else
            angle = Double.parseDouble(sleepingAngle.getText().toString());
        return angle;
    }

    public void enable(ComponentName componentName) {
        boolean active = deviceManger.isAdminActive(sleepingCompName);
        if (active) {
            deviceManger.removeActiveAdmin(componentName);
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivityForResult(intent, SLEEPING_MODE_REQ_CODE);
        }
    }


    public void disable(Intent intent) {
        deviceManger = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        try {
            sleepingCompName = new ComponentName(MainActivity.this, DeviceAdmin.class);
            boolean active = deviceManger.isAdminActive(sleepingCompName);
            if (active)
                deviceManger.removeActiveAdmin(sleepingCompName);
            stopService(intent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SLEEPING_MODE_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                sleepingSwitch.setChecked(true);
                SleepService.devicePolicyManager = deviceManger;
                sleepingServiceIntent = new Intent(MainActivity.this, SleepService.class);
                sleepingServiceIntent.putExtra(getString(R.string.sleepingModeAngle), getAngle());
                startService(sleepingServiceIntent);
            } else {
                sleepingSwitch.setChecked(false);
            }
        } else if (requestCode == SHAKING_MODE_REQ_CODE) {
            shakingSwitch.setChecked(true);
            ShakeService.devicePolicyManager = deviceManger;
            shakingServiceIntent = new Intent(MainActivity.this, SleepService.class);
            startService(shakingServiceIntent);
        }
    }
}
