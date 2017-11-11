package com.firstadie.csftcarroll.b00641329.firstaide.ui.ShakeSenstiiveActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firstadie.csftcarroll.b00641329.firstaide.api.API;
import com.firstadie.csftcarroll.b00641329.firstaide.api.WeatherAPI;
import com.firstadie.csftcarroll.b00641329.firstaide.location.LocationSingleton;

/**
 * Created by tigh on 10/11/17.
 */

public abstract class ShakeSensitiveAppCompatActivity extends AppCompatActivity implements SensorEventListener {

    private static float SHAKE_THRESHOLD_GRAVITY = 2.3f;

    private Sensor mSensor;
    private SensorManager mSensorManager;

    private long mLastTimeRegistered;

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

        if(gForce >= ShakeSensitiveAppCompatActivity.SHAKE_THRESHOLD_GRAVITY) {
            if(currentTime - mLastTimeRegistered > 400) {
                mLastTimeRegistered = currentTime;

                String lat = String.valueOf(LocationSingleton.get().getLatitude());
                String lon = String.valueOf(LocationSingleton.get().getLongitude());
                API api = new WeatherAPI();

                api.addParam("lat", lat);
                api.addParam("lon", lon);
                api.query();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLastTimeRegistered = System.currentTimeMillis();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(
                ShakeSensitiveAppCompatActivity.this,
                mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            processDeviceMovement(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
