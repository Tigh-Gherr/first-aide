package com.firstadie.csftcarroll.b00641329.firstaide;

/**
 * Created by tigh on 03/11/17.
 */

public class Event {
    protected String mTitle;
    protected int mDuration;

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
}
