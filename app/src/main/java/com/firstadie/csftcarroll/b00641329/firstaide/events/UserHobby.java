package com.firstadie.csftcarroll.b00641329.firstaide.events;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tigh on 03/11/17.
 */

public class UserHobby extends Event {
    private int mPriority;
    private int mId;
    private int mDuration;

    public UserHobby(String title, int duration, int priority, int id) {
        super(title);
        mDuration = duration;
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
    public int getDuration() {
        return mDuration;
    }

    @Override
    public int getEventType() {
        return Event.HOBBY;
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
