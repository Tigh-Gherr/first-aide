package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.User;
import com.firstadie.csftcarroll.b00641329.firstaide.UserSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.calendartools.CalendarHelper;
import com.firstadie.csftcarroll.b00641329.firstaide.calendartools.TimelinePlanner;
import com.firstadie.csftcarroll.b00641329.firstaide.events.CalendarEvent;
import com.firstadie.csftcarroll.b00641329.firstaide.events.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.events.RightNow;
import com.firstadie.csftcarroll.b00641329.firstaide.location.LocationSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.NetworkUtils;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimelineActivityFragment extends Fragment implements AccessibleFragment<Event> {

    private User mUser;

    private CalendarHelper mCalendarHelper;
    private List<Event> mCalendarEvents;

    private LinearLayout mNoTimelineLinearLayout;
    private ProgressBar mNoTimelineProgressBar;
    private AppCompatTextView mNoTimelineTextView;

    private RecyclerView mTimelineRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;

    private AccessibleActivity<Event> mAccessibleActivity;

    public TimelineActivityFragment() {
    }

    private void prePopulateTimeline() {
        CalendarEvent nextEvent = null;
        for(int i = 0; i < mCalendarEvents.size() && nextEvent == null; i++) {
            Event event = mCalendarEvents.get(i);
            if(event.isCurrentEvent() && mCalendarEvents.indexOf(event) != mCalendarEvents.size() - 1) {
                nextEvent = (CalendarEvent) mCalendarEvents.get(i + 1);
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

        if(!NetworkUtils.isNetworkAvailable(getActivity()) || nextEvent == null ||
                nextEvent.getEventLocation().isEmpty() || LocationSingleton.get().isNull()) {
            planner.planTimeline();
        } else {
            planner.planTimelineWithNextEvent(nextEvent);
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

        swapContentViews(mNoTimelineLinearLayout, mTimelineRecyclerView);
    }

    private void swapContentViews(final View toHide, final View toDisplay) {
        toDisplay.setAlpha(0f);
        toDisplay.setVisibility(View.GONE);

        toHide.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                toHide.setVisibility(View.GONE);
                toDisplay.setVisibility(View.VISIBLE);
                toDisplay.animate().setDuration(500).alpha(1f).start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).setDuration(500).alpha(0f).start();
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

        mNoTimelineLinearLayout = view.findViewById(R.id.linearlayout_noTimeline);
        mNoTimelineTextView = view.findViewById(R.id.textview_noTimeline);
        mNoTimelineProgressBar = view.findViewById(R.id.progressbar_noTimeline);


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

        mNoTimelineTextView.setText("Generating timeline...");
        mNoTimelineProgressBar.setVisibility(View.VISIBLE);
        if(mUser == null) {
            NavUtils.navigateUpFromSameTask(getActivity());
        } else {
            mCalendarEvents = mCalendarHelper.getCalendarEventsWithRightNow();

            if(mCalendarEvents.size() == 0 ||
                    (mCalendarEvents.size() == 1 && mCalendarEvents.get(0) instanceof RightNow)) {
                displayEmptyTimelineMessage();
            } else {
                prePopulateTimeline();
            }
        }
    }

    private void displayEmptyTimelineMessage() {
        mNoTimelineTextView.setText("No events in calendar. Add some!");
        mNoTimelineProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTimelineRecyclerView.setVisibility(View.GONE);
        mTimelineRecyclerView.setAlpha(0f);
        mNoTimelineLinearLayout.setVisibility(View.VISIBLE);
        mNoTimelineLinearLayout.setAlpha(1f);
    }
}
