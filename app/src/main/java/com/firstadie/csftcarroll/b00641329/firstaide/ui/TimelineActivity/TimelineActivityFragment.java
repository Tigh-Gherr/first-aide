package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstadie.csftcarroll.b00641329.firstaide.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.calendartools.TimelinePlanner;
import com.firstadie.csftcarroll.b00641329.firstaide.calendartools.CalendarEvent;
import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.User;
import com.firstadie.csftcarroll.b00641329.firstaide.UserSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.calendartools.CalendarHelper;

import org.json.JSONArray;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimelineActivityFragment extends Fragment {

    private User mUser;
    private AppCompatTextView mUserInfoTextView;

    private CalendarHelper mCalendarHelper;
    private List<CalendarEvent> mCalendarEvents;

    public TimelineActivityFragment() {
    }

    private void populateTimeline() {
        mCalendarEvents = mCalendarHelper.getCalendarEvents();

        JSONArray array = new JSONArray();
        for(CalendarEvent event : mCalendarEvents) {
            array.put(event.toJSONObject());
        }

        Log.d(getClass().getSimpleName(), array.toString());

        //TimelinePlanner.fillList(getActivity(), mCalendarEvents);

        TimelinePlanner planner = new TimelinePlanner(mCalendarEvents, mUser.getUserHobbies());
        List<Event> timelineEvents = planner.planTimeline();

        JSONArray eventArray = new JSONArray();
        for(Event event : timelineEvents) {
            eventArray.put(event.toJSONObject());
        }
        Log.d(getClass().getSimpleName(), eventArray.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mCalendarHelper = new CalendarHelper(getActivity());

        mUser = UserSingleton.get(getActivity()).getUser();

        mUserInfoTextView = view.findViewById(R.id.textview_userInfo);
        mUserInfoTextView.setText(mUser.toJSONString());

    }

    @Override
    public void onResume() {
        super.onResume();
        populateTimeline();
    }
}
