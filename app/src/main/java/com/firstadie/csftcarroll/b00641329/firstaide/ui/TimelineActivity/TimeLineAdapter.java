package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
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

    public interface OnCalendarEventTouchListener {
        void onCalendarEventTouched(Event event);
    }

    private List<Event> mEvents;

    private OnCalendarEventTouchListener mOnCalendarEventTouchListener;

    public TimeLineAdapter(List<Event> events) {
        mEvents = events;
    }

    public void setOnCalendarEventTouchListener(OnCalendarEventTouchListener onCalendarEventTouchListener) {
        mOnCalendarEventTouchListener = onCalendarEventTouchListener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = TimelineView.getTimeLineViewType(position, getItemCount());
        return viewType;
    }

    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_event, parent, false);
        return new TimelineViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        final Event event = mEvents.get(position);

        String title = event.getTitle();
        int duration = event.getDuration();

        holder.mTitleTextView.setText(title);
        holder.mDurationTextView.setText(String.valueOf(duration));

        switch (event.getEventType()) {
            case Event.CALENDAR_EVENT:
                holder.mCalendarEventIcon.setVisibility(View.VISIBLE);
                holder.mEventCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnCalendarEventTouchListener.onCalendarEventTouched(event);
                    }
                });
                break;
            case Event.HOBBY:
                holder.mCalendarEventIcon.setVisibility(View.INVISIBLE);
                break;
            case Event.RIGHT_NOW:
                holder.mCalendarEventIcon.setVisibility(View.INVISIBLE);
        }

        if(event.isCurrentEvent()) {
//            holder.mMarkerTimelineView.setMarker(
//                    VectorDrawableCompat.create(
//                            mContext.getResources(),
//                            R.drawable.marker_current_event,
//                            mContext.getTheme()
//                    )
//            );
        }
    }

    public int currentEventPosition() {
        for(int i = 0; i < mEvents.size(); i++) {
            Event e = mEvents.get(i);
            if(e.isCurrentEvent()) {
                return i;
            }
        }
        return 0;
    }

    public int getPositionOfEvent(Event event) {
        for(int i = 0; i < mEvents.size(); i++) {
            Event current = mEvents.get(i);

            if(event.getId() == current.getId()) {
                return i;
            }
        }

        return -1;
    }

    public class TimelineViewHolder extends RecyclerView.ViewHolder {
        CardView mEventCardView;
        TimelineView mMarkerTimelineView;
        AppCompatTextView mTitleTextView;
        AppCompatTextView mDurationTextView;
        AppCompatImageView mCalendarEventIcon;

        public TimelineViewHolder(View itemView) {
            super(itemView);

            mEventCardView = itemView.findViewById(R.id.cardview_eventCard);
            mMarkerTimelineView = itemView.findViewById(R.id.timelineview_marker);
            mTitleTextView = itemView.findViewById(R.id.textview_eventTitle);
            mDurationTextView = itemView.findViewById(R.id.textview_eventDuration);
            mCalendarEventIcon = itemView.findViewById(R.id.imageview_calendarEventIcon);
        }
    }

}

