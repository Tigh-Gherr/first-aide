package com.firstadie.csftcarroll.b00641329.firstaide.events;

import com.firstadie.csftcarroll.b00641329.firstaide.utils.CalendarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by tigh on 03/11/17.
 */

public abstract class Event implements Serializable {

    public static final String JSON_KEY_TITLE = "title";
    public static final String JSON_KEY_START_TIME = "start_time";
    public static final String JSON_KEY_END_TIME = "end_time";
    public static final String JSON_KEY_DURATION = "duration";

    public static final int RIGHT_NOW = -1;
    public static final int HOBBY = 0;
    public static final int CALENDAR_EVENT = 1;

    private String mTitle;
    private long mStartTime;
    private long mEndTime;

    public Event(String title) {
        mTitle = title;
        mStartTime = 0;
        mEndTime = 0;
    }

    public Event(String title, long startTime, long endTime) {
        mTitle = title;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public abstract int getEventType();

    public int getDuration() {
        return CalendarUtils.calculateDifferenceInMinutes(mStartTime, mEndTime);
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public void setEndTime(long endTime) {
        mEndTime = endTime;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public boolean isCurrentEvent() {
        switch (this.getEventType()) {
            case RIGHT_NOW:
                return true;
            case HOBBY:
                return false;
            case CALENDAR_EVENT:
                long currentTime = System.currentTimeMillis();
                return this.getStartTime() < currentTime && currentTime < this.getEndTime();
        }

        return false;
    }

    public abstract int getId();

    public abstract JSONObject toJSONObject();

    public abstract String toJSONString();
}
