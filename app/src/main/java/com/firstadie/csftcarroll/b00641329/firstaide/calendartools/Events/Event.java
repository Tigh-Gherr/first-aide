package com.firstadie.csftcarroll.b00641329.firstaide.calendartools.Events;

import com.firstadie.csftcarroll.b00641329.firstaide.utils.CalendarUtils;

import org.json.JSONObject;

/**
 * Created by tigh on 03/11/17.
 */

public abstract class Event {

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

    public abstract JSONObject toJSONObject();

    public abstract String toJSONString();
}
