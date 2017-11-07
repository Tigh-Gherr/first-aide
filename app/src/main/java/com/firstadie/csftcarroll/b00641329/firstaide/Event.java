package com.firstadie.csftcarroll.b00641329.firstaide;

import org.json.JSONObject;

/**
 * Created by tigh on 03/11/17.
 */

public abstract class Event {
    private String mTitle;
    private int mDuration;

    public Event(String title, int duration) {
        mTitle = title;
        mDuration = duration;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getDuration() {
        return mDuration;
    }

    protected void setDuration(int duration) {
        mDuration = duration;
    }

    public abstract boolean isCalendarEvent();

    public abstract JSONObject toJSONObject();

    public abstract String toJSONString();
}
