package com.pac.sensor;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pac.sensor.service.ShakeService;
import com.pac.sensor.service.SleepService;

public class MainActivity extends AppCompatActivity {
    private Switch sleepingSwitch;
    private EditText sleepingAngle;
    private Button sleepingAngleButton;
    private final static int DEFAULT_ANGLE = 10;
    private Intent sleepingServiceIntent;

    private ComponentName compName ;
    DevicePolicyManager deviceManger ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_sleeping_mode();

//        ShakeService shakeService = new ShakeService();
//        shakeService.startService(this, "low");
    }

    private void init_sleeping_mode(){
        sleepingSwitch = findViewById(R.id.sleeping_mode_switch);
        sleepingAngleButton = findViewById(R.id.submit_angle_button);
        sleepingAngle = findViewById(R.id.sleeping_mode_angle);

        deviceManger = (DevicePolicyManager)
                getSystemService(Context. DEVICE_POLICY_SERVICE ) ;
        compName = new ComponentName( this, DeviceAdmin. class ) ;
//        boolean active = deviceManger .isAdminActive( compName ) ;
//        if (active) {
//            btnEnable .setText( "Disable" ) ;
//            btnLock .setVisibility(View. VISIBLE ) ;
//        } else {
//            btnEnable .setText( "Enable" ) ;
//            btnLock .setVisibility(View. GONE ) ;
//        }


        sleepingAngleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                resetSleepingService(getAngle());
                enable();
            }
        });

        sleepingSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean on = ((Switch) v).isChecked();
                if(on)
                {
//                    resetSleepingService(getAngle());
                    enable();
                }
                else
                {
                    disable();
//                    if (sleepingServiceIntent != null)
//                        stopService(sleepingServiceIntent);
                }
            }
        });
    }

    private double getAngle(){
        double angle;
        if (sleepingAngle.getText().toString().isEmpty())
            angle = DEFAULT_ANGLE;
        else
            angle = Double.parseDouble(sleepingAngle.getText().toString());
        return angle;
    }

    private void resetSleepingService(double angle){
        if (sleepingServiceIntent != null)
            stopService(sleepingServiceIntent);
        sleepingServiceIntent = new Intent(MainActivity.this, SleepService.class);
        sleepingServiceIntent.putExtra(getString(R.string.sleepingModeAngle), angle);
        startService(sleepingServiceIntent);
    }

    public void enable () {
        boolean active = deviceManger .isAdminActive( compName ) ;
        if (active) {
            deviceManger .removeActiveAdmin( compName ) ;
        } else {
            System.out.println("enableee");
            Intent intent = new Intent(DevicePolicyManager. ACTION_ADD_DEVICE_ADMIN ) ;
            intent.putExtra(DevicePolicyManager. EXTRA_DEVICE_ADMIN , compName ) ;
            intent.putExtra(DevicePolicyManager. EXTRA_ADD_EXPLANATION , "You should enable the app!" ) ;
            startActivityForResult(intent , 11 ) ;
        }
    }

    public void disable(){
        deviceManger = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        try {
            compName = new ComponentName(MainActivity.this, DeviceAdmin.class);
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.removeActiveAdmin(compName);
                stopService(sleepingServiceIntent);
            } else {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                stopService(sleepingServiceIntent);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void lockPhone (View view) {
        deviceManger .lockNow() ;
    }

//    private void stopSleepingService(){
//        componentName = new ComponentName(MainActivity.this, DeviceAdmin.class);
//        boolean active = devicePolicyManager.isAdminActive(componentName);
//        if (active) {
//            ((DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE)).removeActiveAdmin(componentName);
//            stopService(sleepingServiceIntent);
//        } else {
//            stopService(sleepingServiceIntent);
//        }
//    }
private double angle = 10;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("hereee");
        if (requestCode == 11) {
            System.out.println("hoora");
            if (resultCode == Activity.RESULT_OK) {
                System.out.println("thereee");
                sleepingSwitch.setChecked(true);
                SleepService.devicePolicyManager = deviceManger;
                sleepingServiceIntent.putExtra(getString(R.string.sleepingModeAngle), angle);
                startService(sleepingServiceIntent);
            } else {
                sleepingSwitch.setChecked(false);
            }
        }
    }
}
