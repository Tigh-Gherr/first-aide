package com.firstadie.csftcarroll.b00641329.firstaide.location;

import android.content.Context;

/**
 * Created by tigh on 10/11/17.
 */

public class LocationSingleton {
    private LocationHelper mLocationHelper;
    private static LocationSingleton sLocationSingleton;

    private LocationSingleton() {
    }

    public static LocationSingleton get() {
        if(sLocationSingleton == null) {
            sLocationSingleton = new LocationSingleton();
        }

        return sLocationSingleton;
    }

    public void setLocationHelper(LocationHelper locationHelper) {
        mLocationHelper = locationHelper;
    }

    public double getLatitude() {
        return mLocationHelper.getLatitude();
    }

    public double getLongitude() {
        return mLocationHelper.getLongitude();
    }

    public static String getFormattedLatLng() {
        String latlng = get().getLatitude() + "," + get().getLongitude();

        return latlng;
    }

    public boolean isNull() {
        return mLocationHelper == null || mLocationHelper.isNull() || !mLocationHelper.isLocationEnabled();
    }

    public static void cancelLocationUpdates() {
        get().mLocationHelper.cancelUpdates();
    }
}
