package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.ui.WeatherActivity.WeatherActivity;

public class TimelineActivity extends AppCompatActivity implements SensorEventListener {

    private static float SHAKE_THRESHOLD_GRAVITY = 2.3f;

    private Sensor mSensor;
    private SensorManager mSensorManager;
    private long mLastTimeRegistered;

    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }


    private void processDeviceMovement(SensorEvent event) {
        long currentTime = System.currentTimeMillis();

        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt((gX * gX) + (gY * gY) + (gZ * gZ));

        if (gForce >= SHAKE_THRESHOLD_GRAVITY && currentTime - mLastTimeRegistered > 400) {
            mLastTimeRegistered = currentTime;

            mVibrator.vibrate(300);
            startActivity(new Intent(TimelineActivity.this, WeatherActivity.class));
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            processDeviceMovement(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLastTimeRegistered = System.currentTimeMillis();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(
                TimelineActivity.this,
                mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}