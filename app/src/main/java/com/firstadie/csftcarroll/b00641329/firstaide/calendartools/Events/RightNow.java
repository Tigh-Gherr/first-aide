package com.firstadie.csftcarroll.b00641329.firstaide.calendartools.Events;

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
            jsonObject.put("Title", getTitle());
            jsonObject.put("Start time", getStartTime());
            jsonObject.put("End time", getEndTime());
            jsonObject.put("Duration", getDuration());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public String toJSONString() {
        return null;
    }
}
