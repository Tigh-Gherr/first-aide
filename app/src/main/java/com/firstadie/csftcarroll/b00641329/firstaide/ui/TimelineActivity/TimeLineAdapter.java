package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
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
import com.firstadie.csftcarroll.b00641329.firstaide.utils.TextFormatUtils;

import java.util.List;

/**
 * Created by tigh on 10/11/17.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimelineViewHolder> {

    private Context mContext;

    public interface OnCalendarEventTouchListener {
        void onCalendarEventTouched(Event event);
    }

    private List<Event> mEvents;

    private OnCalendarEventTouchListener mOnCalendarEventTouchListener;

    public TimeLineAdapter(Context context, List<Event> events) {
        mContext = context;
        mEvents = events;
    }

    public void setOnCalendarEventTouchListener(OnCalendarEventTouchListener onCalendarEventTouchListener) {
        mOnCalendarEventTouchListener = onCalendarEventTouchListener;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
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

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        final Event event = mEvents.get(position);

        String title = event.getTitle();
        int duration = event.getDuration();
        String durationText = TextFormatUtils.secondsToTime(duration * 60, false);

        holder.mTitleTextView.setText(title);
        holder.mDurationTextView.setText(durationText);

        switch (event.getEventType()) {
            case Event.CALENDAR_EVENT:
                holder.mCalendarEventIcon.setVisibility(View.VISIBLE);
                holder.mEventLocationCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnCalendarEventTouchListener.onCalendarEventTouched(event);
                    }
                });
                break;
            case Event.HOBBY:
                holder.mCalendarEventIcon.setVisibility(View.GONE);
                break;
            case Event.RIGHT_NOW:
                holder.mCalendarEventIcon.setVisibility(View.GONE);
        }

        if(event.isCurrentEvent()) {
            setTimelineColors(
                    holder.mMarkerTimelineView,
                    R.drawable.marker_current_event,
                    R.color.current_event_green
            );
        } else {
            setTimelineColors(
                    holder.mMarkerTimelineView,
                    R.drawable.marker_event,
                    R.color.other_event_red
            );
        }
    }

    private void setTimelineColors(TimelineView timelineView, int marker, int color) {
        timelineView.setMarker(
                ContextCompat.getDrawable(mContext, marker)
        );

        timelineView.setStartLine(new ColorDrawable(
                ContextCompat.getColor(mContext, color)
        ));

        timelineView.setEndLine(new ColorDrawable(
                ContextCompat.getColor(mContext, color)
        ));
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
        CardView mEventLocationCardView;
        TimelineView mMarkerTimelineView;
        AppCompatTextView mTitleTextView;
        AppCompatTextView mDurationTextView;
        AppCompatImageView mCalendarEventIcon;

        public TimelineViewHolder(View itemView) {
            super(itemView);

            mEventLocationCardView = itemView.findViewById(R.id.cardview_eventCard);
            mMarkerTimelineView = itemView.findViewById(R.id.timelineview_marker);
            mTitleTextView = itemView.findViewById(R.id.textview_eventTitle);
            mDurationTextView = itemView.findViewById(R.id.textview_eventDuration);
            mCalendarEventIcon = itemView.findViewById(R.id.imageview_calendarEventIcon);
        }
    }

}

