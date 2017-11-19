package com.firstadie.csftcarroll.b00641329.firstaide.events;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tigh on 10/11/17.
 */

public class RightNow extends Event {

    public RightNow(long currentTime) {
        super("Right Now", currentTime, currentTime);
    }

    @Override
    public int getEventType() {
        return Event.RIGHT_NOW;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(JSON_KEY_TITLE, getTitle());
            jsonObject.put(JSON_KEY_START_TIME, getStartTime());
            jsonObject.put(JSON_KEY_END_TIME, getEndTime());
            jsonObject.put(JSON_KEY_DURATION, getDuration());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public String toJSONString() {
        return toJSONObject().toString();
    }
}
