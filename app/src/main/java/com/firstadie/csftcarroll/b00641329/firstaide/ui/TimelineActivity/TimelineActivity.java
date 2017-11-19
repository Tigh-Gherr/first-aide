package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.calendartools.CalendarHelper;
import com.firstadie.csftcarroll.b00641329.firstaide.events.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.ui.WeatherActivity.WeatherActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

public class TimelineActivity extends AppCompatActivity
        implements SensorEventListener, OnArrowPressedListener, AccessibleActivity<Event> {

    private static float SHAKE_THRESHOLD_GRAVITY = 2.3f;

    private Sensor mSensor;
    private SensorManager mSensorManager;
    private long mLastTimeRegistered;

    private List<Event> mCalendarEvents;

    private Vibrator mVibrator;

    private SlidingUpPanelLayout mTimelinePanelLayout;
    private ViewPager mCalendarEventViewPager;

    private AccessibleFragment<Event> mTimelineAccessibleFragment;

    private void initViewPager() {
        CalendarHelper helper = new CalendarHelper(TimelineActivity.this);
        mCalendarEvents = helper.getCalendarEvents();

        FragmentManager fm = getSupportFragmentManager();
        final AccessibleFragmentStatePagerAdapter adapter = new AccessibleFragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return EventDetailFragment.newInstance(mCalendarEvents.get(position));
            }

            @Override
            public int getCount() {
                return mCalendarEvents != null ? mCalendarEvents.size() : 0;
            }
        };

        mCalendarEventViewPager.setAdapter(adapter);

        mCalendarEventViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                position = mCalendarEventViewPager.getCurrentItem();

                float panelOffset = mTimelinePanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ? 1f : 0f;

                AccessibleFragment previousFragment = adapter.getAccessibleFragment(position - 1);
                AccessibleFragment currentFragment = adapter.getAccessibleFragment(position);
                AccessibleFragment nextFragment = adapter.getAccessibleFragment(position + 1);

                if(previousFragment != null) {
                    previousFragment.sendData(panelOffset);
                }

                if(currentFragment != null) {
                    currentFragment.sendData(panelOffset);
                }

                if(nextFragment != null) {
                    nextFragment.sendData(panelOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(mTimelineAccessibleFragment != null) {
                    Event e = mCalendarEvents.get(position);
                    mTimelineAccessibleFragment.sendData(e);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTimelinePanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                int position = mCalendarEventViewPager.getCurrentItem();
                AccessibleFragment fragment = adapter.getAccessibleFragment(position);
                fragment.sendData(slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTimelinePanelLayout = findViewById(R.id.panellayout_timeline);
        mCalendarEventViewPager = findViewById(R.id.viewpager_calendarEventDetails);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof TimelineActivityFragment) {
            mTimelineAccessibleFragment = (AccessibleFragment) fragment;
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

        initViewPager();
    }

    @Override
    public void onArrowPressed() {
        if(mTimelinePanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mTimelinePanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        } else {
            mTimelinePanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    public void sendData(Event data) {
        int position = -1;
        for(int i = 0; i < mCalendarEvents.size(); i++) {
            Event event = mCalendarEvents.get(i);
            if(event.getId() == data.getId()) {
                position = i;
                break;
            }
        }

        if(position != -1) {
            mCalendarEventViewPager.setCurrentItem(position, true);
            mTimelinePanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }

        Log.d(getClass().getSimpleName(), "Position: " + position);
    }
}