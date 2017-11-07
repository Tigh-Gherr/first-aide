package com.firstadie.csftcarroll.b00641329.firstaide.calendartools;

import com.firstadie.csftcarroll.b00641329.firstaide.Event;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public boolean isCalendarEvent() {
        return false;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", getTitle());
            jsonObject.put("duration", getDuration());
            jsonObject.put("priority", mPriority);
            jsonObject.put("id", mId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public String toJSONString() {
        return toJSONObject().toString();
    }
}
