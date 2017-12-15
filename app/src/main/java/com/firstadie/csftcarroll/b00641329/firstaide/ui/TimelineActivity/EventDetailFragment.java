package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.firstadie.csftcarroll.b00641329.firstaide.OnEndpointQueryCompleteListener;
import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.api.GooglePlace;
import com.firstadie.csftcarroll.b00641329.firstaide.api.GooglePlacesAPI;
import com.firstadie.csftcarroll.b00641329.firstaide.api.GooglePlacesDirectionsAPI;
import com.firstadie.csftcarroll.b00641329.firstaide.events.CalendarEvent;
import com.firstadie.csftcarroll.b00641329.firstaide.location.LocationSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.CalendarUtils;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.NetworkUtils;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.TextFormatUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.Serializable;

public class EventDetailFragment extends Fragment
        implements AccessibleFragment<Float> {

    private CalendarEvent mCalendarEvent;
    private GooglePlace mGooglePlace;

    private AppCompatImageView mArrowImageView;
    private AppCompatTextView mTitleTextView;
    private AppCompatTextView mTimeTextView;
    private AppCompatTextView mTimeUntilEventTextView;

    private MapView mEventMapView;
    private AppCompatTextView mMapsNoInternetTextView;

    private AppCompatTextView mDistanceTextView;
    private AppCompatTextView mTravelTimeTextView;
    private RelativeLayout mPanelHeadingRelativeLayout;

    private CardView mEventDescriptionCardView;
    private AppCompatTextView mEventDescriptionTextView;

    private ValueAnimator mTopPanelBackgroundAnimator;
    private ValueAnimator mTopPanelContentAnimator;

    private OnArrowPressedListener mOnArrowPressedListener;

    public EventDetailFragment() {
    }

    public static EventDetailFragment newInstance(Serializable calendarEvent) {
        Bundle args = new Bundle();
        EventDetailFragment fragment = new EventDetailFragment();

        args.putSerializable("calendarEvent", calendarEvent);
        fragment.setArguments(args);

        return fragment;
    }

    private void displayNoInternetInfo() {
        mEventMapView.setVisibility(View.INVISIBLE);
        mMapsNoInternetTextView.setVisibility(View.VISIBLE);
    }

    private void displayPlacesInfo() {
        mMapsNoInternetTextView.setVisibility(View.GONE);
        mEventMapView.onCreate(null);
        mEventMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(getActivity().getApplicationContext());
                googleMap.addMarker(new MarkerOptions().position(mGooglePlace.getLatLng()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(mGooglePlace.getLatLng()));
            }
        });

        if (!LocationSingleton.get().isNull()) {
            mDistanceTextView.setText(mGooglePlace.getDistanceMiles());
            mTravelTimeTextView.setText(mGooglePlace.getTravelTimeFormatted() + " to travel.");
        }
    }

    private void initGooglePlacesWithTravelInfo() {
        mMapsNoInternetTextView.setVisibility(View.GONE);
        final GooglePlacesDirectionsAPI api = new GooglePlacesDirectionsAPI();

        api.setOnEndpointQueryCompleteListener(new OnEndpointQueryCompleteListener() {
            @Override
            public void onQueryComplete(String result) throws JSONException {
                mGooglePlace = api.parse(result);
                displayPlacesInfo();
            }
        });

        api.addParam(GooglePlacesDirectionsAPI.PARAM_ORIGIN, LocationSingleton.getFormattedLatLng());
        api.addParam(GooglePlacesDirectionsAPI.PARAM_DESTINATION, mCalendarEvent.getEventLocation());

        api.query();
    }

    private void initGooglePlaces() {
        final GooglePlacesAPI api = new GooglePlacesAPI();

        api.setOnEndpointQueryCompleteListener(new OnEndpointQueryCompleteListener() {
            @Override
            public void onQueryComplete(String result) throws JSONException {
                mGooglePlace = api.parse(result);
                displayPlacesInfo();
            }
        });

        api.addParam(GooglePlacesAPI.PARAM_ADDRESS, mCalendarEvent.getEventLocation());

        api.query();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mCalendarEvent = (CalendarEvent) getArguments().getSerializable("calendarEvent");

        mTitleTextView = view.findViewById(R.id.textview_eventTitle);
        mTimeTextView = view.findViewById(R.id.textview_eventTime);
        mTimeUntilEventTextView = view.findViewById(R.id.textview_timeUntilEvent);
        mArrowImageView = view.findViewById(R.id.imageview_arrow);
        mDistanceTextView = view.findViewById(R.id.textview_distance);
        mTravelTimeTextView = view.findViewById(R.id.textview_travelTime);

        mPanelHeadingRelativeLayout = view.findViewById(R.id.relativelayout_topPanel);

        mEventMapView = view.findViewById(R.id.mapview_event);
        mMapsNoInternetTextView = view.findViewById(R.id.textview_noInternetMapView);

        mEventDescriptionCardView = view.findViewById(R.id.cardview_eventDescriptionCard);
        mEventDescriptionTextView = view.findViewById(R.id.textview_eventDescription);

        String startTime = TextFormatUtils.epochToTime(mCalendarEvent.getStartTime());
        String endTime = TextFormatUtils.epochToTime(mCalendarEvent.getEndTime());

        if(System.currentTimeMillis() < mCalendarEvent.getStartTime()) {
            int minutesUntilEvent = CalendarUtils.calculateDifferenceInMinutes(System.currentTimeMillis(), mCalendarEvent.getStartTime());

            String timeUntilEvent = TextFormatUtils.secondsToTime(minutesUntilEvent * 60);
            mTimeUntilEventTextView.setText(" || " + timeUntilEvent);
        } else {
            mTimeUntilEventTextView.setVisibility(View.INVISIBLE);
        }

        mTitleTextView.setText(mCalendarEvent.getTitle());
        mTimeTextView.setText(startTime + " - " + endTime);

        if (mCalendarEvent.getDescription() == null || mCalendarEvent.getDescription().isEmpty()) {
            mEventDescriptionCardView.setVisibility(View.GONE);
        } else {
            mEventDescriptionTextView.setText(mCalendarEvent.getDescription());
        }

        mArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnArrowPressedListener.onArrowPressed();
            }
        });

        setAnimators();
    }

    private void setAnimators() {
        mTopPanelBackgroundAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.colorPrimary)
        );

        mTopPanelContentAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.white)
        );

        mTopPanelBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPanelHeadingRelativeLayout
                        .setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });

        mTopPanelContentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                mTitleTextView.setTextColor(value);
                mTimeTextView.setTextColor(value);
                mTimeUntilEventTextView.setTextColor(value);
                mArrowImageView.setColorFilter(value);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public void adjustPanelForOffset(float offset) {
        mTopPanelBackgroundAnimator.setCurrentFraction(offset);
        mTopPanelContentAnimator.setCurrentFraction(offset);

        mPanelHeadingRelativeLayout.setElevation(offset * 16);
        mArrowImageView.setRotation(0 - (offset * 180));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mOnArrowPressedListener = (OnArrowPressedListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(NetworkUtils.isNetworkAvailable(getActivity())) {
            if (LocationSingleton.get().isNull()) {
                initGooglePlaces();
            } else {
                initGooglePlacesWithTravelInfo();
            }
        } else {
            displayNoInternetInfo();
        }
    }

    @Override
    public void passData(Float data) {
        adjustPanelForOffset(data);
    }
}
