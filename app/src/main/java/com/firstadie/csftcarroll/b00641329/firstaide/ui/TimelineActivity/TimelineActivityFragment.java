package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstadie.csftcarroll.b00641329.firstaide.events.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.calendartools.TimelinePlanner;
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

    private CalendarHelper mCalendarHelper;
    private List<Event> mCalendarEvents;

    private RecyclerView mTimelineRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;

    public TimelineActivityFragment() {
    }

    private void populateTimeline() {
        mCalendarEvents = mCalendarHelper.getCalendarEvents();

        TimelinePlanner planner = new TimelinePlanner(
                mCalendarEvents,
                mUser.getUserHobbies()
        );

        List<Event> timelineEvents = planner.planTimeline();

        mTimeLineAdapter = new TimeLineAdapter(getActivity(), timelineEvents);
        mTimelineRecyclerView.setAdapter(mTimeLineAdapter);

        mTimelineRecyclerView.scrollToPosition(mTimeLineAdapter.currentEventPosition());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mCalendarHelper = new CalendarHelper(getActivity());
        mTimelineRecyclerView = view.findViewById(R.id.recyclerview_timeline);
        mTimelineRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTimelineRecyclerView.setHasFixedSize(false);

        mUser = UserSingleton.get(getActivity()).getUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTimeline();
    }
}
