package com.pac.sensor;

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

import androidx.appcompat.app.AppCompatActivity;

import com.pac.sensor.service.ShakeService;
import com.pac.sensor.service.SleepService;

public class MainActivity extends AppCompatActivity {
    private Switch sleepingSwitch;
    private EditText sleepingAngle;
    private Button sleepingAngleButton;
    private final static int DEFAULT_ANGLE = 10;
    private Intent sleepingServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_sleeping_mode();

        ShakeService shakeService = new ShakeService();
        shakeService.startService(this, "low");
    }

    private void init_sleeping_mode(){
        sleepingSwitch = findViewById(R.id.sleeping_mode_switch);
        sleepingAngleButton = findViewById(R.id.submit_angle_button);
        sleepingAngle = findViewById(R.id.sleeping_mode_angle);

        sleepingAngleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                resetSleepingService(getAngle());
            }
        });

        sleepingSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Is the switch is on?
                boolean on = ((Switch) v).isChecked();
                if(on)
                {
                    resetSleepingService(getAngle());
                }
                else
                {
                    if (sleepingServiceIntent != null)
                        stopService(sleepingServiceIntent);
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
}
