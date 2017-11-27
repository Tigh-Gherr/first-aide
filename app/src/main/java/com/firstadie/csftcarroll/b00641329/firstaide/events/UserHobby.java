package com.firstadie.csftcarroll.b00641329.firstaide.events;

import com.firstadie.csftcarroll.b00641329.firstaide.UserSingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tigh on 03/11/17.
 */

public class UserHobby extends Event {

    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_USER_ID = "user_id";
    public static final String JSON_KEY_PRIORITY = "priority";

    private int mId;
    private int mPriority;
    private int mDuration;

    public UserHobby() {
        super("");
        mDuration = 0;
        mPriority = 1;
        mId = -1;
    }

    public UserHobby(String title, int duration, int priority, int id) {
        super(title);
        mDuration = duration;
        mPriority = priority;
        mId = id;
    }

    public int getPriority() {
        return mPriority;
    }

    @Override
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

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public int getUserId() {
        return UserSingleton.get().getUser().getId();
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSON_KEY_ID, mId);
            jsonObject.put(JSON_KEY_USER_ID, getUserId());
            jsonObject.put(JSON_KEY_TITLE, getTitle());
            jsonObject.put(JSON_KEY_DURATION, getDuration());
            jsonObject.put(JSON_KEY_PRIORITY, mPriority);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
