package com.firstadie.csftcarroll.b00641329.firstaide.calendartools;

import com.firstadie.csftcarroll.b00641329.firstaide.OnEndpointQueryCompleteListener;
import com.firstadie.csftcarroll.b00641329.firstaide.api.GooglePlace;
import com.firstadie.csftcarroll.b00641329.firstaide.api.GooglePlacesDirectionsAPI;
import com.firstadie.csftcarroll.b00641329.firstaide.events.CalendarEvent;
import com.firstadie.csftcarroll.b00641329.firstaide.events.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.events.UserHobby;
import com.firstadie.csftcarroll.b00641329.firstaide.location.LocationSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.CalendarUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by tigh on 07/11/17.
 */

public class TimelinePlanner {

    public interface OnPlanningFinishedListener {
        void onPlanningFinished(List<Event> timelineEvents);
    }

    private List<Event> mCalendarEvents;
    private List<UserHobby> mUserHobbies;
    private OnPlanningFinishedListener mOnPlanningFinishedListener;

    public TimelinePlanner(List<Event> calendarEvents, List<UserHobby> userHobbies) {
        mCalendarEvents = calendarEvents;
        mUserHobbies = userHobbies;
    }

    public void setOnPlanningFinishedListener(OnPlanningFinishedListener onPlanningFinishedListener) {
        mOnPlanningFinishedListener = onPlanningFinishedListener;
    }

    public void planTimelineBeta(CalendarEvent nextEvent) {

        final GooglePlacesDirectionsAPI api = new GooglePlacesDirectionsAPI();
        String origin = LocationSingleton.get().getLatitude() + "," +
                LocationSingleton.get().getLongitude();
        api.addParam("origin", origin);
        api.addParam("destination", nextEvent.getEventLocation());
        final CalendarEvent finalNextEvent = nextEvent;
        api.setOnEndpointQueryCompleteListener(new OnEndpointQueryCompleteListener() {
            @Override
            public void onQueryComplete(String result) throws JSONException {
                GooglePlace googlePlace = api.parse(result);
                List<Event> timelineEvents = planTimelineWithNextEvent(finalNextEvent, googlePlace);
                if (mOnPlanningFinishedListener != null) {
                    mOnPlanningFinishedListener.onPlanningFinished(timelineEvents);
                }
            }
        });

        api.query();
    }

    public List<Event> planTimelineWithNextEvent(CalendarEvent nextEvent, GooglePlace googlePlace) {
        List<Event> timelineEvents = new ArrayList<>();
        long previousEndTime = CalendarUtils.getStartOfDayInMillis();

        for (Event event : mCalendarEvents) {
            if (event == nextEvent) {
                bridgeEvents(event, previousEndTime, timelineEvents, googlePlace);
            } else {
                bridgeEvents(event, previousEndTime, timelineEvents);
            }

            timelineEvents.add(event);
            previousEndTime = event.getEndTime();
        }

        int freeTime = CalendarUtils.calculateDifferenceInMinutes(
                mCalendarEvents.get(mCalendarEvents.size() - 1).getEndTime(),
                CalendarUtils.getEndOfDayInMillis()
        );
        bridgeFreeTime(freeTime, timelineEvents);

        return timelineEvents;
    }

    public void planTimeline() {
        List<Event> timelineEvents = new ArrayList<>();
        long previousEndTime = CalendarUtils.getStartOfDayInMillis();

        for (Event event : mCalendarEvents) {
            bridgeEvents(event, previousEndTime, timelineEvents);
            timelineEvents.add(event);
            previousEndTime = event.getEndTime();
        }

        int freeTime = CalendarUtils.calculateDifferenceInMinutes(
                mCalendarEvents.get(mCalendarEvents.size() - 1).getEndTime(),
                CalendarUtils.getEndOfDayInMillis()
        );
        bridgeFreeTime(freeTime, timelineEvents);

        if (mOnPlanningFinishedListener != null) {
            mOnPlanningFinishedListener.onPlanningFinished(timelineEvents);
        }
    }

    private void bridgeEvents(Event event, long previousEndTime, List<Event> timelineEvents) {
        int freeTime = CalendarUtils
                .calculateDifferenceInMinutes(previousEndTime, event.getStartTime());

        bridgeFreeTime(freeTime, timelineEvents);
    }

    private void bridgeEvents(Event event, long previousEndTime, List<Event> timelineEvents, GooglePlace googlePlace) {
        int freeTime = CalendarUtils
                .calculateDifferenceInMinutes(previousEndTime, event.getStartTime());

        bridgeFreeTime(freeTime - googlePlace.getTravelTimeMinutes(), timelineEvents);
    }

    private void bridgeFreeTime(int freeTime, List<Event> timelineEvents) {
        List<UserHobby> bridgeHobbies = new ArrayList<>();
        List<UserHobby> shuffledHobbies = orderHobbies();

        int timeBridged = 0;
        for (int i = 0; i < shuffledHobbies.size() && timeBridged <= freeTime; i++) {
            UserHobby hobby = shuffledHobbies.get(i);
            if (timeBridged + hobby.getDuration() < freeTime) {
                timeBridged += hobby.getDuration();
                bridgeHobbies.add(hobby);
            }
        }

        timelineEvents.addAll(bridgeHobbies);
    }

    private List<UserHobby> orderHobbies() {
        final ArrayList<UserHobby> userHobbies = (ArrayList<UserHobby>) mUserHobbies;
        Collections.sort(mUserHobbies, new Comparator<UserHobby>() {
            @Override
            public int compare(UserHobby userHobby, UserHobby t1) {
                return t1.getPriority() - userHobby.getPriority();
            }
        });

        return mUserHobbies;
    }
}
