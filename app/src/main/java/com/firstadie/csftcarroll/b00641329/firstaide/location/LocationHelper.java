package com.firstadie.csftcarroll.b00641329.firstaide.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

/**
 * Created by tigh on 10/11/17.
 */

public class LocationHelper {

    private LocationManager mLocationManager;
    private Location mLocation;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            mLocationManager.removeUpdates(mLocationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @SuppressLint("MissingPermission")
    public LocationHelper(Context c) {
        mLocationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        mLocation = getLastKnownLocation(c);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, mLocationListener);
    }

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation(Context c) {
        mLocationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        List<String> locationProviders = mLocationManager.getProviders(true);
        Location location = null;

        for(String provider : locationProviders) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if(l == null) {
                continue;
            }

            if(location == null || l.getAccuracy() < location.getAccuracy()) {
                location = l;
            }
        }

        return location;
    }

    public double getLongitude() {
        return mLocation.getLongitude();
    }

    public double getLatitude() {
        return mLocation.getLatitude();
    }

    public void cancelUpdates() {
        mLocationManager.removeUpdates(mLocationListener);
    }

}
