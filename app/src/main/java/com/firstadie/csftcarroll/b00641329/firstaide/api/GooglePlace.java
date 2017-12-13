package com.firstadie.csftcarroll.b00641329.firstaide.api;

import com.firstadie.csftcarroll.b00641329.firstaide.utils.TextFormatUtils;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tigh on 17/11/17.
 */

public class GooglePlace {
    private int mDistance;
    private int mTravelTime;

    private String mEndAddress;

    private LatLng mLatLng;

    public GooglePlace(String address, double lat, double lng) {
        new GooglePlace(0, 0, address, lat, lng);
    }

    public GooglePlace(int distance, int travelTime, String endAddress, double lat, double lng) {
        mDistance = distance;
        mTravelTime = travelTime;
        mEndAddress = endAddress;

        mLatLng = new LatLng(lat, lng);
    }

    public int getDistance() {
        return mDistance;
    }

    public int getTravelTimeSeconds() {
        return mTravelTime;
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

    public String getTravelTimeFormatted() {
        return TextFormatUtils.secondsToTime(mTravelTime);
    }

    public int getTravelTimeMinutes() {
        return mTravelTime / 60;
    }
}
