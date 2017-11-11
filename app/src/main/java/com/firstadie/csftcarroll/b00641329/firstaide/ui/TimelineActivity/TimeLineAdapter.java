package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.content.Context;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstadie.csftcarroll.b00641329.firstaide.events.Event;
import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineView.TimelineView;

import java.util.List;

/**
 * Created by tigh on 10/11/17.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimelineViewHolder> {

    private Context mContext;
    private List<Event> mEvents;

    private int mCurrentEventPosition;

    public TimeLineAdapter(Context context, List<Event> events) {
        mContext = context;
        mEvents = events;

        mCurrentEventPosition = 0;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = TimelineView.getTimeLineViewType(position, getItemCount());
        return viewType;
    }

    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_view_item_event, parent, false);
        return new TimelineViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        Event event = mEvents.get(position);

        String title = event.getTitle();
        int duration = event.getDuration();

        holder.mTitleTextView.setText(title);
        holder.mDurationTextView.setText(String.valueOf(duration));

        switch (event.getEventType()) {
            case Event.CALENDAR_EVENT:
                holder.mCalendarEventIcon.setVisibility(View.VISIBLE);
                break;
            case Event.HOBBY:
                holder.mCalendarEventIcon.setVisibility(View.INVISIBLE);
                break;
        }

        if(event.isCurrentEvent()) {
            holder.mMarkerTimelineView.setMarker(
                    VectorDrawableCompat.create(
                            mContext.getResources(),
                            R.drawable.marker_current_event,
                            mContext.getTheme()
                    )
            );

            mCurrentEventPosition = position;
        }
    }

    public int currentEventPosition() {
        return mCurrentEventPosition;
    }

    public class TimelineViewHolder extends RecyclerView.ViewHolder {
        TimelineView mMarkerTimelineView;
        AppCompatTextView mTitleTextView;
        AppCompatTextView mDurationTextView;
        AppCompatImageView mCalendarEventIcon;

        public TimelineViewHolder(View itemView) {
            super(itemView);

            mMarkerTimelineView = itemView.findViewById(R.id.timelineview_marker);
            mTitleTextView = itemView.findViewById(R.id.textview_eventTitle);
            mDurationTextView = itemView.findViewById(R.id.textview_eventDuration);
            mCalendarEventIcon = itemView.findViewById(R.id.imageview_calendarEventIcon);
        }
    }

}

