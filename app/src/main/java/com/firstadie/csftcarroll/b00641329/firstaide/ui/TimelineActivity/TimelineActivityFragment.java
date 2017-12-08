package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.User;
import com.firstadie.csftcarroll.b00641329.firstaide.UserSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.calendartools.CalendarHelper;
import com.firstadie.csftcarroll.b00641329.firstaide.calendartools.TimelinePlanner;
import com.firstadie.csftcarroll.b00641329.firstaide.events.CalendarEvent;
import com.firstadie.csftcarroll.b00641329.firstaide.events.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.location.LocationSingleton;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimelineActivityFragment extends Fragment implements AccessibleFragment<Event> {

    private User mUser;

    private CalendarHelper mCalendarHelper;
    private List<Event> mCalendarEvents;

    private RecyclerView mTimelineRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;

    private AccessibleActivity<Event> mAccessibleActivity;

    public TimelineActivityFragment() {
    }

    private void prePopulateTimeline() {
        mCalendarEvents = mCalendarHelper.getCalendarEventsWithRightNow();

        CalendarEvent nextEvent = null;
        for(int i = 0; i < mCalendarEvents.size(); i++) {
            Event event = mCalendarEvents.get(i);
            if(event.isCurrentEvent()) {
                if(mCalendarEvents.indexOf(event) != mCalendarEvents.size() - 1) {
                    nextEvent = (CalendarEvent) mCalendarEvents.get(i + 1);
                    break;
                }
            }
        }

        TimelinePlanner planner = new TimelinePlanner(
                mCalendarEvents,
                mUser.getUserHobbies()
        );

        planner.setOnPlanningFinishedListener(new TimelinePlanner.OnPlanningFinishedListener() {
            @Override
            public void onPlanningFinished(List<Event> timelineEvents) {
                populateTimeline(timelineEvents);
            }
        });

        if(nextEvent == null || nextEvent.getEventLocation().isEmpty() || LocationSingleton.get().isNull()) {
            planner.planTimeline();
        } else {
            planner.planTimelineBeta(nextEvent);
        }

    }

    private void populateTimeline(List<Event> timelineEvents) {
        mTimeLineAdapter = new TimeLineAdapter(getActivity(), timelineEvents);
        mTimeLineAdapter.setOnCalendarEventTouchListener(new TimeLineAdapter.OnCalendarEventTouchListener() {
            @Override
            public void onCalendarEventTouched(Event event) {
                mAccessibleActivity.passData(event);
            }
        });

        mTimelineRecyclerView.setAdapter(mTimeLineAdapter);

        int currentEventPosition = mTimeLineAdapter.currentEventPosition();
        mTimelineRecyclerView.scrollToPosition(currentEventPosition);
    }

    @Override
    public void passData(Event data) {
        int pos = mTimeLineAdapter.getPositionOfEvent(data);
        if(pos != -1) {
            LinearLayoutManager llm = (LinearLayoutManager) mTimelineRecyclerView.getLayoutManager();
            llm.scrollToPositionWithOffset(pos, 0);
        }
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

        mUser = UserSingleton.get().getUser();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mAccessibleActivity = (AccessibleActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mUser == null) {
            NavUtils.navigateUpFromSameTask(getActivity());
        } else {
            prePopulateTimeline();
        }
    }
}
