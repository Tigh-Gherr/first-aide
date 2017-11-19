package com.firstadie.csftcarroll.b00641329.firstaide.events;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by tigh on 07/11/17.
 */

public class CalendarEvent extends Event implements Serializable {

    public static final String JSON_KEY_CALENDAR_ID = "calendar_id";
    public static final String JSON_KEY_DESCRIPTION = "description";
    public static final String JSON_KEY_EVENT_LOCATION = "event_location";

    private int mCalendarId;
    private String mDescription;
    private String mEventLocation;

    public CalendarEvent(int calendarId, String title, String description, long startTime, long endTime, String eventLocation) {
        super(title, startTime, endTime);
        mCalendarId = calendarId;
        mDescription = description;
        mEventLocation = eventLocation;
    }

    @Override
    public int getId() {
        return mCalendarId;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getEventLocation() {
        return mEventLocation;
    }

    @Override
    public int getEventType() {
        return Event.CALENDAR_EVENT;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSON_KEY_CALENDAR_ID, mCalendarId);
            jsonObject.put(JSON_KEY_TITLE, getTitle());
            jsonObject.put(JSON_KEY_DESCRIPTION, mDescription);
            jsonObject.put(JSON_KEY_START_TIME, getStartTime());
            jsonObject.put(JSON_KEY_END_TIME, getEndTime());
            jsonObject.put(JSON_KEY_EVENT_LOCATION, mEventLocation);
            jsonObject.put(JSON_KEY_DURATION, getDuration());
        } catch (JSONException e) {
            // Do nothing
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public String toJSONString() {
        return toJSONObject().toString();
    }
}
