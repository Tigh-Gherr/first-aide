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
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by tigh on 07/11/17.
 */

public class ActivityCalculator {
    public static List<Event> fillList(Context c, List<CalendarEvent> calendarEvents) {
        List<Event> timelineList = new ArrayList<>();

        CalendarEvent currentEvent = calendarEvents.get(0);
        populateBeginning(timelineList, currentEvent, c);
        timelineList.add(currentEvent);

        long previousEndTime = currentEvent.getEndTime();
        for(int i = 1; i < calendarEvents.size(); i++) {
            currentEvent = calendarEvents.get(i);

            int duration = CalendarUtils
                    .calculateTimeDifference(previousEndTime, currentEvent.getStartTime(), TimeUnit.MILLISECONDS, TimeUnit.MINUTES);

            List<UserHobby> startingHobbies = fillFreeTime(duration, c);

            timelineList.addAll(startingHobbies);
            timelineList.add(currentEvent);

            previousEndTime = currentEvent.getEndTime();
        }

        timelineList.addAll(
                fillFreeTime(
                    CalendarUtils.calculateTimeDifference(
                    previousEndTime,
                    CalendarUtils.getDayBracketInMillis(false),
                    TimeUnit.MILLISECONDS,
                    TimeUnit.MINUTES), c)
        );

        JSONArray array = new JSONArray();
        for(Event e : timelineList) {
            array.put(e.toJSONObject());
        }

        Log.d(ActivityCalculator.class.getSimpleName(), array.toString());

        return timelineList;
    }

    private static void populateBeginning(List<Event> timelineList, CalendarEvent calendarEvent, Context c) {
        long dayStart = CalendarUtils.getDayBracketInMillis(true);

        int duration = CalendarUtils.calculateTimeDifference(dayStart, calendarEvent.getStartTime(), TimeUnit.MILLISECONDS, TimeUnit.MINUTES);

        List<UserHobby> startingHobbies = fillFreeTime(duration, c);

        timelineList.addAll(0, startingHobbies);
    }


    private static List<UserHobby> fillFreeTime(int duration, Context c) {
        List<UserHobby> paddedHobbies = new ArrayList<>();
        List<UserHobby> shuffledHubbies = shuffleHobbies(c);

        int totalDuration = 0;

        for(UserHobby hobby : shuffledHubbies) {
            if(totalDuration + hobby.getDuration() < duration) {
                totalDuration += hobby.getDuration();
                paddedHobbies.add(hobby);
            }

            if(totalDuration == duration) {
                break;
            }
        }

        return paddedHobbies;
    }

    private static List<UserHobby> shuffleHobbies(Context c) {
        ArrayList<UserHobby> userHobbies = (ArrayList<UserHobby>) UserSingleton.get(c).getUser().getUserHobbies();
        userHobbies = (ArrayList<UserHobby>) userHobbies.clone();
        Collections.shuffle(userHobbies);

        return userHobbies;
    }
}
