package com.firstadie.csftcarroll.b00641329.firstaide.calendartools;

import com.firstadie.csftcarroll.b00641329.firstaide.api.API;
import com.firstadie.csftcarroll.b00641329.firstaide.api.GooglePlacesAPI;
import com.firstadie.csftcarroll.b00641329.firstaide.events.CalendarEvent;
import com.firstadie.csftcarroll.b00641329.firstaide.events.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.events.UserHobby;
import com.firstadie.csftcarroll.b00641329.firstaide.location.LocationSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tigh on 07/11/17.
 */

public class TimelinePlanner {

    private List<Event> mCalendarEvents;
    private List<UserHobby> mUserHobbies;

    public TimelinePlanner(List<Event> calendarEvents, List<UserHobby> userHobbies) {
        mCalendarEvents = calendarEvents;
        mUserHobbies = userHobbies;
    }

    public List<Event> planTimeline() {
        API api = new GooglePlacesAPI();

        List<Event> timelineEvents = new ArrayList<>();
        long previousEndTime = CalendarUtils.getDayBracketInMillis(true);

        for(Event event : mCalendarEvents) {
            bridgeEvents(event, previousEndTime, timelineEvents);
            timelineEvents.add(event);
            previousEndTime = event.getEndTime();

            if(event instanceof CalendarEvent) {
                CalendarEvent calendarEvent = (CalendarEvent) event;
                String origin = LocationSingleton.get().getLatitude() + "," +
                        LocationSingleton.get().getLongitude();
                api.addParam("origin", origin);
                api.addParam("destination", calendarEvent.getEventLocation());

                //api.query();
            }
        }

        int freeTime = CalendarUtils.calculateDifferenceInMinutes(
                mCalendarEvents.get(mCalendarEvents.size() - 1).getEndTime(),
                CalendarUtils.getDayBracketInMillis(false)
        );
        bridgeFreeTime(freeTime, timelineEvents);

        return timelineEvents;
    }

    private void bridgeEvents(Event event, long previousEndTime, List<Event> timelineEvents) {
        int freeTime = CalendarUtils
                .calculateDifferenceInMinutes(previousEndTime, event.getStartTime());

        bridgeFreeTime(freeTime, timelineEvents);
    }

    private void bridgeFreeTime(int freeTime, List<Event> timelineEvents) {
        List<UserHobby> bridgeHobbies = new ArrayList<>();
        List<UserHobby> shuffledHobbies = shuffleHobbies();

        int timeBridged = 0;
        for(int i = 0; i < shuffledHobbies.size() && timeBridged <= freeTime; i++) {
            UserHobby hobby = shuffledHobbies.get(i);
            if(timeBridged + hobby.getDuration() < freeTime) {
                timeBridged += hobby.getDuration();
                bridgeHobbies.add(hobby);
            }
        }

        timelineEvents.addAll(bridgeHobbies);
    }

    private List<UserHobby> shuffleHobbies() {
        ArrayList<UserHobby> userHobbies = (ArrayList<UserHobby>) mUserHobbies;
        userHobbies = (ArrayList<UserHobby>) userHobbies.clone();
        Collections.shuffle(userHobbies);

        return userHobbies;
    }
}
