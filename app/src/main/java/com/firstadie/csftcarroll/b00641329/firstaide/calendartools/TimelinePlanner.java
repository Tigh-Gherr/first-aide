package com.firstadie.csftcarroll.b00641329.firstaide.calendartools;

import android.content.Context;
import android.util.Log;

import com.firstadie.csftcarroll.b00641329.firstaide.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.UserSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.CalendarUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tigh on 07/11/17.
 */

public class TimelinePlanner {

    private List<CalendarEvent> mCalendarEvents;
    private List<UserHobby> mUserHobbies;

    public TimelinePlanner(List<CalendarEvent> calendarEvents, List<UserHobby> userHobbies) {
        mCalendarEvents = calendarEvents;
        mUserHobbies = userHobbies;
    }

    public List<Event> planTimeline() {
        List<Event> timelineEvents = new ArrayList<>();
        long previousEndTime = CalendarUtils.getDayBracketInMillis(true);
        for(CalendarEvent event : mCalendarEvents) {
            bridgeEvents(event, previousEndTime, timelineEvents);
            timelineEvents.add(event);
            previousEndTime = event.getEndTime();
        }

        int freeTime = CalendarUtils.calculateDifferenceInMinutes(
                mCalendarEvents.get(mCalendarEvents.size() - 1).getEndTime(),
                CalendarUtils.getDayBracketInMillis(false)
        );
        bridgeFreeTime(freeTime, timelineEvents);

        return timelineEvents;
    }

    private void bridgeEvents(CalendarEvent event, long previousEndTime, List<Event> timelineEvents) {
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
