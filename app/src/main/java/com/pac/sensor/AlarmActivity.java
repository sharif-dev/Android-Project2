package com.pac.sensor;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity
{

    private MediaPlayer player;
    private TextView time;
    private CountDownTimer countDownTimer;
    public long timeLeftInMilliSeconds;
    private Gyroscope gyroscope;
    private Double speed;
    private Vibrator vibrator;
    private String ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        time = findViewById(R.id.countdown_timer);

        speed = Double.parseDouble(getIntent().getStringExtra("SPEED"));
        ringtone = getIntent().getStringExtra("RINGTONE");
        timeLeftInMilliSeconds = 600000L;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(timeLeftInMilliSeconds);
        gyroscope = new Gyroscope(this);

        switch (ringtone)
        {
            case "Ringtone 1":
                player = MediaPlayer.create(this, R.raw.ringtone1);
                break;
            case "Ringtone 2":
                player = MediaPlayer.create(this, R.raw.ringtone2);
                break;
            case "Ringtone 3":
                player = MediaPlayer.create(this, R.raw.ringtone3);
                break;
            case "Ringtone 4":
                player = MediaPlayer.create(this, R.raw.ringtone4);
                break;
        }
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.start();
            }
        });

        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                timeLeftInMilliSeconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish()
            {
                player.release();
                vibrator.cancel();
                finish();
            }
        }.start();

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                if (Math.abs(rz) > Math.abs(speed))
                {
                    player.release();
                    vibrator.cancel();
                    Toast.makeText(AlarmActivity.this, "Alarm turned off", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void updateTimer()
    {
        int minutes = (int) timeLeftInMilliSeconds / 60000;
        int seconds = (int) timeLeftInMilliSeconds % 60000 / 1000;
        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;
        time.setText(timeLeftText);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gyroscope.register();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gyroscope.unregister();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
