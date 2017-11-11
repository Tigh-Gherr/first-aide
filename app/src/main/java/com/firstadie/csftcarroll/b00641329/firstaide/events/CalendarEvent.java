package com.firstadie.csftcarroll.b00641329.firstaide.events;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tigh on 07/11/17.
 */

public class CalendarEvent extends Event {

    private int mCalendarId;
    private String mDescription;
    private String mEventLocation;

    public CalendarEvent(int calendarId, String title, String description, long startTime, long endTime, String eventLocation) {
        super(title, startTime, endTime);
        mCalendarId = calendarId;
        mDescription = description;
        mEventLocation = eventLocation;
    }

    public int getCalendarId() {
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
            jsonObject.put("calendar_id", mCalendarId);
            jsonObject.put("title", getTitle());
            jsonObject.put("description", mDescription);
            jsonObject.put("start_time", getStartTime());
            jsonObject.put("end_time", getEndTime());
            jsonObject.put("event_location", mEventLocation);
            jsonObject.put("duration", getDuration());
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
