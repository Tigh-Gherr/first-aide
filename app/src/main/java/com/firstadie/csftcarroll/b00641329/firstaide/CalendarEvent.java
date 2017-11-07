package com.firstadie.csftcarroll.b00641329.firstaide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by tigh on 07/11/17.
 */

public class CalendarEvent extends Event {

    private int mCalendarId;
    private String mDescription;
    private long mStartTime;
    private long mEndTime;
    private String mEventLocation;

    public CalendarEvent(int calendarId, String title, String description, long startTime, long endTime, String eventLocation) {
        super(title, 0);
        mCalendarId = calendarId;
        mStartTime = startTime;
        mEndTime = endTime;
        mDescription = description;
        mEventLocation = eventLocation;

        setDuration((int) TimeUnit.MINUTES.convert(mEndTime - mStartTime, TimeUnit.MILLISECONDS));
    }

    public int getCalendarId() {
        return mCalendarId;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getEventLocation() {
        return mEventLocation;
    }

    public Date getStartDate() {
        return new Date(mStartTime);
    }

    public Date getEndDate() {
        return new Date(mEndTime);
    }

    @Override
    public boolean isCalendarEvent() {
        return true;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calendar_id", mCalendarId);
            jsonObject.put("title", getTitle());
            jsonObject.put("description", mDescription);
            jsonObject.put("start_time", mStartTime);
            jsonObject.put("end_time", mEndTime);
            jsonObject.put("event_location", mEventLocation);
        } catch (JSONException e) {
            // Do nothing
            e.printStackTrace();
        }

        return jsonObject;
    }
}
