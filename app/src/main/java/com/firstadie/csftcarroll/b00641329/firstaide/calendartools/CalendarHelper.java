package com.firstadie.csftcarroll.b00641329.firstaide.calendartools;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.firstadie.csftcarroll.b00641329.firstaide.events.CalendarEvent;
import com.firstadie.csftcarroll.b00641329.firstaide.events.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.events.RightNow;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.CalendarUtils;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.LoginUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigh on 07/11/17.
 */

public class CalendarHelper {

    private Context mContext;

    private String[] mCalendarFields;
    private String[] mEventFields;
    private static final String EVENT_QUERY = CalendarContract.Instances.CALENDAR_ID + " = ?";

    public CalendarHelper(Context context) {
        mContext = context;

        mCalendarFields = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };

        mEventFields = new String[]{
                CalendarContract.Instances.EVENT_ID,
                CalendarContract.Instances.TITLE,
                CalendarContract.Instances.DESCRIPTION,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.END,
                CalendarContract.Instances.EVENT_LOCATION
        };

    }

    public List<Event> getCalendarEventsWithRightNow() {
        List<Event> calendarEvents = new ArrayList<>();

        List<String> calendarIds = getCalendars();
        for (String id : calendarIds) {
            parseCalendar(id, calendarEvents);
        }

        findRightNow(calendarEvents);

        return calendarEvents;
    }

    private void findRightNow(List<Event> calendarEvents) {
        long previousTime = calendarEvents.size() > 0 ?
                calendarEvents.get(0).getEndTime() :
                CalendarUtils.getDayBracketInMillis(true);

        long currentTime = System.currentTimeMillis();

        boolean rightNowSet = false;
        for(Event e : calendarEvents) {
            if(previousTime < currentTime && currentTime < e.getStartTime()) {
                calendarEvents.add(new RightNow(currentTime));
                rightNowSet = true;
                break;
            }
        }

        if(!rightNowSet) {
            calendarEvents.add(new RightNow(currentTime));
        }
    }

    public List<Event> getCalendarEvents() {
        List<Event> calendarEvents = new ArrayList<>();

        List<String> calendarIds = getCalendars();
        for (String id : calendarIds) {
            parseCalendar(id, calendarEvents);
        }

        return calendarEvents;
    }

    @SuppressLint("MissingPermission")
    private List<String> getCalendars() {
        Cursor cursor = mContext.getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                mCalendarFields,
                null,
                null,
                null
        );

        List<String> calendarIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String displayName = cursor.getString(1);

            if (LoginUtils.isValidEmail(displayName)) {
                calendarIds.add(id);
            }
        }

        cursor.close();

        return calendarIds;
    }

    private void parseCalendar(String id, List<Event> calendarEvents) {
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();

        ContentUris.appendId(builder, CalendarUtils.getDayBracketInMillis(true));
        ContentUris.appendId(builder, CalendarUtils.getDayBracketInMillis(false));

        Cursor eventCursor = mContext.getContentResolver().query(
                builder.build(),
                mEventFields,
                EVENT_QUERY,
                new String[]{id},
                CalendarContract.Instances.BEGIN
        );

        while (eventCursor.moveToNext()) {
            int calendarId = eventCursor.getInt(0);
            String title = eventCursor.getString(1);
            String description = eventCursor.getString(2);
            long startDate = eventCursor.getLong(3);
            long endDate = eventCursor.getLong(4);
            String eventLocation = eventCursor.getString(5);

            Event event = new CalendarEvent(
                    calendarId,
                    title,
                    description,
                    startDate,
                    endDate,
                    eventLocation
            );

            calendarEvents.add(event);
        }

        eventCursor.close();
    }

}
