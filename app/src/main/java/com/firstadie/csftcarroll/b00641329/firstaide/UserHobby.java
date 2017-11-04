package com.firstadie.csftcarroll.b00641329.firstaide;

/**
 * Created by tigh on 03/11/17.
 */

public class UserHobby extends Event {
    private int mPriority;
    private int mId;

    public UserHobby(String title, int duration, int priority, int id) {
        super(title, duration);
        mPriority = priority;
        mId = id;
    }

    public int getPriority() {
        return mPriority;
    }

    public int getId() {
        return mId;
    }
}
