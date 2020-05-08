package com.pac.sensor;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
//    private Switch sleepingSwitch;
//    private Switch shakingSwitch;
//    private EditText sleepingAngle;
//    private EditText shakingPower;
//
//    private final static int DEFAULT_ANGLE = 10;
//    private final static String DEFAULT_Shak_POWER = "low";
//    private final static int SLEEPING_MODE_REQ_CODE = 1;
//    private final static int SHAKING_MODE_REQ_CODE = 2;
//
//    private Intent sleepingServiceIntent;
//    private Intent shakingServiceIntent;
//
//    private ComponentName sleepingCompName;
//    private ComponentName shakingCompName;
//
//    DevicePolicyManager deviceManger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ServicesData.ALARM_SERVICE_DATA.put("toggleSwitch", "off");
        ServicesData.ALARM_SERVICE_DATA.put("hour", "0");
        ServicesData.ALARM_SERVICE_DATA.put("minute", "0");
        ServicesData.ALARM_SERVICE_DATA.put("speed", "Rotation Speed To Stop Alarm");

        ServicesData.SHAKE_SERVICE_DATA.put("toggleSwitch", "off");
        ServicesData.SHAKE_SERVICE_DATA.put("sensitivity", "Shaking Sensitivity");

        ServicesData.SLEEP_SERVICE_DATA.put("toggleSwitch", "off");
        ServicesData.SLEEP_SERVICE_DATA.put("angle", "Rotation Angle To Turn On Screen");

        Intent intent = new Intent(this, HeavySleepingServiceActivity.class);
        startActivity(intent);
//        init_sleeping_mode();
//        init_shaking_mode();

    }

//    private void init_shaking_mode() {
//        shakingSwitch = findViewById(R.id.shake_mode_sw);
//        Button shakeBtn = findViewById(R.id.shake_mode_btn);
//        shakingPower = findViewById(R.id.shake_mode_tv);
//
//        shakingServiceIntent = new Intent(MainActivity.this, ShakeService.class);
//
//        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        shakingCompName = new ComponentName(this, DeviceAdmin.class);
//
//        shakeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (shakingSwitch.isChecked()) {
//                    shakingSwitch.performClick();
//                }
//            }
//        });
//
//        shakingSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean on = ((Switch) v).isChecked();
//                if (on)
//                    enable(shakingCompName, SHAKING_MODE_REQ_CODE);
//                else
//                    disable(shakingServiceIntent);
//            }
//        });
//    }
//
//    private String getPower() {
//        String power;
//        if (shakingPower.getText().toString().isEmpty())
//            power = DEFAULT_Shak_POWER;
//        else
//            power = shakingPower.getText().toString();
//        return power;
//    }
//
//    private void init_sleeping_mode() {
//        sleepingSwitch = findViewById(R.id.sleeping_mode_switch);
//        Button sleepingAngleButton = findViewById(R.id.submit_angle_button);
//        sleepingAngle = findViewById(R.id.sleeping_mode_angle);
//
//        sleepingServiceIntent = new Intent(MainActivity.this, SleepService.class);
//        sleepingServiceIntent.putExtra(getString(R.string.sleepingModeAngle), getAngle());
//
//        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        sleepingCompName = new ComponentName(this, DeviceAdmin.class);
//
//        sleepingAngleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (sleepingSwitch.isChecked()) {
//                    sleepingSwitch.performClick();
//                }
//            }
//        });
//
//        sleepingSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean on = ((Switch) v).isChecked();
//                if (on)
//                    enable(sleepingCompName, SLEEPING_MODE_REQ_CODE);
//                else
//                    disable(sleepingServiceIntent);
//            }
//        });
//    }
//
//    private double getAngle() {
//        double angle;
//        if (sleepingAngle.getText().toString().isEmpty())
//            angle = DEFAULT_ANGLE;
//        else
//            angle = Double.parseDouble(sleepingAngle.getText().toString());
//        return angle;
//    }
//
//    public void enable(ComponentName componentName, int requestCode) {
//        boolean active = deviceManger.isAdminActive(componentName);
//        if (active) {
//            deviceManger.removeActiveAdmin(componentName);
//        } else {
//            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
//            startActivityForResult(intent, requestCode);
//        }
//    }
//
//
//    public void disable(Intent intent) {
//        deviceManger = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
//        try {
//            sleepingCompName = new ComponentName(MainActivity.this, DeviceAdmin.class);
//            boolean active = deviceManger.isAdminActive(sleepingCompName);
//            if (active)
//                deviceManger.removeActiveAdmin(sleepingCompName);
//            stopService(intent);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SLEEPING_MODE_REQ_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                sleepingSwitch.setChecked(true);
//                SleepService.devicePolicyManager = deviceManger;
//                sleepingServiceIntent = new Intent(MainActivity.this, SleepService.class);
//                sleepingServiceIntent.putExtra(getString(R.string.sleepingModeAngle), getAngle());
//                startService(sleepingServiceIntent);
//            } else {
//                sleepingSwitch.setChecked(false);
//            }
//        } else if (requestCode == SHAKING_MODE_REQ_CODE) {
//            shakingSwitch.setChecked(true);
//            shakingServiceIntent = new Intent(MainActivity.this, ShakeService.class);
//            shakingServiceIntent.putExtra(getString(R.string.shakingModePower), getPower());
//            startService(shakingServiceIntent);
//        }
//    }
}
