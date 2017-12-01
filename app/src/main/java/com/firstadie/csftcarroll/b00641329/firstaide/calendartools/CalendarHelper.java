package com.firstadie.csftcarroll.b00641329.firstaide.calendartools;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.firstadie.csftcarroll.b00641329.firstaide.events.CalendarEvent;
import com.firstadie.csftcarroll.b00641329.firstaide.events.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.events.RightNow;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigh on 07/11/17.
 */

public class CalendarHelper {

    private Context mContext;

    private String[] mCalendarFields;
    private String[] mEventFields;
    private static final String ALL_DAY_FALSE_QUERY = CalendarContract.Instances.ALL_DAY + " = 0";

    public CalendarHelper(Context context) {
        mContext = context;

        mCalendarFields = new String[]{
                CalendarContract.Calendars._ID,
        };

        mEventFields = new String[]{
                CalendarContract.Instances.CALENDAR_ID,
                CalendarContract.Instances.EVENT_ID,
                CalendarContract.Instances.TITLE,
                CalendarContract.Instances.DESCRIPTION,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.END,
                CalendarContract.Instances.EVENT_LOCATION
        };

    }

    public List<Event> getCalendarEvents() {
        List<Event> events = new ArrayList<>();

        List<String> ids = getCalendars();

        parseCalendars(ids, events);

        return events;
    }

    public List<Event> getCalendarEventsWithRightNow() {
        List<Event> events = new ArrayList<>();

        List<String> ids = getCalendars();

        parseCalendars(ids, events);

        findRightNow(events);

        return events;
    }

    private void parseCalendars(List<String> ids, List<Event> events) {
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();

        ContentUris.appendId(builder, CalendarUtils.getStartOfDayInMillis());
        ContentUris.appendId(builder, CalendarUtils.getEndOfDayInMillis());

        Log.d(getClass().getSimpleName(), builder.build().toString());
        StringBuilder clauseBuilder = new StringBuilder("(");
        String condition = "";
        for (int i = 0; i < ids.size(); i++) {
            clauseBuilder.append(condition)
                    .append(CalendarContract.Instances.CALENDAR_ID).append(" = ?");
            condition = " OR ";
        }
        clauseBuilder.append(") AND ").append(ALL_DAY_FALSE_QUERY);

        String clause = clauseBuilder.toString();
        Cursor eventCursor = mContext.getContentResolver().query(
                builder.build(),
                mEventFields,
                clause,
                ids.toArray(new String[ids.size()]),
                CalendarContract.Instances.BEGIN
        );

        while (eventCursor.moveToNext()) {
            int index = -1;
            int calendarId = eventCursor.getInt(++index);
            int eventId = eventCursor.getInt(++index);
            String title = eventCursor.getString(++index);
            String description = eventCursor.getString(++index);
            long startDate = eventCursor.getLong(++index);
            long endDate = eventCursor.getLong(++index);
            String eventLocation = eventCursor.getString(++index);

            Event event = new CalendarEvent(
                    calendarId,
                    eventId,
                    title,
                    description,
                    startDate,
                    endDate,
                    eventLocation
            );

            events.add(event);
        }

        eventCursor.close();
    }

    private void findRightNow(List<Event> calendarEvents) {
        long currentTime = System.currentTimeMillis();

        for(int i = 0; i < calendarEvents.size(); i++) {
            Event e = calendarEvents.get(i);
            if(currentTime < e.getStartTime()) {
                if (i == 0) {
                    calendarEvents.add(0, new RightNow(currentTime));
                    return;
                } else {
                    Event previousEvent = calendarEvents.get(i - 1);
                    if(currentTime > previousEvent.getEndTime()) {
                        calendarEvents.add(i, new RightNow(currentTime));
                        return;
                    }
                }
            }
        }

        if(currentTime > calendarEvents.get(calendarEvents.size() - 1).getEndTime()) {
            calendarEvents.add(new RightNow(currentTime));
        }
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
            calendarIds.add(id);
        }

        cursor.close();

        return calendarIds;
    }
}
