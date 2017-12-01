package com.firstadie.csftcarroll.b00641329.firstaide.api;

import com.firstadie.csftcarroll.b00641329.firstaide.utils.TextFormatUtils;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tigh on 17/11/17.
 */

public class GooglePlace {
    private int mDistance;
    private int mTravelTime;

    private String mStartAddress;
    private String mEndAddress;

    private LatLng mLatLng;

    public GooglePlace(int distance, int travelTime, String startAddress, String endAddress, double lat, double lng) {
        mDistance = distance;
        mTravelTime = travelTime;
        mStartAddress = startAddress;
        mEndAddress = endAddress;

        mLatLng = new LatLng(lat, lng);
    }

    public int getDistance() {
        return mDistance;
    }

    public int getTravelTime() {
        return mTravelTime;
    }

    public String getStartAddress() {
        return mStartAddress;
    }

    public String getEndAddress() {
        return mEndAddress;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public String getDistanceMiles() {
        return TextFormatUtils.kilometersToMiles(mDistance);
    }

    public String getTravelTimeMinutes() {
        return TextFormatUtils.secondsToTime(mTravelTime);
    }
}
