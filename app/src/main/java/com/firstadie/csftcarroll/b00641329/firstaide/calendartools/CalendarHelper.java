package com.firstadie.csftcarroll.b00641329.firstaide.calendartools;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

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

    public List<CalendarEvent> getCalendarEvents() {
        List<CalendarEvent> calendarEvents = new ArrayList<>();

        Cursor cursor = createCursor();
        while (cursor.moveToNext()) {
            int calendarId = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            long startDate = cursor.getLong(3);
            long endDate = cursor.getLong(4);
            String eventLocation = cursor.getString(5);

            calendarEvents.add(new CalendarEvent(
                    calendarId,
                    title,
                    description,
                    startDate,
                    endDate,
                    eventLocation
                )
            );
        }

        return calendarEvents;
    }

    @SuppressLint("MissingPermission")
    private Cursor createCursor() {
        Cursor cursor = mContext.getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                mCalendarFields,
                null,
                null,
                null
        );

        String[] calendarId = null;

        while (calendarId == null && cursor.moveToNext()) {
            String id = cursor.getString(0);
            String displayName = cursor.getString(1);

            String message = id + " " + displayName;
            Log.d(getClass().getSimpleName(), message);

            if (LoginUtils.isValidEmail(displayName)) {
                calendarId = new String[]{
                        cursor.getString(0)
                };
            }
        }


        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();

        ContentUris.appendId(builder, CalendarUtils.getDayBracketInMillis(true));
        ContentUris.appendId(builder, CalendarUtils.getDayBracketInMillis(false));

        return mContext.getContentResolver().query(
                builder.build(),
                mEventFields,
                EVENT_QUERY,
                calendarId,
                CalendarContract.Instances.BEGIN
        );
    }

}
